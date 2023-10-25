package com.nicknorman.com.rsmselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.time.Duration.*;

public class HomePage {

    WebDriver driver;
    String url = "https://www.amazon.co.uk/";

    By searchField = By.id("twotabsearchtextbox");
    By searchBtn = By.id("nav-search-submit-button");
    By titleResults = By.cssSelector("h2 > a.a-link-normal.s-underline-text");
    By backToTopLabel = By.id("navBackToTop");
    By sectionResults = By.cssSelector("div[data-component-type=s-search-result");


    public HomePage(WebDriver driver) {
        this.driver=driver;
    }

    public void goToPage() throws InterruptedException {
        driver.get(url);

        //method is only here for homework due to human interaction needed
        Thread.sleep(15000);
    }
    public void searchForItem(String search) {
        driver.findElement(searchField).sendKeys(search);
        driver.findElement(searchBtn).click();
    }
    public List<String> getSearchTitles() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        //make sure footer is visible before searching
        wait.until(ExpectedConditions.visibilityOfElementLocated(backToTopLabel));

        List<String> titles = new ArrayList<>();
        List<WebElement> results = driver.findElements(titleResults);

        for(WebElement ele : results)
        {
            titles.add(ele.getText());
        }

        return titles;

    }
    public List<String> getSearchSections() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        //make sure footer is visible before searching
        wait.until(ExpectedConditions.visibilityOfElementLocated(backToTopLabel));

        List<String> sections = new ArrayList<>();
        List<WebElement> results = driver.findElements(sectionResults);

        for(WebElement ele : results)
        {
            sections.add(ele.getText());
        }

        return sections;

    }
}
