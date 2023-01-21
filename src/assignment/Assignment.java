package assignment;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Assignment {
	WebDriver driver;
	
	@BeforeTest
	public void setup() {
		WebDriverManager.chromedriver().setup();
		this.driver = new ChromeDriver();
	}
	
	@BeforeMethod
	public void urldetails() {
		driver.get("https://sakshingp.github.io/assignment/login.html");
	}
	
	@Test
	public void Titleverification() {
		String exp="Demo App";
		String actual=driver.getTitle();
		
		if (exp.contentEquals(actual)) {
			System.out.println("pass");
		} else {
			System.out.println("fail");
		}
	}

	@Test
	public void content() {
		WebElement head=driver.findElement(By.xpath("//*[@class='auth-header']"));
	    Assert.assertTrue(head.isDisplayed());	
	}	
	
	public void login() {
		WebElement Usernametext = driver.findElement(By.id("username"));
		WebElement Passwordtext = driver.findElement(By.id("password"));
		
		Usernametext.sendKeys("nagesh");
		Passwordtext.sendKeys("qwerty123");
		driver.findElement(By.id("log-in")).click();
	}
	
	public WebElement getTable() {
		return driver.findElement(By.id("transactionsTable"));
	}
	
	@Test
	public void loginpage() {
		WebElement Usernametext = driver.findElement(By.id("username"));
		WebElement Passwordtext = driver.findElement(By.id("password"));
		Assert.assertTrue(Usernametext.isDisplayed());
		Assert.assertTrue(Passwordtext.isDisplayed());
		
		boolean isElementDisabled=driver.findElement(By.xpath("//*[@class='form-check-input']")).isSelected();
		Assert.assertFalse(isElementDisabled,"the element should not be enabled");
		
		this.login();	
		
		// verify if table is present
		WebElement tableElement = this.getTable();
		Assert.assertTrue(tableElement.isDisplayed());
	}
	
	@Test
	public void homepage() {
		this.login();
		
		WebElement tableElement = this.getTable();
	
		List<WebElement> rows = tableElement.findElements(By.xpath("tbody/tr"));
	
		float[] amounts = new float[rows.size()]; 	
		
		for (int index = 0; index < rows.size(); index += 1) {
			WebElement row = rows.get(index);
			String text = row.findElement(By.xpath("td[5]/span")).getText();
			String formattedValue = text.replace("USD", "").replace(",", "").replace(" ", "");
			
			// convert to number
			float amount = Float.valueOf(formattedValue);
			
			// put into array
			amounts[index] = amount;
		}
		
		// sort the array in asc order
		Arrays.sort(amounts);
		
		// click on the table item
		driver.findElement(By.id("amount")).click();
		
		// read rows of table again
		rows = tableElement.findElements(By.xpath("tbody/tr"));
		
		for (int index = 0; index < rows.size(); index += 1) {
			WebElement row = rows.get(index);
			String text = row.findElement(By.xpath("td[5]/span")).getText();
			String formattedValue = text.replace("USD", "").replace(",", "").replace(" ", "");
			
			float newValue = Float.valueOf(formattedValue);
			
         //	System.out.println("comparing" + newValue + " and " + amounts[index]);
			
			// comparing new value with sorted array's value
			Assert.assertEquals(newValue, amounts[index]);
		}
		
		driver.close();
		
	}
	
}
