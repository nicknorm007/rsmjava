package com.nicknorman.com.rsmselenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class TestHomePage {

    public static void main(String[] args)  {

        //Instantiating chrome driver
        WebDriver driver = new ChromeDriver();

        //Opening web application
        driver.get("https://www.amazon.co.uk/");


        driver.quit();
    }


}
