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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import objects.ExcelOperations;
import objects.SeleniumUtils;
import objects.Utility;
import utilities.ExcelReader;

public class RVU {
	Logger logger = LogManager.getLogger(RVU.class);

	String projDirPath, status, claimNo ,claimNumAvaility, DOB ,serviceDate ,firstName, lastName,memberID,ecwStatus,DOS, claimStatus,dateofbirth, npivalue, charges,currency, error, originalTab, checkNum;

	SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yy");

	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	List<WebElement> PatientList = new ArrayList<WebElement>();
	public static ExcelReader excel, excel1; 
	public static String sheetName = "Sheet1";
	
	int rowNum = 1;
	int renameRowNum = 1;
	boolean skipFlag =false, screen;
	Set matchSet = new HashSet();
	WebDriver driver;
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



		String url = "https://secure.maxrvu.com/";
		driver.get(url);
		logger.info("Open url: " + url);
		sel.pauseClick(driver.findElement(By.xpath("//div[@id='navbarCollapse']/descendant::a[@class='nav-link login-window' and text()='Sign In']")), 20);
	//	Thread.sleep(8000);
		driver.findElement(By.xpath("//div[@id='navbarCollapse']/descendant::a[@class='nav-link login-window' and text()='Sign In']")).click();
    	logger.info("Clicked on Sign in");
    	
    	sel.pauseClick(driver.findElement(By.id("user_session_email")),10);
    	driver.findElement(By.id("user_session_email")).sendKeys("Rbell@medcaremso.com");
    	logger.info("Email Entered");
    	driver.findElement(By.xpath("//input[@value='Next']")).click();
    	logger.info("Clicked on Next");
    	
    	
    	sel.pauseClick(driver.findElement(By.id("user_session_password")),10);
    	driver.findElement(By.id("user_session_password")).sendKeys("EL5Aw8Ge6Q!nbJ");
    	logger.info("Password Entered");
    	
    	
    	driver.findElement(By.xpath("//label[@class='password']/following-sibling::input[@value='Submit']")).click();
    	logger.info("Submit button clicked");
    	
    	Thread.sleep(3000);
    	sel.pauseClick(driver.findElement(By.xpath("//a[text()='patients']")),20);
    	driver.findElement(By.xpath("//a[text()='patients']")).click();
    	logger.info("Clicked on patients");

    	
		
	}
	@Test(dataProvider= "getData",priority=1)
	public  void pdfDownload(Hashtable<String,String> data) throws IOException, InterruptedException, ParseException {
		rowNum++;
		status= data.get("Status");
		

		if(status.isBlank()|| status.isEmpty()) {
			firstName= data.get("FirstName");
			lastName= data.get("LastName");
			DOB= data.get("DOB");
			
			SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yy");
		
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
			Thread.sleep(3000);
	    	sel.pauseClick(driver.findElement(By.xpath("//a[text()='patients']")),20);
	    	driver.findElement(By.xpath("//a[text()='patients']")).click();
	    	
			dateofbirth=	formatter.format(parser.parse(DOB));
			
    		Thread.sleep(3000);
    		sel.pauseClick(driver.findElement(By.id("search")), 10);
    		driver.findElement(By.id("search")).clear();
        	driver.findElement(By.id("search")).sendKeys(lastName+ ", "+ firstName);
        	logger.info("PAtient name entered");
        	Thread.sleep(7000);
        //	sel.pauseClick(driver.findElement(By.xpath("//li/a[text()='"+lastName+ ", "+ firstName+"']/small[contains(text(),'"+dateofbirth+"')]")), 20);
//         try {  PatientList.addAll(driver.findElements(By.xpath("//li/a[text()='"+lastName+ ", "+ firstName+"']/small[contains(text(),'"+dateofbirth+"')]")));
//       System.out.println("List populated");
//       System.out.println(PatientList.size());
//         }catch(Exception e) {
//        	 System.out.println("Exception occured");
//         }
//          
         driver.findElement(By.xpath("//li[1]/a[text()='"+lastName+ ", "+ firstName+"']")).click();
         
      //   for(WebElement i :PatientList ) {
            	
       //     	System.out.println( i.getText());
            	
       //     }
            
          
     //       if(PatientList.size()==1 ||PatientList.size()==2) {
            //	PatientList.get(0).click();
            //	logger.info("Clicked on Patient");
            	Thread.sleep(3000);
            	sel.pauseClick(driver.findElement(By.xpath("//img[contains(@src,'facesheet')]")), 20);
            	driver.findElement(By.xpath("//img[contains(@src,'facesheet')]")).click();
            	logger.info("Clicked on Facesheet");
            	Thread.sleep(5000);
           // 	sel.pauseClick(driver.findElement(By.xpath("//a[contains(@href,'facesheet') and @title = 'Download as PDF']")), 30);
            	
            	
            	try {
            		sel.waitFunc(driver.findElement(By.xpath("//img[contains(@src,'facesheet')]")));
    				}catch(Exception e) {
    					for(int i=0; i<9; i++) {
    						Thread.sleep(4000);
    					try {
    						driver.findElement(By.xpath("//a[contains(@href,'facesheet') and @title = 'Download as PDF']")).isDisplayed();
    						break;
    					}catch(Exception e1) {}	
    				}
    					
    				}
            	
            	
            	driver.findElement(By.xpath("//a[contains(@href,'facesheet') and @title = 'Download as PDF']")).click();
            	logger.info("Clicked on Download button");
            	
            	Thread.sleep(3000);
            	driver.findElement(By.xpath("//a[@id='cboxclose']")).click();
            	logger.info("Clicked on close button");
            	
            	Thread.sleep(3000);
            	sel.pauseClick(driver.findElement(By.xpath("//a[@class='back']")), 20);
            	driver.findElement(By.xpath("//a[@class='back']")).click();
            	logger.info("Clicked on back arrow");
            	excel.setCellData(sheetName, "Status", rowNum, "Pass");
            	File lastModifiedFile = getLastModified(System.getProperty("user.dir")+"\\DownloadedFiles");
            	if (lastModifiedFile != null) {
                    String newFileName = firstName+lastName+".pdf"; // Provide the new file name
                    File renamedFile = new File("C:\\Users\\jmartin\\eclipse-workspace\RVU\\DownloadedFiles", newFileName);

                    if (lastModifiedFile.renameTo(renamedFile)) {
                        System.out.println("File renamed successfully: " + renamedFile.getName());
                        excel.setCellData(sheetName, "Rename Status", renameRowNum, "Success");
                    } else {
                        System.out.println("Failed to rename file.");
                        excel.setCellData(sheetName, "Rename Status", renameRowNum, "Failure");
                    }
                } else {
                    System.out.println("No files found in the directory.");
                    excel.setCellData(sheetName, "Rename Status", renameRowNum, "No Files Found");
                }
            	
            	
            
           // }
           // else {
           // 	System.out.println("in else");
           // 	excel.setCellData(sheetName, "Status", rowNum, "Multiple Records");
          //  	driver.findElement(By.id("search")).clear();
          //  }
        	
   
    	
    	
    	
    	}
    	
	

}
	public static File getLastModified(String directoryFilePath)
	{
		File directory = new File(directoryFilePath);
		File[] files = directory.listFiles(File::isFile);
		long lastModifiedTime = Long.MIN_VALUE;
		File chosenFile = null;

		if (files != null)
		{
			for (File file : files)
			{
				if (file.lastModified() > lastModifiedTime)
				{
					chosenFile = file;
					lastModifiedTime = file.lastModified();
				}
			}
		}

		return chosenFile;
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
