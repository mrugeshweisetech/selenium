package org.example.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.example.utils.ExtentReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;
import java.util.Set;

class SocialmediafooterTest {

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
    public void clickSocialMediaLinks() {
        ExtentTest test = extent.createTest( "Social Media Footer Links Test" );

        try {
            String baseUrl = "https://dev.weisetechdev.com/weisetech/";
            driver.get(baseUrl);
            Thread.sleep(2000);


            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1500);


            List<WebElement> socialLinks = driver.findElements(By.cssSelector("footer a"));


            String[] platforms = {"instagram", "linkedin", "facebook"};

            for (String platform : platforms) {
                boolean found = false;

                for (WebElement link : socialLinks) {
                    String href = link.getAttribute("href");
                    if (href != null && href.toLowerCase().contains(platform)) {
                        found = true;
                        String originalWindow = driver.getWindowHandle();


                        ((JavascriptExecutor) driver).executeScript("window.open(arguments[0]);", href);
                        Thread.sleep(2000);


                        Set<String> allTabs = driver.getWindowHandles();
                        for (String tab : allTabs) {
                            if (!tab.equals(originalWindow)) {
                                driver.switchTo().window(tab);
                                break;
                            }
                        }

                        Thread.sleep(2000);
                        String tabTitle = driver.getTitle();
                        test.pass(" Clicked " + platform + " | Opened page title: " + tabTitle);

                        // Close new tab and return
                        driver.close();
                        driver.switchTo().window(originalWindow);
                        break;
                    }
                }

                if (!found) {
                    test.warning(" No footer link found for: " + platform);
                }
            }

        } catch (Exception e) {
            test.fail(" Social media footer test failed: " + e.getMessage());
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
