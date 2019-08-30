package net.io.storm.unit;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.tuple.Tuple;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This class verifies Bolt interaction with mongoDB
 * 
 */
public class BoltMongoTest
{
           
    @Test
    public void testWordCountSpout()
    {
        MongoClient mongo = mock(MongoClient.class);
        DB db = mock(DB.class);
        DBCollection dbCollection = mock(DBCollection.class);

        when(mongo.getDB("foo")).thenReturn(db);
        when(db.getCollection("bar")).thenReturn(dbCollection);
        // given
        WordCountBoltMongo bolt = new WordCountBoltMongo(mongo);
        OutputCollector collector = mock(OutputCollector.class);
       
        Tuple t = mock(Tuple.class);       
        when(t.getString(0)).thenReturn("Hello");
        
        bolt.prepare(null, null, collector);

        // when
        bolt.execute(t);
        // then
        BasicDBObject obj = new BasicDBObject();
        obj.put("word","Hello");
        obj.put("count",1);
        
        verify(dbCollection).insert(obj);
       
    }
}
