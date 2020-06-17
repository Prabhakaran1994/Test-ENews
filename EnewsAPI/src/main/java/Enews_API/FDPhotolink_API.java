package Enews_API;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.aventstack.extentreports.ExtentTest;
import Enews_Constants.AppConstants;
import Enews_WDMethods.ProjectMethods;

public class FDPhotolink_API extends ProjectMethods {



	private String result="";
	private ArrayList<JSONObject> PhotosJSONArray = new ArrayList<JSONObject>();

	public FDPhotolink_API(ExtentTest test) {

		this.test=test;

	}

	public FDPhotolink_API photoApi(String api) {
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

						if(Articletype.equalsIgnoreCase("PHOTO_GALLERIES")) {
							JSONObject news1=  (JSONObject) articlelength.get(l);
							PhotosJSONArray.add(news1);
						}
					}
				}
				else{
					reportStep("The Widget name is:"+" " + widget, "INFO");
				}
			}
			System.out.println("Photo items is : " +PhotosJSONArray.size()); 
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return this;
	}


	public FDPhotolink_API photolink() throws ClientProtocolException, IOException {

		int photoCount=PhotosJSONArray.size();
		for(int i=0;i<photoCount;i++) {
			JSONObject photo=PhotosJSONArray.get(i);
			//reportStep(i+": "+"The Photo name is:"+" "+photo.get("socialTitle") , "INFO");
			System.out.println("*******************************************************************");
			reportStep( i + "  The front door gallery item is ", "INFO");
			System.out.println("*******************************************************************");
			String link=photo.getString("link");

			if(!link.isEmpty()){	
				if(!link.equalsIgnoreCase("null")){
					String photoJSONlink="https://eol-feeds.eonline.com"+link;
					System.out.println(photoJSONlink);
					reportStep("The Photolink is:"+link+" - ", "PASS");
					api(photoJSONlink);
				}
				else {
					reportStep("The Photolink is not present :"+link+" - ", "FAIL");
				}
			}
		}
		return this;
	}


	public FDPhotolink_API api(String api) {
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

					dataRef();
					gallery_getvalues();
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
			//E.printStackTrace();
		}
		return this;
	}


	public FDPhotolink_API gallery_getvalues() {

		try {
			commonkeys();
		
		}
		catch(Exception E) {

		}
		return this;
	}


	public FDPhotolink_API dataRef() throws URISyntaxException {
		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject gallery= Bodykeyvalue.getJSONObject("featuredContent");
		if(gallery.has("gallery")) {
			JSONObject galleryItem=gallery.getJSONObject("gallery");
			Iterator<String> key=galleryItem.keys();

			while(key.hasNext()) {
				String currentDynamickey=key.next();

				if(currentDynamickey.equalsIgnoreCase("galleryItemsDataRef")) {
					String currentGalleryLink=galleryItem.getString(currentDynamickey);
					if (currentGalleryLink.isEmpty()) {
						reportStep(String.format(AppConstants.IS_EMPTY, currentDynamickey), "FAIL");
					} else if (currentDynamickey.equalsIgnoreCase("null")) {
						reportStep(String.format(AppConstants.IS_NULL, currentDynamickey), "FAIL");
					} else {
						
						reportStep("The Keyname is:" + currentDynamickey + " - " + currentGalleryLink, "PASS");

						String URL=currentGalleryLink;
						String[] Split=URL.split("[?]page=[{]page[}]&num=[{]num[}]");
						for (String a : Split) 
							currentGalleryLink=a;

						URIBuilder builder = new URIBuilder(currentGalleryLink);
						builder.setParameter("page", "1").setParameter("num", "20");

						String photoJSONlink="https://eol-feeds.eonline.com"+builder.build();
						reportStep("The DataRef Photolink is:"+photoJSONlink+" - ", "PASS");
						api(photoJSONlink);

					}
				}
			}
		}


		return this;
	}

	public FDPhotolink_API commonkeys() {

		
		JSONArray itemskeyvalue=new JSONObject(result).getJSONArray("items");
		int itemsCount=itemskeyvalue.length();
		System.out.println("GALLERY CHECK");
		System.out.println("***************");
		for(int i=0;i<itemsCount;i++) {
			System.out.println("********************************");
			System.out.println("Gallery Item No : "+ i);
			System.out.println("********************************");
			JSONObject Keyvalue_Gallery=(JSONObject) itemskeyvalue.get(i);
			Iterator<String> key=Keyvalue_Gallery.keys();

			ArrayList<String> Actualkeyname=new ArrayList<String>();
			Actualkeyname.add("contentItemType");
			Actualkeyname.add("title");
			Actualkeyname.add("socialTitle");
			Actualkeyname.add("pageType");


			while(key.hasNext()) {
				String Currentdynamickey=key.next();
				for(int j=0;j<Actualkeyname.size();j++) {
					if(Currentdynamickey.equalsIgnoreCase(Actualkeyname.get(j))) {
						if(!Keyvalue_Gallery.getString(Currentdynamickey).isEmpty()) {
							if(!Keyvalue_Gallery.getString(Currentdynamickey).equalsIgnoreCase("null")) {
								reportStep("The Gallery item keyname is : "+ i +"-"+Currentdynamickey+" - "+Keyvalue_Gallery.getString(Currentdynamickey), "PASS");
								//stop=true;
								
							}
							else {
								reportStep("The Gallery item Keyname is: "+Currentdynamickey+" - "+" having NULL value ", "FAIL");
							}
						}
						else {
							reportStep("The Gallery item Keyname is: "+Currentdynamickey+" - "+" having EMPTY value ", "FAIL");
						}
					}
				}
			}
			System.out.println("GALLERY IMAGE ITEM CHECK");
			System.out.println("************************");
			galleryImage();	
			
		}

		return this;
		
	}



	public FDPhotolink_API galleryImage() {
		boolean stop=false;
		JSONArray itemskeyvalue=new JSONObject(result).getJSONArray("items");
		int itemsCount=itemskeyvalue.length();
		for(int i=0;i<itemsCount;i++) {
			JSONObject Keyvalue_Gallery=(JSONObject) itemskeyvalue.get(i);
			if(Keyvalue_Gallery.has("image")) {
				JSONObject imageItem = Keyvalue_Gallery.getJSONObject("image");
				Iterator<String> key=imageItem.keys();
				ArrayList<String> actualkeyname=new ArrayList<String>();

				actualkeyname.add("credits");
				actualkeyname.add("title");
				actualkeyname.add("src");
				actualkeyname.add("resizeUrl");
				

		

				while(key.hasNext()) {
					String Currentdynamickey1=(String) key.next();
					for(int j=0;j<actualkeyname.size();j++) {
						if(Currentdynamickey1.equalsIgnoreCase(actualkeyname.get(j))) {
							if(!imageItem.getString(Currentdynamickey1).isEmpty()) {
								if(!imageItem.getString(Currentdynamickey1).equalsIgnoreCase("null")) {
									reportStep("The Gallery Image item Keyname is:"+Currentdynamickey1+" - "+imageItem.getString(Currentdynamickey1), "PASS");
									stop=true;
								}
								else {
									reportStep("The Gallery Image item Keyname is:"+Currentdynamickey1+" - "+"having NULL value", "FAIL");
								}
							}
							else {
								reportStep("The Galley Image item Keyname is:"+Currentdynamickey1+" - "+"having EMPTY value", "FAIL");
							}
						}
					}
				}
				if(stop) {
					break;
				}
			}
		}

		return this;
	}


}


