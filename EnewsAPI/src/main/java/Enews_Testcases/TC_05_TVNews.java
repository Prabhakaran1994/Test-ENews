package Enews_Testcases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Enews_API.Overall_API;
import Enews_WDMethods.ProjectMethods;


public class TC_05_TVNews extends ProjectMethods{
	
	private String tvnews="";
	
	@BeforeTest
	public void setData()
	{

		testCaseName = "TC_05_TVNews";
		testDescription = "To validate the response code of the front door";
		authors = "Prabhakaran";
		
	}
	
	@Test(priority=4)
	public void ArticleAPI() throws FileNotFoundException, IOException {
		
		Properties prop=new Properties();
		prop.load(new FileInputStream("/home/tringapps-admin/Documents/API/EnewsAPI/config.properties"));
		tvnews=prop.getProperty("TVNews");
		System.out.println(tvnews);
		
		new Overall_API(test)
		.Article(tvnews);
		
		
		
	}
	
	
	

}
