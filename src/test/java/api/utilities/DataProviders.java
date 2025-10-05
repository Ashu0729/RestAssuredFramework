package api.utilities;

import org.testng.annotations.DataProvider;
import java.util.Iterator;
import java.util.List;

public class DataProviders {
    @DataProvider(name = "userDataFromExcel")
    public static Iterator<Object[]> userDataFromExcel() throws Exception {
        String excelPath = "src/test/resources/DataDriven.xlsx";
        ExcelUtils excel = new ExcelUtils(excelPath);
        List<Object[]> data = excel.getSheetData("User"); // Change sheet name if needed
        return data.iterator();
    }
}
