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
    public static void main(String[] args) {

        System.setProperty("webdriver.gecko.driver", "/snap/bin/geckodriver");

        FirefoxOptions options = new FirefoxOptions();

        // Conditional headless (pipeline safe)
        String headless = System.getProperty("headless");
        if ("true".equals(headless)) {
            options.addArguments("--headless");
        }

        WebDriver driver = new FirefoxDriver(options);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {

            // ================= TAB 1 =================
            System.out.println("========== TAB 1 ==========");

            driver.get("https://www.saucedemo.com/");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();

            wait.until(ExpectedConditions.urlContains("inventory"));
            System.out.println("TAB 1 Login Success ✔");

            // ================= TAB 2 =================
            System.out.println("========== TAB 2 ==========");

            js.executeScript("window.open('https://automationexercise.com/', '_blank');");

            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1));

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/products']"))).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_product"))).sendKeys("t-shirt");
            driver.findElement(By.id("submit_search")).click();

            // Proper Add to Cart handling
            WebElement firstProduct = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("(//div[@class='product-image-wrapper'])[1]")
                    )
            );

            js.executeScript("arguments[0].scrollIntoView(true);", firstProduct);

            WebElement addBtn = firstProduct.findElement(By.xpath(".//a[@data-product-id]"));
            js.executeScript("arguments[0].click();", addBtn);

            WebElement viewCart = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//u[text()='View Cart']"))
            );
            viewCart.click();

            System.out.println("TAB 2 Add to Cart Success ✔");

            // ================= TAB 3 =================
            System.out.println("========== TAB 3 ==========");

            js.executeScript("window.open('https://practicetestautomation.com/practice-test-login/', '_blank');");

            tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(2));

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("student");
            driver.findElement(By.id("password")).sendKeys("Password123");

            WebElement loginBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("submit"))
            );
            loginBtn.click();

            wait.until(ExpectedConditions.urlContains("logged-in-successfully"));
            System.out.println("TAB 3 Login Success ✔");

            // ================= FINAL =================
            System.out.println("ALL 3 TABS COMPLETED SUCCESSFULLY ✔");

        } catch (Exception e) {
            System.out.println("Test Failed: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
