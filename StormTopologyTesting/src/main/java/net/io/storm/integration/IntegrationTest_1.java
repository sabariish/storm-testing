package net.io.storm.integration;

import com.google.common.collect.ImmutableMap;
import junit.framework.TestCase;
import net.io.storm.WordCountBolt;
import net.io.storm.WordReaderSpout;
import org.apache.storm.Config;
import org.apache.storm.st.wrapper.TopoWrap;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.st.wrapper.StormCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

/**
 * THis class is an integration test case for wordcount topology
 * THis test case requires storm cluster running on localhost
 * @author hardik
 */
public class IntegrationTest_1 extends TestCase
{

    protected final String topologyName = "WordCounterTopology";
    private TopoWrap topo;
    private StormCluster cluster;
    private ImmutableMap<String, Object> configMap;

    @BeforeTest
    @Override
    public void setUp()
    {
        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(1);
        this.configMap = ImmutableMap.copyOf(conf);
        this.cluster = new StormCluster();
    }

    @Test
    public void testWordCountTopology() throws Exception
    {
        topo = new TopoWrap(cluster, topologyName, getStormTopology());
        topo.submitSuccessfully(this.configMap);
        int minSpountEmits = 1000;
        //This assertion is required to capture the out put of spout and bolts
        topo.assertProgress(minSpountEmits, 1,"word-counter", 120);
        //Get the log of specified component from topology
        final String actualOutput = topo.getLogs("word-counter");
        final String[] expectedOutout = {"Hello, 10","hello, 200","World, 500"};
        for(String outPut:expectedOutout)
            Assert.assertTrue(actualOutput.contains(outPut), "Couldn't find in matching :"+outPut);
    }

    
    private StormTopology getStormTopology()
    {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("word-reader", new WordReaderSpout());
        builder.setBolt("word-counter", new WordCountBolt(), 1).shuffleGrouping("word-reader");
        return builder.createTopology();
    }

    @AfterTest
    public void tearDown() throws Exception
    {
        if(topo != null)
        {
            topo.killOrThrow();
            topo = null;
        }

        System.out.println("Cleanup done !");
    }
}
