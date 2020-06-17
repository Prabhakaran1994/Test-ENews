package Enews_Testcases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Enews_API.Overall_API;
import Enews_WDMethods.ProjectMethods;

public class TC_06_Fashionweek extends ProjectMethods{
	
	private String fashionweek="";
	
	@BeforeTest
	public void setData()
	{

		testCaseName = "TC_06_Fashionweek";
		testDescription = "To validate the response code of the front door";
		authors = "Prabhakaran";
		
	}
	
	@Test(priority=5)
	public void ArticleAPI() throws FileNotFoundException, IOException {
		
		Properties prop=new Properties();
		prop.load(new FileInputStream("/home/tringapps-admin/Documents/API/EnewsAPI/config.properties"));
		fashionweek=prop.getProperty("Fashionweek");
		System.out.println(fashionweek);
		
		new Overall_API(test)
		.Article(fashionweek);
		
		
		
	}
	
	
	

}
