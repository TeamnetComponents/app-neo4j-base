package ro.teamnet.neo.config;

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
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ro.teamnet.neo.plugin.DefaultNeo4jConfigurationPlugin;
import ro.teamnet.neo.plugin.DefaultNeoPackagesToScanPlugin;
import ro.teamnet.neo.plugin.Neo4jConfigurationPlugin;
import ro.teamnet.neo.plugin.NeoPackagesToScanPlugin;

@Configuration
@EnablePluginRegistries({Neo4jConfigurationPlugin.class, NeoPackagesToScanPlugin.class})
@ComponentScan(basePackages = {"ro.teamnet.neo.plugin"})
@EnableTransactionManagement

public class Neo4JBasePluginConfiguration implements EnvironmentAware {

private RelaxedPropertyResolver propertyResolver;


    private NeoConfig neoConfig() {
        NeoConfig ret = new NeoConfig();
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

    @Override
    public void setEnvironment(Environment environment) {
        YamlPropertiesFactoryBean ret = new YamlPropertiesFactoryBean();
        String[] activeProfiles = environment.getActiveProfiles();
        Resource[] resources = new Resource[activeProfiles.length];
        for (int i = 0; i < activeProfiles.length; i++) {
            String activeProfile = activeProfiles[i];
            resources[i] = new ClassPathResource("config/neo4j-" + activeProfile + ".yml");
        }
        ret.setResources(resources);
        ((StandardEnvironment) environment).getPropertySources().addFirst(new PropertiesPropertySource("neo", ret.getObject()));
        propertyResolver = new RelaxedPropertyResolver(environment, "neo.");


    }
}
