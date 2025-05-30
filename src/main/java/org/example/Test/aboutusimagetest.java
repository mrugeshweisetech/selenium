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
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class aboutusimagetest {

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
    public void checkBrokenImagesOnAboutUs() {
        test = extent.createTest("Broken Image Check on About Us Page");

        try {
            String aboutUsUrl = "https://dev.weisetechdev.com/weisetech/about-us";
            driver.get(aboutUsUrl);
            Thread.sleep(2000);

            List<WebElement> images = driver.findElements(By.tagName("img"));
            test.info(" Total images found: " + images.size());

            int brokenCount = 0;

            for (int i = 0; i < images.size(); i++) {
                try {
                    WebElement img = images.get(i);
                    String imgSrc = img.getAttribute("src");

                    if (imgSrc == null || imgSrc.isEmpty()) {
                        test.warning(" Image src is empty or missing.");
                        continue;
                    }

                    HttpURLConnection connection = (HttpURLConnection) new URL(imgSrc).openConnection();
                    connection.setRequestMethod("HEAD");
                    connection.connect();
                    int responseCode = connection.getResponseCode();

                    if (responseCode >= 400) {
                        brokenCount++;
                        String ssPath = takeScreenshot("broken_image_aboutus_" + i);
                        test.fail("Broken Image: " + imgSrc + " (HTTP " + responseCode + ")",
                                MediaEntityBuilder.createScreenCaptureFromPath(ssPath).build());
                    } else {
                        test.pass(" Valid Image: " + imgSrc);
                    }

                } catch (Exception e) {
                    String ssPath = takeScreenshot("exception_aboutus_image_" + i);
                    test.fail(" Exception checking image " + i + ": " + e.getMessage(),
                            MediaEntityBuilder.createScreenCaptureFromPath(ssPath).build());
                }
            }

            if (brokenCount == 0) {
                test.pass(" All images on the About Us page are working properly.");
            } else {
                test.warning(" Total broken images found: " + brokenCount);
            }

        } catch (Exception e) {
            String ssPath = takeScreenshot("aboutus_exception");
            test.fail(" Test failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(ssPath).build());
        }
    }

    public String takeScreenshot(String name) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "screenshots/" + name + "_" + timestamp + ".png";
            File test = new File(path);
            FileUtils.copyFile(src, test);
            return path;
        } catch (Exception e) {
            System.out.println("Screenshot capture failed: " + e.getMessage());
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

