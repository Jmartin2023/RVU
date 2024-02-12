package rpa;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
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
		renameRowNum++;
		status= data.get("Status");
		

		if(status.isBlank()|| status.isEmpty()) {
			firstName= data.get("FirstName");
			lastName= data.get("LastName");
			DOB= data.get("DOB");
			
			SimpleDateFormat parser = new SimpleDateFormat("M/d/yyyy");
		
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
			Thread.sleep(3000);
	    	sel.pauseClick(driver.findElement(By.xpath("//a[text()='patients']")),20);
	    	driver.findElement(By.xpath("//a[text()='patients']")).click();
	    	
			dateofbirth=	formatter.format(parser.parse(DOB));
			 System.out.println("Patient is "+firstName+" "+lastName+" "+dateofbirth);
    		Thread.sleep(3000);
    		sel.pauseClick(driver.findElement(By.id("search")), 10);
    		driver.findElement(By.id("search")).clear();
        	driver.findElement(By.id("search")).sendKeys(lastName+ ", "+ firstName);
        	logger.info("PAtient name entered");
        	Thread.sleep(7000);
    
        	try{   
        		//li/a[text()='Olivan, Johnnie']/small[contains(text(),'01/06/1939')]
         driver.findElement(By.xpath("//li/a[text()='"+lastName+ ", "+ firstName+"']/small[contains(text(),'"+dateofbirth+"')]")).click();
        
        	}catch(Exception e) {
        		logger.info("Patient not found");
        		
        		excel.setCellData(sheetName, "Status", rowNum, "Patient not found");
        		throw new SkipException("Patient not found");
        	}
        

            	Thread.sleep(3000);
            	
            	try {
            		
            		sel.pauseClick(driver.findElement(By.xpath("//img[contains(@src,'facesheet')]")), 20);
            		driver.findElement(By.xpath("//img[contains(@src,'facesheet')]")).click();
            	
            		logger.info("Facesheet Clicked");
            	}catch(Exception e) {
            		logger.info("Facesheet not found");
            		excel.setCellData(sheetName, "Rename Status", renameRowNum, "No Files Found");
            		excel.setCellData(sheetName, "Status", rowNum, "Facesheet not found");
            		throw new SkipException("Facesheet not found");
            	}
            	
            	Thread.sleep(5000);
           // 	sel.pauseClick(driver.findElement(By.xpath("//a[contains(@href,'facesheet') and @title = 'Download as PDF']")), 30);
            	
            	
            	try {
            		sel.waitFunc(driver.findElement(By.xpath("//img[contains(@src,'facesheet')]")));
    				}catch(Exception e) {
    					for(int i=0; i<20; i++) {
    						Thread.sleep(4000);
    					try {
    						driver.findElement(By.xpath("//a[contains(@href,'facesheet') and @title = 'Download as PDF']")).isDisplayed();
    						System.out.println("Download icon found");
    						break;
    					}catch(Exception e1) {
    						
    					}	
    				}
    					
    				}
            	
            	
            //	driver.findElement(By.xpath("//a[contains(@href,'facesheet') and @title = 'Download as PDF']")).click();
            //	System.out.println("URL is "+ URL);
            //	String targetFolderPath = System.getProperty("user.dir")+"\\DownloadedFiles";
           // 	 String targetFileName = firstName+lastName+".pdf";
            	
            	
            	

            	Thread.sleep(6000);
           try {
        	   driver.findElement(By.xpath("//*[contains(text(),'500 Internal Server Error')]")).isDisplayed();
        	   driver.navigate().back();
        	   System.out.println("500 Internal Server Error");
        	   excel.setCellData(sheetName, "Status", rowNum, "Fail");
        	   throw new SkipException("500 Internal Server Error");
           }catch(Exception e) {
        	   
           }
            	
            	
            	
            	
            	
            	
            	driver.findElement(By.xpath("//a[contains(@href,'facesheet') and @title = 'Download as PDF']")).click();
            	URL = driver.findElement(By.xpath("//a[contains(@href,'facesheet') and @title = 'Download as PDF']")).getAttribute("href");
            	System.out.println(URL);
            	String fileNum = URL.split("facesheet/")[1].split(".pdf")[0];
            	System.out.println(fileNum);
            	logger.info("Clicked on Download button");
            	excel.setCellData(sheetName, "File Name", rowNum, fileNum);
            	//	waitForTheFileToDownload(fileNum, 1000); // Wait for 1 second between checks
                 //   System.out.println("File found!");
                    
                   
                    
               
            	Thread.sleep(3000);
            	
            	
            	driver.findElement(By.xpath("//a[@id='cboxclose']")).click();
            	logger.info("Clicked on close button");
            	
            	Thread.sleep(3000);
            	sel.pauseClick(driver.findElement(By.xpath("//a[@class='back']")), 20);
            	driver.findElement(By.xpath("//a[@class='back']")).click();
            	logger.info("Clicked on back arrow");
            	excel.setCellData(sheetName, "Status", rowNum, "Pass");
            
            	
            	
            	
            
           // }
           // else {
           // 	System.out.println("in else");
           // 	excel.setCellData(sheetName, "Status", rowNum, "Multiple Records");
          //  	driver.findElement(By.id("search")).clear();
          //  }
        	
   
    	
		}
    	
    	}
    	
	


	
	private static void downloadFile(String fileUrl,String targetFolderPath,  String targetFileName) throws IOException {
        URL url = new URL(fileUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

        // Set up basic authentication if needed
        // Replace "username" and "password" with your actual credentials
        String username = "Rbell@medcaremso.com";
        String password = "EL5Aw8Ge6Q!nbJ";
        String userCredentials = username + ":" + password;
        String basicAuth = "Basic " + java.util.Base64.getEncoder().encodeToString(userCredentials.getBytes());
        httpConn.setRequestProperty("Authorization", basicAuth);

        try (InputStream in = httpConn.getInputStream()) {
            // Using java.nio.file for simplicity
            Path targetPath = Path.of(targetFolderPath,targetFileName);
            Files.createDirectories(targetPath.getParent()); // Ensure that the parent directories exist
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File saved to: " + targetPath.toAbsolutePath());
   
        } finally {
            httpConn.disconnect();
        }
    }

	

	  public static void waitForTheExcelFileToDownload(String fileName, int timeWait)
              throws IOException, InterruptedException {
          String downloadPath = System.getProperty("user.dir")+"\\DownloadedFiles";
          File dir = new File(downloadPath);
          File[] dirContents = dir.listFiles();

          for (int i = 0; i < 3; i++) {
              if (dirContents[i].getName().contains(fileName)) {
                  break;
              }else {
                  Thread.sleep(timeWait);
              }
          }
      }
	
	  
	  public static void waitForTheFileToDownload(String fileName, int timeWait)
	            throws IOException, InterruptedException {
	        String downloadPath = System.getProperty("user.dir") + "\\DownloadedFiles";
	        File dir = new File(downloadPath);

	        // Continue waiting until the file is found
	        while (true) {
	            File[] dirContents = dir.listFiles();

	            if (dirContents != null) {
	                for (File file : dirContents) {
	                    if (file.getName().contains(fileName)) {
	                        System.out.println("File found: " + file.getAbsolutePath());
	                       
	                        return; // File found, exit the method
	                        
	                    }
	                }
	            }

	            System.out.println("File not found. Waiting...");
	            Thread.sleep(timeWait);
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
