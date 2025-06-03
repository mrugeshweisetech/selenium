package org.example.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.io.FileUtils;
//import org.example.utils.ExtentReportManager;
import org.example.utils.ExtentReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
//import utils.ExtentReportManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AGHomepagevideotest {

    private WebDriver driver;
    private ExtentReports extent;
    private ExtentTest test;

    String url = "https://development:development@weisetech.dev/adventuregamers/daily-deals/";

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
    public void playVideoIfPresent() {
        test = extent.createTest("Video Detection and Play Test");

        try {
            driver.get(url);
            Thread.sleep(3000);

            boolean videoPlayed = false;

            List<WebElement> videoTags = driver.findElements(By.tagName("video"));
            if (!videoTags.isEmpty()) {
                WebElement video = videoTags.get(0);
                ((JavascriptExecutor) driver).executeScript("arguments[0].play()", video);
                Thread.sleep(4000);
                ((JavascriptExecutor) driver).executeScript("arguments[0].pause()", video);

                String ss = takeScreenshot("html5_video_played");
                test.pass("HTML5 video played and paused",
                        MediaEntityBuilder.createScreenCaptureFromPath(ss).build());
                videoPlayed = true;
            }

            if (!videoPlayed) {
                List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
                for (WebElement frame : iframes) {
                    String src = frame.getAttribute("src");
                    if (src != null && src.contains("youtube")) {
                        driver.switchTo().frame(frame);
                        try {
                            WebElement playBtn = driver.findElement(By.cssSelector("button[aria-label='Play']"));
                            playBtn.click();
                            Thread.sleep(5000);
                            String ss = takeScreenshot("iframe_video_played");
                            test.pass("YouTube iframe video played",
                                    MediaEntityBuilder.createScreenCaptureFromPath(ss).build());
                        } catch (Exception e) {
                            test.warning("Couldn't auto-play iframe video: " + e.getMessage());
                        }
                        driver.switchTo().defaultContent();
                        videoPlayed = true;
                        break;
                    }
                }
            }

            if (!videoPlayed) {
                test.skip("No playable video found on the page.");
            }

        } catch (Exception e) {
            String ss = takeScreenshot("video_test_error");
            test.fail("Exception during video test: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(ss).build());
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

