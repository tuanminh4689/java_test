package StoreDB;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import Models.Cases;
import Models.Instance;
import Utils.PropertiesUtils;
import io.jsondb.*;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;


public class StoreDB {
	private static String FILE_NAME = "FilesTest/TestSpec.xlsx";

	public static void main(String[] args) throws GeneralSecurityException, IOException {
		String rootFolder = System.getProperty("user.dir");
		String dbFolderPath = rootFolder + "/" +  PropertiesUtils.getProperty("db.dir") + "/";
		String filePath = rootFolder + "/" + FILE_NAME;
		
		File dbFolder = new File(dbFolderPath);
		if(!dbFolder.exists()) {
			dbFolder.mkdir();
		}
		
		System.out.println("Start read excel: " + new Date().getTime());
		//read excel
		FileInputStream excelFile = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        int totalRow = datatypeSheet.getLastRowNum();
        int groupNoColumn = -1;
        int lastGroupNoColumn = -1;
        int groupNoRow = -1;
        int subGroupNoColumn = -1;
        int beginTestCondition = -1;
        String testScreenId = "";
        String testScreenName = "";
        
        String testScreenIdIdentity = PropertiesUtils.getProperty("excel.test.screen.id.identity");
        String testScreenNameIdentity = PropertiesUtils.getProperty("excel.test.screen.name");
        String groupNoIdentity = PropertiesUtils.getProperty("excel.testcase.group.no");
        String subGroupNoIdentity = PropertiesUtils.getProperty("excel.testcase.sub.group.no");
        for(int rownum = 0;rownum <= totalRow; rownum++) {
        	Row currentRow = datatypeSheet.getRow(rownum);
        	if(currentRow != null) {
        		int lastCellNum = currentRow.getLastCellNum();
        		for(int cellnum = 0; cellnum <= lastCellNum; cellnum++) {
	        		Cell currentCell = currentRow.getCell(cellnum);
	        		if(currentCell != null) {
	        			if(currentCell.getCellTypeEnum() == CellType.STRING) {
	        				System.out.println(currentRow.getCell(cellnum).getStringCellValue());
		                	if(currentCell.getStringCellValue().equalsIgnoreCase(testScreenIdIdentity)) {
		                		testScreenId = currentRow.getCell(cellnum + 1).getStringCellValue();
		                	}else if(currentCell.getStringCellValue().equalsIgnoreCase(testScreenNameIdentity)) {
		                		testScreenName = currentRow.getCell(cellnum + 1).getStringCellValue();
		                	}
		                	else if(currentCell.getStringCellValue().equalsIgnoreCase(groupNoIdentity)) {
		                		groupNoColumn = cellnum;
		                		lastGroupNoColumn = lastCellNum;
		                		groupNoRow = rownum;
		                	}
		                	else if(currentCell.getStringCellValue().equalsIgnoreCase(subGroupNoIdentity)) {
		                		subGroupNoColumn = cellnum;
		                		beginTestCondition = rownum + 1;
		                		break;
		                	}
		                }
	        		}
        		}
        		if(beginTestCondition != -1) {
        			break;
        		}
        	}
        }
//        System.out.println(testScreenId);
//        System.out.println(testScreenName);
//        System.out.println(groupNoColumn);
//        System.out.println(subGroupNoColumn);
//        System.out.println(beginTestCondition);
        int noColumn = -1;
        int fieldNameColumn = -1;
//        int required = -1;
        int typeColumn = -1;
        int numberOfDigitColumn = -1;
        String noHeader = PropertiesUtils.getProperty("test.condition.header.no");
        String fieldNameHeader = PropertiesUtils.getProperty("test.condition.header.field.name");
//        String requiredHeader = PropertiesUtils.getProperty("test.condition.header.required");
        String typeHeader = PropertiesUtils.getProperty("test.condition.header.type");
        String numberOfDigitHeader = PropertiesUtils.getProperty("test.condition.header.number.of.digit");

        //lay vi tri cac cot trong table
        Row conditionHeaderRow = datatypeSheet.getRow(beginTestCondition);
        if(conditionHeaderRow != null) {
        	for(int cellnum = 0; cellnum <= conditionHeaderRow.getLastCellNum(); cellnum++) {
        		Cell currentCell = conditionHeaderRow.getCell(cellnum);
        		if(currentCell != null) {
        			if(currentCell.getCellTypeEnum() == CellType.STRING) {
            			if(currentCell.getStringCellValue().equalsIgnoreCase(noHeader)) {
            				noColumn = cellnum;
            			}else if(currentCell.getStringCellValue().equalsIgnoreCase(fieldNameHeader)) {
            				fieldNameColumn = cellnum;
            			}else if(currentCell.getStringCellValue().equalsIgnoreCase(typeHeader)) {
            				typeColumn = cellnum;
            			}else if(currentCell.getStringCellValue().equalsIgnoreCase(numberOfDigitHeader)) {
            				numberOfDigitColumn = cellnum;
            			}
            		}
        		}
        	}
        }
        
        //lay danh sach field name dua vao object
//        for(int rownum = beginTestCondition + 1; rownum <= totalRow; rownum++) {
//        	Row currentRow = datatypeSheet.getRow(rownum);
//        	if(currentRow != null) {
//        		
//        	}
//        }
        List<Cases> cases = new ArrayList<Cases>(); 
        Row groupNoRowData = datatypeSheet.getRow(groupNoRow);
        Row subGroupNoRowData = datatypeSheet.getRow(groupNoRow + 1);
        for(int group = groupNoColumn + 1; group < lastGroupNoColumn; group++) {
        	int groupNo = (int)groupNoRowData.getCell(group).getNumericCellValue();
        	int subGroupNo = (int)subGroupNoRowData.getCell(group).getNumericCellValue();
        	
        	for(int rownum = beginTestCondition + 1; rownum < totalRow; rownum++) {
        		Row currentRow = datatypeSheet.getRow(rownum);
        		if(currentRow != null) {
        			if(currentRow.getCell(fieldNameColumn) == null) {
        				break;
        			}
        			int no = (int)currentRow.getCell(noColumn).getNumericCellValue();
        			String fieldName = currentRow.getCell(fieldNameColumn).getStringCellValue();
        			String type = currentRow.getCell(typeColumn).getStringCellValue();
        			int numberOfDigit = (int)currentRow.getCell(numberOfDigitColumn).getNumericCellValue();
        			String value = currentRow.getCell(group).getStringCellValue();
        			Cases instance = new Cases(testScreenId, testScreenName, groupNo, subGroupNo, 
        					no, fieldName, type, numberOfDigit, value);
        			cases.add(instance);
        					
        		}
        	}
        	
        	
//        	Row currentRow = datatypeSheet.getRow();
//        	if(currentRow != null) {
//        		
//        	}
        }
        
        
        printArray(cases);

        workbook.close();
        
		//end
		System.out.println("End read excel: " + new Date().getTime());
		
		//DB
		String baseScanPackage = "Models";
		ICipher cipher = new DefaultAESCBCCipher("1r8+24pibarAWgS85/Heeg==");
		JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(dbFolderPath, baseScanPackage, cipher);
		if(!jsonDBTemplate.collectionExists(Instance.class))
		{
			jsonDBTemplate.createCollection(Instance.class);	
		}
		
		System.out.println(new Date().getTime());
		for(int i=0;i<1000;i++) {
			Instance instance = new Instance();
			instance.setHostname("host" + i);
//			instances.add(instance);
			jsonDBTemplate.insert(instance);
			jsonDBTemplate.reLoadDB();
		}
//		jsonDBTemplate.insert
//		jsonDBTemplate.insert(instances, "instances");
		System.out.println(new Date().getTime());
		System.out.println("Done!!!!");
	}
	
	public static void printArray(List<Cases> cases) {
		System.out.println(cases.size());
		for(Cases obj: cases) {
			System.out.println(obj.getFieldName() + ": " + obj.getValue());
		}
	}
}
