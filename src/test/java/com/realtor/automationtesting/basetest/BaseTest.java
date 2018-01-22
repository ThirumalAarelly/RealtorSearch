package com.realtor.automationtesting.basetest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class BaseTest {

	public static WebDriver driver;
	public ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest test;

	public FileInputStream fis = null;
	public Properties prop = null;

	@BeforeSuite
	public void init() throws Exception {

		fis = new FileInputStream(System.getProperty("user.dir")
				+ "/src/main/resources/com.realtor.automationtesting/repo/config.properties");
		prop = new Properties();
		prop.load(fis);

		if (extent == null) {
			htmlReporter = new ExtentHtmlReporter(
					System.getProperty("user.dir") + "/test-output/RealtorSearchReport.html");
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);

			extent.setSystemInfo("OS", "Windows");
			extent.setSystemInfo("Host Name", "Thirumal");
			extent.setSystemInfo("Environment", "QA");
			extent.setSystemInfo("User Name", "Thirumal A");

			htmlReporter.config().setChartVisibilityOnOpen(true);
			htmlReporter.config().setDocumentTitle("Realtor Search Report");
			htmlReporter.config().setReportName("Realtor Report");
			htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
			htmlReporter.config().setTheme(Theme.DARK);
		}

		if (driver == null) {

			if (prop.get("browser").equals("chrome")) {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")
						+ "/src/main/resources/com.realtor.automationtesting.drivers/chromedriver.exe");
				driver = new ChromeDriver();

			} else if (prop.get("browser").equals("firefox")) {
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")
						+ "/src/main/resources/com.realtor.automationtesting.drivers/geckodriver.exe");
				driver = new FirefoxDriver();

			}
			driver.manage().window().maximize();
			driver.get(prop.getProperty("siteUrl"));
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		}
	}

	public void logIntoReport(String logInfo) {
		test.info(MarkupHelper.createLabel(logInfo, ExtentColor.BLUE));
	}

	@AfterMethod
	public void getResult(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			String screenShotPath = capture(driver, "screenShotName");
			test.fail(MarkupHelper.createLabel(result.getName() + " Test case FAILED due to below issues:",
					ExtentColor.RED));
			test.fail(result.getThrowable());
			test.fail("Snapshot below: " + test.addScreenCaptureFromPath(screenShotPath));
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.pass(MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
		} else {
			test.skip(MarkupHelper.createLabel(result.getName() + " Test Case SKIPPED", ExtentColor.ORANGE));
			test.skip(result.getThrowable());
		}

	}

	public void verifyTitle(String title) {
		Assert.assertEquals(driver.getTitle(), title);
	}

	public static String capture(WebDriver driver, String screenShotName) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String dest = System.getProperty("user.dir") + "/ErrorScreenshots/" + screenShotName + getCurrentDateTime()
				+ ".png";
		File destination = new File(dest);
		FileUtils.copyFile(source, destination);

		return dest;
	}

	public static String getCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();

		return sdf.format(date);
	}

	@AfterSuite
	public void tearDown() {
		extent.flush();
		driver.quit();
	}
}
