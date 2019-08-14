package net.io.storm.unit;

import java.util.Map;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.testing.CompletableSpout;
import org.apache.storm.testing.TestWordSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
/**
 * This class represents a Word reader Spout
 */
public class WordReaderSpout extends TestWordSpout implements CompletableSpout
{

    public SpoutOutputCollector collector;
    private String[] sentences =
    {
        "Hello World",
        //"Apache Storm",
        //"Big Data",        
        //"World",
        "hello"
    };
    boolean isCompleted=false;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector)
    {
        this.collector = collector;
    }

    @Override
    public void nextTuple()
    {
        if(!isCompleted)
        {
            for(String sentence : sentences)
            {
                for(String word : sentence.split(" "))
                {
                    System.out.println("Emmited:" + word);
                    collector.emit(new Values(word));
                }
            }
            isCompleted = true;
        }
        else
        {
            this.close();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        declarer.declare(new Fields("word"));
    }

    @Override
    public boolean isExhausted()
    {
        return this.isCompleted;
    }

}
