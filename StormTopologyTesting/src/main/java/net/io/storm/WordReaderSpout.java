package net.io.storm;

import java.util.Map;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.st.utils.TimeUtil;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.testing.CompletableSpout;
import org.apache.storm.testing.TestWordSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
/**
 * THis class is a Spout which emits words from a sentence
 * @author hardik
 */
public class WordReaderSpout extends TestWordSpout implements CompletableSpout
{

    private static final long serialVersionUID = 1L;

    public SpoutOutputCollector collector;
    private String[] sentences =
    {
        "Hello World",       
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
       
            for(String sentence : sentences)
            {
                for(String word : sentence.split(" "))
                {
                    System.out.println("Emmited:" + word);
                    collector.emit(new Values(word));
                }
            }
            TimeUtil.sleepMilliSec(100);
            //isCompleted = true;
                
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
