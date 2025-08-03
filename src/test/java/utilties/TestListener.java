package utilties;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        // Initialize the report before any tests start
        ExtentManager.initReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentManager.createTest(
            result.getMethod().getMethodName(),
            result.getMethod().getDescription() != null ? result.getMethod().getDescription() : ""
        );
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.logTestResult(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentManager.logFail("Test failed: " + result.getThrowable().getMessage());
        //ExtentManager.logScreenshot("Failure Screenshot", "screenshots/fail_" + result.getName() + ".png");
        ExtentManager.logTestResult(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.logTestResult(result);
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flushReport();
    }
}
