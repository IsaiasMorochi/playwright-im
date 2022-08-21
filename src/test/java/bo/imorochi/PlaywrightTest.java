package bo.imorochi;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlaywrightTest extends BaseConfigTest {


    public PlaywrightTest() {
        super();
    }

    @Test
    @Order(1)
    void verifyHomePageIsVisible() {
        try (Playwright playwright = Playwright.create()) {
            Page page = createPage(playwright, "https://www.saucedemo.com/", 1980, 1080);

            login(page);

            boolean imInHomePage = page.isVisible("span.title");
            assertTrue(imInHomePage);

            logout(page);
        }
    }

    @Test
    @Order(2)
    void verifyThatItemCanBeAddedToTheCart() {
        try (Playwright playwright = Playwright.create()) {
            Page page = createPage(playwright, "https://www.saucedemo.com/", 1980, 1080);

            login(page);

            page.click("text=Sauce Labs Backpack");
            page.click("id=add-to-cart-sauce-labs-backpack");
            page.click("a.shopping_cart_link");

            boolean imInHomePage = page.isVisible("text=Sauce Labs Backpack");
            assertTrue(imInHomePage);

            boolean imInInventoryItem = page.isVisible("text=1");
            assertTrue(imInInventoryItem);

            logout(page);
        }
    }

    @Test
    @Order(3)
    void verifyThatItemAddedToTheCartCanBeCheckout() {
        try (Playwright playwright = Playwright.create()) {
            Page page = createPage(playwright, "https://www.saucedemo.com/", 1980, 1080);

            //#1 y 2
            login(page);

            //#3
            page.click("text=Sauce Labs Backpack");
            page.click("id=add-to-cart-sauce-labs-backpack");

            page.click("id=back-to-products");

            page.click("text=Sauce Labs Fleece Jacket");
            page.click("id=add-to-cart-sauce-labs-fleece-jacket");

            boolean imInInventoryItem = page.isVisible("text=2");
            assertTrue(imInInventoryItem);

            //#4
            page.click("a.shopping_cart_link");

            //#5
            boolean imItemOne = page.isVisible("text=Sauce Labs Backpack");
            assertTrue(imItemOne);

            boolean imItemTwo = page.isVisible("text=Sauce Labs Fleece Jacket");
            assertTrue(imItemTwo);

            //#6
            page.click("id=checkout");

            //#7
            page.type("id=first-name", "Isaias");
            page.type("id=last-name", "Morochi");
            page.type("id=postal-code", "0701");
            page.click("id=continue");

            String textTotal = page.innerText("div.summary_total_label");
            assertEquals("Total: $86.38", textTotal);

            //#9
            page.click("id=finish");

            //#10
            boolean checkoutCompleted = page.isVisible("text=Checkout: Complete!");
            assertTrue(checkoutCompleted);

            //#11
            page.click("id=back-to-products");

            //#12
            logout(page);
        }
    }

    @Test
    @Order(4)
    void verifyThatItemAddedAndRemoveToTheCart() {
        try (Playwright playwright = Playwright.create()) {
            Page page = createPage(playwright, "https://www.saucedemo.com/", 1980, 1080);

            //#1 y 2
            login(page);

            //#3
            page.click("text=Sauce Labs Backpack");
            page.click("id=add-to-cart-sauce-labs-backpack");

            page.click("id=back-to-products");

            //#4
            page.click("text=Sauce Labs Fleece Jacket");
            page.click("id=add-to-cart-sauce-labs-fleece-jacket");

            boolean imInInventoryItem = page.isVisible("text=2");
            assertTrue(imInInventoryItem);

            //#5
            page.click("a.shopping_cart_link");

            //#6
            boolean imItemOne = page.isVisible("text=Sauce Labs Backpack");
            assertTrue(imItemOne);

            boolean imItemTwo = page.isVisible("text=Sauce Labs Fleece Jacket");
            assertTrue(imItemTwo);

            //#7
            page.click("id=remove-sauce-labs-backpack");

            //#8
            page.click("id=continue-shopping");

            //#9
            logout(page);
        }
    }

    @Test
    @Order(5)
    void verifyTheOrderOfTheProducts() {
        try (Playwright playwright = Playwright.create()) {
            Page page = createPage(playwright, "https://www.saucedemo.com/", 1980, 1080);

            //#1
            login(page);

            //Price (low to high)
            page.selectOption("select.product_sort_container", "lohi");
            String lowPriceElement = page.innerText("div#inventory_container div div#inventory_container.inventory_container div.inventory_list div.inventory_item div.inventory_item_description div.pricebar div.inventory_item_price");
            assertEquals("$7.99", lowPriceElement);

            //Price (high to low)
            page.selectOption("select.product_sort_container", "hilo");
            String highPriceElement = page.innerText("div#inventory_container div div#inventory_container.inventory_container div.inventory_list div.inventory_item div.inventory_item_description div.pricebar div.inventory_item_price");
            assertEquals("$49.99", highPriceElement);

            //#
            logout(page);
        }
    }

    @Test
    @Order(6)
    public void verifyUserIsLogged() {
        try (Playwright playwright = Playwright.create()) {
            Page page = createPage(playwright, "https://www.saucedemo.com/", 1980, 1080);
            page.type("[placeholder=Username]", "standard_user");
            page.type("[placeholder=Password]", "secret_sauce");
            page.click("id=login-button");
            boolean imInHomePage = page.isVisible("span.title");
            assertTrue(imInHomePage);
        }
    }

}
