package bo.imorochi;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import lombok.Data;

@Data
public class BaseConfigTest {

    private String urlPage;
    private Integer viewPortX;
    private Integer viewPortY;

    public Page createPage(Playwright playwright, String urlPage, Integer viewPortX, Integer viewPortY) {
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(250);
        Page page = playwright.chromium().launch(launchOptions).newPage();
        page.setViewportSize(viewPortX, viewPortY);
        page.navigate(urlPage);
        return page;
    }

    public void login(Page page) {
        page.type("[placeholder=Username]", "standard_user");
        page.type("[placeholder=Password]", "secret_sauce");
        page.click("id=login-button");
    }

    public void logout(Page page) {
        page.click("id=react-burger-menu-btn");
        page.click("id=logout_sidebar_link");
    }

}
