package api.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {
    private String filePath;

    public ExcelUtils(String filePath) {
        this.filePath = filePath;
    }

    public List<Object[]> getSheetData(String sheetName) throws IOException {
        List<Object[]> data = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);
        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext()) rowIterator.next(); // skip header
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Object[] rowData = new Object[row.getLastCellNum()];
            for (int i = 0; i < row.getLastCellNum(); i++) {
                Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String value = "";
                switch (cell.getCellType()) {
                    case STRING:
                        value = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            value = cell.getDateCellValue().toString();
                        } else {
                            value = String.valueOf(cell.getNumericCellValue());
                            if (value.endsWith(".0")) value = value.substring(0, value.length() - 2);
                        }
                        break;
                    case BOOLEAN:
                        value = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        value = cell.getCellFormula();
                        break;
                    default:
                        value = "";
                }
                rowData[i] = value;
            }
            data.add(rowData);
        }
        workbook.close();
        fis.close();
        return data;
    }
}