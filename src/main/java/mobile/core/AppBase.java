package mobile.core;





import java.awt.AWTException;

import org.openqa.selenium.Dimension;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.cucumber.listener.Reporter;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import mobile.core.AppController.Mobile_OSName;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;

import java.time.Duration;

import static io.appium.java_client.touch.offset.PointOption.point;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AppBase {
	
	
	AppParamManager pm = new AppParamManager();
	//public static AppiumDriver<MobileElement> driver = AppController.getAppiumDriver();

	public void sleep(long milliSeconds) {
		try {
			Thread.sleep(milliSeconds * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
	}

	public static MobileElement findElement(String objDetails) {
		MobileElement ele = null;
		Object driver;
		if (objDetails.substring(0, 2).toLowerCase().equals("id")) {
			ele = driver.findElementById(objDetails.substring(3));
		} else if (objDetails.substring(0, 5).toLowerCase().equals("xpath")) {
			ele = driver.findElement(By.xpath(objDetails.substring(6)));

		}

		return ele;
	}

	public static List<MobileElement> findElements(String objDetails) {
		List<MobileElement> ele = null;
		if (objDetails.substring(0, 2).toLowerCase().equals("id")) {
			ele = driver.findElements(By.id(objDetails.substring(3)));
		} else if (objDetails.substring(0, 5).toLowerCase().equals("xpath")) {
			ele = driver.findElements(By.xpath(objDetails.substring(6)));

		}
		return ele;
	}

	public static String getCurrentTime() {
		Date date = new Date();
		String strDateFormat = "hh:mm a";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		String formattedDate = dateFormat.format(date);
		return formattedDate;
	}

	public void addScreenshot() {
		String fileSuffix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		addScreenshot(fileSuffix);
	}

	public void addScreenshot(String screenshotName) {
		screenshotName = screenshotName.replaceAll(" ", "_");
		try {
			sleep(2);
			driver = AppController.getAppiumDriver();
			// System.out.println(driver.getSessionDetails().toString());
			// This takes a screenshot from the driver at save it to the specified location
			File sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			// Building up the destination path for the screenshot to save
			// Also make sure to create a folder 'screenshots' with in the cucumber-report
			// folder
			File destinationPath = new File(
					System.getProperty("user.dir") + "/target/cucumber-reports/screenshots/" + screenshotName + ".png");

			// Copy taken screenshot from source location to destination location
			FileUtils.copyFile(sourcePath, destinationPath);

			// This attach the specified screenshot to the test
			Reporter.addScreenCaptureFromPath(destinationPath.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void elementscreenshot(MobileElement ele) {
		try {
			sleep(2);
			driver = AppController.getAppiumDriver();
			File srcFile = ele.getScreenshotAs(OutputType.FILE);
			String fileSuffix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			File targetFile = new File(
					System.getProperty("user.dir") + "/target/cucumber-reports/screenshots/" + fileSuffix + ".png");
			FileUtils.copyFile(srcFile, targetFile);
			Reporter.addScreenCaptureFromPath(targetFile.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// log(targetFile.getAbsolutePath());
	}

	public void compareValues(String fieldName, String appValue, String featureValue) {
		Reporter.addStepLog("Comparison Field Name: " + fieldName);
		appValue = appValue.trim().replaceAll("\\\n", " ");
		featureValue = featureValue.trim();
		String failed_log = AppController.getFailedLog();
		if (appValue.equals(featureValue)) {
			Reporter.addStepLog("Comparison Result: PASS");
		} else {
			failed_log = failed_log + "\n" + "Comparison Field Name: " + fieldName;
			failed_log = failed_log + "\n" + "Expected Value: " + featureValue;
			failed_log = failed_log + "\n" + "Actual Value: " + appValue;
			Reporter.addStepLog("Comparison Result: FAIL");
		}
		Reporter.addStepLog("Expected Value: " + featureValue);
		Reporter.addStepLog("Actual Value: " + appValue);
		Reporter.addStepLog("------------------------------------------------------------------------- ");
		AppController.setFailedLog(failed_log);
	}

	public void clik(MobileElement param) throws InterruptedException, AWTException {
		((JavascriptExecutor) driver).executeScript(
				"var clickEvent = document.createEvent('MouseEvents');clickEvent.initEvent ('click', true, true);arguments[0].dispatchEvent (clickEvent);",
				param);

	}

	public static void setDateField(MobileElement ele, String value) throws InterruptedException, AWTException {
		value = value.trim();
		if (value.equalsIgnoreCase(""))
			return;
		ele.click();
		ele.sendKeys(value);
		Thread.sleep(2000);
		ele.sendKeys(Keys.ESCAPE);
	}

	public String jsonData(JSONObject jsonObject, String str) {
		return (String) jsonObject.get(str);
	}

	public JSONObject loadObjectRepository(String p_OR_Path) {

		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			Object obj = parser.parse(new FileReader(p_OR_Path));

			jsonObject = (JSONObject) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject;

	}

	/**
	 * Performs swipe from the center of screen
	 *
	 * @param dir the direction of swipe
	 * @version java-client: 7.3.0
	 **/
	
	public static void swipeLeft() {
		Dimension size = driver.manage().window().getSize();
		int startX = (int) (size.width * 0.90);
		int endX = (int) (size.width * 0.10);
		int startY = (int) (size.height / 2);
		new TouchAction(driver).longPress(PointOption.point(startX, startY))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1))).moveTo(PointOption.point(endX, startY))
				.release().perform();
	}

	public static void swipeRight() {
		Dimension size = driver.manage().window().getSize();
		int startX = (int) (size.width * 0.10);
		int endX = (int) (size.width * 0.90);
		int startY = size.height / 2;
		new TouchAction(driver).longPress(PointOption.point(startX, startY))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1))).moveTo(PointOption.point(endX, startY))
				.release().perform();
	}

	public static void scrollUp() {
		int deviceWidth = driver.manage().window().getSize().getWidth();
		int deviceHeight = driver.manage().window().getSize().getHeight();
		int midX = (deviceWidth / 2);
		int midY = (deviceHeight * 2 / 3);
		int topEdge = (int) (deviceHeight * 0.15f);
		new TouchAction(driver).longPress(PointOption.point(midX, midY))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1))).moveTo(point(midX, topEdge)).release()
				.perform();
	}

	public static void scrollDown() {
		int deviceWidth = driver.manage().window().getSize().getWidth();
		int deviceHeight = driver.manage().window().getSize().getHeight();
		int midX = (deviceWidth / 2);
		int midY = (deviceHeight / 2);
		int bottomEdge = (int) (deviceHeight * 0.80f);
		new TouchAction(driver).longPress(PointOption.point(midX, midY))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1))).moveTo(point(midY, bottomEdge)).release()
				.perform();
	}

	public void scrollToElement(String text) {
		if (AppController.getMobileOSName() == Mobile_OSName.Android) {
			AndroidDriver<MobileElement> driver2 = (AndroidDriver<MobileElement>) AppController.getAppiumDriver();
			String uiSelector = "new UiSelector().textContains(\"" + text + "\")";
			String command = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView("
					+ uiSelector + ");";
			driver2.findElementByAndroidUIAutomator(command);
		}
		if (AppController.getMobileOSName() == Mobile_OSName.iOS) {

			MobileElement element = driver.findElementByName(text);

			JavascriptExecutor js = (JavascriptExecutor) driver;

			HashMap scrollObjects = new HashMap();
			scrollObjects.put("element", ((RemoteWebElement) element).getId());
			scrollObjects.put("direction", "down");
			driver.executeScript("mobile: scroll", scrollObjects);

		}
	}

	public void scroldown() {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	public void SendKeys(MobileElement ele, String S) {
		if (AppController.getMobileOSName() == Mobile_OSName.Android) {
			Actions action = new Actions(driver);
			action.sendKeys(ele, S).build().perform();
		}

		if (AppController.getMobileOSName() == Mobile_OSName.iOS) {
			ele.sendKeys(S);
		}
	}

	public void swipecardleft(MobileElement ele, MobileElement ele1, MobileElement ele2) {
		if (AppController.getMobileOSName() == Mobile_OSName.Android) {
			swipeElement(ele, Direction.LEFT);

		}

		if (AppController.getMobileOSName() == Mobile_OSName.iOS) {
			swipeElement(ele, Direction.LEFT);
			ele1.click();
			sleep(1);
			ele2.click();
			sleep(2);

		}
	}

	public void swipecardright(MobileElement ele, MobileElement ele1, MobileElement ele2) {
		if (AppController.getMobileOSName() == Mobile_OSName.Android) {

			swipeElement(ele, Direction.RIGHT);
		}

		if (AppController.getMobileOSName() == Mobile_OSName.iOS) {
			swipeElement(ele, Direction.RIGHT);
			ele1.click();
			sleep(1);
			ele2.click();
		}
	}

	public void closeKeyBoard(String xpath) {
		if (AppController.getMobileOSName() == Mobile_OSName.Android) {
			driver.navigate().back();
		}

		if (AppController.getMobileOSName() == Mobile_OSName.iOS) {
			MobileElement element = driver.findElement(By.xpath(xpath.substring(6)));
			element.click();
		}
	}

	public enum Direction {
		DOWN, UP, LEFT, RIGHT;
	}

	public void swipeElement(MobileElement el, Direction dir) {

		// Animation default time:
		// - Android: 300 ms
		// - iOS: 200 ms
		// final value depends on your app and could be greater
		final int ANIMATION_TIME = 1000; // ms

		final int PRESS_TIME = 1000; // ms

		// init screen variables
		Dimension dims = driver.manage().window().getSize();
		Rectangle rect = el.getRect();

		// check element overlaps screen
		if (rect.x >= dims.width || rect.x + rect.width <= 0 || rect.y >= dims.height || rect.y + rect.height <= 0) {
			throw new IllegalArgumentException("swipeElementIOS(): Element outside screen");
		}

		// init borders per your app screen
		// or make them configurable with function variables
		int leftBorder, rightBorder, upBorder, downBorder;
		leftBorder = 0;
		rightBorder = 0;
		upBorder = 0;
		downBorder = 0;

		// find rect that overlap screen
		if (rect.x < 0) {
			rect.width = rect.width + rect.x;
			rect.x = 0;
		}
		if (rect.y < 0) {
			rect.height = rect.height + rect.y;
			rect.y = 0;
		}
		if (rect.width > dims.width)
			rect.width = dims.width;
		if (rect.height > dims.height)
			rect.height = dims.height;

		PointOption pointOptionStart, pointOptionEnd;
		switch (dir) {
		case DOWN: // from up to down
			pointOptionStart = PointOption.point(rect.x + rect.width / 2, rect.y + upBorder);
			pointOptionEnd = PointOption.point(rect.x + rect.width / 2, rect.y + rect.height - downBorder);
			break;
		case UP: // from down to up
			pointOptionStart = PointOption.point(rect.x + rect.width / 2, rect.y + rect.height - downBorder);
			pointOptionEnd = PointOption.point(rect.x + rect.width / 2, rect.y + upBorder);
			break;
		case LEFT: // from right to left
			pointOptionStart = PointOption.point(rect.x + rect.width - rightBorder, (int) (rect.y + rect.height / 3));
			pointOptionEnd = PointOption.point(rect.x + leftBorder, (int) (rect.y + rect.height / 1.5));
			break;
		case RIGHT: // from left to right
			pointOptionStart = PointOption.point(rect.x + leftBorder, rect.y + rect.height / 2);
			pointOptionEnd = PointOption.point(rect.x + rect.width - rightBorder, rect.y + rect.height / 2);
			break;
		default:
			throw new IllegalArgumentException("swipeElementIOS(): dir: '" + dir + "' NOT supported");
		}

		// execute swipe using TouchAction
		try {
			new TouchAction(driver).press(pointOptionStart)
					// a bit more reliable when we add small wait
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(PRESS_TIME))).moveTo(pointOptionEnd).release()
					.perform();
		} catch (Exception e) {
			System.err.println("swipeElementIOS(): TouchAction FAILED\n" + e.getMessage());
			return;
		}

		// always allow swipe action to complete
		try {
			Thread.sleep(ANIMATION_TIME);
		} catch (InterruptedException e) {
			// ignore
		}
	}

	public static MobileElement find(String objDetails, String value) {
		MobileElement ele = null;
		if (objDetails.substring(0, 5).toLowerCase().equals("xpath")) {
			ele = driver.findElement(By.xpath(objDetails.replace("$paramName$", value.trim()).substring(6)));
		}
		return ele;
	}

	public static List<MobileElement> finds(String objDetails, String value) {
		List<MobileElement> ele = null;
		if (objDetails.substring(0, 5).toLowerCase().equals("xpath")) {
			ele = driver.findElements(By.xpath(objDetails.replace("$paramName$", value.trim()).substring(6)));
		}
		return ele;
	}
	
	public void scrollIntoViewClick(MobileElement ele) {

		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("arguments[0].scrollIntoView(true);", ele);
		ele.click();
		sleep(5);
	}

	public void scrollIntoView(MobileElement ele) {

		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("arguments[0].scrollIntoView(true);", ele);

	}

	public void moveCursorToAnElement(MobileElement ele) {
		if (AppController.getMobileOSName() == Mobile_OSName.Android) {
			Actions builder = new Actions(driver);
			builder.moveToElement(ele).build().perform();
		}

		if (AppController.getMobileOSName() == Mobile_OSName.iOS) {
			JavascriptExecutor js = (JavascriptExecutor) driver;

			HashMap scrollObjects = new HashMap();
			scrollObjects.put("element", ((RemoteWebElement) ele).getId());
			scrollObjects.put("direction", "down");
			driver.executeScript("mobile: scroll", scrollObjects);
		}

	}

	public void addreminder(String xpath, String param, String xpath1,MobileElement ele,  List<List<String>> arg1) {
		pm.runVerticalParamManager(arg1);
		String[] reminder1 = pm.getVParam(param).split(";");
		ele.click();
		MobileElement ele1 = driver
				.findElement(By.xpath(xpath.replace("$paramName$", reminder1[1].trim()).substring(6)));
		ele1.click();
		sleep(2);
		MobileElement elle2 = driver.findElement(By.xpath(xpath1.substring(6)));
		elle2.clear();
		elle2.sendKeys(reminder1[0]);
	}

	public void addnotifaction(String xpath, String param, List<List<String>> arg1) {
		pm.runVerticalParamManager(arg1);
		String[] Notification1 = pm.getVParam(param).split(";");
		for (int i = 0; i < Notification1.length; i++) {
			MobileElement ele = driver
					.findElement(By.xpath(xpath.replace("$paramName$", Notification1[i].trim()).substring(6)));
			ele.click();
		}
	}
	
	
	public void back() {
		for (int i = 0; i < 3; i++) {
			driver.navigate().back();
			sleep(2);
		}
	}

	public void swipeall(List<MobileElement> ele, MobileElement element) {
		List<MobileElement> list = ele;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isSelected()) {
				for (int j = list.size() - i; j > 0; j--) {
					swipeLeft();
				}
				for (int k = 0; k < list.size(); k++) {
					addScreenshot();
					swipeRight();
				}
				list.get(i).click();
			}
		}
	}

	public void textcompare(String xpath, String featureValue) {
		MobileElement element = driver
				.findElement(By.xpath(xpath.replace("$paramName$", featureValue.trim()).substring(6)));
		element.isDisplayed();
		String appValue = element.getText();
		if (appValue.contains(featureValue)) {
			compareValues(featureValue + "  present", appValue, featureValue);
		}

	}

	public void verifyText(String xpath, List<List<String>> data) {
		HashMap<String, String> key = new LinkedHashMap();
		List<List<String>> datavalue = data;
		for (List<String> param : datavalue) {
			String paramkey = param.get(0).toString().trim();
			String paramvalue = param.get(1).toString().trim();
			String[] strArray = null;
			strArray = paramvalue.split(";");
			for (int i = 0; i < strArray.length; i++) {
				String featureValue = strArray[i];
				if (strArray[i].equals("TRUE")) {
				} else {
					if (featureValue.contains("'") | strArray[i] != "") {
						String featurevalue = featureValue.replace("'", "’");
						textcompare(xpath, featurevalue);
					}
				}
			}
		}
	}

	public void getValues(List<List<String>> data) {
		HashMap<String, String> key = new LinkedHashMap();
		List<List<String>> datavalue = data;
		for (List<String> param : datavalue) {
			String paramkey = param.get(0).toString().trim();
			String paramvalue = param.get(1).toString().trim();
			for (String str : paramvalue.split(";")) {
				if (str.equals("TRUE")) {
				}
			}
		}
	}
	
	public void click(MobileElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).click().perform();
	}

	public void matchedvalue(String xpath, List<List<String>> data) {
		HashMap<String, String> key = new LinkedHashMap();
		for (List<String> param : data) {
			String paramValue = param.get(1).toString().trim();
			String[] strArray = null;
			strArray = paramValue.split(";");
			for (int i = 0; i < strArray.length; i++) {
				String value = strArray[i];
				if (strArray[i].equals("TRUE")) {
				} else {
					MobileElement element = driver
							.findElement(By.xpath(xpath.replace("$paramName$", value.trim()).substring(6)));
					if (element.isDisplayed()) {
					}
				}
			}
		}
	}

	public void matchedvalueClick(String xpath, List<List<String>> data) {
		HashMap<String, String> key = new LinkedHashMap();
			for (List<String> param : data) {
				String paramValue = param.get(1).toString().trim();
				String[] strArray = null;
				strArray = paramValue.split(";");
				for (int i = 0; i < strArray.length; i++) {
					String value = strArray[i];
					if (strArray[i].equals("TRUE")) {
					} else {
						MobileElement element = driver.findElement(By.xpath(xpath.replace("$paramName$", value.trim()).substring(6)));
						if (element.isDisplayed()) {
							element.click();
						}
					}
				}
			}
	}

	public void verifyIconText(List<MobileElement> element, List<List<String>> data) {
		getValues(data);
		List<MobileElement> l = element;
		if (l.size() > 0) {
			MobileElement eachelement = l.get(0);
			assertTrue(eachelement.isDisplayed(), "Image displayed");
			String elementText = eachelement.getText();
			Reporter.addStepLog("Button Text: " + elementText);
			Reporter.addStepLog("------------------------------------------------------------------------- ");
		}
	}

	public boolean checkButtonVisible(List<MobileElement> element, List<List<String>> data) {
		getValues(data);
		List<MobileElement> buttons = element;
		if (buttons.size() > 0 && buttons.get(0).isDisplayed()) {
			return true;
		}
		return false;
	}

	public void Wait() {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	public void waitForElement(AppiumDriver<?> appiumDriver, String xpath) {
		Wait<AppiumDriver> wait = new FluentWait<AppiumDriver>(appiumDriver).withTimeout(Duration.ofSeconds(40))
				.pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class)
				.ignoring(TimeoutException.class);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath.substring(6))));
	}

	public void waitForElementText(AppiumDriver<?> appiumDriver, String xpath,String featureValue) {
		Wait<AppiumDriver> wait = new FluentWait<AppiumDriver>(appiumDriver).withTimeout(Duration.ofSeconds(40))
				.pollingEvery(Duration.ofSeconds(7)).ignoring(NoSuchElementException.class)
				.ignoring(TimeoutException.class);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath.replace("$paramName$", featureValue.trim()).substring(6))));
	}
	
	
	public void setChips(String xpath, String value) throws Throwable, AWTException {
		Wait();
		if (value.trim().equalsIgnoreCase(""))
			return;
		value = value.trim();
		xpath = xpath.trim();
		for (String str : value.split(",")) {
			sleep(3);
			MobileElement ele = driver.findElement(By.xpath(xpath.replace("$replaceToken$", str.trim()).substring(6)));
			int i = 0;
			while (i <= 5) {
				if (!(ele.isDisplayed() && ele.isEnabled())) {
					scrollIntoView(ele);
					sleep(3);
					i++;
					continue;
				} else {
					moveCursorToAnElement(ele);
					break;
				}
			}
			addScreenshot();
			ele.click();
		}
	}

	public void setChips(String xpath, String value, String xpath_input) {
		sleep(2);
		value = value.trim();
		if (value.equalsIgnoreCase(""))
			return;
		for (String str : value.split(",")) {
			if (str.contains(";")) {
				String str1[] = str.split(";");
				driver.findElement(By.xpath(xpath.replace("$replaceToken$", str1[0]).substring(6))).click();
				sleep(2);
				driver.findElements(By.xpath(xpath_input.replace("$replaceToken$", str1[0]).substring(6))).get(0)
						.click();
				driver.findElements(By.xpath(xpath_input.replace("$replaceToken$", str1[0]).substring(6))).get(0)
						.clear();
				driver.findElements(By.xpath(xpath_input.replace("$replaceToken$", str1[0]).substring(6))).get(0)
						.sendKeys(str1[1]);

				if (!str1[2].trim().equals("[Blank]")) {
					sleep(2);
					driver.findElements(By.xpath(xpath_input.replace("$replaceToken$", str1[0]).substring(6))).get(1)
							.click();
					driver.findElements(By.xpath(xpath_input.replace("$replaceToken$", str1[0]).substring(6))).get(1)
							.clear();
					driver.findElements(By.xpath(xpath_input.replace("$replaceToken$", str1[0]).substring(6))).get(1)
							.sendKeys(str1[2]);
				}
			} else {
				driver.findElement(By.xpath(xpath.replace("$replaceToken$", str).substring(6))).click();
			}
			sleep(2);
		}
	}

	public void setChipsText(String xpath, String value) {
		if (value.trim().equalsIgnoreCase(""))
			return;
		value = value.trim();
		xpath = xpath.trim();
		for (String str : value.split(";")) {
			MobileElement ele = driver.findElement(By.xpath(xpath.replace("$replaceToken$", str.trim()).substring(6)));
			if (!(ele.isDisplayed() && ele.isEnabled())) {
				scrollIntoView(ele);
				sleep(3);
			} else {
				moveCursorToAnElement(ele);
			}
			addScreenshot();
			ele.click();
			ele.clear();
			ele.sendKeys(value);
			sleep(2);
		}
	}

	public void setChipsResponse(String xpath, String value, List<List<String>> data) {
		pm.runVerticalParamManager(data);

		sleep(2);
		MobileElement ele = driver
				.findElement(By.xpath(xpath.replace("$replaceToken$", pm.getVParam(value).trim()).substring(6)));
		scrollUp();
		moveCursorToAnElement(ele);
		addScreenshot();
		compareValues(value + "  present", ele.getText(), pm.getVParam(value));
	}


	public void setChipsResponses(String xpath, List<List<String>> data) {
		HashMap<String, String> key = new LinkedHashMap();
		List<List<String>> datavalue = data;
		for (List<String> param : datavalue) {
			String paramkey = param.get(0).toString().trim();
			for (String str : paramkey.split(";")) {
				sleep(2);
				MobileElement ele = driver.findElement(By.xpath(xpath.replace("$paramName$", str.trim()).substring(6)));
				moveCursorToAnElement(ele);

				if (ele.isDisplayed()) {
					addScreenshot();
					compareValues(str + "  present", ele.getText(), str);
					sleep(2);
				}
			}
		}
	}

	
	public void setChipsResponses(String xpath, String value) throws Throwable, AWTException {
		Wait();
		if (value.trim().equalsIgnoreCase(""))
			return;
		value = value.trim();
		xpath = xpath.trim();
		for (String str : value.split(";")) {
			sleep(1);
			if (AppController.getMobileOSName() == Mobile_OSName.Android) {
				scrollUntilElementFoundsetChips(xpath, str);
			}
			MobileElement ele = driver.findElement(By.xpath(xpath.replace("$replaceToken$", str.trim()).substring(6)));
			moveCursorToAnElement(ele);
			 if (AppController.getMobileOSName() == Mobile_OSName.iOS) {
				scrollUntilElementUpiOS(ele);
			}

			if (ele.isDisplayed()) {
				addScreenshot();
				compareValues(str + "  present", ele.getText(), str);
			}
		}
	}
	
	public void scrollUntilElementFound(String xpath, String featureValue) {
		while(true) {
			try {
				MobileElement element=(MobileElement)driver.findElement(By.xpath(xpath.replace("$paramName$", featureValue.trim()).substring(6)));
				if(element.isDisplayed()) {
					break;
					
				}
			}catch(Exception e) {
				Dimension size = driver.manage().window().getSize();
				int starty = (int) (size.height * 0.8);
				int endy = (int) (size.height * 0.2);
				int startx = size.width / 2;
				new TouchAction(driver)
				.longPress(
						LongPressOptions.longPressOptions()
						.withDuration(Duration.ofSeconds(1))
						.withPosition(PointOption.point(startx, starty)))
				.moveTo(PointOption.point(startx,endy))
				.release()
				.perform();
			}
		}
	}
	
	public void scrollUntilElementFoundsetChips(String xpath, String featureValue) {
		while(true) {
			try {
				MobileElement ele = driver.findElement(By.xpath(xpath.replace("$replaceToken$", featureValue.trim()).substring(6)));
				if(ele.isDisplayed()) {
					break;
					
				}
			}catch(Exception e) {
				Dimension size = driver.manage().window().getSize();
				int starty = (int) (size.height * 0.8);
				int endy = (int) (size.height * 0.2);
				int startx = size.width / 2;
				new TouchAction(driver)
				.longPress(
						LongPressOptions.longPressOptions()
						.withDuration(Duration.ofSeconds(1))
						.withPosition(PointOption.point(startx, starty)))
				.moveTo(PointOption.point(startx,endy))
				.release()
				.perform();
			}
		}
	}
	
	public void scrollUntilElementDowniOS(MobileElement element) {
	if (AppController.getMobileOSName() == Mobile_OSName.iOS) {
		while (true) {
			if (element.isDisplayed()) {
				break;
			} else {
				scrollDown();
				Wait();
				}
			}
		}
	}
	
	public void scrollUntilElementUpiOS(MobileElement element) {
		if (AppController.getMobileOSName() == Mobile_OSName.iOS) {
			while (true) {
				if (element.isDisplayed()) {
					break;
				} else {
					scrollUp();
					Wait();
					}
				}
			}
		}
	
}



