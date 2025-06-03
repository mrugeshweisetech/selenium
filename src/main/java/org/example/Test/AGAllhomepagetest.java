package org.example.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.io.FileUtils;
import org.example.utils.ExtentReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
//import utils.ExtentReportManager;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AGAllhomepagetest {

    private WebDriver driver;
    private ExtentReports extent;
    private ExtentTest test;

    private final String homeUrl = "https://development:development@weisetech.dev/adventuregamers/daily-deals/";

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
    public void clickAllHomePageLinks() {
        test = extent.createTest("Click All Links on Homepage");

        try {
            driver.get(homeUrl);
            Thread.sleep(2000);

            List<WebElement> linkElements = driver.findElements(By.tagName("a"));
            Set<String> linkSet = new LinkedHashSet<>();

            for (WebElement link : linkElements) {
                String href = link.getAttribute("href");
                if (href != null && !href.trim().isEmpty() &&
                        !href.startsWith("mailto:") &&
                        !href.startsWith("javascript:")) {
                    linkSet.add(href.trim());
                }
            }

            test.info("Total clickable links found: " + linkSet.size());

            int count = 0;
            for (String link : linkSet) {
                try {
                    driver.get(homeUrl);
                    Thread.sleep(1500);

                    WebElement target = driver.findElement(By.xpath("//a[@href='" + link + "']"));

                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", target);
                    target.click();
                    Thread.sleep(2000);

                    String ss = takeScreenshot("link_click_success_" + count);
                    test.pass("Clicked link: " + link,
                            MediaEntityBuilder.createScreenCaptureFromPath(ss).build());

                } catch (Exception e) {
                    String ss = takeScreenshot("link_click_error_" + count);
                    test.fail("Failed to click link: " + link + " | Error: " + e.getMessage(),
                            MediaEntityBuilder.createScreenCaptureFromPath(ss).build());
                }
                count++;
            }

        } catch (Exception e) {
            String ss = takeScreenshot("homepage_link_test_error");
            test.fail("Overall test failure: " + e.getMessage(),
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

