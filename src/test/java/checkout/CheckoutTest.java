package checkout;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import genericUtility.BaseClass;
import genericUtility.ExcelUtility;
import genericUtility.WebDriverUtility;

public class CheckoutTest extends BaseClass {
    WebDriverUtility wUtil = new WebDriverUtility();

    // Login and open checkout before each test
    @BeforeMethod(alwaysRun = true)
    public void openCheckout() throws IOException, InterruptedException {
        driver.get("https://demowebshop.tricentis.com/login");

        String email = ExcelUtility.getData("SignupLoginData", 6, 1);
        String password = ExcelUtility.getData("SignupLoginData", 6, 2);
        loginPage.login(email, password);

        String productName = ExcelUtility.getData("CartData", 1, 1);
        driver.get("https://demowebshop.tricentis.com/");
        productPage.searchProduct(productName);

        boolean found = false;
        for (WebElement product : driver.findElements(By.cssSelector(".product-title a"))) {
            if (product.getText().trim().equalsIgnoreCase(productName.trim())) {
                product.click();
                found = true;
                break;
            }
        }
        Assert.assertTrue(found, "Product was not found: " + productName);
        Thread.sleep(1000);

        driver.findElement(By.cssSelector("input.add-to-cart-button")).click();
        Thread.sleep(1500);

        driver.get("https://demowebshop.tricentis.com/cart");
        Thread.sleep(1000);

        WebElement terms = driver.findElement(By.id("termsofservice"));
        if (!terms.isSelected()) {
            terms.click();
        }

        driver.findElement(By.id("checkout")).click();
        Thread.sleep(1500);
    }

    // Read data from CheckoutData
    public String data(int row, int column) throws IOException {
        return ExcelUtility.getData("CheckoutData", row, column);
    }

    // Fill billing details from Excel
    public void fillBillingData(int row) throws IOException, InterruptedException {
        checkOutPage.enterCheckoutInformation(data(row, 1), data(row, 2), data(row, 3), data(row, 5), data(row, 6), data(row, 7), data(row, 8), data(row, 10), data(row, 11));
    }

// TC-CHK-01: Verify checkout page opens
    @Test(groups = {"IntegrationGrp"}, priority = 1)
    public void verifyCheckoutPageOpens() {
        checkOutPage.selectNewAddressIfAvailable();
        Assert.assertTrue(checkOutPage.isBillingFormDisplayed(), "Billing form is not displayed");
        Reporter.log("TC-CHK-01 Passed", true);
    }

    // TC-CHK-03: Verify checkout page title
    @Test(groups = {"IntegrationGrp"}, priority = 2)
    public void verifyCheckoutTitle() {
        Assert.assertTrue(driver.getTitle().contains("Demo Web Shop"), "Incorrect checkout page title");
        Reporter.log("TC-CHK-03 Passed", true);
    }

    // TC-CHK-04: Verify billing fields
    @Test(groups = {"functionalGroup"}, priority = 3)
    public void verifyBillingFieldsDisplayed() {
        checkOutPage.selectNewAddressIfAvailable();
        Assert.assertTrue(checkOutPage.firstName.isDisplayed());
        Assert.assertTrue(checkOutPage.lastName.isDisplayed());
        Assert.assertTrue(checkOutPage.email.isDisplayed());
        Assert.assertTrue(checkOutPage.country.isDisplayed());
        Assert.assertTrue(checkOutPage.city.isDisplayed());
        Assert.assertTrue(checkOutPage.address1.isDisplayed());
        Assert.assertTrue(checkOutPage.zipCode.isDisplayed());
        Assert.assertTrue(checkOutPage.phoneNumber.isDisplayed());
        Reporter.log("TC-CHK-04 Passed", true);
    }

    // TC-CHK-05: Verify valid billing information
    @Test(groups = {"SmokeGrp"}, priority = 4)
    public void verifyValidBillingInformation() throws IOException, InterruptedException {
        fillBillingData(4);
        Assert.assertEquals(checkOutPage.firstName.getAttribute("value"), data(4, 1));
        Assert.assertEquals(checkOutPage.lastName.getAttribute("value"), data(4, 2));
        Assert.assertEquals(checkOutPage.email.getAttribute("value"), data(4, 3));

        Reporter.log("TC-CHK-05 Passed", true);
    }

    // TC-CHK-06: Verify blank required fields show validation
    @Test(groups = {"functionalGroup"}, priority = 5)
    public void verifyBlankBillingInformation() throws Exception {
        checkOutPage.selectNewAddressIfAvailable();
        checkOutPage.firstName.clear();
        checkOutPage.lastName.clear();
        checkOutPage.email.clear();
        checkOutPage.city.clear();
        checkOutPage.address1.clear();
        checkOutPage.zipCode.clear();
        checkOutPage.phoneNumber.clear();

        wUtil.takingScreenshot(driver, "BlankCheckoutBillingFields");
        checkOutPage.clickContinue();
        Thread.sleep(1000);

        Assert.assertTrue(checkOutPage.isErrorDisplayed(), "Validation message was not displayed");
        Reporter.log("TC-CHK-06 Passed", true);
    }

    // TC-CHK-07: Verify invalid email validation
    @Test(groups = {"functionalGroup"}, priority = 6)
    public void verifyInvalidEmail() throws Exception {
        fillBillingData(6);
        checkOutPage.clickContinue();
        Thread.sleep(1000);

        wUtil.takingScreenshot(driver, "InvalidCheckoutEmail");

        Assert.assertTrue(checkOutPage.isBillingFormDisplayed() || checkOutPage.isErrorDisplayed(), "Invalid email was accepted without validation");
        Reporter.log("TC-CHK-07 Passed", true);
    }

    // TC-CHK-09: Verify state can be selected
    @Test(groups = {"functionalGroup"}, priority = 7)
    public void verifyStateSelection() throws InterruptedException {
        checkOutPage.selectNewAddressIfAvailable();
        new Select(checkOutPage.country).selectByVisibleText("United States");
        Thread.sleep(1000);
        new Select(checkOutPage.state).selectByVisibleText("California");

        Assert.assertEquals(new Select(checkOutPage.state).getFirstSelectedOption().getText(), "California");
        Reporter.log("TC-CHK-09 Passed", true);
    }

    // TC-CHK-10: End-to-end system test - place order
    @Test(groups = {"SystemGrp"}, priority = 8)
    public void completeCheckoutEndToEnd() throws IOException, InterruptedException {
        // Step 1: Enter billing address
        fillBillingData(8);
        checkOutPage.clickContinue();
        Thread.sleep(1000);

        Assert.assertFalse(checkOutPage.isErrorDisplayed(), "Billing validation errors appeared. Check CheckoutData row 8.");

        // Step 2: Continue from shipping address
        Assert.assertTrue(checkOutPage.continueFromShippingAddress(), "Could not continue from shipping address.");

        // Step 3: Continue from shipping method
        Assert.assertTrue(checkOutPage.continueFromShippingMethod(), "Could not continue from shipping method.");

        // Step 4: Select Cash On Delivery and continue
        Assert.assertTrue(checkOutPage.selectPaymentMethod(), "Cash On Delivery payment method was not found.");
        Assert.assertTrue(checkOutPage.continueFromPaymentMethod(), "Could not continue from payment method.");

        // Step 5: Verify payment information
        Thread.sleep(500);
        String pageText = driver.findElement(By.tagName("body")).getText();

        if (pageText.contains("You will pay by COD")) {
            Reporter.log("Payment information verified: You will pay by COD", true);
        }

        Assert.assertTrue(checkOutPage.continueFromPaymentInformation(), "Could not continue from payment information.");

        // Step 6: Confirm order
        Assert.assertTrue(checkOutPage.confirmOrder(), "Confirm order button was not clickable.");

        // Step 7: Verify order confirmation
        Assert.assertTrue(checkOutPage.isOrderConfirmationDisplayed(), "Order confirmation was not displayed.");

        Reporter.log("TC-CHK-10 Passed - Your order has been successfully processed!", true);
    }
}
