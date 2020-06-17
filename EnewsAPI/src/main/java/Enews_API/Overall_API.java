package Enews_API;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import Enews_WDMethods.ProjectMethods;

@Test
public class Overall_API extends ProjectMethods{
	
	private String result="";

	public Overall_API(ExtentTest test) {
		
		this.test=test;
	}
	
	public Overall_API Article(String api) {
		
		try {
			System.out.println(api);
			CloseableHttpResponse response= GETJSON(api);
			HttpEntity ent=response.getEntity();
			if(ent!=null) 
			{
				result=EntityUtils.toString(ent);
				//  System.out.println(result);
			}
			JSONObject responseObj=new JSONObject(result);
			Boolean httpstat= responseObj.has("httpStatus");
			System.out.println(httpstat);
			
			if(httpstat == true)
			{
			int responsecode=(Integer) responseObj.get("httpStatus");
			String responsemessage=(String) responseObj.get("message");
			if(responsecode>=200 && responsecode<=299)
			{
				reportStep("The status code is:"+" "+ responsecode+" "+responsemessage, "PASS");
			}
			else
			{
				reportStep("The status code is:"+" "+ responsecode+" "+responsemessage, "FAIL");
			}
			}
			else {
				reportStep("The API response does not have the Status code"+" "+api, "FAIL");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return this;
	}
	
}
