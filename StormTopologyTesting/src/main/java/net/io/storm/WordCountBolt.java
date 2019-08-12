package net.io.storm;

import java.util.HashMap;
import java.util.Map;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
/**
 * This class is a Bolt which calculates emitted word counts from spout
 * @author hardik
 */
public class WordCountBolt extends BaseRichBolt
{

    private static final long serialVersionUID = 1L;

    private Map< String, Integer> counters;
    private Integer id;
    private String name;
    private String fileName;
    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector)
    {
        this.counters = new HashMap<>();
        this.name = context.getThisComponentId();
        this.id = context.getThisTaskId();
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple)
    {
        String word = tuple.getString(0);
        Integer count = counters.get(word);
        if(count == null)
        {
            count = 0;
        }
        count++;
        counters.put(word, count);
        collector.emit(tuple,new Values(word, count));
        collector.ack(tuple);
        System.out.println("Bolt:"+word);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        declarer.declare(new Fields("word", "count"));
    }

    @Override
    public void cleanup()
    {
//        System.out.println("Final word count:::::");
//        for(Map.Entry< String, Integer> entry : counters.entrySet())
//        {
//            System.out.println(entry.getKey() + "-" + entry.getValue());
//        }
    }

}
