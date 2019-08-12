package net.io.storm.integration;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.stream.Collectors;
import junit.framework.TestCase;
import org.apache.storm.Config;
import org.apache.storm.st.wrapper.StormCluster;
import org.apache.storm.st.wrapper.TopoWrap;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
/**
 * This class represents Exclamation word topology
 * @author hardik
 */
public final class IntegrationTest_2 extends TestCase
{
    protected final String topologyName = "ExclamationTopology";
        
    private static final List<String> exclaim2Output = ExclamationTopology.FixedOrderWordSpout.WORDS.stream()
            .map(word -> word + "!!!!!!")
            .collect(Collectors.toList());
    private StormCluster cluster;
    private TopoWrap topo;
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
    public void testExclamationTopology() throws Exception
    {
        topo = new TopoWrap(cluster, topologyName, ExclamationTopology.getStormTopology());
        topo.submitSuccessfully(configMap);
        
        final int minSpountEmits = 1000;
         //This assertion is required to capture the out put of spout and bolts
        topo.assertProgress(minSpountEmits, ExclamationTopology.SPOUT_EXECUTORS, ExclamationTopology.WORD, 180);
        //Get the log of specified component from topology
        final String actualOutput = topo.getLogs(ExclamationTopology.EXCLAIM_2);
        for(String oneExpectedOutput : exclaim2Output)
        {
            Assert.assertTrue(actualOutput.contains(oneExpectedOutput), "Couldn't find " + oneExpectedOutput + " in urls");
        }
    }

    @AfterTest
    @Override
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
