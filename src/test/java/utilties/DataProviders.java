package utilties;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {
	
	@DataProvider(name = "datas")
	public Object[][] getAllData() {
	    String path = System.getProperty("user.dir") + "//testData//data.xlsx";
	    ExcelUtility xl = new ExcelUtility(path, "sheet1");

	    int rownum = xl.getRowCount();
	    int colcount = xl.getCellCount(1);

	    List<Object[]> validData = new ArrayList<>();

	    for (int i = 1; i <= rownum; i++) {
	        boolean isRowValid = false;
	        String[] row = new String[colcount];

	        for (int j = 0; j < colcount; j++) {
	            String cellValue = xl.getCellData(i, j);
	            row[j] = cellValue;

	            if (cellValue != null && !cellValue.trim().isEmpty()) {
	                isRowValid = true;
	            }
	        }

	        if (isRowValid) {
	            validData.add(row);
	        }
	    }

	    return validData.toArray(new Object[0][0]);
	}

	
	@DataProvider(name = "usernames")
	public Object[][] getUserNames() {
	    String path = System.getProperty("user.dir") + "//testData//data.xlsx";
	    ExcelUtility xl = new ExcelUtility(path, "sheet1");

	    int rownum = xl.getRowCount();
	    List<Object[]> userList = new ArrayList<>();

	    for (int i = 1; i <= rownum; i++) {
	        String username = xl.getCellData(i, 1);
	        if (username != null && !username.trim().isEmpty()) {
	            userList.add(new Object[]{username});
	        }
	    }

	    return userList.toArray(new Object[0][0]);
	}

}
