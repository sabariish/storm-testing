package net.io.storm;

import net.io.storm.integration.IntegrationTest_1;
import net.io.storm.integration.IntegrationTest_2;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * This class is JUnit test case runner 
 * @author hardik
 */
public class TestRunner
{

    public static void main(String[] args)
    {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        //Add one or more Test case classes here
        Result result = junit.run(StormTopologyTest.class);
        resultReport(result);
        System.exit(0);
    }
    
    private static void resultReport(Result result) {
    System.out.println("Finished. Result: Failures: " +
      result.getFailureCount() + ". Ignored: " +
      result.getIgnoreCount() + ". Tests run: " +
      result.getRunCount() + ". Time: " +
      result.getRunTime() + "ms.");
    }
}
