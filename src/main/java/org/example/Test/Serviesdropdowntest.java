package org.example.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.example.utils.ExtentReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;

class Servicesdropdowntest {

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
    public void testServicesDropdownNavigation() {
        test = extent.createTest("Services Dropdown Navigation Test");

        try {
            String homeUrl = "https://dev.weisetechdev.com/weisetech/";
            driver.get(homeUrl);
            Thread.sleep(2000);

            WebElement servicesMenu = driver.findElement(By.xpath("//a[contains(text(),'Services')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", servicesMenu);
            Thread.sleep(500);
            servicesMenu.click();
            Thread.sleep(1000);

            List<WebElement> serviceDropdownItems = driver.findElements(By.cssSelector("ul.dropdown-menu li a"));
            test.info("Found " + serviceDropdownItems.size() + " dropdown items under Services.");

            for (int i = 0; i < serviceDropdownItems.size(); i++) {
                try {
                    // Refresh home page and re-fetch elements to avoid stale reference
                    driver.get(homeUrl);
                    Thread.sleep(1500);

                    WebElement menu = driver.findElement(By.xpath("//a[contains(text(),'Services')]"));
                    menu.click();
                    Thread.sleep(1000);

                    serviceDropdownItems = driver.findElements(By.cssSelector("ul.dropdown-menu li a"));
                    WebElement item = serviceDropdownItems.get(i);
                    String linkText = item.getText().trim();

                    item.click();
                    Thread.sleep(2000);

                    String title = driver.getTitle();
                    test.pass("Clicked service: '" + linkText + "' | Page title: " + title);

                    // Navigate back to homepage
                    driver.navigate().to(homeUrl);
                    Thread.sleep(1000);

                } catch (Exception e) {
                    test.fail("Failed to click dropdown service " + (i + 1) + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            test.fail("Test failed due to: " + e.getMessage());
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
