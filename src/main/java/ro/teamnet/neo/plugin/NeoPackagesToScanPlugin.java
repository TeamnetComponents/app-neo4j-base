package ro.teamnet.neo.plugin;

import org.springframework.plugin.core.Plugin;

import java.util.List;

public interface NeoPackagesToScanPlugin extends Plugin<Neo4JType> {

    public List<String> packagesToScan();

}
