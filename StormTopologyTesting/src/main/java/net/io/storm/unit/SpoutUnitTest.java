package net.io.storm.unit;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.tuple.Values;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * This class demonstrates how to unit test Spout
 * 
 */
public class SpoutUnitTest
{
    @Test
    public void testWordReaderSpout()
    {
        // given
        WordReaderSpout spout = new WordReaderSpout();
        SpoutOutputCollector collector = mock(SpoutOutputCollector.class);
        spout.open(null, null, collector);

        // when
        spout.nextTuple();
        
        // then
        verify(collector).emit(new Values("Hello"));
        verify(collector).emit(new Values("World"));
        verify(collector).emit(new Values("hello"));
    }
}
