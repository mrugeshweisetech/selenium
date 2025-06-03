package org.example.Test;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AGHomepagewordscount {
    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    @BeforeClass
    public void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeMethod
    public void openBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void countWordsOnHomePage() {
        test = extent.createTest("Word Count on Home Page");

        try {
            driver.get("https://development:development@weisetech.dev/adventuregamers/daily-deals/");
            Thread.sleep(2000);

            String text = driver.findElement(By.tagName("body")).getText();
            int wordCount = text.trim().split("\\s+").length;

            test.pass("Total words on homepage: " + wordCount);
            System.out.println("Total words: " + wordCount);

            String ss = takeScreenshot("word_count");
            test.info("📸 Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(ss).build());

        } catch (Exception e) {
            String ss = takeScreenshot("word_count_error");
            test.fail("Error: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(ss).build());
        }
    }

    public String takeScreenshot(String name) {
        try {
            File folder = new File("screenshots");
            if (!folder.exists()) folder.mkdir();

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "screenshots/" + name + "_" + timestamp + ".png";
            FileUtils.copyFile(src, new File(path));
            return path;
        } catch (Exception e) {
            return null;
        }
    }

    @AfterMethod
    public void closeBrowser() {
        if (driver != null) driver.quit();
    }

    @AfterClass
    public void flushReport() {
        extent.flush();
    }
}
