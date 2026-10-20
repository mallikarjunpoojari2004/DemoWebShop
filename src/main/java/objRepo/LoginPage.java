package objRepo;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    WebDriver driver;
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Email
    @FindBy(id = "Email")
    public WebElement email;

    // Password
    @FindBy(id = "Password")
    public WebElement passWord;

    // Login button
    @FindBy(css = "input.login-button")
    public WebElement loginBtn;

    // Login link
    @FindBy(className = "ico-login")
    public WebElement loginLink;

    // Logout link after successful login
    @FindBy(className = "ico-logout")
    public WebElement logoutLink;

    @FindBy(css = ".message-error, .validation-summary-errors, .field-validation-error")
    public List<WebElement> errorMessages;

    // Enter email
    public void enterEmail(String value) throws InterruptedException {
        email.clear();
        email.sendKeys(value);
        Thread.sleep(1000);
    }

    // Enter password
    public void enterPassword(String value) throws InterruptedException {
        passWord.clear();
        passWord.sendKeys(value);
        Thread.sleep(1000);
    }

    // Click Login
    public void clickLogin() throws InterruptedException {
        loginBtn.click();
        Thread.sleep(2000);
    }

    // Complete login
    public void login(String emailValue, String password) throws InterruptedException {
        enterEmail(emailValue);
        enterPassword(password);
        clickLogin();
    }

    // Check whether an error is displayed
    public boolean isErrorDisplayed() {
        for (WebElement error : errorMessages) {
            if (error.isDisplayed() && !error.getText().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}