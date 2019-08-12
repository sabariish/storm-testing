package net.io.storm;

import java.util.Map;
import org.apache.storm.Config;
import org.apache.storm.Testing;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.testing.CompleteTopologyParam;
import org.apache.storm.testing.MockedSources;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Values;
import junit.framework.TestCase;
import org.apache.storm.LocalCluster;

import org.junit.Test;

/**
 * This class represents Storm topology test with mocked spout data source and
 * Local storm cluster
 */

public class StormTopologyTest extends TestCase
{
    private StormTopology topology;
    private LocalCluster cluster;
    
    @Test
    @SuppressWarnings ("unchecked")
    public void testSpoutBolt() throws Exception
    {
        final Config conf = new Config();
        conf.setNumWorkers(1);
        conf.put(Config.STORM_LOCAL_MODE_ZMQ, false);
        conf.setMessageTimeoutSecs(30);
        
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("word-reader", new WordReaderSpout());
        builder.setBolt("word-counter", new WordCountBolt()).shuffleGrouping("word-reader");
        
        topology = builder.createTopology();

        cluster = new LocalCluster();

        MockedSources mockedSources = new MockedSources();
        mockedSources.addMockData("word-reader",
                new Values("hello"),
                new Values("world"),
                new Values("world"),
                new Values("how"));

        final CompleteTopologyParam completeTopologyParam = new CompleteTopologyParam();
        completeTopologyParam.setMockedSources(mockedSources);
        completeTopologyParam.setStormConf(conf);
        completeTopologyParam.setTimeoutMs(30000);

        final Map result = Testing.completeTopology(cluster, topology, completeTopologyParam);
        
        final Values input = new Values(new Values("hello"), new Values("world"), new Values("world"),
                new Values("how"));
        final Values output = new Values(new Values("hello",1), new Values("world",1),new Values("world",2), new Values("how",1));

//        List spoutTuples = Testing.readTuples(result, "word-reader");
//        List boltTuples = Testing.readTuples(result, "word-counter");              
//                
        assertTrue("Error in word-reader:",
                Testing.multiseteq(input, Testing.readTuples(result, "word-reader")));
        assertTrue("Error in word-counter:",
                Testing.multiseteq(output, Testing.readTuples(result, "word-counter")));
                
        
    }
    
    public void tearDown()
    {
        topology.clear();
        cluster.shutdown();       
    }
   
}
