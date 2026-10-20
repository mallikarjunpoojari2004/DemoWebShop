
package objRepo;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CartPage {
    WebDriver driver;
    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Cart page title
    @FindBy(css = ".page-title h1")
    public WebElement pageTitle;

    // Cart rows
    @FindBy(css = ".cart-item-row")
    public List<WebElement> cartItems;

    // Product names in cart
    @FindBy(css = ".product-name")
    public List<WebElement> cartProductNames;

    // Quantity fields
    @FindBy(css = ".qty-input")
    public List<WebElement> quantities;

    // Remove checkboxes
    @FindBy(css = ".remove-from-cart input")
    public List<WebElement> removeCheckboxes;

    // Update cart button
    @FindBy(name = "updatecart")
    public WebElement updateCartBtn;

    // Continue shopping button
    @FindBy(name = "continueshopping")
    public WebElement continueShoppingBtn;

    // Terms of service checkbox
    @FindBy(id = "termsofservice")
    public WebElement termsCheckbox;

    // Checkout button
    @FindBy(id = "checkout")
    public WebElement checkoutBtn;

    // Cart line subtotals
    @FindBy(css = ".product-subtotal")
    public List<WebElement> productSubtotals;

    // Get cart title
    public String getPageTitle() {
        return pageTitle.getText();
    }

    // Get cart item count
    public int getCartItemCount() {
        return cartItems.size();
    }

    // Check whether product is displayed
    public boolean isProductDisplayed(String productName) {
        for (WebElement product : cartProductNames) {
            if (product.getText().trim().equalsIgnoreCase(productName.trim())) {
                return true;
            }
        }
        return false;
    }

    // Update first product quantity
    public void updateQuantity(String quantity) throws InterruptedException {
        quantities.get(0).clear();
        quantities.get(0).sendKeys(quantity);
        updateCartBtn.click();
        Thread.sleep(1500);
    }

    // Remove first product
    public void removeFirstProduct() throws InterruptedException {
        removeCheckboxes.get(0).click();
        updateCartBtn.click();
        Thread.sleep(1500);
    }

    // Continue shopping
    public void continueShopping() throws InterruptedException {
        continueShoppingBtn.click();
        Thread.sleep(1500);
    }

    // Get first product subtotal text
    public String getFirstProductSubtotal() {
        return productSubtotals.get(0).getText().replaceAll("[^0-9.]", "");
    }

    // Proceed to checkout
    public void checkout() throws InterruptedException {
        if (!termsCheckbox.isSelected()) {
            termsCheckbox.click();
        }
        checkoutBtn.click();
        Thread.sleep(2000);
    }
}
