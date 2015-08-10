package ro.teamnet.neo.config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.cross_store.config.CrossStoreNeo4jConfiguration;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ro.teamnet.neo.plugin.Neo4JType;
import ro.teamnet.neo.plugin.Neo4jConfigurationPlugin;
import ro.teamnet.neo.plugin.NeoPackagesToScanPlugin;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@Import(Neo4JBasePluginConfiguration.class)
@DependsOn("entityManagerFactory")
@EnableTransactionManagement
public class Neo4jBaseConfiguration extends CrossStoreNeo4jConfiguration {

    @Bean
    public GraphDatabaseService graphDatabaseService(
            @Qualifier("neo4jConfigurationPluginRegistry")
            PluginRegistry<Neo4jConfigurationPlugin, Neo4JType> neo4jConfigurationPluginRegistry,
            @Qualifier("neoPackagesToScanPluginRegistry")
            PluginRegistry<NeoPackagesToScanPlugin, Neo4JType> neoPackagesToScanPluginRegistry,
            EntityManagerFactory emf
    ) {

        List<NeoPackagesToScanPlugin> defaultNeoPackagesToScanPlugins=neoPackagesToScanPluginRegistry.getPluginsFor(Neo4JType.PACKAGE_TO_SCAN);
        List<String> neoPackages=new ArrayList<>();
        for (NeoPackagesToScanPlugin defaultNeoPackagesToScanPlugin : defaultNeoPackagesToScanPlugins) {
                neoPackages.addAll(defaultNeoPackagesToScanPlugin.packagesToScan());
        }

        super.setBasePackage(neoPackages.toArray(new String[neoPackages.size()]));
        setEntityManagerFactory(emf);

        return neo4jConfigurationPluginRegistry.getPluginFor(
                Neo4JType.NEO_4J_CONFIGURATION,
                neo4jConfigurationPluginRegistry.getPluginFor(
                        Neo4JType.DEFAULT_NEO_4J_CONFIGURATION)).graphDatabaseService();


    }


}
