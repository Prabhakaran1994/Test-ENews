package Enews_WDMethods;

import java.io.IOException;
import java.net.MalformedURLException;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

public class ProjectMethods extends CommonMethods{

	public String browserName;
	public String testDescription;
	public String testbed;
	public String runCategory;
	public String runGroup;

	public boolean stageTesting;
	public String applicationUrl;

	@BeforeSuite(groups = { "Regression", "Sanity", "Smoke"})
	public void beforeSuite() throws IOException {
		startResult();
		
		
	}

	@BeforeTest(groups = { "Regression", "Sanity", "Smoke" })
	public void beforeTest() {
		// startResult();
	}

	@Parameters({ "browser", "platform", "url" })
	@BeforeMethod(groups = { "Regression", "Sanity", "Smoke" })
	public void beforeMethod() throws IOException {
		
		createreport(testCaseName);
		test = startTestModule(testCaseName + " ",
				testDescription);
		test.assignCategory(runCategory);
		test.assignAuthor(authors);
		//startApp(browser, platform, applicationUrl, testCaseName);
	}

	@AfterSuite()
	public void afterSuite()
	{
		endTestcase();
		
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) throws IOException, IOException {
		endResult();
		
		//endTestcase();
		//closeAllBrowsers();

	}

	@AfterTest()
	public void afterTest() {
		//endResult();
		// closeAllBrowsers();
	}
}