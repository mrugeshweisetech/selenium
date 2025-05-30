package org.example.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.example.utils.ExtentReportManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class perfomance {


    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        ExtentReports extent = ExtentReportManager.getReportInstance();
        ExtentTest test = ExtentReportManager.createTest( "Homepage Test" );



        try {
            String url = "https://dev.weisetechdev.com/weisetech/";
            long startTime = System.currentTimeMillis();

            driver.get(url);


            Thread.sleep(3000);

            long endTime = System.currentTimeMillis();
            long loadTime = endTime - startTime;

            test.info("Page loaded in " + loadTime + " ms");


            if (loadTime <= 3000) {
                test.pass(" Page loaded within acceptable time: " + loadTime + " ms");
            } else {
                test.fail(" Page took too long to load: " + loadTime + " ms");
            }

        } catch (Exception e) {
            test.fail("Test failed with exception: " + e.getMessage());
        }
    }
}
