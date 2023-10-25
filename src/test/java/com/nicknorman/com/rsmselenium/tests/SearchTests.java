package com.nicknorman.com.rsmselenium.tests;

import com.nicknorman.com.rsmselenium.pages.HomePage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchTests {

    private static WebDriver driver;

    @BeforeClass
    public static void setup() {
        driver = new ChromeDriver();
    }
    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

    @Test
    public void testTitleContainsSearchCriteria() throws InterruptedException {
        String title = "Harry Potter and the Cursed Child";
        Set<String> acceptablePaperbackMatches = new HashSet<String>(){{
            add("Paperback");
            add("Mass Market Paperback");
        }};

        HomePage home = new HomePage(driver);
        home.goToPage();
        home.searchForItem(title);
        List<String> searchTitles = home.getSearchTitles();
        Assert.assertTrue(searchTitles.get(0).contains(title));

        List<String> sections = home.getSearchSections();
        String[] lineItems = sections.get(0).split("\\r?\\n");
        List<String> list = Arrays.asList(lineItems);
        boolean containsPaperback = list.stream()
                .anyMatch(acceptablePaperbackMatches::contains);

        Assert.assertTrue(containsPaperback);

        Thread.sleep(1000);

    }
}
