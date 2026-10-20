
package genericUtility;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import objRepo.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseClass {
    protected WebDriver driver;
    protected LoginPage loginPage;
    protected RegisterPage registerPage;
    protected ProductPage productPage;
    protected CartPage cartPage;
    protected CheckOutPage checkOutPage;
    protected LogoutPage logoutPage;

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional("") String browserName) throws IOException {
        String browser = browserName.isEmpty()
                ? PropertyUtility.getData("browser")
                : browserName;

        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();

            options.addArguments("--disable-save-password-bubble");
            options.addArguments("--disable-features=AutofillServerCommunication");

            Map<String, Object> prefs = new HashMap<>();
            prefs.put("autofill.profile_enabled", false);
            prefs.put("autofill.credit_card_enabled", false);
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);

            options.setExperimentalOption("prefs", prefs);
            driver = new ChromeDriver(options);

        } else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();

        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();

        } else {
            throw new IllegalArgumentException("Invalid browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        Reporter.log("Test Execution Started", true);
        Reporter.log("Browser Launched", true);

        driver.get(PropertyUtility.getData("url"));

        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        checkOutPage = new CheckOutPage(driver);
        logoutPage = new LogoutPage(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            Reporter.log("Browser Closed", true);
            System.out.println("--------------------------------");
        }
    }
}
