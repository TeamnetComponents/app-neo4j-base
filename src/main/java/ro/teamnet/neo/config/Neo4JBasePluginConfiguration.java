package ro.teamnet.neo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
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

@Configuration
@EnablePluginRegistries({Neo4jConfigurationPlugin.class, NeoPackagesToScanPlugin.class, Neo4jResourcePropertiesPathPlugin.class})
@ComponentScan(basePackages = {"ro.teamnet.neo.plugin"})
@EnableTransactionManagement

public class Neo4JBasePluginConfiguration {

private RelaxedPropertyResolver propertyResolver;
    private Environment environment;


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
    public Neo4jConfigurationPlugin defaultNeo4jConfigurationPlugin(){
        return new DefaultNeo4jConfigurationPlugin(neoConfig());
    }

    @Bean
    @Order(1000)
    public Neo4jResourcePropertiesPathPlugin defaultNeo4jResourcePropertiesPathPlugin(){
        return new DefaultNeo4jResourcePropertiesPathPlugin();
    }




    @Inject
    public void setNeoConfig(@Qualifier("neo4jResourcePropertiesPathPluginRegistry")
                                 PluginRegistry<Neo4jResourcePropertiesPathPlugin, Neo4JType> neo4jResourcePropertiesPathPluginRegistry,Environment environment){

        Neo4jResourcePropertiesPathPlugin defaultNeo4jResourcePropertiesPathPlugin=
                neo4jResourcePropertiesPathPluginRegistry.getPluginFor(Neo4JType.DEFAULT_RESOURCE_PROPERTIES_PATH);

        String neoConfigResource;

        Neo4jResourcePropertiesPathPlugin neo4jResourcePropertiesPathPlugin=
                neo4jResourcePropertiesPathPluginRegistry.getPluginFor(Neo4JType.RESOURCE_PROPERTIES_PATH,
                        defaultNeo4jResourcePropertiesPathPlugin);

        if(neo4jResourcePropertiesPathPlugin==null)
            return;

        neoConfigResource=neo4jResourcePropertiesPathPlugin.neo4jResourcePropertiesPath();

        if(neoConfigResource==null)
            return;
        YamlPropertiesFactoryBean ret = new YamlPropertiesFactoryBean();

        String[] activeProfiles = environment.getActiveProfiles();
        Resource[] resources = new Resource[activeProfiles.length];
        for (int i = 0; i < activeProfiles.length; i++) {
            String activeProfile = activeProfiles[i];
            resources[i] = new ClassPathResource(neoConfigResource+"-" + activeProfile + ".yml");
        }
        ret.setResources(resources);
        ((StandardEnvironment) environment).getPropertySources().addFirst(new PropertiesPropertySource("neo", ret.getObject()));
        propertyResolver = new RelaxedPropertyResolver(environment, "neo.");

    }
}
