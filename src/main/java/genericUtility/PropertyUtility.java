
package genericUtility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtility {
    public static String getData(String key) throws IOException {
        FileInputStream file = new FileInputStream("src/main/resources/CommonData.properties");
        Properties prop = new Properties();
        prop.load(file);
        String value = prop.getProperty(key);
        file.close();
        return value;
    }
}
