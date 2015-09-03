package ro.teamnet.neo.plugin;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import ro.teamnet.neo.config.NeoConfig;

public class DefaultNeo4jConfigurationPlugin implements Neo4jConfigurationPlugin {


    private NeoConfig neoConfig;

    public DefaultNeo4jConfigurationPlugin(NeoConfig neoConfig) {
        this.neoConfig = neoConfig;
    }

    public GraphDatabaseService graphDatabaseService() {
        if (neoConfig.getUseEmbeddedDatabase()) {
            return new GraphDatabaseFactory().newEmbeddedDatabase(neoConfig.getEmbeddedDatabasePath());
        }
        String neoDbUri = neoConfig.getSchema() + "://" + neoConfig.getHost() + ":" + neoConfig.getPort() + "/db/data";
        return new SpringRestGraphDatabase(neoDbUri, neoConfig.getUser(), neoConfig.getPassword());
    }

    @Override
    public boolean supports(Neo4JType delimiter) {
        return delimiter == Neo4JType.DEFAULT_NEO_4J_CONFIGURATION;
    }
}
