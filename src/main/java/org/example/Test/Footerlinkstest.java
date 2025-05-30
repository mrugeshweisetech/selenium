package org.example.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.example.utils.ExtentReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;

class FooterlinksTest {

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
    public void clickAllFooterLinks() {
        test = extent.createTest("Footer Links Click Test");

        try {
            String baseUrl = "https://dev.weisetechdev.com/weisetech/";
            driver.get(baseUrl);
            Thread.sleep(2000);

            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1500);


            List<WebElement> footerLinks = driver.findElements(By.cssSelector("footer a"));
            test.info("✅ Found " + footerLinks.size() + " footer links.");

            for (int i = 0; i < footerLinks.size(); i++) {
                try {

                    driver.get(baseUrl);
                    Thread.sleep(1500);
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                    Thread.sleep(1000);


                    footerLinks = driver.findElements(By.cssSelector("footer a"));
                    WebElement link = footerLinks.get(i);

                    String linkText = link.getText().trim();
                    if (linkText.isEmpty()) {
                        linkText = link.getAttribute("href"); // fallback
                    }

                    link.click();
                    Thread.sleep(2000);

                    String pageTitle = driver.getTitle();
                    test.pass(" Clicked '" + linkText + "' | Title: " + pageTitle);

                } catch (Exception e) {
                    test.fail(" Error clicking footer link at index " + i + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            test.fail(" Footer test setup failed: " + e.getMessage());
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
