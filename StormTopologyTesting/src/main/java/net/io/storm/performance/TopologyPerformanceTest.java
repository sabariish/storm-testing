package net.io.storm.performance;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import junit.framework.TestCase;
import net.io.storm.WordCountBolt;
import net.io.storm.WordReaderSpout;
import org.apache.storm.Config;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.st.wrapper.StormCluster;
import org.apache.storm.st.wrapper.TopoWrap;
import org.apache.storm.topology.TopologyBuilder;
import org.testng.annotations.AfterTest;


/**
 * This topology helps measure how fast a spout can produce data.
 *
 * <p>Spout generates a stream of a fixed words.
 */
public class TopologyPerformanceTest extends TestCase{

    public static final String TOPOLOGY_NAME = "TopologyPerformanceTest";
    public static final String SPOUT_ID = "word-reader";
    private TopoWrap topo;
    private StormCluster cluster;
            
    public void testPerformance() throws Exception {
       
        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(1);
        conf.setStatsSampleRate(1.0d);
       
        ImmutableMap<String, Object> configMap = ImmutableMap.copyOf(conf);
        //create storm cluster
        cluster = new StormCluster();
        //create TopoWrap
        topo = new TopoWrap(cluster, TOPOLOGY_NAME, getStormTopology());
        //submit tolology
        topo.submitSuccessfully(configMap);
        
        //topo.assertProgress(1000, 1, SPOUT_ID,120);
        //Wait for topology to run
        TestHelper.waitForTopology(120);        
        
        //collect Topology stats:THis method uses sampling which you can modify to suit your needs
        //Map<String,String> metrics = TestHelper.collectTopologyStats(TOPOLOGY_NAME, 60, 120);  
        
        //collect topology component stats
        Map<String,Long> cStats = TestHelper.getComponentStats(topo, SPOUT_ID);
        
        //System.out.println(metrics);
        System.out.println(cStats);
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

