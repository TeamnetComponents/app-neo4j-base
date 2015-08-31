package ro.teamnet.neo.plugin;

public class DefaultLiquigraphChangelogPathPlugin implements LiquigraphChangelogPathPlugin {

    @Override
    public String liquigraphChangelogPath() {
        return "config/liquigraph/changelog/master.xml";
    }

    @Override
    public boolean supports(Neo4JType delimiter) {
        return delimiter == Neo4JType.DEFAULT_LIQUIGRAPH_CHANGELOG_PATH;
    }

}
