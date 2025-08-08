package stepDefinition;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utilties.ExtentManager;

public class TestHooks {
	
    @Before
    public void beforeScenario(Scenario scenario) {
        // Create a test node in ExtentReport for the scenario
    	ExtentManager.initReport();
        ExtentManager.createTest(scenario.getName(), "BDD Scenario Execution");
        
        
    }
    
    @After
    public void afterSceanrio() {
    	ExtentManager.flushReport();
    }
    
}
