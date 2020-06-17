package Enews_Testcases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Enews_API.FrontdoorAPI;
import Enews_Reporter.consoleoutput;
import Enews_WDMethods.ProjectMethods;

public class TC_17_Frontdoorarticle extends ProjectMethods{
	
private String frontdoor="";
	
	@BeforeTest
	public void setData()
	{

		testCaseName = "TC_17_Frontdoorarticle";
		testDescription = "To validate the response code of the front door";
		authors = "Prabhakaran";
		
	}
	
	@Test(priority = 16)
	public void frontdoorapi() throws FileNotFoundException, IOException {
		
		/*new consoleoutput()
		.createreport(testCaseName);*/
		
		Properties prop=new Properties();
		prop.load(new FileInputStream("/home/tringapps-admin/Documents/API/EnewsAPI/config.properties"));
		frontdoor=prop.getProperty("FrontDoor");
		System.out.println(frontdoor);
		
		
		
		new FrontdoorAPI(test)
		.articleApi(frontdoor)
		.News_getarticlevalues();
		
		
		
		
					
	}
	
	

}
