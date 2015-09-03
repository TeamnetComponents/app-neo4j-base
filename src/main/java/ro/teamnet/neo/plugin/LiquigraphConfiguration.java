package ro.teamnet.neo.plugin;

import org.liquigraph.core.configuration.Configuration;
import org.liquigraph.core.configuration.ConfigurationBuilder;
import ro.teamnet.neo.config.NeoConfig;

/**
 * Created by Oana.Mihai on 7/20/2015.
 */
public class LiquigraphConfiguration {

    private NeoConfig neoConfig;
    private String liquigraphChangelogPath;

    public LiquigraphConfiguration(NeoConfig neoConfig, String liquigraphChangelogPath) {
        this.neoConfig = neoConfig;
        this.liquigraphChangelogPath = liquigraphChangelogPath;
    }

    public Configuration getConfiguration() {
        ConfigurationBuilder liquigraphConfigurationBuilder = new ConfigurationBuilder()
                .withMasterChangelogLocation(liquigraphChangelogPath)
                .withRunMode();

        if (neoConfig.getUseEmbeddedDatabase()) {
            return liquigraphConfigurationBuilder
                    .withUri("jdbc:neo4j:file:" + neoConfig.getEmbeddedDatabasePath())
                    .build();
        }

        return liquigraphConfigurationBuilder
                .withUri("jdbc:neo4j://" + neoConfig.getHost() + ":" + neoConfig.getPort())
                .withUsername(neoConfig.getUser())
                .withPassword(neoConfig.getPassword())
                .build();
    }
}
