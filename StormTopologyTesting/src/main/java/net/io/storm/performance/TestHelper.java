package net.io.storm.performance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.ExecutorStats;
import org.apache.storm.generated.ExecutorSummary;
import org.apache.storm.generated.KillOptions;
import org.apache.storm.generated.Nimbus;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.generated.TopologyInfo;
import org.apache.storm.st.utils.TimeUtil;
import org.apache.storm.st.wrapper.TopoWrap;
import org.apache.storm.thrift.TException;
import org.apache.storm.utils.NimbusClient;
import org.apache.storm.utils.ObjectReader;
import org.apache.storm.utils.Utils;

/**
 *
 * @author hardik
 */
public class TestHelper
{

    private static final int POLL_INTERVAL = 60;

    public static void kill(Nimbus.Iface client, String topoName) throws Exception
    {
        KillOptions opts = new KillOptions();
        opts.set_wait_secs(0);
        client.killTopologyWithOpts(topoName, opts);
    }
   
    /**
     * This function allows topology to run for specified time
     *
     * @param maxWaitSec
     * @throws TException
     */
    public static void waitForTopology(int maxWaitSec) throws TException
    {

        for (int i = 0; i < (maxWaitSec + 1) / 2; ++i) {
            
           // long emitCount = getAllTimeEmittedCount(componentName);
           
           TimeUtil.sleepSec(2);
        }

    }

    /**
     * THis function collects metrics for entire topology
     * 
     * @param topologyName
     * @param pollInterval
     * @param duration
     * @return
     * @throws Exception
     */
    public static Map<String, String> collectTopologyStats(String topologyName, Integer pollInterval, int duration) throws Exception
    {
        Map<String, Object> clusterConf = Utils.readStormConfig();
        Nimbus.Iface client = NimbusClient.getConfiguredClient(clusterConf).getClient();
        Map<String, String> metrics;
        try(BasicMetricsCollector metricsCollector = new BasicMetricsCollector(topologyName, clusterConf))
        {

            if(duration > 0)
            {
                int times = duration / pollInterval;
                metricsCollector.collect(client);
                for(int i = 0; i < times; i++)
                {
                    Thread.sleep(pollInterval * 1000);
                    metricsCollector.collect(client);
                }
            }
            else
            {
                while(true)
                { //until Ctrl-C
                    metricsCollector.collect(client);
                    Thread.sleep(pollInterval * 1000);
                }
            }
            metrics = metricsCollector.getAllMetrics();
        }
        return metrics;
    }

    /**
     * This functions returns stats of the specified topology component
     * @param topo
     * @param componentId
     * @return
     * @throws TException 
     */
    public static Map<String, Long> getComponentStats(final TopoWrap topo, final String componentId) throws TException
    {
        TopologyInfo info = topo.getInfo();
        final List<ExecutorSummary> executors = info.get_executors();
        Map<String, Long> stats = new HashMap<>();

        long emmited = executors.stream()
                .filter(summary -> summary != null && summary.get_component_id().equals(componentId))
                .mapToLong(summary ->
                {
                    ExecutorStats executorStats = summary.get_stats();
                    if(executorStats == null)
                    {
                        return 0L;
                    }
                    Map<String, Map<String, Long>> emitted = executorStats.get_emitted();
                    if(emitted == null)
                    {
                        return 0L;
                    }
                    Map<String, Long> allTime = emitted.get(":all-time");
                    if(allTime == null)
                    {
                        return 0L;
                    }
                    return allTime.get(Utils.DEFAULT_STREAM_ID);
                }).sum();

        long transfered = executors.stream()
                .filter(summary -> summary != null && summary.get_component_id().equals(componentId))
                .mapToLong(summary ->
                {
                    ExecutorStats executorStats = summary.get_stats();
                    if(executorStats == null)
                    {
                        return 0L;
                    }
                    Map<String, Map<String, Long>> trans = executorStats.get_transferred();
                    if(trans == null)
                    {
                        return 0L;
                    }
                    Map<String, Long> allTime = trans.get(":all-time");
                    if(allTime == null)
                    {
                        return 0L;
                    }
                    return allTime.get(Utils.DEFAULT_STREAM_ID);
                }).sum();

        stats.put("emmited", emmited);
        stats.put("transfered", transfered);

        return stats;
    }

}
