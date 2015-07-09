package ro.teamnet.neo.plugin;

public class DefaultNeo4jResourcePropertiesPathPlugin implements Neo4jResourcePropertiesPathPlugin {
    @Override
    public String neo4jResourcePropertiesPath() {
        return "config/default-neo4j";
    }

    @Override
    public boolean supports(Neo4JType delimiter) {
        return delimiter==Neo4JType.DEFAULT_RESOURCE_PROPERTIES_PATH;
    }
}
