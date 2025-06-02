package org.example.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.io.FileUtils;
import org.example.utils.ExtentReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Contentaudittest {

    private WebDriver driver;
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeClass
    public void setupReport() {
        extent = ExtentReportManager.getReportInstance();
    }

    @BeforeMethod
    public void openBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void countImagesAndWordsOnModule() {
        test = extent.createTest("Content Audit: Image + Word Count");

        try {
            String moduleUrl = "https://dev.weisetechdev.com/weisetech/";
            driver.get(moduleUrl);
            Thread.sleep(2000);

            List<WebElement> images = driver.findElements(By.tagName("img"));
            int imageCount = images.size();
            test.pass("Total images on page: " + imageCount);
            System.out.println("Total images: " + imageCount);

            WebElement body = driver.findElement(By.tagName("body"));
            String text = body.getText();
            int wordCount = 0;
            if (text != null && !text.isEmpty()) {
                wordCount = text.trim().split("\\s+").length;
                test.pass("Total visible words: " + wordCount);
                System.out.println("Total words: " + wordCount);
            } else {
                test.warning("No visible text found.");
            }

            String screenshotPath = captureScreenshot("content_audit_success");
            test.info("Screenshot of module content:",
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

        } catch (Exception e) {
            String ss = captureScreenshot("content_audit_error");
            test.fail("Test failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(ss).build());
        }
    }

    public String captureScreenshot(String name) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "screenshots/" + name + "_" + timestamp + ".png";
            File dest = new File(path);
            FileUtils.copyFile(src, dest);
            return path;
        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
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
