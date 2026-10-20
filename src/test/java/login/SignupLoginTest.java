package login;
import java.io.IOException;
import java.util.Random;
import genericUtility.WebDriverUtility;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import genericUtility.BaseClass;
import genericUtility.ExcelUtility;
public class SignupLoginTest extends BaseClass {
    WebDriverUtility wUtil = new WebDriverUtility();

    // TC-AUTH-01: Verify registration controls
    @Test(groups = {"functionalGroup"}, priority = 1)
    public void verifyRegistrationControls() throws InterruptedException {
        registerPage.registerLink.click();
        Thread.sleep(1000);

        Assert.assertTrue(registerPage.maleGender.isDisplayed());
        Assert.assertTrue(registerPage.firstName.isDisplayed());
        Assert.assertTrue(registerPage.lastName.isDisplayed());
        Assert.assertTrue(registerPage.email.isDisplayed());
        Assert.assertTrue(registerPage.password.isDisplayed());
        Assert.assertTrue(registerPage.confirmPassword.isDisplayed());
        Assert.assertTrue(registerPage.registerBtn.isDisplayed());

        Reporter.log("TC-AUTH-01 Passed", true);
    }

    // TC-AUTH-02: Register with valid details
    @Test(groups = {"SmokeGrp"}, priority = 2)
    public void registerNewUser() throws IOException, InterruptedException {
        String password = ExcelUtility.getData("SignupLoginData", 1, 2);
        String firstName = ExcelUtility.getData("SignupLoginData", 1, 3);
        String lastName = ExcelUtility.getData("SignupLoginData", 1, 4);
        String confirmPassword = ExcelUtility.getData("SignupLoginData", 1, 5);
        String gender = ExcelUtility.getData("SignupLoginData", 1, 6);

        String email = "ugram" + (100000 + new Random().nextInt(900000)) + "@gmail.com";

        registerPage.registerLink.click();
        Thread.sleep(1000);
        registerPage.register(gender, firstName, lastName, email, password, confirmPassword);
        Thread.sleep(1500);

        Assert.assertTrue(registerPage.isRegistrationSuccessful(), "Registration was not successful");
        Reporter.log("TC-AUTH-02 Passed", true);
    }

    // TC-AUTH-03: Verify blank registration fields
    @Test(groups = {"functionalGroup"}, priority = 3)
    public void registerWithBlankFields() throws Exception {
        registerPage.registerLink.click();
        Thread.sleep(1000);
        registerPage.clickRegister();
        Thread.sleep(1000);

        wUtil.takingScreenshot(driver, "BlankRegistrationFields");
        Assert.assertTrue(registerPage.isValidationDisplayed(), "Required-field validation was not displayed");
        Reporter.log("TC-AUTH-03 Passed", true);
    }

    // TC-AUTH-04: Verify invalid registration email
    @Test(groups = {"functionalGroup"}, priority = 4)
    public void registerWithInvalidEmail() throws Exception {
        String email = ExcelUtility.getData("SignupLoginData", 3, 1);
        String password = ExcelUtility.getData("SignupLoginData", 3, 2);
        String firstName = ExcelUtility.getData("SignupLoginData", 3, 3);
        String lastName = ExcelUtility.getData("SignupLoginData", 3, 4);
        String confirmPassword = ExcelUtility.getData("SignupLoginData", 3, 5);
        String gender = ExcelUtility.getData("SignupLoginData", 3, 6);

        registerPage.registerLink.click();
        Thread.sleep(1000);
        registerPage.register(gender, firstName, lastName, email, password, confirmPassword);
        Thread.sleep(1000);

        wUtil.takingScreenshot(driver, "InvalidRegistrationEmail");
        Assert.assertTrue(registerPage.isValidationDisplayed(), "Invalid-email validation was not displayed");
        Reporter.log("TC-AUTH-04 Passed", true);
    }

    // TC-AUTH-06: Verify valid login
    @Test(groups = {"IntegrationGrp", "SmokeGrp"}, priority = 5)
    public void validLoginTest() throws IOException, InterruptedException {
        String email = ExcelUtility.getData("SignupLoginData", 4, 1);
        String password = ExcelUtility.getData("SignupLoginData", 4, 2);

        loginPage.loginLink.click();
        Thread.sleep(1000);
        loginPage.login(email, password);
        Thread.sleep(1000);

        Assert.assertTrue(loginPage.logoutLink.isDisplayed(), "Login was not successful");
        Reporter.log("TC-AUTH-06 Passed", true);
    }

    // TC-AUTH-08: Verify incorrect password
    @Test(groups = {"functionalGroup"}, priority = 6)
    public void invalidPasswordTest() throws Exception {
        String email = ExcelUtility.getData("SignupLoginData", 5, 1);
        String password = ExcelUtility.getData("SignupLoginData", 5, 2);

        loginPage.loginLink.click();
        Thread.sleep(1000);
        loginPage.login(email, password);
        Thread.sleep(1000);

        wUtil.takingScreenshot(driver, "InvalidLoginPassword");
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Login error was not displayed for an incorrect password");
        Reporter.log("TC-AUTH-08 Passed", true);
    }

    // TC-AUTH-09: Verify unregistered email
    @Test(groups = {"functionalGroup"}, priority = 7)
    public void unregisteredEmailTest() throws Exception {
        String email = ExcelUtility.getData("SignupLoginData", 7, 1);
        String password = ExcelUtility.getData("SignupLoginData", 7, 2);

        loginPage.loginLink.click();
        Thread.sleep(1000);
        loginPage.login(email, password);
        Thread.sleep(1000);

        wUtil.takingScreenshot(driver, "UnregisteredLoginEmail");
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Login error was not displayed for an unregistered email");
        Reporter.log("TC-AUTH-09 Passed", true);
    }
}