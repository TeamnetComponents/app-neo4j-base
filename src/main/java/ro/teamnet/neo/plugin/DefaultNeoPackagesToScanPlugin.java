package ro.teamnet.neo.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultNeoPackagesToScanPlugin implements NeoPackagesToScanPlugin {

    private List<String> jpaPacks=new ArrayList<>();

    public static DefaultNeoPackagesToScanPlugin instance(){
        return new DefaultNeoPackagesToScanPlugin();
    }

    public DefaultNeoPackagesToScanPlugin addAllPackages(Collection<? extends String> c) {
        jpaPacks.addAll(c);
        return this;
    }

    public DefaultNeoPackagesToScanPlugin addPackage(String s) {
        jpaPacks.add(s);
        return this;
    }

    @Override
    public List<String> packagesToScan() {
        return jpaPacks;
    }



    @Override
    public boolean supports(Neo4JType delimiter) {
        return delimiter==Neo4JType.PACKAGE_TO_SCAN;
    }
}
