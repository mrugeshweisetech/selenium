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

public class Homepageimagecount{

    private WebDriver driver;
    private ExtentReports extent;

    @BeforeClass
    public void setupReport() {
        extent = ExtentReportManager.getReportInstance();
    }

    @BeforeMethod
    public void launchBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void countImagesAndTakeScreenshots() {
        ExtentTest test = extent.createTest( "Home Page Image Count & Screenshot Test" );

        try {
            String homeUrl = "https://dev.weisetechdev.com/weisetech/";
            driver.get(homeUrl);
            Thread.sleep(2000);

            List<WebElement> images = driver.findElements(By.tagName("img"));
            int imageCount = images.size();

            test.info("Total images found on homepage: " + imageCount);

            for (int i = 0; i < imageCount; i++) {
                try {
                    WebElement image = images.get(i);

                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", image);
                    Thread.sleep(500);

                    String screenshotPath = captureFullPageScreenshot("image_" + (i + 1));
                    String imgSrc = image.getAttribute("src");

                    test.pass("Screenshot captured for image " + (i + 1) + " — " + imgSrc,
                            MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

                } catch (Exception e) {
                    String screenshotPath = captureFullPageScreenshot("image_error_" + (i + 1));
                    test.fail("Failed to capture screenshot for image " + (i + 1) + ": " + e.getMessage(),
                            MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                }
            }

        } catch (Exception e) {
            String ss = captureFullPageScreenshot("home_image_count_error");
            test.fail("Test failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(ss).build());
        }
    }

    public String captureFullPageScreenshot(String name) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "screenshots/" + name + "_" + timestamp + ".png";
            File dest = new File(path);
            FileUtils.copyFile(src, dest);
            return path;
        } catch (Exception e) {
            System.out.println("Screenshot error: " + e.getMessage());
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
