package org.example.Test;



import com.aventstack.extentreports.ExtentTest;
import org.example.base.BaseTest;
import org.example.pages.LoginPage;
import org.example.utils.ExtentReportManager;
import org.example.utils.Logs;
import org.testng.Assert;
import org.testng.annotations.Test;


public class LoginTest extends  BaseTest {

    private Logs Log;

    @Test
    public void testValidLogin() throws InterruptedException {


        Logs.info("Starting login test...");
        ExtentTest test = ExtentReportManager.createTest("Login Test");

        ExtentTest navigatingToUrl = test.info("Navigating to URL");
        LoginPage loginPage = new LoginPage(driver);

        Logs.info("Adding credentials");
        test.info("Adding credentials");
        loginPage.enterUsername("admin@yourstore.com");
        loginPage.enterpassword("admin");
        test.info("Clicking on Login button");
        loginPage.clickLogin();

        System.out.println("Title of the page is :  "+driver.getTitle());
        Logs.info("Verifying page title");
        test.info("Verifying page title");
       ExtentTest loginSuccessful = test.pass("Login Successful");
        Logs.info("Verifying page title!!!");
        test.info("Verifying page title!!!");
        System.out.println("The Title Of The Page Is:- " + driver.getTitle());
        Thread.sleep(1000);
        Assert.assertEquals(driver.getTitle(), "Just a moment...");
        Thread.sleep(1000);
        Logs.info("Login Test Completed!!!");
        test.pass("Login Successful!!!");
        Thread.sleep(1000);
    }


}
