package org.example.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.example.utils.ExtentReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class Homepagewordcount {

    private WebDriver driver;
    private ExtentReports extent;

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
    public void countWordsOnAboutUsPage() {
        ExtentTest test = extent.createTest( "Word Count on Home Page" );

        try {
            String url = "https://dev.weisetechdev.com/weisetech/";
            driver.get(url);
            Thread.sleep(2000);


            WebElement body = driver.findElement(By.tagName("body"));
            String text = body.getText();

            if (text != null && !text.isEmpty()) {
                String[] words = text.trim().split("\\s+");
                int wordCount = words.length;

                test.pass(" Total visible words on Home page: " + wordCount);
                System.out.println(" Total words on Home page: " + wordCount);
            } else {
                test.warning("️No text found on About Us page.");
            }

        } catch (Exception e) {
            test.fail(" Exception occurred: " + e.getMessage());
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
