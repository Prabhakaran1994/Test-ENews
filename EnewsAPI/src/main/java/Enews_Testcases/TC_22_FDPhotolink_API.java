package Enews_Testcases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Enews_API.FDPhotolink_API;
import Enews_WDMethods.ProjectMethods;

public class TC_22_FDPhotolink_API extends ProjectMethods{
	
private String frontdoor="";
	
	@BeforeTest
	public void setData()
	{
		testCaseName = "TC_22_FDPhotolink_API";
		testDescription = "To validate the response code of the front door";
		authors = "Prabhakaran";
	}
	
	@Test(priority = 21)
	public void frontdoorphotos() throws FileNotFoundException, IOException {
		
		Properties prop=new Properties();
		prop.load(new FileInputStream("/home/tringapps-admin/Documents/API/EnewsAPI/config.properties"));
		frontdoor=prop.getProperty("FrontDoor");
		System.out.println(frontdoor);
		
		new FDPhotolink_API(test)
		.photoApi(frontdoor)
		.photolink();
	
	}
}
