package BDD.Octane;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(//plugin="com.hpe.alm.octane.OctaneGherkinFormatter:gherkin-results\\RunCucumberTest_OctaneGherkinResults.xml",
features = "features", glue= {"BDD.Octane"})
public class RunCucumberTest {
}
