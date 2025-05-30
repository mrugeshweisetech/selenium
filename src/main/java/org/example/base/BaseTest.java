package org.example.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.example.utils.ExtentReportManager;
import org.example.utils.Logs;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

public class BaseTest {

    protected WebDriver driver;
    protected static ExtentReports extent;
    protected ExtentTest tests;
    protected ExtentReportManager ExtentReportsManager;

    @BeforeSuite
    public void setupReport() {
        extent = ExtentReportManager.getReportInstance();
    }

    @AfterSuite
    public void teardownReport() {
        extent.flush();
    }

    @BeforeMethod
    public void setup(Method method) {
        Logs.info("Starting WebDriver...");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        Logs.info("Navigating to URL...");
        driver.get("https://admin-demo.nopcommerce.com/login");

        // Initialize ExtentTest for the current test method
        tests = extent.createTest(method.getName());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ExtentReportManager.captureScreenshot(driver, result.getName());
            tests.fail("Test Failed... Check Screenshot!!!",
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            tests.pass("Test Passed Successfully.");
        } else if (result.getStatus() == ITestResult.SKIP) {
            tests.skip("Test Skipped.");
        }

        if (driver != null) {
            Logs.info("Closing Browser...");
            driver.quit();
        }
    }
}