
package objRepo;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LogoutPage {
    WebDriver driver;

    public LogoutPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Logout link
    @FindBy(className = "ico-logout")
    public WebElement logoutLink;

    // Login link
    @FindBy(className = "ico-login")
    public WebElement loginLink;

    // Contact Us link
    @FindBy(css = "a[href='/contactus']")
    public WebElement contactUsLink;

    // Contact Us page title
    @FindBy(css = ".page-title h1")
    public WebElement pageTitle;

    // Contact form fields
    @FindBy(id = "FullName")
    public WebElement fullName;

    @FindBy(id = "Email")
    public WebElement email;

    @FindBy(id = "Enquiry")
    public WebElement enquiry;

    // Submit button
    @FindBy(css = "input.contact-us-button")
    public WebElement submitBtn;

    // Contact form result
    @FindBy(css = ".result")
    public WebElement resultMessage;

    // Validation messages
    @FindBy(css = ".field-validation-error, .validation-summary-errors")
    public List<WebElement> errorMessages;

    // Click Logout
    public void logout() throws InterruptedException {
        logoutLink.click();
        Thread.sleep(1500);
    }

    // Open Contact Us page
    public void openContactUs() throws InterruptedException {
        contactUsLink.click();
        Thread.sleep(1500);
    }

    // Enter full name
    public void enterFullName(String value) {
        fullName.clear();
        fullName.sendKeys(value);
    }

    // Enter email
    public void enterEmail(String value) {
        email.clear();
        email.sendKeys(value);
    }

    // Enter enquiry
    public void enterEnquiry(String value) {
        enquiry.clear();
        enquiry.sendKeys(value);
    }

    // Submit contact form
    public void submitContactForm() throws InterruptedException {
        submitBtn.click();
        Thread.sleep(1500);
    }

    // Check whether validation message is displayed
    public boolean isErrorDisplayed() {
        for (WebElement error : errorMessages) {
            if (error.isDisplayed() && !error.getText().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
