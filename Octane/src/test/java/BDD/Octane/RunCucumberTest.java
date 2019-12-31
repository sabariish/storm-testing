package BDD.Octane;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin="com.hpe.alm.octane.OctaneGherkinFormatter:gherkin-results\\RunCucumberTest_OctaneGherkinResults.xml",
features = "features", glue= {"BDD.Octane"})
public class RunCucumberTest {
}
