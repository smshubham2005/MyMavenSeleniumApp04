package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.gecko.driver", "/snap/bin/geckodriver");

        FirefoxOptions options = new FirefoxOptions();
        WebDriver driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(120));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Use 60 seconds wait throughout
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        // ============================================================
        // TAB 1 - https://www.saucedemo.com/ - LOGIN
        // ============================================================
        System.out.println("========== TAB 1: saucedemo.com ==========");

        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();
        Thread.sleep(2000);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        driver.findElement(By.id("user-name")).clear();
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        Thread.sleep(500);

        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        Thread.sleep(500);

        driver.findElement(By.id("login-button")).click();
        Thread.sleep(2000);

        System.out.println("TAB 1 - Saucedemo Login Successful!");
        System.out.println("TAB 1 - URL: " + driver.getCurrentUrl());

        // ============================================================
        // TAB 2 - https://automationexercise.com/ - SEARCH & ADD TO CART
        // ============================================================
        System.out.println("\n========== TAB 2: automationexercise.com ==========");

        js.executeScript("window.open('https://automationexercise.com/', '_blank');");
        Thread.sleep(2000);

        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        driver.manage().window().maximize();
        Thread.sleep(3000);

        WebElement productsMenu = wait.until(
            ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/products']"))
        );
        productsMenu.click();
        Thread.sleep(2000);

        WebElement searchBox = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("search_product"))
        );
        searchBox.clear();
        searchBox.sendKeys("t-shirt");
        Thread.sleep(500);

        driver.findElement(By.id("submit_search")).click();
        Thread.sleep(2000);

        WebElement firstAddToCart = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("(//div[@class='product-image-wrapper']//a[@data-product-id])[1]")
            )
        );
        js.executeScript("arguments[0].click();", firstAddToCart);
        Thread.sleep(2000);

        WebElement viewCart = wait.until(
            ExpectedConditions.elementToBeClickable(By.xpath("//u[text()='View Cart']"))
        );
        js.executeScript("arguments[0].click();", viewCart);
        Thread.sleep(2000);

        System.out.println("TAB 2 - T-shirt added to cart successfully!");
        System.out.println("TAB 2 - URL: " + driver.getCurrentUrl());

        // ============================================================
        // TAB 3 - https://practicetestautomation.com/ - LOGIN
        // KEY FIX: Wait 30 seconds before opening Tab 3
        // to avoid rate limiting from previous requests
        // ============================================================
        System.out.println("\n========== TAB 3: practicetestautomation.com ==========");
        System.out.println("Waiting 30 seconds before opening Tab 3...");
        Thread.sleep(30000); // 30 sec gap to avoid rate limit

        js.executeScript("window.open('https://practicetestautomation.com/practice-test-login/', '_blank');");
        Thread.sleep(3000);

        tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(2));
        driver.manage().window().maximize();

        // Wait extra long for page to fully load
        Thread.sleep(8000);

        // Wait up to 60 seconds for username field
        WebElement usernameField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );
        usernameField.clear();
        usernameField.sendKeys("student");
        Thread.sleep(1000);

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys("Password123");
        Thread.sleep(1000);

        WebElement loginBtn = wait.until(
            ExpectedConditions.elementToBeClickable(By.id("submit"))
        );
        js.executeScript("arguments[0].click();", loginBtn);
        Thread.sleep(3000);

        WebElement successMsg = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(),'Logged In Successfully')]")
            )
        );

        System.out.println("TAB 3 - Login Successful!");
        System.out.println("TAB 3 - URL: " + driver.getCurrentUrl());
        System.out.println("TAB 3 - Message: " + successMsg.getText());

        // ============================================================
        // FINAL SUMMARY
        // ============================================================
        System.out.println("\n========== ALL 3 TABS COMPLETED ==========");
        System.out.println("TAB 1 -> saucedemo.com              : Logged In Successfully ✅");
        System.out.println("TAB 2 -> automationexercise.com     : T-shirt Added to Cart  ✅");
        System.out.println("TAB 3 -> practicetestautomation.com : Logged In Successfully ✅");

        Thread.sleep(5000);
        driver.quit();
    }
}
