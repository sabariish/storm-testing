package net.io.storm.unit;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import java.util.HashMap;
import java.util.Map;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
/**
 * This class represents a Bolt which interacts with mongoDB
 */
public class WordCountBoltMongo extends BaseRichBolt
{

    private static final long serialVersionUID = 1L;

    private Map< String, Integer> counters;
    private Integer id;
    private String name;
    private String fileName;
    private OutputCollector collector;
    private DBCollection mongoCollection;
    
    public WordCountBoltMongo(MongoClient mongo)
    {
        DB db = mongo.getDB("foo");
        this.mongoCollection = db.getCollection("bar");
    }
    
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector)
    {
        this.counters = new HashMap<>();
//        this.name = context.getThisComponentId();
//        this.id = context.getThisTaskId();
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
        
        BasicDBObjectBuilder documentBuilderDetail = BasicDBObjectBuilder.start()
	.add("word", word)
	.add("count", count);        
        mongoCollection.insert(documentBuilderDetail.get());
        //collector.emit(new Values(word, count));
        collector.ack(tuple);
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
