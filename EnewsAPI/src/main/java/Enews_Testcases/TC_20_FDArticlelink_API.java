package Enews_Testcases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Enews_API.FDArticlelink_API3;
import Enews_API.FDArticlelink_API4;
import Enews_WDMethods.ProjectMethods;

public class TC_20_FDArticlelink_API extends ProjectMethods{
	
private String frontdoor="";
	
	@BeforeTest
	public void setData()
	{
		testCaseName = "TC_20_FDArticlelink_API";
		testDescription = "To validate the response code of the front door";
		authors = "Prabhakaran";
	}
	
	@Test(priority = 21)
	public void frontdoorphotos() throws FileNotFoundException, IOException, URISyntaxException {
		
		Properties prop=new Properties();
		prop.load(new FileInputStream("/home/tringapps-admin/Documents/API/EnewsAPI/config.properties"));
		frontdoor=prop.getProperty("FrontDoor");
		System.out.println(frontdoor);
		
		
		
		new FDArticlelink_API4(test)
		.Articleapi(frontdoor)
		.articlelink();
	
	}
}
