package ro.teamnet.neo.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ro.teamnet.neo.plugin.Neo4JType;
import ro.teamnet.neo.plugin.Neo4jConfigurationPlugin;

import javax.inject.Inject;

//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Neo4JBasePluginConfiguration.class})

@ActiveProfiles("dev")
public class Neo4jBasePluginConfigurationTest {
    @Inject
    @Qualifier("neo4jConfigurationPluginRegistry")
    private PluginRegistry<Neo4jConfigurationPlugin,Neo4JType> neo4jConfigurationPluginRegistry;

//    @Test
    public void testProperties() throws Exception {

        Assert.assertTrue(neo4jConfigurationPluginRegistry.hasPluginFor(Neo4JType.DEFAULT_NEO_4J_CONFIGURATION));


    }
}