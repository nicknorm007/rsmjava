package com.nicknorman.com.rsmselenium.tests;

import com.nicknorman.com.rsmselenium.pages.HomePage;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;
import java.util.function.Predicate;

public class SearchAndAddToBasketTests {

    private static WebDriver driver;
    HomePage home;

    private static String title;
    private static Set<String> acceptablePaperbackMatches;
    private static List<String> searchTitles;
    private static List<String> sections;
    private static WebElement linkPaperBack;
    private static Map<String, WebElement> paperBackLinksWithTitles;

    @BeforeClass
    public static void setup(){
        driver = new ChromeDriver();

        title = "Harry Potter and the Cursed Child";
        acceptablePaperbackMatches = new HashSet<String>(){{
            add("Paperback");
            add("Mass Market Paperback");
        }};

    }
    @Before
    public void setupPageSearch() throws InterruptedException {
        home = new HomePage(driver);
        home.goToPage();
        home.clickAcceptCookies();
        home.searchForItem(title);
        searchTitles = home.getSearchTitles();
        sections = home.getSearchSections();

        linkPaperBack = home.getPaperbackLinkFromFirstItem();
        paperBackLinksWithTitles = home.getPaperbackLinksWithTitlesByTitleSearch(title);
    }
    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

    @Test
    public void testFirstSearchResultContainsCorrectTitle()  {
        Assert.assertTrue(searchTitles.get(0).contains(title));
    }
    @Test
    public void testSearchContainsPaperback(){

        String[] lineItems = sections.get(0).split("\\r?\\n");
        List<String> list = Arrays.asList(lineItems);
        boolean containsPaperback = list.stream()
                .anyMatch(acceptablePaperbackMatches::contains);

        Assert.assertTrue(containsPaperback);

    }
    @Test
    public void testSearchContainsPrice(){

        Predicate<String> poundPriceMatcher = text -> (text.matches(".*£\\d+\\.\\d{2}.*"));

        String[] lineItems = sections.get(0).split("\\r?\\n");
        List<String> list = Arrays.asList(lineItems);
        boolean containsPrice = list.stream()
                .anyMatch(poundPriceMatcher);

        Assert.assertTrue(containsPrice);

    }
    @Test
    public void testAddToBasketWithPaperbackEdition() throws InterruptedException {

        boolean foundPaperBackEdition = false;
        String price = "";

        for (Map.Entry<String,WebElement> entry : paperBackLinksWithTitles.entrySet())
        {
            WebElement ele = entry.getValue();
            Thread.sleep(2000); //for site issues and testing purposes
            ele.click();
            foundPaperBackEdition = home.isAddToBasketBtnPresent();
            if(foundPaperBackEdition){
                List<WebElement> eles = home.findBasketPrices();
                price = eles.get(1).getText().split("\\r?\\n")[1];
                Assert.assertTrue(eles.get(1).getText().split("\\r?\\n")[0].equals("Paperback"));
                home.clickGiftWrapCheckbox();
                home.clickAddToBasketBtn();
                break;
            }
            else
            {
                home.goToPage();
                home.searchForItem(title);
            }
        }
        Assert.assertTrue(foundPaperBackEdition);
        home.clickGoToBasketBtn();
        home.waitForGiftCheckbox();

        //assert gift box is selected
        Assert.assertTrue(home.isGiftCheckBoxSelected());

        String basket_price = home.getBasketPrice();

        //assert prices match
        Assert.assertTrue(basket_price.equals(price));

        //assert only one item in basket
        Assert.assertTrue(home.getNumberOfItemsInCart() == 1);

        //assert title in basket is correct
        List<WebElement> titlesIn = home.getTitlesInBasket();

        boolean found=false;
        for(WebElement ele : titlesIn)
        {
            found = ele.getText().contains(title) ? true : false;
            if(found){
                break;
            }

        }
        //we found the title inside the go to basket section
        Assert.assertTrue(found);
    }
}
