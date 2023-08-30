package mobile.core;





import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.remote.DesiredCapabilities;



import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class AppController {
  
     //driver = new AppiumDriver<MobileElement>();
	private static AppiumDriver<MobileElement> driver;
	//private static AppiumDriver<WebElement> driver;
	private static String mt_Env = "STAGE"; 
	public static AppiumDriver<MobileElement> t_mobileDriver;
	//  public static  AndroidDriver<MobileElement> m_mobileDriver;
	
	private static String AppiumServer = "http://localhost:4723/wd/hub";
	private static AppiumDriverLocalService service;
	private static AppiumServiceBuilder builder;
	private static final long t_lMaxWait = 30;
	//private static String m_MobileDeviceConfig = "Pixel-Nougat";
	private static String t_MobileDeviceConfig = "Pixel 6 pro";
	private static String t_currentUser = "AnushaPhone";
	private static String t_failed_log = "";
	private static String PINStatus = "Enabled";
	private static String app_name;
	public static String appBundleId;
	
	
	//private static  String app_name;
	
	public enum Mobile_OSName {
		Android, iOS
	}
	 
	public static  Mobile_OSName t_MobileOSName = getMobileOSName();
	
	
	 
	public static File f = new File("src/test/resources");
	public static File fs = new File(f,"app-staging.apk");
	
	public static AppiumDriver<MobileElement> setupAppiumDriver() throws MalformedURLException  {
		long lMaxWait = getMaxWaitTime();
		int j;
		DesiredCapabilities caps = new DesiredCapabilities();
		System.out.println(getMobileOSName().toString());
		
		int a=0;
		if ( AppController.getMobileOSName() == Mobile_OSName.Android)
		{
			a=0;
		}
		else if (AppController.getMobileOSName() == Mobile_OSName.iOS)
		{
			a=3;
		}

		switch (a) {
		case 0:
			caps.setCapability("avd", "Pixel_4_XL_API_30");
			caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel_4_XL_API_30");
			caps.setCapability(MobileCapabilityType.APP,"Your .apk file path");
			caps.setCapability("avd", "Pixel_4_XL_API_30");
			caps.setCapability("udid", "emulator-5554");
			caps.setCapability("platformName", getMobileOSName().toString());
			caps.setCapability("platformVersion", "11.0");
			caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
			caps.setCapability("autoGrantPermissions", "true");
			caps.setCapability("skipUnlock","true");
			caps.setCapability("noReset","false");
			caps.setCapability("appWaitForLaunch", "false");
			caps.setCapability("noSign","true");
			caps.setCapability("ignoreHiddenApiPolicyError", "true");
			caps.setCapability("newCommandTimeout", 300);
			
			//driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

			
			break;

		case 1:
			caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel_4_XL_API_31");
			caps.setCapability(MobileCapabilityType.APP, fs.getAbsolutePath());
			caps.setCapability(MobileCapabilityType.APP,"Your .apk file path");
			caps.setCapability("udid", "emulator-5554");
			caps.setCapability("platformName", getMobileOSName().toString());
			caps.setCapability("platformVersion", "8.0.0");
			caps.setCapability("newCommandTimeout", 300);

			
			break;

		case 2:
			caps.setCapability("testName", "AWS Demo - Samsung Galaxy");
			break;
		case 3:
			caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITEST");
			
			caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
			caps.setCapability("platformName",getMobileOSName().toString());
			caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "15.5");
			caps.setCapability("deviceName","iPhone 13");
			caps.setCapability(MobileCapabilityType.APP , app_name);
			caps.setCapability("autoGrantPermissions", "true");
			caps.setCapability("ignoreHiddenApiPolicyError", "true");
			
			caps.setCapability("skipUnlock","false");
			caps.setCapability("noReset","false");
			caps.setCapability("appWaitForLaunch", "true");
			caps.setCapability("noSign","true");
			caps.setCapability("newCommandTimeout", 300);
			
		
break;
		default:
			caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel_4_XL_API_29");
			caps.setCapability(MobileCapabilityType.APP, fs.getAbsolutePath());
			caps.setCapability("udid", "emulator-5556");
			caps.setCapability("platformName", getMobileOSName().toString());
			caps.setCapability("platformVersion", "7.1.1");
			caps.setCapability("newCommandTimeout", 300);

			break;
		}
		
		switch (getMobileOSName()) {
		case Android:
			caps.setCapability("appPackage", "your desired package name");
			caps.setCapability("appActivity", "your desired activity name");
			caps.setCapability("noReset", "false");
			break;
		case iOS:
			if (appBundleId.equalsIgnoreCase("Based on your app")) {
				caps.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "your desired bundleid");
			} else {
				caps.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "your desired bundleid");
			}
			break;
		}

		if (getMobileOSName() == Mobile_OSName.Android) {
			driver = new AndroidDriver<MobileElement>(new URL(AppiumServer), caps);
		} else if (getMobileOSName() == Mobile_OSName.iOS) {
			driver = new IOSDriver<MobileElement>(new URL(AppiumServer), caps);

		}

		return driver;

	}
	
	public static long getMaxWaitTime() {
		return t_lMaxWait;
	}
	
	public static String getPINStatus() {
		return PINStatus;
	}
	
	public static void setPINStatus(String status) {
		PINStatus = status;
		return;
	}
	
	public static void setFailedLog(String failed_log) {
		t_failed_log = failed_log;
		return;
	}
	public static String getMobileDeviceConfig() {
		return t_MobileDeviceConfig;
	}

	public static void setMobileDeviceConfig(String t_MobileDeviceConfig) {
		AppController.t_MobileDeviceConfig = t_MobileDeviceConfig;
	}
	

	
	public static String getEnvironment() {
		return mt_Env;
	}

	public static void setEnvironment(String p_Env) {
		mt_Env = p_Env;
		return;
	}
	
	public static Mobile_OSName getMobileOSName() {
		return t_MobileOSName;
	}
	
	public static String getFailedLog() {
		return t_failed_log;
	}
	
	public static void  setMobileOSName(Mobile_OSName t_MobileOSName) {
		AppController.t_MobileOSName = t_MobileOSName;
	}
	
	
	public static void setAppName (String appName){
		
		app_name = appName;
	}
	
	public static String getAppName(){
		return app_name;
	}
	
	public static void startAppiumServer() {
		builder = new AppiumServiceBuilder();
		builder.withIPAddress("127.0.0.1");
		builder.usingPort(4723);
		//builder.withCapabilities(cap);
		builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
		builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");
		service = AppiumDriverLocalService.buildService(builder);
		
		service.start();
	}
	
	public static void stopAppiumServer() {
		
		service.stop();
	}

	public static AppiumDriver<MobileElement> getAppiumDriver() {
		// TODO Auto-generated method stub
		return driver;
	}

}

