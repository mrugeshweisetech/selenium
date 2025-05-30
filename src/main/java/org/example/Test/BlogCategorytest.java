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
import java.util.List;

public class BlogCategorytest {

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
    public void clickAllBlogCategories() {
        test = extent.createTest("Blog Category Click Test");

        try {
            String blogUrl = "https://dev.weisetechdev.com/weisetech/blogs";
            driver.get(blogUrl);
            Thread.sleep(2000);


            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight / 3);");
            Thread.sleep(1000);


            List<WebElement> categories = driver.findElements(By.cssSelector(".blog-category a, .category-list a, .tag a"));

            test.info("Found " + categories.size() + " blog categories.");

            for (int i = 0; i < categories.size(); i++) {
                try {

                    driver.get(blogUrl);
                    Thread.sleep(1500);

                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight / 3);");
                    Thread.sleep(1000);

                    categories = driver.findElements(By.cssSelector(".blog-category a, .category-list a, .tag a"));
                    WebElement category = categories.get(i);
                    String categoryName = category.getText().trim();

                    if (categoryName.isEmpty()) {
                        categoryName = category.getAttribute("href");
                    }

                    category.click();
                    Thread.sleep(2000);

                    String title = driver.getTitle();
                    String ssPath = takeScreenshot("blog_category_" + i);

                    test.pass("Clicked blog category: '" + categoryName + "' | Title: " + title,
                            MediaEntityBuilder.createScreenCaptureFromPath(ssPath).build());

                } catch (Exception e) {
                    String ssPath = takeScreenshot("blog_category_error_" + i);
                    test.fail("Error clicking category " + i + ": " + e.getMessage(),
                            MediaEntityBuilder.createScreenCaptureFromPath(ssPath).build());
                }
            }

        } catch (Exception e) {
            String ssPath = takeScreenshot("blog_test_error");
            test.fail("Test failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(ssPath).build());
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
