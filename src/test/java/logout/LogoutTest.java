package logout;
import java.io.IOException;
import genericUtility.WebDriverUtility;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import genericUtility.BaseClass;
import genericUtility.ExcelUtility;
public class LogoutTest extends BaseClass {
    WebDriverUtility wUtil = new WebDriverUtility();
    // Read data from LogoutData sheet
    public String data(int row, int column) throws IOException {
        return ExcelUtility.getData("LogoutData", row, column);
    }

    // Login before testing logout
    public void loginUser() throws IOException, InterruptedException {
        driver.get("https://demowebshop.tricentis.com/login");

        String email = ExcelUtility.getData("SignupLoginData", 6, 1);
        String password = ExcelUtility.getData("SignupLoginData", 6, 2);

        loginPage.login(email, password);
        Assert.assertTrue(driver.getCurrentUrl().contains("demowebshop.tricentis.com"), "Login did not complete");
    }

    // TC-LOGOUT-01: Verify Logout link after successful login
    @Test(groups = {"functionalGroup"}, priority = 1)
    public void verifyLogoutLinkDisplayed() throws IOException, InterruptedException {
        loginUser();

        Assert.assertTrue(logoutPage.logoutLink.isDisplayed(), "Logout link is not displayed after login");
        Reporter.log("TC-LOGOUT-01 Passed", true);
    }

    // TC-LOGOUT-02: Verify user can log out successfully
    @Test(groups = {"SmokeGrp"}, priority = 2)
    public void verifyLogoutFunctionality() throws IOException, InterruptedException {
        loginUser();
        logoutPage.logout();

        Assert.assertTrue(logoutPage.loginLink.isDisplayed(), "Login link is not displayed after logout");
        Reporter.log("TC-LOGOUT-02 Passed", true);
    }

    // TC-LOGOUT-03: Verify Contact Us page opens
    @Test(groups = {"functionalGroup"}, priority = 3)
    public void verifyContactUsPageOpens() throws InterruptedException {
        driver.get("https://demowebshop.tricentis.com/contactus");
        Thread.sleep(1500);

        Assert.assertTrue(driver.getCurrentUrl().contains("contactus"), "Contact Us page did not open");
        Reporter.log("TC-LOGOUT-03 Passed", true);
    }

    // TC-LOGOUT-04: Verify Contact Us form fields
    @Test(groups = {"functionalGroup"}, priority = 4)
    public void verifyContactFormFields() throws InterruptedException {
        driver.get("https://demowebshop.tricentis.com/contactus");
        Thread.sleep(1500);

        Assert.assertTrue(logoutPage.fullName.isDisplayed(), "Full name field is not displayed");
        Assert.assertTrue(logoutPage.email.isDisplayed(), "Email field is not displayed");
        Assert.assertTrue(logoutPage.enquiry.isDisplayed(), "Enquiry field is not displayed");
        Assert.assertTrue(logoutPage.submitBtn.isDisplayed(), "Submit button is not displayed");

        Reporter.log("TC-LOGOUT-04 Passed", true);
    }

    // TC-LOGOUT-05: Verify Contact Us form submission with valid details
    @Test(groups = {"SystemGrp"}, priority = 5)
    public void submitContactFormWithValidData() throws IOException, InterruptedException {
        driver.get("https://demowebshop.tricentis.com/contactus");
        Thread.sleep(1500);

        logoutPage.enterFullName(data(1, 1));
        logoutPage.enterEmail(data(1, 2));
        logoutPage.enterEnquiry(data(1, 3));
        logoutPage.submitContactForm();
        Thread.sleep(1000);

        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("successfully sent"), "Contact form submission was not successful");

        Reporter.log("TC-LOGOUT-05 Passed", true);
    }

    // TC-LOGOUT-06: Verify invalid email validation on Contact Us form
    @Test(groups = {"functionalGroup"}, priority = 6)
    public void submitContactFormWithInvalidEmail() throws IOException, InterruptedException {
        driver.get("https://demowebshop.tricentis.com/contactus");
        Thread.sleep(1500);

        // Data index 2 is the invalid-email row in the reduced LogoutData sheet
        logoutPage.enterFullName(data(2, 1));
        logoutPage.enterEmail(data(2, 2));
        logoutPage.enterEnquiry(data(2, 3));
        logoutPage.submitContactForm();
        Thread.sleep(1000);

        wUtil.takingScreenshot(driver, "InvalidContactFormEmail");
        Assert.assertTrue(logoutPage.isErrorDisplayed(), "Validation message was not displayed for invalid email");
        Reporter.log("TC-LOGOUT-06 Passed", true);
    }
}