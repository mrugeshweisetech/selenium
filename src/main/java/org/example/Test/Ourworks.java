package org.example.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.example.utils.ExtentReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class Ourworks {

    private WebDriver driver;
    private ExtentReports extent;
    private ExtentTest test;

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
    public void testOurWorksNavigationAndProjectClicks() {
        test = extent.createTest("Our Works Section Test with Screenshots");

        try {
            String baseUrl = "https://dev.weisetechdev.com/weisetech/";
            driver.get(baseUrl);
            Thread.sleep(2000);

            WebElement ourWorksMenu = driver.findElement(By.linkText("Our Works"));
            ourWorksMenu.click();
            test.info(" Clicked 'Our Works' from navbar");

            Thread.sleep(2000);
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(2000);

            List<WebElement> projectLinks = driver.findElements(By.cssSelector(".portfolio-block a"));

            if (projectLinks.size() >= 2) {
                test.info(" Found " + projectLinks.size() + " projects under Our Works");

                for (int i = 0; i < 2; i++) {
                    try {
                        driver.get(baseUrl);
                        Thread.sleep(1500);

                        driver.findElement(By.linkText("Our Works")).click();
                        Thread.sleep(1500);

                        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                        Thread.sleep(1000);

                        projectLinks = driver.findElements(By.cssSelector(".portfolio-block a"));
                        WebElement project = projectLinks.get(i);
                        String projectName = project.getText().trim();

                        project.click();
                        Thread.sleep(2000);

                        String title = driver.getTitle();
                        String screenshotPath = takeScreenshot("Project_" + (i + 1));

                        test.pass(" Clicked project: '" + projectName + "' | Page title: " + title,
                                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

                    } catch (Exception e) {
                        String screenshotPath = takeScreenshot("Error_Project_" + (i + 1));
                        test.fail(" Error clicking project " + (i + 1) + ": " + e.getMessage(),
                                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                    }
                }
            } else {
                test.fail(" Less than 2 projects found under Our Works");
            }

        } catch (Exception e) {
            String screenshotPath = takeScreenshot("OurWorks_Exception");
            test.fail(" Test setup failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
    }

    public String takeScreenshot(String screenshotName) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "screenshots/" + screenshotName + "_" + timestamp + ".png";
            File dest = new File(path);
            org.apache.commons.io.FileUtils.copyFile(src, dest);
            return path;
        } catch (Exception e) {
            System.out.println(" Screenshot capture failed: " + e.getMessage());
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
