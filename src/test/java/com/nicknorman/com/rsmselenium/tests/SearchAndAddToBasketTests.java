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
    private static String searchTitle;
    private static String targetPrice;
    private static Set<String> acceptablePaperbackMatches;
    private static List<String> searchTitles;
    private static List<String> sections;
    private static WebElement linkPaperBack;
    private static Map<String, WebElement> paperBackLinksWithTitles;

    @BeforeClass
    public static void setup(){
        driver = new ChromeDriver();

        title = "Harry Potter and the Cursed Child";
        searchTitle = "Harry Potter and the Cursed Child Parts 1 and 2";
        acceptablePaperbackMatches = new HashSet<String>(){{
            add("Paperback");
            add("Mass Market Paperback");
        }};

    }
    @Before
    public void setupPageSearch() {
        home = new HomePage(driver);
        home.goToPage();
        home.clickAcceptCookies();
        home.gotoBooksDepartment();
        home.searchForItem(searchTitle);
        searchTitles = home.getSearchTitles();
        sections = home.getSearchSections();

        linkPaperBack = home.getPaperbackLinkFromFirstItem();
        paperBackLinksWithTitles = home.getPaperbackLinksWithTitlesByTitleSearch(title);
        targetPrice = home.getPriceOfFirstPaperbackEdition(sections);
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
    public void testFirstSearchResultMatchesSearch()  {
        Assert.assertTrue(searchTitles.get(0).toLowerCase()
                .matches("harry potter and the cursed child.*parts (1|one) (and|&) (2|two).*"));
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
    public void testSearchContainsAnyPrice(){

        Predicate<String> poundPriceMatcher = text -> (text.matches(".*£\\d+\\.\\d{2}.*"));

        String[] lineItems = sections.get(0).split("\\r?\\n");
        List<String> list = Arrays.asList(lineItems);
        boolean containsPrice = list.stream()
                .anyMatch(poundPriceMatcher);

        Assert.assertTrue(containsPrice);

    }
    @Test
    public void testAddToBasketWithPaperbackEdition() throws InterruptedException {

        String price = "";

        WebElement elem = home.getPaperbackLinkFromFirstEligibleItem();
        elem.click();

        List<WebElement> eles = home.findBasketPrices();
        price = eles.get(1).getText().split("\\r?\\n")[1];
        Assert.assertTrue(eles.get(1).getText().split("\\r?\\n")[0].equals("Paperback"));
        home.clickGiftWrapCheckbox();
        home.clickAddToBasketBtn();
        home.clickGoToBasketBtn();
        home.waitForGiftCheckbox();

        //assert gift box is selected
        Assert.assertTrue(home.isGiftCheckBoxSelected());

        String basket_price = home.getBasketPrice();

        //assert prices match
        Assert.assertTrue(basket_price.contains(price));

        //assert price from search results matches
        Assert.assertTrue(basket_price.contains(targetPrice));

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
