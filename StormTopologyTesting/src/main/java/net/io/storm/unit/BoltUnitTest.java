package net.io.storm.unit;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This class demonstrates how to unit test a Bolt
 * @author hardik
 */
public class BoltUnitTest
{
    
    @Test
    public void testWordCountSpout()
    {
        // given
        WordCountBolt bolt = new WordCountBolt();
        OutputCollector collector = mock(OutputCollector.class);
       
        Tuple t = mock(Tuple.class);       
        when(t.getString(0)).thenReturn("Hello");
        
        bolt.prepare(null, null, collector);

        // when
        bolt.execute(t);
        // then
        verify(collector).emit(new Values("Hello",1));
       
    }
}
