package org.example.pages;


import org.example.utils.Logs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    private final WebDriver driver;


    @FindBy(id="Email")
    WebElement usernameTextbox;

    @FindBy(id="Password")
    WebElement passwordTextbox;

    @FindBy(xpath = "//*[@id=\"main\"]/div/div/div/div[2]/div[1]/div/form/div[3]/button")
    WebElement loginButton;

//    private final By usernameTextBox = By.id("Email");
//    private final By passwordTextBox = By.id("Password");
//    private final By loginButton = By.xpath("//*[@id=\"main\"]/div/div/div/div[2]/div[1]/div/form/div[3]/button");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterUsername(String username) {

        usernameTextbox.clear();
        usernameTextbox.sendKeys(username);
//        driver.findElement(usernameTextBox).clear();
//        driver.findElement(usernameTextBox).sendKeys("username");
    }

    public void enterpassword(String password) {
        passwordTextbox.clear();
        passwordTextbox.sendKeys(password);
//        driver.findElement(passwordTextBox).clear();
//        driver.findElement(passwordTextBox).sendKeys("password");

    }

    public void clickLogin() {

        Logs.info("Clicking login button..");
        loginButton.click();
        driver.findElement((By) loginButton).click();

    }

}
