package ro.teamnet.neo.plugin;

import org.springframework.plugin.core.Plugin;

public interface Neo4jResourcePropertiesPathPlugin extends Plugin<Neo4JType> {

    public String neo4jResourcePropertiesPath();
}
