package ro.teamnet.neo.config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.cross_store.config.CrossStoreNeo4jConfiguration;
import org.springframework.plugin.core.PluginRegistry;
import ro.teamnet.neo.plugin.Neo4JType;
import ro.teamnet.neo.plugin.Neo4jConfigurationPlugin;

@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@Import(Neo4JBasePluginConfiguration.class)
public class Neo4jBaseConfiguration extends CrossStoreNeo4jConfiguration {


    @Bean
    @ConditionalOnBean(name = "neo4jConfigurationPluginRegistry")
    public GraphDatabaseService graphDatabaseService(@Qualifier("neo4jConfigurationPluginRegistry")
                                                         PluginRegistry<Neo4jConfigurationPlugin, Neo4JType> neo4jConfigurationPluginRegistry) {
        return neo4jConfigurationPluginRegistry.getPluginFor(
                Neo4JType.NEO_4J_CONFIGURATION,
                neo4jConfigurationPluginRegistry.getPluginFor(
                        Neo4JType.DEFAULT_NEO_4J_CONFIGURATION)).graphDatabaseService();


    }


}
