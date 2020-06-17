package Enews_Testcases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Enews_API.FrontdoorAPI;
import Enews_Reporter.consoleoutput;
import Enews_WDMethods.ProjectMethods;

public class TC_18_FrontdoorVideos_API extends ProjectMethods{
	
private String frontdoor="";
	
	@BeforeTest
	public void setData()
	{

		testCaseName = "TC_18_FrontdoorVideos_API";
		testDescription = "To validate the response code of the front door";
		authors = "Prabhakaran";
		
	}
	
	@Test(priority = 17)
	public void frontdoorvideo() throws FileNotFoundException, IOException {
		
		Properties prop=new Properties();
		prop.load(new FileInputStream("/home/tringapps-admin/Documents/API/EnewsAPI/config.properties"));
		frontdoor=prop.getProperty("FrontDoor");
		System.out.println(frontdoor);
		
		
		
		new FrontdoorAPI(test)
		.articleApi(frontdoor)
		.Video_getvalues();
			
	}
	

}
