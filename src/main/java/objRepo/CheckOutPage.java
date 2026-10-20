package objRepo;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
public class CheckOutPage {
    WebDriver driver;
    WebDriverWait wait;
    public CheckOutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".page-title h1")
    public WebElement pageTitle;

    @FindBy(id = "BillingNewAddress_FirstName")
    public WebElement firstName;

    @FindBy(id = "BillingNewAddress_LastName")
    public WebElement lastName;

    @FindBy(id = "BillingNewAddress_Email")
    public WebElement email;

    @FindBy(id = "BillingNewAddress_CountryId")
    public WebElement country;

    @FindBy(id = "BillingNewAddress_StateProvinceId")
    public WebElement state;

    @FindBy(id = "billing-address-select")
    public WebElement billingAddressSelect;

    @FindBy(id = "BillingNewAddress_City")
    public WebElement city;

    @FindBy(id = "BillingNewAddress_Address1")
    public WebElement address1;

    @FindBy(id = "BillingNewAddress_ZipPostalCode")
    public WebElement zipCode;

    @FindBy(id = "BillingNewAddress_PhoneNumber")
    public WebElement phoneNumber;

    @FindBy(css = "input.new-address-next-step-button")
    public WebElement continueBtn;

    @FindBy(css = ".field-validation-error, .validation-summary-errors")
    public List<WebElement> errorMessages;

    // Select New Address when a saved-address dropdown is available
    public void selectNewAddressIfAvailable() {
        List<WebElement> dropdowns = driver.findElements(By.id("billing-address-select"));
        if (!dropdowns.isEmpty() && dropdowns.get(0).isDisplayed()) {
            Select addressSelect = new Select(dropdowns.get(0));

            for (WebElement option : addressSelect.getOptions()) {
                if (option.getText().trim().equalsIgnoreCase("New Address")) {
                    addressSelect.selectByVisibleText(option.getText());
                    break;
                }
            }
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BillingNewAddress_FirstName")));
    }

    // Enter billing details
    public void enterCheckoutInformation(String first, String last, String mail, String countryName, String stateName, String cityValue, String address, String zip, String phone) throws InterruptedException {
        List<WebElement> dropdowns = driver.findElements(By.id("billing-address-select"));
        if (!dropdowns.isEmpty() && dropdowns.get(0).isDisplayed()) {
            Select addressSelect = new Select(dropdowns.get(0));

            for (WebElement option : addressSelect.getOptions()) {
                if (option.getText().trim().equalsIgnoreCase("New Address")) {
                    addressSelect.selectByVisibleText(option.getText());
                    Thread.sleep(500);
                    break;
                }
            }
        }

        WebElement firstField = wait.until(ExpectedConditions.elementToBeClickable(By.id("BillingNewAddress_FirstName")));
        firstField.clear();
        firstField.sendKeys(first);

        WebElement lastField = driver.findElement(By.id("BillingNewAddress_LastName"));
        lastField.clear();
        lastField.sendKeys(last);

        WebElement emailField = driver.findElement(By.id("BillingNewAddress_Email"));
        emailField.clear();
        emailField.sendKeys(mail);

        new Select(driver.findElement(By.id("BillingNewAddress_CountryId"))).selectByVisibleText(countryName);

        Thread.sleep(1000);

        WebElement stateField = driver.findElement(By.id("BillingNewAddress_StateProvinceId"));

        if (stateName != null && !stateName.trim().isEmpty()) {
            new Select(stateField).selectByVisibleText(stateName);
        }

        WebElement cityField = driver.findElement(By.id("BillingNewAddress_City"));
        cityField.clear();
        cityField.sendKeys(cityValue);

        WebElement addressField = driver.findElement(By.id("BillingNewAddress_Address1"));
        addressField.clear();
        addressField.sendKeys(address);

        WebElement zipField = driver.findElement(By.id("BillingNewAddress_ZipPostalCode"));
        zipField.clear();
        zipField.sendKeys(zip);

        WebElement phoneField = driver.findElement(By.id("BillingNewAddress_PhoneNumber"));
        phoneField.clear();
        phoneField.sendKeys(phone);
    }

    // Continue from billing details
    public void clickContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.new-address-next-step-button"))).click();
    }

    // Continue from shipping address
    public boolean continueFromShippingAddress() throws InterruptedException {

        By locator = By.cssSelector("#checkout-step-shipping .new-address-next-step-button, " + "#shipping-address-buttons-container .new-address-next-step-button, " + "input.shipping-address-next-step-button");

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement button = wait.until(d -> {
                List<WebElement> buttons = d.findElements(locator);

                for (WebElement element : buttons) {
                    if (element.isDisplayed() && element.isEnabled()) {
                        return element;
                    }
                }

                return null;
            });

            button.click();
            Thread.sleep(1000);
            return true;

        } catch (Exception e) {
            System.out.println("Shipping address Continue button was not found.");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Current page text:");
            System.out.println(driver.findElement(By.tagName("body")).getText());
            return false;
        }
    }

    // Continue from shipping method
    public boolean continueFromShippingMethod() {
        return clickStepButton(By.cssSelector(".shipping-method-next-step-button"), "Shipping method");
    }

    // Select Cash On Delivery
    public boolean selectPaymentMethod() {
        List<WebElement> methods = driver.findElements(By.cssSelector("input[name='paymentmethod']"));

        for (WebElement method : methods) {
            String value = method.getAttribute("value");

            if (value != null && value.toLowerCase().contains("cashondelivery")) {
                if (!method.isSelected()) {
                    method.click();
                }
                return true;
            }
        }

        return false;
    }

    // Continue from payment method
    public boolean continueFromPaymentMethod() {
        return clickStepButton(By.cssSelector(".payment-method-next-step-button"), "Payment method");
    }

    // Continue from payment information
    public boolean continueFromPaymentInformation() {
        return clickStepButton(By.cssSelector(".payment-info-next-step-button"), "Payment information");
    }

    // Confirm order
    public boolean confirmOrder() {
        return clickStepButton(By.cssSelector(".confirm-order-next-step-button"), "Confirm order");
    }

    // Click a checkout step button
    private boolean clickStepButton(By locator, String stepName) {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(locator));
            button.click();
            return true;
        } catch (Exception e) {
            System.out.println(stepName + " Continue button was not clickable or not found.");
            return false;
        }
    }

    // Verify order confirmation
    public boolean isOrderConfirmationDisplayed() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(5)).until(d -> d.findElement(By.tagName("body")).getText().contains("Your order has been successfully processed!"));
        } catch (Exception e) {
            return false;
        }
    }

    // Check billing form visibility
    public boolean isBillingFormDisplayed() {
        List<WebElement> elements = driver.findElements(By.id("BillingNewAddress_FirstName"));

        for (WebElement element : elements) {
            if (element.isDisplayed()) {
                return true;
            }
        }

        return false;
    }

    // Check validation message visibility
    public boolean isErrorDisplayed() {
        for (WebElement error : errorMessages) {
            if (error.isDisplayed() && !error.getText().trim().isEmpty()) {
                return true;
            }
        }

        return false;
    }
}
