package Enews_Testcases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Enews_API.FDArticlelink_API2;
import Enews_API.FDVideolink_API;
import Enews_WDMethods.ProjectMethods;

public class TC_21_FDVideolink_API extends ProjectMethods{
	
private String frontdoor="";
	
	@BeforeTest
	public void setData()
	{
		testCaseName = "TC_21_FDVideolink_API";
		testDescription = "To validate the response code of the front door";
		authors = "Prabhakaran";
	}
	
	@Test(priority = 20)
	public void frontdoorvideos() throws FileNotFoundException, IOException {
		
		Properties prop=new Properties();
		prop.load(new FileInputStream("/home/tringapps-admin/Documents/API/EnewsAPI/config.properties"));
		frontdoor=prop.getProperty("FrontDoor");
		System.out.println(frontdoor);
		
		
		
		new FDVideolink_API(test)
		.Videoapi(frontdoor)
		.videolink();
	
	}
}
