package org.example.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.example.utils.ExtentReportManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

class Contactus {

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
    public void fillContactUsForm() {
        ExtentTest test = extent.createTest( "Contact Us Form Test" );

        try {

            driver.get("https://dev.weisetechdev.com/weisetech/contact-us");
            Thread.sleep(2000);


            WebElement name = driver.findElement(By.name("name"));
            WebElement email = driver.findElement(By.name("email"));
            WebElement subject = driver.findElement(By.name("subject"));
            WebElement message = driver.findElement(By.name("message"));

            name.sendKeys("Test User");
            email.sendKeys("test@example.com");
            subject.sendKeys("Testing Contact Form");
            message.sendKeys("This is a sample automated message using Selenium + TestNG.");

            test.info("✅ Filled form fields");


            WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
            submitButton.click();

            test.info("✅ Clicked submit");

            Thread.sleep(2000); // wait for confirmation


            boolean submitted = driver.getPageSource().toLowerCase().contains("thank you")
                    || driver.getPageSource().toLowerCase().contains("success");

            if (submitted) {
                test.pass(" Contact form submitted successfully");
            } else {
                test.fail(" Contact form did not submit properly or no success message found.");
            }

        } catch (Exception e) {
            test.fail(" Exception during test: " + e.getMessage());
        }
    }

    @AfterMethod
    public void closeBrowser() {
        if (driver != null) driver.quit();
    }

    @AfterClass
    public void tearDownReport() {
        extent.flush();
    }
}
