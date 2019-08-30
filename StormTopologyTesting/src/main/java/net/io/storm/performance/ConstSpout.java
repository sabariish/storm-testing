package net.io.storm.performance;

import java.util.ArrayList;
import java.util.Map;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.ObjectReader;

/**
 *
 * @author hardik
 */
public class ConstSpout extends BaseRichSpout
{

    private static final String DEFAUT_FIELD_NAME = "str";
    private String value;
    private String fieldName = DEFAUT_FIELD_NAME;
    private SpoutOutputCollector collector = null;
    private int count = 0;
    private Long sleep = 0L;
    private int ackCount = 0;

    public ConstSpout(String value)
    {
        this.value = value;
    }

    public ConstSpout withOutputFields(String fieldName)
    {
        this.fieldName = fieldName;
        return this;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        declarer.declare(new Fields(fieldName));
    }

    @Override
    public void open(Map<String, Object> conf, TopologyContext context, SpoutOutputCollector collector)
    {
        this.collector = collector;
        this.sleep = ObjectReader.getLong(conf.get("spout.sleep"), 10L);
    }

    @Override
    public void nextTuple()
    {
        ArrayList<Object> tuple = new ArrayList<Object>(1);
        tuple.add(value);
        collector.emit(tuple, count++);
        try
        {
            if(sleep > 0)
            {
                Thread.sleep(sleep);
            }
        }
        catch(InterruptedException e)
        {
            return;
        }
    }

    @Override
    public void ack(Object msgId)
    {
        ackCount++;
        super.ack(msgId);
    }
}
