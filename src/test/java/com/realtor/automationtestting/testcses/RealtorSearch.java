package com.realtor.automationtestting.testcses;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.realtor.automationtesting.basetest.BaseTest;

public class RealtorSearch extends BaseTest {

	@Test(priority = 1)
	public void ValidatingRealtorSearchFunctionalitiesTest() throws Exception {
		test = extent.createTest("ValidatingRealtorSearchFunctionalities");
		driver.findElement(By.xpath(".//*[@id='searchBox']")).sendKeys("Morgantown WV");
		driver.findElement(By.xpath(".//*[@id='homepage-header']/div[2]/div/div/div[2]/span/button[2]")).click();

		try {
			String Homesceen = driver.findElement(By.xpath(".//*[@id='srp-header']/div/div[1]/div/h1")).getText();
			System.out.println(Homesceen);
			Assert.assertEquals(Homesceen, "Morgantown, WV Real Estate & Homes for Sale");
		} catch (Exception e) {
			System.out.println("Exception throws" + e.getMessage());
		}
	}

	@Test(priority = 2)
	public void ValidatingRealtorsearchnumberFunctionalitiesTest() throws InterruptedException, IOException {
		test = extent.createTest("ValidatingRealtorsearchnumberFunctionalities");
		try {
			String value = driver.findElement(By.xpath(".//*[@id='srp-sort-count-wrap']/span")).getText();
			System.out.println(value);
			Assert.assertEquals(value, "780 Homes");
		} catch (Exception e1) {
			System.out.println("Exception throws" + e1.getMessage());
		} finally {
			JavascriptExecutor js20 = (JavascriptExecutor) driver;
			js20.executeScript("window.scrollBy(0,500)");
		}
	}

	@Test(priority = 3)
	public void ValidatingRealtorsecondaddresssearchresultFunctionalitiesTest()
			throws InterruptedException, IOException {
		test = extent.createTest("ValidatingRealtorsecondaddresssearchresultFunctionalities");
		try {
			String houseprice = driver.findElement(By.xpath(".//*[@id='2']/div[2]/div[2]/span")).getText();
			System.out.println(houseprice);
			driver.findElement(By.xpath(".//*[@id='2']/div[2]/div[3]")).click();
			Thread.sleep(2000);
			String houseviewdetailsprice = driver.findElement(By.xpath(".//*[@id='ldp-pricewrap']/div/div/span"))
					.getText();
			Assert.assertEquals(houseprice, houseviewdetailsprice);
		} catch (Exception e1) {
			System.out.println("Exception throws" + e1.getMessage());
		}
	}
}
