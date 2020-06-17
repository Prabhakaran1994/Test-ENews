package Enews_Testcases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Enews_API.Overall_API;
import Enews_WDMethods.ProjectMethods;


public class TC_03_FullEpisodes extends ProjectMethods{
	
	private String fullepisodes="";
	
	@BeforeTest
	public void setData()
	{

		testCaseName = "TC_03_FullEpisodes";
		testDescription = "To validate the response code of the front door";
		authors = "Prabhakaran";
		
	}
	
	@Test(priority=2)
	public void ArticleAPI() throws FileNotFoundException, IOException {
		
		Properties prop=new Properties();
		prop.load(new FileInputStream("/home/tringapps-admin/Documents/API/EnewsAPI/config.properties"));
		fullepisodes=prop.getProperty("FullEpisodes");
		System.out.println(fullepisodes);
		
		new Overall_API(test)
		.Article(fullepisodes);
		
		
		
	}
	
	
	

}
