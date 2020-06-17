package Enews_WDMethods;



import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import Enews_Reporter.Reporter;

@Test
public class CommonMethods extends Reporter {

	public CloseableHttpResponse GETJSON(String API) throws ClientProtocolException, IOException {
		
		HttpGet request= new HttpGet(API);
		request.addHeader("version", "v2");   
	    CloseableHttpClient httpClient = HttpClients.createDefault();
	   
	    CloseableHttpResponse response=httpClient.execute(request);
	   
	   // System.out.println("prabu"+ " " +response.getStatusLine().toString());
		
		return response;
		
	}
	
	
	

}
