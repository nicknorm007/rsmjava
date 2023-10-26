package com.nicknorman.com.rsmselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage {

    WebDriver driver;
    String url = "https://www.amazon.co.uk/";

    By searchField = By.id("twotabsearchtextbox");
    By searchBtn = By.id("nav-search-submit-button");
    By titleResults = By.cssSelector("h2 > a.a-link-normal.s-underline-text");
    By backToTopLabel = By.id("navBackToTop");
    By sectionResults = By.cssSelector("div[data-component-type=s-search-result]");
    By addToBasketBtn = By.id("add-to-cart-button");
    By acceptCookies = By.id("sp-cc-accept");
    By basketPricesList = By.cssSelector("#tmmSwatches li");
    By giftOptionCheckBox = By.id("gift-wrap");
    By goToBasket = By.partialLinkText("Go to basket");
    By orderContainsGiftCheckbox = By.id("sc-buy-box-gift-checkbox");
    By basketPrice = By.className("sc-item-price-block-badge");
    By cartItems = By.cssSelector("div[data-asin]");
    By titleInBasket = By.cssSelector("span.a-list-item");

    public HomePage(WebDriver driver) {
        this.driver=driver;
    }

    public void goToPage() {
        driver.get(url);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(backToTopLabel));

    }
    public void clickAcceptCookies() {
        boolean b = driver.findElements(acceptCookies).isEmpty();
        if(!b) {
            driver.findElement(acceptCookies).click();
        }
    }
    public String getBasketPrice()
    {
       return driver.findElement(basketPrice).getText();

    }
    public List<WebElement> getTitlesInBasket()
    {
        List<WebElement> eles = driver.findElements(titleInBasket);
        return eles;
    }
    public Integer getNumberOfItemsInCart()
    {
        return driver.findElements(cartItems).size();

    }
    public void clickGiftWrapCheckbox() {
        driver.findElement(giftOptionCheckBox).click();
    }
    public void waitForGiftCheckbox() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(orderContainsGiftCheckbox));
    }
    public void clickGoToBasketBtn() {
        driver.findElement(goToBasket).click();
    }
    public boolean isGiftCheckBoxSelected()
    {
        return driver.findElement(orderContainsGiftCheckbox).isSelected();

    }
    public List<WebElement> findBasketPrices()
    {
        return driver.findElements(basketPricesList);

    }
    public boolean isAddToBasketBtnPresent() {
        return !driver.findElements(addToBasketBtn).isEmpty();
    }
    public void clickAddToBasketBtn() {
        driver.findElement(addToBasketBtn).click();
    }
    public void searchForItem(String search) {
        driver.findElement(searchField).sendKeys(search);
        driver.findElement(searchBtn).click();
    }
    public List<String> getSearchTitles()  {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(backToTopLabel));

        List<String> titles = new ArrayList<>();
        List<WebElement> results = driver.findElements(titleResults);

        for(WebElement ele : results)
        {
            titles.add(ele.getText());
        }

        return titles;

    }
    public List<String> getSearchSections() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(backToTopLabel));

        List<String> sections = new ArrayList<>();
        List<WebElement> results = driver.findElements(sectionResults);

        for(WebElement ele : results)
        {
            sections.add(ele.getText());
        }

        return sections;

    }
    public WebElement getPaperbackLinkFromFirstItem() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(backToTopLabel));

        List<String> sections = new ArrayList<>();
        List<WebElement> results = driver.findElements(sectionResults);
        WebElement element = results.get(0);

        WebElement paperBackLink = element.findElement(By.linkText("Paperback"));

        return paperBackLink;

    }
    public Map<String, WebElement> getPaperbackLinksWithTitlesByTitleSearch(String titleStr) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(backToTopLabel));

        List<WebElement> results = driver.findElements(sectionResults);
        Map<String,WebElement> paperLinks = new HashMap<String, WebElement>();

        for(WebElement ele : results)
        {
            boolean hasNoPlink = ele.findElements(By.linkText("Paperback")).isEmpty();
            if(! hasNoPlink)
            {
                WebElement title = ele.findElement(titleResults);
                WebElement pLink = ele.findElement(By.linkText("Paperback"));
                if(title.getText().contains(titleStr)){
                    paperLinks.put(title.getText(), pLink);
                }
            }

        }
        return paperLinks;
    }

}
