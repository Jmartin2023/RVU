package objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SeleniumUtils {

    private WebDriver driver;
    private WebDriverWait wait5, wait10, wait20;
    private String downloadPath = System.getProperty("user.dir");
    private WebDriverWait wait;
    public void waitFunc(WebElement webEle) {
		wait.until(ExpectedConditions.elementToBeClickable(webEle));
	}
    public SeleniumUtils() {
        this.driver = initiateDriver(this.downloadPath);
        intiateWait();
    }

    public SeleniumUtils(String downloadPath) {
        this.downloadPath = downloadPath;
        this.driver = initiateDriver(downloadPath);
        intiateWait();
    }
    
    public SeleniumUtils(WebDriver driver) {
        this.driver = driver;
        intiateWait();
    }
    
    public WebDriver getDriver() {
    	return this.driver;
    }

    public WebDriverWait getWait10() {
        return wait10;
    }

    public WebDriverWait getWait20() {
        return wait20;
    }

    public WebDriver initiateDriver(String path) {

        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", path);
        chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("plugins.always_open_pdf_externally", true);
      //  chromePrefs.put("download.default_directory", "C:\\Users\\jmartin\\eclipse-workspace\\AR Insurance Potal ASR\\DownloadedFiles");
        chromePrefs.put("download.default_directory",  System.getProperty("user.dir")+"\\DownloadedFiles");
        
       
        ChromeOptions options = new ChromeOptions();

        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        options.setCapability(ChromeOptions.CAPABILITY, options);

        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");

        options.setExperimentalOption("prefs", chromePrefs);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
       
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(options);
      

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", "Object.defineProperty(navigator, 'webdriver', { get: () => false})");

        ((ChromiumDriver) driver).executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", params);

        driver.manage().window().maximize();

        System.out.println("Chrome driver started successfully");
        
        return driver;

    }
    public void pauseVisibility(WebElement element, int sec) {
    	try {
    		WebDriverWait wait = extracted(sec);
    		wait.until(ExpectedConditions.visibilityOf(element));
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    }

    public void pauseInvisibility(WebElement element, int sec) {
    	try {
	        WebDriverWait wait = extracted(sec);
	        wait.until(ExpectedConditions.invisibilityOf(element));
    	} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }

    public void pauseClick(WebElement element, int sec) {
    	try {
			WebDriverWait wait = extracted(sec);
			wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }

    public void pauseWindowsCount(WebElement element, int sec, int numOfWindows) {
    	try {
	    	WebDriverWait wait = extracted(sec);
	        wait.until(ExpectedConditions.numberOfWindowsToBe(numOfWindows));
    	} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    private void intiateWait() {
        this.wait5 = new WebDriverWait(this.driver, Duration.ofSeconds(5));
        this.wait10 = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        this.wait20 = new WebDriverWait(this.driver, Duration.ofSeconds(20));
    }
    
    private WebDriverWait extracted(int sec) {
		WebDriverWait wait;
		if(sec==5) wait = this.wait5;
        else if(sec==10) wait = this.wait10;
        else if(sec==20) wait = this.wait10;
        else wait = new WebDriverWait(this.driver, Duration.ofSeconds(sec));
		return wait;
	}

}
