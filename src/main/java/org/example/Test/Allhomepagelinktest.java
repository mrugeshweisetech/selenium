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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Allhomepagelinktest {

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
    public void testAllLinksOnHomePage() {
        test = extent.createTest("All Links Click Test on Home Page");

        try {
            String baseUrl = "https://dev.weisetechdev.com/weisetech/";
            driver.get(baseUrl);
            Thread.sleep(2000);

            List<WebElement> links = driver.findElements(By.tagName("a"));
            Set<String> visited = new HashSet<>();

            test.info(" Found " + links.size() + " anchor links on the homepage");

            for (int i = 0; i < links.size(); i++) {
                try {
                    WebElement link = links.get(i);
                    String href = link.getAttribute("href");

                    if (href == null || href.isEmpty() || href.startsWith("mailto:") || href.startsWith("javascript:")) {
                        test.warning("️ Skipping invalid link: " + href);
                        continue;
                    }

                    if (visited.contains(href)) {
                        test.info(" Skipping already visited link: " + href);
                        continue;
                    }

                    visited.add(href);

                    HttpURLConnection connection = (HttpURLConnection) new URL(href).openConnection();
                    connection.setRequestMethod("HEAD");
                    connection.connect();
                    int responseCode = connection.getResponseCode();

                    if (responseCode >= 400) {
                        String screenshotPath = takeScreenshot("broken_link_" + i);
                        test.fail(" Broken link: " + href + " (HTTP " + responseCode + ")",
                                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                    } else {
                        test.pass(" Working link: " + href + " (HTTP " + responseCode + ")");
                    }

                } catch (Exception e) {
                    String screenshotPath = takeScreenshot("link_exception_" + i);
                    test.fail(" Exception while checking link index " + i + ": " + e.getMessage(),
                            MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                }
            }

        } catch (Exception e) {
            String ss = takeScreenshot("homepage_links_exception");
            test.fail(" Failed to test homepage links: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(ss).build());
        }
    }

    public String takeScreenshot(String name) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "screenshots/" + name + "_" + timestamp + ".png";
            File dest = new File(path);
            FileUtils.copyFile(src, dest);
            return path;
        } catch (Exception e) {
            System.out.println(" Screenshot failed: " + e.getMessage());
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
