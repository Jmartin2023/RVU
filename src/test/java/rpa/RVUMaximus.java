package rpa;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import objects.ExcelOperations;
import objects.SeleniumUtils;
import objects.Utility;
import utilities.ExcelReader;

public class RVUMaximus {
	Logger logger = LogManager.getLogger(RVU.class);

	String projDirPath, status, claimNo ,claimNumAvaility, DOB ,serviceDate ,firstName, lastName,memberID,ecwStatus,DOS, claimStatus,dateofbirth, npivalue, charges,currency, error, originalTab, checkNum;

	SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yy");

	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	List<WebElement> PatientList = new ArrayList<WebElement>();
	public static ExcelReader excel, excel1; 
	public static String sheetName = "Sheet1";
	String filename,maximusStatus="";
	int rowNum = 1;
	int renameRowNum = 1;
	boolean skipFlag =false, screen;
	Set matchSet = new HashSet();
	WebDriver driver;
	String URL;
String CPT2= ", 80307";
	//JavascriptExecutor js;
	SeleniumUtils sel;
	Utility utility;

	ExcelOperations excelFile;

	static String excelFileName="Updated File Chest and Critical Care.xlsx",excelFileName1= "Toxicology Drug classes. Updated (1).xlsx", accessionNum,renameStatus, CPT="";

	@BeforeTest
	public void preRec() throws InterruptedException, SAXException, IOException, ParserConfigurationException {

		sel = new SeleniumUtils(projDirPath);

		driver = sel.getDriver();
		//excelFileName= "practice file toxy new.xlsx";

		utility = new Utility();



		String url = "https://caremaximus.medcaremso.com/";
		driver.get(url);
		logger.info("Open url: " + url);
		sel.pauseClick(driver.findElement(By.id("txtEmail")), 20);

    	driver.findElement(By.id("txtEmail")).sendKeys("mdanyal@medcaremso.com");
    	logger.info("Email Entered");
    
    	
    	
    	sel.pauseClick(driver.findElement(By.id("txtPaswd")),10);
    	driver.findElement(By.id("txtPaswd")).sendKeys("Medcaremso1234@");
    	logger.info("Password Entered");
    	
    	
    	driver.findElement(By.id("btnSubmit")).click();
    	logger.info("Login button clicked");
    	
    	Thread.sleep(3000);
    	sel.pauseClick(driver.findElement(By.xpath("//h4[contains(text(),'Chest And Critical Care Consultants')]")),20);
    	try {
    	driver.findElement(By.xpath("//h4[contains(text(),'Chest And Critical Care Consultants')]")).isDisplayed();
    	}catch(Exception e) {
    		throw new SkipException("Practice not found");
    	}
    	
    	Thread.sleep(3000);
    	driver.findElement(By.xpath("//a[text()='Dashboard']")).click();
    	logger.info("Clicked on dashboard");
    	Thread.sleep(3000);
    	//sel.pauseClick(driver.findElement(By.xpath("//a[@title='Patients']")),20);
    	//driver.findElement(By.xpath("//a[@title='Patients']")).click();
    	

    	((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("//a[@title='Patients']")));
    	
    	logger.info("Clicked on patients");
    	
		
	}
	@Test(dataProvider= "getData",priority=1)
	public  void pdfUpload(Hashtable<String,String> data) throws IOException, InterruptedException, ParseException {
		rowNum++;
		
		status= data.get("Status");
		filename=data.get("File Name");
		maximusStatus=data.get("Maximus Status");
		if(status.equals("Pass") && (maximusStatus.isBlank()||maximusStatus.isEmpty()) && !filename.isBlank()) {	
			firstName= data.get("FirstName").trim();
			lastName= data.get("LastName").trim();
			DOB= data.get("DOB");
			
			SimpleDateFormat parser = new SimpleDateFormat("M/d/yyyy");
		
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
	
	    	
			dateofbirth=	formatter.format(parser.parse(DOB));
			 System.out.println("Patient is "+firstName+" "+lastName+" "+dateofbirth);
    		Thread.sleep(3000);
		try {
			firstName=firstName.split(" ")[0];
		}catch(Exception e) {
			
		}
		
			driver.findElement(By.id("firstname")).sendKeys(firstName);
	    	logger.info("First name enetered as ");
	    	
	    	if(lastName.contains(" ")) {
	    		driver.findElement(By.id("lastName")).sendKeys(lastName.split(" ")[0]);
	    	//	driver.findElement(By.id("lastName")).sendKeys(" ");
	    	//	driver.findElement(By.id("lastName")).sendKeys(lastName.split(" ")[1]);
		    	logger.info("Last name enetered as ");
	    	}else {
	    	driver.findElement(By.id("lastName")).sendKeys(lastName);
	    	logger.info("Last name enetered as ");
	    	}
	    	driver.findElement(By.id("dob")).sendKeys(dateofbirth);
	    	logger.info("Date of birth entered as "+dateofbirth);
	    	
	    	driver.findElement(By.xpath("//span[text()=' Search ']")).click();
	    	logger.info("Clicked on Search");
			Thread.sleep(3000);
			try {
	    	driver.findElement(By.xpath("//td[contains(@title,'"+lastName+", "+firstName+"')]/parent::tr/td[2]")).click();
	    	logger.info("Clicked on patient");
			}catch(Exception e) {
				
				try {
					
					driver.findElement(By.xpath("//td[contains(@title,'"+lastName.substring(0,1).toUpperCase()+lastName.substring(1).toLowerCase()+", "+firstName.substring(0,1).toUpperCase()+firstName.substring(1).toLowerCase()+"')]/parent::tr/td[2]")).click();
			    	logger.info("Clicked on patient");
					
				}
				
					catch(Exception e1) {
						logger.info("Patient not found");
						excel.setCellData(sheetName, "Maximus Status", rowNum, "Patient Not Found");
				    	Thread.sleep(2000);
				    	driver.findElement(By.id("firstname")).clear();
				    	
				    	
				    	driver.findElement(By.id("lastName")).clear();
				 
				    	
				    	driver.findElement(By.id("dob")).clear();
				    	
				    	
				    	 throw new SkipException("Patient Not Found");
						
					}
				}
				
			
	    	driver.findElement(By.xpath("//span[text()='Documents']")).click();
	    	logger.info("Clicked on Documents");
	    	Thread.sleep(2000);
	    	driver.findElement(By.xpath("//img[@title='add-patient']")).click();
	    	logger.info("Clicked on Add Documents");
	    	
	    	Thread.sleep(2000);
	    	
	    //	((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[text()='Select']")));
	    	
	    //	driver.findElement(By.xpath("//div[text()='Select']")).click();
	   // 	logger.info("Clicked on Select dropdown");
	    	
	    	
	    	
	    	
	    	driver.findElement(By.xpath("//div[text()='Select']/following-sibling::div/input")).sendKeys("Facesheet"+Keys.ENTER+Keys.ENTER);
	    	logger.info("Clicked on Select facesheet");
	    	
	    	Thread.sleep(2000);
	    	driver.findElement(By.xpath("//span[text()='Facesheet']")).click();
	    	logger.info("Clicked on Select facesheet");
	    	Thread.sleep(2000);
	    	
	    	driver.findElement(By.xpath("//a[@id='fileInput']/following-sibling::input")).sendKeys(System.getProperty("user.dir") + "\\image-"+filename+".pdf");
	    //	driver.findElement(By.xpath("//a[@id='fileInput']")).click();
	    	logger.info("Clicked on choose file");
	    	Thread.sleep(5000);
	    	
	    	
	    	try {
        		sel.waitFunc(driver.findElement(By.xpath("//span[contains(text(),'"+filename+".pdf')]")));
        		System.out.println("File Uploaded");
				}catch(Exception e) {
					for(int i=0; i<2; i++) {
						Thread.sleep(4000);
					try {
						driver.findElement(By.xpath("//span[contains(text(),'"+filename+".pdf')]")).isDisplayed();
						System.out.println("File Uploaded");
						break;
					}catch(Exception e1) {
						
					}	
				}
					
				}
	    	
	    	try {
	    		driver.findElement(By.xpath("//span[contains(text(),'"+filename+".pdf')]")).isDisplayed();
	    	}catch(Exception e) {
	    		logger.info("File could not be uploaded");
	    		driver.findElement(By.xpath("//button[text()='Cancel']")).click();
	    		logger.info("Clicked on cancel button");
	    		excel.setCellData(sheetName, "Maximus Status", rowNum, "Fail");
		    	Thread.sleep(2000);
		    	driver.findElement(By.xpath("//a/span/following-sibling::span[text()='Patients']")).click();
		    	logger.info("Clicked on patients icon");
		    	 throw new SkipException("File could not be uploaded");
	    	}
	    	
	    	
	    	driver.findElement(By.xpath("//button[text()='Save']")).click();
logger.info("Save clicked");
	    	excel.setCellData(sheetName, "Maximus Status", rowNum, "Pass");
	    	Thread.sleep(2000);
	    	driver.findElement(By.xpath("//a/span/following-sibling::span[text()='Patients']")).click();
	    	logger.info("Clicked on patients icon");
	}
		}
	
	
	@AfterMethod()
	public void afterMethod(ITestResult result) {

		if(!result.isSuccess()) {
			// Test Failed
			String error = result.getThrowable().getLocalizedMessage();
			logger.info(error);
			//    		return result.getThrowable().getLocalizedMessage().equals("Insurance mapping not found");
			//result.getThrowable().printStackTrace();

		
			
			try {
				TakesScreenshot ts = (TakesScreenshot) driver;
				File ss = ts.getScreenshotAs(OutputType.FILE);
				String ssPath = "./Screenshots/" + result.getName() + " - " + rowNum + ".png";
				FileUtils.copyFile(ss, new File(ssPath));
			} catch (Exception e) {
				System.out.println("Error taking screenshot");
			}

		}
		else {
			logger.info("Test completed successfully");
		}

		System.out.println("\n\n\n");
	}
	
		
		@DataProvider
		public static Object[][] getData(){


			if(excel == null){


				excel = new ExcelReader(System.getProperty("user.dir")+"\\"+excelFileName);


			}


			int rows = excel.getRowCount(sheetName);
			int cols = excel.getColumnCount(sheetName);

			Object[][] data = new Object[rows-1][1];

			Hashtable<String,String> table = null;

			for(int rowNum=2; rowNum<=rows; rowNum++){

				table = new Hashtable<String,String>();

				for(int colNum=0; colNum<cols; colNum++){

					//	data[rowNum-2][colNum]=	excel.getCellData(sheetName, colNum, rowNum);

					table.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName, colNum, rowNum));	
					data[rowNum-2][0]=table;	

				}
			}

			return data;

		}
		
}
