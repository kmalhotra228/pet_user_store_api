package utilties;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtility {
    private String filePath;
    private String sheetName;
    private Workbook workbook;
    private Sheet sheet;

    /**
     * Constructor to initialize Excel file and sheet
     * @param filePath Path to Excel file
     * @param sheetName Name of the sheet to work with
     */
    public ExcelUtility(String filePath, String sheetName) {
        this.filePath = filePath;
        this.sheetName = sheetName;
        initializeWorkbook();
    }

    /**
     * Initialize the workbook and sheet
     */
    private void initializeWorkbook() {
        try {
            FileInputStream excelFile = new FileInputStream(new File(filePath));
            workbook = new XSSFWorkbook(excelFile);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Excel workbook: " + e.getMessage());
        }
    }

    /**
     * Get total number of rows in the sheet
     * @return Row count
     */
    public int getRowCount() {
        if (sheet == null) return 0;
        return sheet.getLastRowNum() + 1; // Adding 1 because getLastRowNum is 0-based
    }

    /**
     * Get number of cells in a specific row
     * @param rowNum Row number (0-based)
     * @return Cell count for the specified row
     */
    public int getCellCount(int rowNum) {
        if (sheet == null) return 0;
        Row row = sheet.getRow(rowNum);
        if (row == null) return 0;
        return row.getLastCellNum(); // This is already the count (1-based)
    }

    /**
     * Get cell data as string
     * @param rowNum Row number (0-based)
     * @param colNum Column number (0-based)
     * @return Cell value as string
     */
    public String getCellData(int rowNum, int colNum) {
        if (sheet == null) return "";
        Row row = sheet.getRow(rowNum);
        if (row == null) return "";
        return getCellValueAsString(row.getCell(colNum));
    }

    /**
     * Get cell data as string using column name
     * @param rowNum Row number (0-based)
     * @param colName Column name (header)
     * @return Cell value as string
     */
    public String getCellData(int rowNum, String colName) {
        if (sheet == null) return "";
        
        // Find column index by name from header row (row 0)
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) return "";
        
        int colIndex = -1;
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && colName.equalsIgnoreCase(getCellValueAsString(cell))) {
                colIndex = i;
                break;
            }
        }
        
        if (colIndex == -1) return "";
        return getCellData(rowNum, colIndex);
    }

    /**
     * Set cell data by row and column numbers
     * @param rowNum Row number (0-based)
     * @param colNum Column number (0-based)
     * @param value Value to set
     */
    public void setCellData(int rowNum, int colNum, String value) {
        if (sheet == null) return;
        
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }
        
        Cell cell = row.getCell(colNum);
        if (cell == null) {
            cell = row.createCell(colNum);
        }
        
        cell.setCellValue(value);
        saveWorkbook();
    }

    /**
     * Set cell data by row number and column name
     * @param rowNum Row number (0-based)
     * @param colName Column name (header)
     * @param value Value to set
     */
    public void setCellData(int rowNum, String colName, String value) {
        if (sheet == null) return;
        
        // Find column index by name from header row (row 0)
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) return;
        
        int colIndex = -1;
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && colName.equalsIgnoreCase(getCellValueAsString(cell))) {
                colIndex = i;
                break;
            }
        }
        
        if (colIndex != -1) {
            setCellData(rowNum, colIndex, value);
        }
    }

    /**
     * Get all data from the sheet as a list of maps (key-value pairs)
     * @return List of maps where each map represents a row
     */
    public List<Map<String, String>> getAllData() {
        List<Map<String, String>> dataList = new ArrayList<>();
        
        // Get header row to use as keys
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            return dataList;
        }
        
        // Iterate through each row (skip header)
        for (int i = 1; i < getRowCount(); i++) {
            Row currentRow = sheet.getRow(i);
            if (currentRow == null) continue;
            
            Map<String, String> rowData = new HashMap<>();
            
            // Iterate through each cell in the row
            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                String header = getCellValueAsString(headerRow.getCell(j));
                String value = getCellValueAsString(currentRow.getCell(j));
                rowData.put(header, value);
            }
            
            dataList.add(rowData);
        }
        
        return dataList;
    }

    /**
     * Get test data for a specific test case
     * @param testCaseName Name of the test case to get data for
     * @return Map containing the test data
     */
    public Map<String, String> getTestData(String testCaseName) {
        // Get header row to use as keys
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            return null;
        }
        
        // Iterate through each row to find the test case
        for (int i = 1; i < getRowCount(); i++) {
            Row currentRow = sheet.getRow(i);
            if (currentRow == null) continue;
            
            // Check if first cell matches test case name
            Cell testCaseCell = currentRow.getCell(0);
            if (testCaseCell != null && testCaseName.equals(getCellValueAsString(testCaseCell))) {
                Map<String, String> testData = new HashMap<>();
                
                // Get all data for this test case
                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    String header = getCellValueAsString(headerRow.getCell(j));
                    String value = getCellValueAsString(currentRow.getCell(j));
                    testData.put(header, value);
                }
                
                return testData;
            }
        }
        
        return null;
    }

    /**
     * Helper method to save workbook changes
     */
    private void saveWorkbook() {
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            
            // Reinitialize workbook after writing
            initializeWorkbook();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write to Excel file: " + e.getMessage());
        }
    }

    /**
     * Helper method to get cell value as string
     * @param cell Cell to get value from
     * @return String representation of cell value
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Return integer if it's a whole number, otherwise double
                    double numValue = cell.getNumericCellValue();
                    if (numValue == (int) numValue) {
                        return String.valueOf((int) numValue);
                    } else {
                        return String.valueOf(numValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return getCellValueAsString(workbook.getCreationHelper()
                            .createFormulaEvaluator().evaluateInCell(cell));
                } catch (Exception e) {
                    return cell.getCellFormula();
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * Close the workbook
     */
    public void close() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}