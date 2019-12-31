package BDD.Octane;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "features", glue= {"BDD.Octane"})
public class RunCucumberTest {
}
