
package genericUtility;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.FileInputStream;
import java.io.IOException;
public class ExcelUtility {
    public static String getData(String sheetName, int row, int cell) throws IOException {
        FileInputStream excel = new FileInputStream("src/test/resources/DemoWebShop_TestData.xlsx");
        Workbook wb = WorkbookFactory.create(excel);
        DataFormatter df = new DataFormatter();

        String data = df.formatCellValue(wb.getSheet(sheetName).getRow(row).getCell(cell));
        wb.close();
        excel.close();
        return data;
    }
}
