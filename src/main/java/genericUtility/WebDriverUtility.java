package genericUtility;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
public class WebDriverUtility {

    public void takingScreenshot(WebDriver driver, String fileName) throws IOException {
        File folder = new File("./screenshots");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File destination = new File(folder, fileName + ".png");
        FileHandler.copy(source, destination);
    }
}

