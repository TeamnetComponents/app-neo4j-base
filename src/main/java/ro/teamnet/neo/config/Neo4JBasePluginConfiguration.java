package ro.teamnet.neo.config;

import org.liquigraph.core.api.Liquigraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ro.teamnet.neo.plugin.*;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnablePluginRegistries({Neo4jConfigurationPlugin.class, NeoPackagesToScanPlugin.class, Neo4jResourcePropertiesPathPlugin.class, LiquigraphChangelogPathPlugin.class})
@ComponentScan(basePackages = {"ro.teamnet.neo.plugin"})
@EnableTransactionManagement

public class Neo4JBasePluginConfiguration {

    private RelaxedPropertyResolver propertyResolver;
    private Environment environment;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private NeoConfig neoConfig() {
        NeoConfig ret = new NeoConfig();
        ret.setSchema(propertyResolver.getProperty("schema"));
        ret.setHost(propertyResolver.getProperty("host"));
        ret.setPort(propertyResolver.getProperty("port"));
        ret.setUser(propertyResolver.getProperty("user"));
        ret.setPassword(propertyResolver.getProperty("password"));
        return ret;
    }

    @Bean
    @Order(0)
    public Neo4jConfigurationPlugin defaultNeo4jConfigurationPlugin() {
        return new DefaultNeo4jConfigurationPlugin(neoConfig());
    }

    @Bean
    @Order(1000)
    public Neo4jResourcePropertiesPathPlugin defaultNeo4jResourcePropertiesPathPlugin() {
        return new DefaultNeo4jResourcePropertiesPathPlugin();
    }

    @Bean
    @Order(1001)
    public LiquigraphChangelogPathPlugin defaultLiquigraphChangelogPathPlugin() {
        return new DefaultLiquigraphChangelogPathPlugin();
    }


    @Inject
    public void setNeoConfig(@Qualifier("neo4jResourcePropertiesPathPluginRegistry")
                             PluginRegistry<Neo4jResourcePropertiesPathPlugin, Neo4JType> neo4jResourcePropertiesPathPluginRegistry, Environment environment) {


        log.debug("setup Neo configuration");
        Neo4jResourcePropertiesPathPlugin defaultNeo4jResourcePropertiesPathPlugin =
                neo4jResourcePropertiesPathPluginRegistry.getPluginFor(Neo4JType.DEFAULT_RESOURCE_PROPERTIES_PATH);

        String neoConfigResource;

        Neo4jResourcePropertiesPathPlugin neo4jResourcePropertiesPathPlugin =
                neo4jResourcePropertiesPathPluginRegistry.getPluginFor(Neo4JType.RESOURCE_PROPERTIES_PATH,
                        defaultNeo4jResourcePropertiesPathPlugin);

        if (neo4jResourcePropertiesPathPlugin == null)
            return;

        neoConfigResource = neo4jResourcePropertiesPathPlugin.neo4jResourcePropertiesPath();

        if (neoConfigResource == null)
            return;
        YamlPropertiesFactoryBean ret = new YamlPropertiesFactoryBean();

        String[] activeProfiles = environment.getActiveProfiles();
        Resource[] resources = new Resource[activeProfiles.length];
        for (int i = 0; i < activeProfiles.length; i++) {
            String activeProfile = activeProfiles[i];
            resources[i] = new ClassPathResource(neoConfigResource + "-" + activeProfile + ".yml");
        }
        ret.setResources(resources);
        ((StandardEnvironment) environment).getPropertySources().addFirst(new PropertiesPropertySource("neo", ret.getObject()));
        propertyResolver = new RelaxedPropertyResolver(environment, "neo.");

    }


    @Bean
    public Liquigraph liquigraph(
            @Qualifier("liquigraphChangelogPathPluginRegistry") PluginRegistry<LiquigraphChangelogPathPlugin, Neo4JType>
                    liquigraphChangelogPathPluginRegistry
    ) {

        log.debug("Running liquigraph migrations");
        LiquigraphChangelogPathPlugin defaultLiquigraphChangelogPathPlugin = liquigraphChangelogPathPluginRegistry
                .getPluginFor(Neo4JType.DEFAULT_LIQUIGRAPH_CHANGELOG_PATH);
        List<LiquigraphChangelogPathPlugin> liquigraphChangelogPathPlugins = liquigraphChangelogPathPluginRegistry
                .getPluginsFor(Neo4JType.LIQUIGRAPH_CHANGELOG_PATH, Arrays.asList(defaultLiquigraphChangelogPathPlugin));

        Liquigraph liquigraph = new Liquigraph();
        for (LiquigraphChangelogPathPlugin liquigraphChangelogPathPlugin : liquigraphChangelogPathPlugins) {
            String liquigraphChangelogPath = liquigraphChangelogPathPlugin.liquigraphChangelogPath();
            log.debug("Running liquigraph migrations from : " + liquigraphChangelogPath);
            LiquigraphConfiguration liquigraphConfiguration = new LiquigraphConfiguration(neoConfig(), liquigraphChangelogPath);
            liquigraph.runMigrations(liquigraphConfiguration.getConfiguration());
        }
        return liquigraph;
    }
}
