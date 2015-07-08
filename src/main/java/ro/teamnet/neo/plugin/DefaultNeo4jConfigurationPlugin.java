package ro.teamnet.neo.plugin;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.neo4j.cross_store.config.CrossStoreNeo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.stereotype.Component;
import ro.teamnet.bootstrap.plugin.jpa.DefaultPackagesToScanPlugin;
import ro.teamnet.bootstrap.plugin.jpa.JpaPackagesToScanPlugin;
import ro.teamnet.neo.config.NeoConfig;

import javax.inject.Inject;
import java.io.IOException;

public class DefaultNeo4jConfigurationPlugin implements Neo4jConfigurationPlugin{



    private NeoConfig neoConfig;

    public DefaultNeo4jConfigurationPlugin(NeoConfig neoConfig) {
        this.neoConfig = neoConfig;
    }

    public GraphDatabaseService graphDatabaseService(){
        return new SpringRestGraphDatabase("http://"+neoConfig.getHost()+":"+neoConfig.getPort()+
                "/db/data",neoConfig.getUser(),neoConfig.getPassword());
    }


    @Override
    public boolean supports(Neo4JType delimiter) {
        return delimiter==Neo4JType.DEFAULT_NEO_4J_CONFIGURATION;
    }
}
