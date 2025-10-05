package api.utilities;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentReportManager implements ITestListener {

    private static ExtentReports extent;
    private static ExtentSparkReporter spark;
    private static final Map<Long, ExtentTest> tests = new ConcurrentHashMap<>();
    private static String reportDir;
    private static String reportFile;

    @Override
    public synchronized void onStart(ITestContext context) {
        try {
            String ts = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            reportDir = "reports/extent";
            reportFile = "API-Test-Report-" + ts + ".html";

            Path outDir = Paths.get(reportDir);
            if (!Files.exists(outDir)) {
                Files.createDirectories(outDir);
            }

            spark = new ExtentSparkReporter(Paths.get(reportDir, reportFile).toString());
            spark.config().setDocumentTitle("RestAssured PETSTORE API Automation Report");
            spark.config().setReportName("RestAssured + TestNG Results");
            spark.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Framework", "RestAssured + TestNG");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("Environment", context.getSuite().getParameter("env") != null
                    ? context.getSuite().getParameter("env") : "QA");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ExtentReports", e);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(testName)
                .assignCategory(result.getMethod().getGroups());
        tests.put(Thread.currentThread().getId(), test);
        test.log(Status.INFO, "Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = tests.get(Thread.currentThread().getId());
        if (test == null) {
            test = extent.createTest(result.getMethod().getMethodName())
                    .assignCategory(result.getMethod().getGroups());
        }
        test.log(Status.PASS, "Test passed: " + result.getMethod().getMethodName());
        attachParametersIfAny(test, result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = tests.get(Thread.currentThread().getId());
        if (test == null) {
            test = extent.createTest(result.getMethod().getMethodName())
                    .assignCategory(result.getMethod().getGroups());
        }
        test.log(Status.FAIL, "Test failed: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            test.fail(result.getThrowable());
        }
        attachParametersIfAny(test, result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = tests.get(Thread.currentThread().getId());
        if (test == null) {
            test = extent.createTest(result.getMethod().getMethodName())
                    .assignCategory(result.getMethod().getGroups());
        }
        test.log(Status.SKIP, "Test skipped: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            test.skip(result.getThrowable());
        }
        attachParametersIfAny(test, result);
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
        System.out.println("Extent report: " + Paths.get(reportDir, reportFile).toAbsolutePath());
    }

    // Helper to add parameters for @DataProvider or @Parameters runs
    private void attachParametersIfAny(ExtentTest test, ITestResult result) {
        Object[] params = result.getParameters();
        if (params != null && params.length > 0) {
            test.info("Parameters: " + Arrays.toString(params));
        }
    }

    // Optional accessor for tests wanting to log into current test
    public static ExtentTest getTest() {
        return tests.get(Thread.currentThread().getId());
    }

    // Optional convenience logging methods
    public static void info(String msg) {
        ExtentTest t = getTest();
        if (t != null) t.log(Status.INFO, msg);
    }

    public static void warn(String msg) {
        ExtentTest t = getTest();
        if (t != null) t.log(Status.WARNING, msg);
    }
}
