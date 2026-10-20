
package objRepo;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductPage {
    WebDriver driver;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Homepage logo
    @FindBy(css = ".header-logo a")
    public WebElement logo;

    // Main navigation categories
    @FindBy(css = ".header-menu > ul > li")
    public List<WebElement> navigationLinks;

    // Search textbox
    @FindBy(id = "small-searchterms")
    public WebElement searchBox;

    // Search button
    @FindBy(css = "input.search-box-button")
    public WebElement searchBtn;

    // Product names in search results
    @FindBy(css = ".product-title a")
    public List<WebElement> productNames;

    // Product prices
    @FindBy(css = ".product-item .prices .price")
    public List<WebElement> productPrices;

    // Product details title
    @FindBy(css = ".product-name h1, .product-name")
    public WebElement productTitle;

    // Shopping cart link
    @FindBy(className = "ico-cart")
    public WebElement cartLink;

    // Search for a product
    public void searchProduct(String value) throws InterruptedException {
        searchBox.clear();
        if (value != null) {
            searchBox.sendKeys(value);
        }
        searchBtn.click();
        Thread.sleep(1500);
    }

    // Check whether a product appears in search results
    public boolean isProductDisplayed(String productName) {
        for (WebElement product : productNames) {
            if (product.getText().trim().equalsIgnoreCase(productName.trim())) {
                return true;
            }
        }
        return false;
    }

    // Open a product from search results
    public void openProduct(String productName) throws InterruptedException {
        for (WebElement product : productNames) {
            if (product.getText().trim().equalsIgnoreCase(productName.trim())) {
                product.click();
                Thread.sleep(1500);
                return;
            }
        }
        throw new RuntimeException("Product not found: " + productName);
    }

    // Get the product details title
    public String getProductTitle() {
        return productTitle.getText();
    }

    // Get the number of search results
    public int getSearchResultCount() {
        return productNames.size();
    }
}
