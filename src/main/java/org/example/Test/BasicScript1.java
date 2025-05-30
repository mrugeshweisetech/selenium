package org.example.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;

public class BasicScript1 {

    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        driver.get("https://admin-demo.nopcommerce.com/login");

        String title = driver.getTitle();

        System.out.println("Title is : "+title);


        WebElement emailBox = driver.findElement(By.id("Email"));
        emailBox.clear();
        emailBox.sendKeys("admin@yourstore.com");

      //  driver.findElement(By.id("Email")).sendKeys("admin@yourstore.com");

        driver.findElement(By.id("Password")).clear();
        driver.findElement(By.id("Password")).sendKeys("admin");

        driver.findElement(By.xpath("//*[@id=\"main\"]/div/div/div/div[2]/div[1]/div/form/div[3]/button")).click();



        driver.close();
        driver.quit();
    }
}
