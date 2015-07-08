package ro.teamnet.neo.plugin;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.plugin.core.Plugin;

public interface Neo4jConfigurationPlugin extends Plugin<Neo4JType> {

    GraphDatabaseService graphDatabaseService();
}
