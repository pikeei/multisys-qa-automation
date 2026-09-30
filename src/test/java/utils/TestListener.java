package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import tests.BaseTest;

/** Writes each test result to the HTML report (target/extent-report/index.html). */
public class TestListener implements ITestListener {

    private static final ExtentReports REPORT = createReport();
    private ExtentTest test;

    private static ExtentReports createReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("target/extent-report/index.html");
        spark.config().setDocumentTitle("Multisys QA Automation Report");
        spark.config().setReportName("UI and API Test Results");
        ExtentReports report = new ExtentReports();
        report.attachReporter(spark);
        return report;
    }

    @Override
    public void onTestStart(ITestResult result) {
        test = REPORT.createTest(nameOf(result), result.getMethod().getDescription());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String screenshot = takeScreenshot(result);
        if (screenshot != null) {
            test.fail(result.getThrowable(),
                    MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build());
        } else {
            test.fail(result.getThrowable());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        REPORT.createTest(nameOf(result)).skip(String.valueOf(result.getThrowable()));
    }

    @Override
    public void onFinish(ITestContext context) {
        REPORT.flush();
    }

    private String nameOf(ITestResult result) {
        String name = result.getMethod().getMethodName();
        Object[] params = result.getParameters();
        return params.length > 0 ? name + " [" + params[0] + "]" : name;
    }

    /** Only UI tests have a browser; API tests return null (no screenshot). */
    private String takeScreenshot(ITestResult result) {
        Object testClass = result.getInstance();
        if (!(testClass instanceof BaseTest)) {
            return null;
        }
        WebDriver driver = ((BaseTest) testClass).getDriver();
        if (driver == null) {
            return null;
        }
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            return null;
        }
    }
}
