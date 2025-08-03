package utilties;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestResult;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExtentManager {

    private static ExtentReports extent;
    private static final Map<Long, ExtentTest> testMap = new HashMap<>();
    private static final String REPORT_PATH = System.getProperty("user.dir") + "/reports/ExtentReport.html";

    // Initialize Extent Report
    public static void initReport() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_PATH);
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle("API & UI Test Report");
            spark.config().setReportName("Automation Report");
            spark.config().setTimelineEnabled(true);

            // Custom CSS for better appearance
            spark.config().setCss(".badge-primary {background-color: #df4759}");
            spark.config().setCss(".pass-bg, .chart-color1 { background-color: #28a745 !important; }");
            spark.config().setCss(".fail-bg, .chart-color2 { background-color: #dc3545 !important; }");
            spark.config().setCss(".skip-bg, .chart-color3 { background-color: #ffc107 !important; }");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            // Environment/system info
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Environment", "QA");
        }
    }

    // Create test entry
    public static void createTest(String testName, String description) {
        ExtentTest test = extent.createTest(testName, description);
        testMap.put(Thread.currentThread().getId(), test);
    }

    // Log a PASS status
    public static void logPass(String message) {
        getTest().pass(message);
    }

    // Log a FAIL status
    public static void logFail(String message) {
        getTest().fail(message);
    }

    // Log an INFO message
    public static void logInfo(String message) {
        getTest().info(message);
    }

    // Log a WARNING message
    public static void logWarning(String message) {
        getTest().warning(message);
    }

    // Log a screenshot with a caption
    public static void logScreenshot(String caption, String imagePath) {
        getTest().addScreenCaptureFromPath(imagePath, caption);
    }

    // Log API request in JSON format
    public static void logApiRequest(String request) {
        getTest().info(MarkupHelper.createCodeBlock(request, CodeLanguage.JSON));
    }

    // Log API response in JSON format
    public static void logApiResponse(String response) {
        getTest().info(MarkupHelper.createCodeBlock(response, CodeLanguage.JSON));
    }

    // Log full API Test - endpoint, method, request, response, status code
    public static void logApiTest(String endpoint, String method, String request, String response, int statusCode) {
        ExtentTest node = getTest().createNode("API: " + method + " " + endpoint);
        node.info("Endpoint: " + endpoint);
        node.info("Method: " + method);
        node.info(MarkupHelper.createLabel("Status Code: " + statusCode, getStatusColor(statusCode)));
        node.info("Request:");
        node.info(MarkupHelper.createCodeBlock(request, CodeLanguage.JSON));
        node.info("Response:");
        node.info(MarkupHelper.createCodeBlock(response, CodeLanguage.JSON));
    }

    // Return color based on HTTP status code
    private static ExtentColor getStatusColor(int statusCode) {
        if (statusCode >= 200 && statusCode < 300) return ExtentColor.GREEN;
        if (statusCode >= 400 && statusCode < 500) return ExtentColor.AMBER;
        return ExtentColor.RED;
    }

    // Log the result of a test (pass/fail/skip)
    public static void logTestResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            getTest().fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            getTest().skip(result.getThrowable());
        } else {
            getTest().pass("Test passed");
        }
    }

    // Flush the report and open it
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
            try {
                Desktop.getDesktop().browse(new File(REPORT_PATH).toURI());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Get the current test object using thread ID
    private static ExtentTest getTest() {
        return testMap.get(Thread.currentThread().getId());
    }
}
