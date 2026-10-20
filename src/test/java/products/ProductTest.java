package products;
import java.io.IOException;
import genericUtility.WebDriverUtility;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import genericUtility.BaseClass;
import genericUtility.ExcelUtility;
public class ProductTest extends BaseClass {
    WebDriverUtility wUtil = new WebDriverUtility();

    // TC-HOME-01: Verify homepage loads
    @Test(groups = {"SmokeGrp"}, priority = 1)
    public void verifyHomepageLoads() {
        Assert.assertTrue(driver.getTitle().contains("Demo Web Shop"), "Homepage title is incorrect");
        Assert.assertTrue(productPage.logo.isDisplayed(), "Homepage logo is not displayed");
        Reporter.log("TC-HOME-01 Passed", true);
    }

    // TC-HOME-02: Verify logo and navigation
    @Test(groups = {"functionalGroup"}, priority = 2)
    public void verifyLogoAndNavigation() {
        Assert.assertTrue(productPage.logo.isDisplayed(), "Logo is not displayed");
        Assert.assertTrue(!productPage.navigationLinks.isEmpty(), "Navigation links are not displayed");
        Reporter.log("TC-HOME-02 Passed", true);
    }

    // TC-HOME-03: Verify search controls
    @Test(groups = {"functionalGroup"}, priority = 3)
    public void verifySearchControls() {
        Assert.assertTrue(productPage.searchBox.isDisplayed(), "Search textbox is not displayed");
        Assert.assertTrue(productPage.searchBtn.isDisplayed(), "Search button is not displayed");
        Assert.assertTrue(productPage.searchBox.isEnabled(), "Search textbox is disabled");
        Assert.assertTrue(productPage.searchBtn.isEnabled(), "Search button is disabled");
        Reporter.log("TC-HOME-03 Passed", true);
    }

    // TC-HOME-04: Search using a valid keyword
    @Test(groups = {"SmokeGrp"}, priority = 4)
    public void searchUsingValidKeyword() throws IOException, InterruptedException {
        String keyword = ExcelUtility.getData("ProductData", 1, 1);
        productPage.searchProduct(keyword);

        Assert.assertTrue(productPage.getSearchResultCount() > 0, "No products found for: " + keyword);
        Reporter.log("TC-HOME-04 Passed", true);
    }

    // TC-HOME-05: Search using complete product name
    @Test(groups = {"SmokeGrp"}, priority = 5)
    public void searchUsingCompleteProductName() throws IOException, InterruptedException {
        String productName = ExcelUtility.getData("ProductData", 2, 1);
        productPage.searchProduct(productName);
        Assert.assertTrue(productPage.isProductDisplayed(productName), "Expected product was not found: " + productName);
        Reporter.log("TC-HOME-05 Passed", true);
    }

    // TC-HOME-06: Search using partial keyword
    @Test(groups = {"functionalGroup"}, priority = 6)
    public void searchUsingPartialKeyword() throws IOException, InterruptedException {
        String keyword = ExcelUtility.getData("ProductData", 3, 1);
        productPage.searchProduct(keyword);
        Assert.assertTrue(productPage.getSearchResultCount() > 0, "No products found for partial keyword: " + keyword);
        Reporter.log("TC-HOME-06 Passed", true);
    }

    // TC-HOME-09: Verify unmatched keyword and blank search validation
    @Test(groups = {"functionalGroup"}, priority = 7)
    public void verifyNegativeSearchScenarios() throws IOException, InterruptedException {
        String keyword = ExcelUtility.getData("ProductData", 4, 1);
        // Negative scenario 1: Search for a product that does not exist
        productPage.searchProduct(keyword);
        Thread.sleep(1000);
        Assert.assertEquals(productPage.getSearchResultCount(), 0, "Unexpected products were displayed for an unmatched keyword");
        wUtil.takingScreenshot(driver, "UnmatchedProductSearch");
        Reporter.log("Unmatched product search verified", true);

        // Negative scenario 2: Blank search
        productPage.searchBox.clear();

        // Take screenshot before clicking Search because a JavaScript alert opens
        wUtil.takingScreenshot(driver, "BlankProductSearchBeforeAlert");
        productPage.searchBtn.click();

        // Handle the alert before performing any other browser action
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        Assert.assertEquals(alertText, "Please enter some search keyword", "Incorrect validation alert for blank search");
        alert.accept();
        Reporter.log("Blank product search validation verified", true);
        Reporter.log("TC-HOME-09 Passed", true);
    }
}
