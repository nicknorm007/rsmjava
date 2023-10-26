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
    public void testPaperbackContainsSamePriceAndTitle(){





    }
    @Test
    public void testTitleContainsPaperbackEdition() {

        boolean foundPaperBackEdition = false;

        for (Map.Entry<String,WebElement> entry : paperBackLinksWithTitles.entrySet())
        {
            WebElement ele = entry.getValue();
            String link = ele.getAttribute("href");
            driver.get(link);
            home.clickAcceptCookies();
            foundPaperBackEdition = home.isAddToBasketBtnPresent();
            if(foundPaperBackEdition){
                break;
            }
        }
        Assert.assertTrue(foundPaperBackEdition);

    }
}
