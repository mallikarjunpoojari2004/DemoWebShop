package cart;
import java.io.IOException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import genericUtility.BaseClass;
import genericUtility.ExcelUtility;
public class CartTest extends BaseClass {
    // Prepare browser before every test, clear any existing cart items, and return to the homepage.
    @BeforeMethod(alwaysRun = true)
    public void setUpCartTest() throws InterruptedException {
        driver.get("https://demowebshop.tricentis.com/cart");
        Thread.sleep(1000);
        List<WebElement> removeCheckboxes = driver.findElements(By.cssSelector(".remove-from-cart input"));
        if (!removeCheckboxes.isEmpty()) {
            for (WebElement checkbox : removeCheckboxes) {
                if (!checkbox.isSelected()) {
                    checkbox.click();
                }
            }
            driver.findElement(By.name("updatecart")).click();
            Thread.sleep(1500);
        }
        driver.get("https://demowebshop.tricentis.com/");
        Thread.sleep(1000);
    }

    // Add a product and open the cart
    public void addProductAndOpenCart(String productName) throws InterruptedException {
        driver.get("https://demowebshop.tricentis.com/search?q=" + productName.replace(" ", "+"));
        Thread.sleep(1500);
        boolean productFound = false;
        for (WebElement product : driver.findElements(By.cssSelector(".product-title a"))) {
            if (product.getText().trim().equalsIgnoreCase(productName.trim())) {
                product.click();
                productFound = true;
                break;
            }
        }

        Assert.assertTrue(productFound, "Product not found in search results: " + productName);
        Thread.sleep(1500);
        WebElement addToCartButton = driver.findElement(By.cssSelector("input.add-to-cart-button, button.add-to-cart-button"));
        addToCartButton.click();
        Thread.sleep(1500);

        // Open cart directly to avoid notification overlay click errors
        driver.get("https://demowebshop.tricentis.com/cart");
        Thread.sleep(1500);
    }

    // TC-CART-01: Verify cart page opens
    @Test(groups = {"IntegrationGrp"}, priority = 1)
    public void verifyCartPageOpens() throws IOException, InterruptedException {
        String productName = ExcelUtility.getData("CartData", 1, 1);
        addProductAndOpenCart(productName);

        Assert.assertTrue(cartPage.getPageTitle().toLowerCase().contains("shopping cart"), "Cart page did not open");
        Reporter.log("TC-CART-01 Passed", true);
    }

    // TC-CART-02: Verify added product appears in cart
    @Test(groups = {"IntegrationGrp"}, priority = 2)
    public void verifyAddedProductAppears() throws IOException, InterruptedException {
        String productName = ExcelUtility.getData("CartData", 2, 1);
        addProductAndOpenCart(productName);

        Assert.assertTrue(cartPage.isProductDisplayed(productName), "Added product is not displayed in cart");
        Reporter.log("TC-CART-02 Passed", true);
    }

    // TC-CART-03: Verify default product quantity
    @Test(groups = {"SmokeGrp"}, priority = 3)
    public void verifyProductQuantityDisplayed() throws IOException, InterruptedException {
        String productName = ExcelUtility.getData("CartData", 3, 1);
        addProductAndOpenCart(productName);

        Assert.assertFalse(cartPage.quantities.isEmpty(), "Quantity field is not displayed");
        Assert.assertEquals(cartPage.quantities.get(0).getAttribute("value"), "1", "Default quantity is not 1");
        Reporter.log("TC-CART-03 Passed", true);
    }

    // TC-CART-04: Update product quantity to 2
    @Test(groups = {"functionalGroup"}, priority = 4)
    public void updateQuantityToTwo() throws IOException, InterruptedException {
        String productName = ExcelUtility.getData("CartData", 4, 1);
        String quantity = ExcelUtility.getData("CartData", 4, 2);
        addProductAndOpenCart(productName);
        cartPage.updateQuantity(quantity);

        Assert.assertEquals(cartPage.quantities.get(0).getAttribute("value"), quantity, "Quantity was not updated to 2");
        Reporter.log("TC-CART-04 Passed", true);
    }

    // TC-CART-05: Update product quantity to 5
    @Test(groups = {"functionalGroup"}, priority = 5)
    public void updateQuantityToFive() throws IOException, InterruptedException {
        String productName = ExcelUtility.getData("CartData", 5, 1);
        String quantity = ExcelUtility.getData("CartData", 5, 2);
        addProductAndOpenCart(productName);
        cartPage.updateQuantity(quantity);

        Assert.assertEquals(cartPage.quantities.get(0).getAttribute("value"), quantity, "Quantity was not updated to 5");
        Reporter.log("TC-CART-05 Passed", true);
    }

    // TC-CART-07: Remove product from cart
    @Test(groups = {"functionalGroup"}, priority = 6)
    public void removeProductFromCart() throws IOException, InterruptedException {
        String productName = ExcelUtility.getData("CartData", 6, 1);
        addProductAndOpenCart(productName);
        cartPage.removeFirstProduct();

        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Product was not removed from cart");
        Assert.assertFalse(cartPage.isProductDisplayed(productName), "Removed product is still displayed");
        Reporter.log("TC-CART-07 Passed", true);
    }

    // TC-CART-08: Verify subtotal for one product
    @Test(groups = {"SystemGrp"}, priority = 7)
    public void verifySubtotalForOneProduct() throws IOException, InterruptedException {
        String productName = ExcelUtility.getData("CartData", 7, 1);
        addProductAndOpenCart(productName);

        Assert.assertFalse(cartPage.productSubtotals.isEmpty(), "Product subtotal is not displayed");
        Assert.assertFalse(cartPage.getFirstProductSubtotal().isEmpty(), "Product subtotal is blank");
        Reporter.log("TC-CART-08 Passed", true);
    }
}