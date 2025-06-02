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

public class Uiinteractiontest {

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
    public void testUIInteractions() {
        test = extent.createTest("UI Element Interaction Test");

        try {
            String url = "https://dev.weisetechdev.com/weisetech/";
            driver.get(url);
            Thread.sleep(2000);

            WebElement aboutUs = driver.findElement(By.linkText("About Us"));
            if (aboutUs.isDisplayed() && aboutUs.isEnabled()) {
                aboutUs.click();
                Thread.sleep(1500);
                String ss1 = takeScreenshot("click_about_us");
                test.pass("Clicked 'About Us' link",
                        MediaEntityBuilder.createScreenCaptureFromPath(ss1).build());
            }

            driver.get(url + "contact-us");
            Thread.sleep(1500);

            WebElement nameField = driver.findElement(By.name("name"));
            nameField.sendKeys("Test User");
            test.pass("Typed into 'Name' field");

            WebElement emailField = driver.findElement(By.name("email"));
            emailField.sendKeys("test@example.com");
            test.pass("Typed into 'Email' field");

            WebElement messageField = driver.findElement(By.name("message"));
            messageField.sendKeys("This is a UI interaction test.");
            test.pass("Typed into 'Message' field");

            String ss2 = takeScreenshot("form_fields_filled");
            test.info("Form fields filled",
                    MediaEntityBuilder.createScreenCaptureFromPath(ss2).build());

            WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
            submitBtn.click();
            Thread.sleep(1000);

            String ss3 = takeScreenshot("form_submitted");
            test.pass("Clicked 'Submit' button",
                    MediaEntityBuilder.createScreenCaptureFromPath(ss3).build());

        } catch (Exception e) {
            String ss = takeScreenshot("ui_interaction_error");
            test.fail("Exception during UI interaction: " + e.getMessage(),
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
