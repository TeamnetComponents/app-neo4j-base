package ro.teamnet.neo.plugin;

import org.springframework.plugin.core.Plugin;

public interface LiquigraphChangelogPathPlugin extends Plugin<Neo4JType> {
    String liquigraphChangelogPath();
}
