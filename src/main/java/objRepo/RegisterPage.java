package objRepo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class RegisterPage {
    WebDriver driver;
    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Gender
    @FindBy(id = "gender-male")
    public WebElement maleGender;

    @FindBy(id = "gender-female")
    public WebElement femaleGender;

    // First Name
    @FindBy(id = "FirstName")
    public WebElement firstName;

    // Last Name
    @FindBy(id = "LastName")
    public WebElement lastName;

    // Email
    @FindBy(id = "Email")
    public WebElement email;

    // Password
    @FindBy(id = "Password")
    public WebElement password;

    // Confirm Password
    @FindBy(id = "ConfirmPassword")
    public WebElement confirmPassword;

    // Register button
    @FindBy(id = "register-button")
    public WebElement registerBtn;

    // Registration result
    @FindBy(css = ".result")
    public List<WebElement> resultMessages;

    // Register link
    @FindBy(className = "ico-register")
    public WebElement registerLink;

    // Registration validation messages
    @FindBy(css = ".validation-summary-errors, .field-validation-error")
    public List<WebElement> validationMessages;

    // Select gender
    public void selectGender(String gender) {
        if (gender.equalsIgnoreCase("Male")) {
            maleGender.click();
        } else if (gender.equalsIgnoreCase("Female")) {
            femaleGender.click();
        }
    }

    // Enter first name
    public void enterFirstName(String value) {
        firstName.clear();
        firstName.sendKeys(value);
    }

    // Enter last name
    public void enterLastName(String value) {
        lastName.clear();
        lastName.sendKeys(value);
    }

    // Enter email
    public void enterEmail(String value) {
        email.clear();
        email.sendKeys(value);
    }

    // Enter password
    public void enterPassword(String value) {
        password.clear();
        password.sendKeys(value);
    }

    // Confirm password
    public void enterConfirmPassword(String value) {
        confirmPassword.clear();
        confirmPassword.sendKeys(value);
    }

    // Click Register
    public void clickRegister() throws InterruptedException {
        registerBtn.click();
        Thread.sleep(2000);
    }

    // Complete registration form
    public void register(String gender, String first, String last, String emailValue, String pwd, String confirmPwd) throws InterruptedException {
        selectGender(gender);
        enterFirstName(first);
        enterLastName(last);
        enterEmail(emailValue);
        enterPassword(pwd);
        enterConfirmPassword(confirmPwd);
        clickRegister();
    }

    // Check whether validation is displayed
    public boolean isValidationDisplayed() {
        for (WebElement message : validationMessages) {
            if (message.isDisplayed() && !message.getText().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // Check whether registration was successful
    public boolean isRegistrationSuccessful() {
        for (WebElement result : resultMessages) {
            if (result.isDisplayed() && result.getText().toLowerCase().contains("completed")) {
                return true;
            }
        }
        return false;
    }
}