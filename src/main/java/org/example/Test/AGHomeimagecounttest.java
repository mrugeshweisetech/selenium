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

public class AGHomeimagecounttest {

    private WebDriver driver;
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeClass
    public void setupExtent() {
        extent = ExtentReportManager.getReportInstance();
    }

    @BeforeMethod
    public void launchBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void countImagesOnHomePage() {
        test = extent.createTest("Image Count on Home Page");

        try {
//            String url = "https://weisetech.dev/adventuregamers/";
            driver.get("https://development:development@weisetech.dev/adventuregamers/daily-deals/");

            Thread.sleep(2000);

            List<WebElement> images = driver.findElements(By.tagName("img"));
            int count = images.size();

            System.out.println("Total images found: " + count);
            test.pass("Total images found on home page: " + count);

            String screenshot = takeScreenshot("image_count_success");
            test.info("Screenshot attached",
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());

        } catch (Exception e) {
            String screenshot = takeScreenshot("image_count_error");
            test.fail("Exception: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());
        }
    }

    public String takeScreenshot(String fileName) {
        try {
            File folder = new File("screenshots");
            if (!folder.exists()) folder.mkdir();

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "screenshots/" + fileName + "_" + timestamp + ".png";
            FileUtils.copyFile(src, new File(path));
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

