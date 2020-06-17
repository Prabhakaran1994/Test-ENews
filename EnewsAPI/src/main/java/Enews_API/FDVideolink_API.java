package Enews_API;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.aventstack.extentreports.ExtentTest;

import Enews_WDMethods.ProjectMethods;

public class FDVideolink_API extends ProjectMethods {



	private String result="";
	private ArrayList<JSONObject> VideosJSONArray = new ArrayList<JSONObject>();
	
	public FDVideolink_API(ExtentTest test) {

		this.test=test;

	}

	public FDVideolink_API Videoapi(String api) {
		try {
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

			JSONArray segarr=new JSONObject(result).getJSONObject("body").getJSONArray("widgets");
			int i=segarr.length();
			System.out.println(i);

			for(int j=0;j<segarr.length();j++)
			{
				JSONObject widgetobj=(JSONObject) segarr.get(j);
				String widget= (String) widgetobj.get("widgetType");
				System.out.println(widget);
				if(widget.equalsIgnoreCase("CATEGORY_GRID_MOBILE"))
				{
					reportStep("Category Grid Mobile widget is present", "PASS");
					JSONArray articlelength=(JSONArray) widgetobj.getJSONObject("contentData").getJSONArray("categoryGridItems");
					int k=articlelength.length();
					//System.out.println(k);


					for(int l=0;l<k;l++) {
						JSONObject articleobj= (JSONObject) articlelength.get(l);
						String Articletype=  (String) articleobj.get("contentItemType");

						if(Articletype.equalsIgnoreCase("VIDEOS")) {
							JSONObject news1=  (JSONObject) articlelength.get(l);
							VideosJSONArray.add(news1);
						}
					}
				}
				else{
					reportStep("The Widget name is:"+" " + widget, "INFO");
				}
			}
			System.out.println("NEWS items is : " +VideosJSONArray.size()); 
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	
	public FDVideolink_API videolink() throws ClientProtocolException, IOException {

		int Videocount=VideosJSONArray.size();
		for(int i=0;i<Videocount;i++) {
			JSONObject video=VideosJSONArray.get(i);
			reportStep(i+": "+"The Video name is:"+" "+video.get("socialTitle") , "INFO");
			System.out.println("*******************************************************************");
			String link=video.getString("link");

			if(!link.isEmpty()){	
				if(!link.equalsIgnoreCase("null")){
					String VideoJSONlink="https://eol-feeds.eonline.com"+link;
					System.out.println(VideoJSONlink);
					reportStep("The Videolink is:"+link+" - ", "PASS");
					api(VideoJSONlink);
				}
				else {
					reportStep("The Videolink is not present :"+link+" - ", "FAIL");
				}
			}
		}
		return this;
	}

	
	public FDVideolink_API api(String api) {
		try {
				CloseableHttpResponse response= GETJSON(api);
				HttpEntity ent=response.getEntity();
				if(ent!=null) 
				{
					result=EntityUtils.toString(ent);
					// System.out.println(result);
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
						Video_getvalues();
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
		catch(Exception E) {
			E.printStackTrace();
		}
		return this;
	}

	
	public FDVideolink_API Video_getvalues() {
		
		try {
			commonkeys();
			thumbnail();
		}
		catch(Exception E) {
			
		}
		return this;
	}
	
	
	public FDVideolink_API commonkeys() {
		
		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject video= Bodykeyvalue.getJSONObject("featuredContent");
		
		
		if(video.has("video")) {
			JSONObject videoItem = video.getJSONObject("video");
			Iterator<String> key=videoItem.keys();
			ArrayList<String> Actualkeyname=new ArrayList<String>();
			
			Actualkeyname.add("id");
			Actualkeyname.add("uri");
			Actualkeyname.add("title");
			Actualkeyname.add("socialTitle");
			Actualkeyname.add("omnitureTitle");
			Actualkeyname.add("pageType");
			Actualkeyname.add("relatedVideosDataRef");
			Actualkeyname.add("videoSrcUrl");
			Actualkeyname.add("analyticsSection");
			
			
			while(key.hasNext()) {
				String Currentdynamickey=key.next();
				for(int i=0;i<Actualkeyname.size();i++) {
					if(Currentdynamickey.equalsIgnoreCase(Actualkeyname.get(i))) {
						if(!videoItem.getString(Currentdynamickey).isEmpty()) {
							if(!videoItem.getString(Currentdynamickey).equalsIgnoreCase("null")) {
								reportStep("The keyname is : "+Currentdynamickey+" - "+videoItem.getString(Currentdynamickey), "PASS");
							}
							else {
								reportStep("The Keyname is: "+Currentdynamickey+" - "+" having NULL value ", "FAIL");
							}
						}
						else {
							reportStep("The Keyname is: "+Currentdynamickey+" - "+" having EMPTY value ", "FAIL");
						}
					}
				}
			}
		}
		else
		{
			reportStep("The object Video is not found", "WARN");
		}
		return this;
	}
	
	
	
	public FDVideolink_API thumbnail() {
		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject video= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject videoItem = video.getJSONObject("video");
		
		ArrayList<String> videoobject=new ArrayList<String>();
		//videoobject.add("thumbnail");
	//	videoobject.add("smallThumbnail");
	//	videoobject.add("mediumThumbnail");
		videoobject.add("largeThumbnail");
	//	videoobject.add("xsmallThumbnail");
		
		for(int i=0;i<videoobject.size();i++) {
			
		if(videoItem.has(videoobject.get(i))) {
			JSONObject keyvalue=videoItem.getJSONObject(videoobject.get(i));
			Iterator<String> key1=keyvalue.keys();
			
			ArrayList<String> Thumbnailkeyname=new ArrayList<String>();
			Thumbnailkeyname.add("src");
			Thumbnailkeyname.add("resizeUrl");
			
			while(key1.hasNext()) {
				String Currentdynamickey1=(String) key1.next();
				for(int j=0;j<Thumbnailkeyname.size();j++) {
					if(Currentdynamickey1.equalsIgnoreCase(Thumbnailkeyname.get(j))) {
						if(!keyvalue.getString(Currentdynamickey1).isEmpty()) {
							if(!keyvalue.getString(Currentdynamickey1).equalsIgnoreCase("null")) {
								reportStep("The "+videoobject.get(i) + " Keyname is:"+Currentdynamickey1+" - "+keyvalue.getString(Currentdynamickey1), "PASS");
								
							}
							else {
								reportStep("The " + videoobject.get(i) + " Keyname is:"+Currentdynamickey1+" - "+"having NULL value", "FAIL");
							}
						}
						else {
							reportStep("The " + videoobject.get(i) + " Keyname is:"+Currentdynamickey1+" - "+"having EMPTY value", "FAIL");
						}
					}
				}
			}
		}
		else
		{
			reportStep("The object " + videoobject.get(i)+ " is not found for this article", "WARN");
		}
		}
		
		return this;
	}
	
	
}


