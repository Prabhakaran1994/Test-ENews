package Enews_API;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sikuli.script.App;
import org.testng.annotations.Test;

import Enews_Constants.AppConstants;
import Enews_Constants.FrontDoorConstants;

import com.aventstack.extentreports.ExtentTest;

import Enews_WDMethods.ProjectMethods;

@Test
public class FrontdoorAPI extends ProjectMethods {

	private String result = "";
	private ArrayList<JSONObject> NewsJSONArray = new ArrayList<JSONObject>();
	private ArrayList<JSONObject> VideoJSONArray = new ArrayList<JSONObject>();
	private ArrayList<JSONObject> PhotosJSONArray = new ArrayList<JSONObject>();
	ArrayList<String> Articlekeys = new ArrayList<String>();
	String currentDynamicKey = "";
	String currentDynamicKey1 = "";

	public FrontdoorAPI(ExtentTest test) {

		this.test = test;

	}

	public FrontdoorAPI articleApi(String api) {
		try {
			CloseableHttpResponse response = GETJSON(api);
			HttpEntity ent = response.getEntity();

			if (ent != null) {
				result = EntityUtils.toString(ent);
				// System.out.println(result);
			}
			JSONObject responseObj = new JSONObject(result);
			Boolean httpstat = responseObj.has("httpStatus");
			System.out.println(httpstat);

			if (httpstat == true) {
				int responsecode = (Integer) responseObj.get("httpStatus");
				String responsemessage = (String) responseObj.get(AppConstants.MESSAGE);
				if (responsecode >= 200 && responsecode <= 299) {
					reportStep("The status code is:" + " " + responsecode + " " + responsemessage, "PASS");
				} else {
					reportStep("The status code is:" + " " + responsecode + " " + responsemessage, "FAIL");
				}
			} else {
				reportStep("The API response does not have the Status code" + " " + api, "FAIL");
			}

			JSONArray segmentArray = new JSONObject(result).getJSONObject("body").getJSONArray("widgets");
			int i = segmentArray.length();
			System.out.println(i);

			for (int j = 0; j < segmentArray.length(); j++) {
				JSONObject widgetobj = (JSONObject) segmentArray.get(j);
				String widget = (String) widgetobj.get("widgetType");
				System.out.println(widget);
				if (widget.equalsIgnoreCase(FrontDoorConstants.CATEGORY_GRID_MOBILE)) {
					reportStep("Category Grid Mobile widget is present", "PASS");
					JSONArray articlelength = (JSONArray) widgetobj.getJSONObject("contentData")
							.getJSONArray("categoryGridItems");
					int k = articlelength.length();
					// System.out.println(k);

					for (int l = 0; l < k; l++) {
						JSONObject articleobj = (JSONObject) articlelength.get(l);
						String articletype = (String) articleobj.get("contentItemType");

						switch (articletype) {
						case AppConstants.NEWS:
							JSONObject news1 = (JSONObject) articlelength.get(l);
							NewsJSONArray.add(news1);
							break;

						case AppConstants.VIDEOS:
							JSONObject news2 = (JSONObject) articlelength.get(l);
							VideoJSONArray.add(news2);
							break;

						case AppConstants.PHOTO_GALLERIES:
							JSONObject news3 = (JSONObject) articlelength.get(l);
							PhotosJSONArray.add(news3);
							break;

						default:
							break;
						}
					}
				} else {
					reportStep("The Widget name is:" + " " + widget, "INFO");
				}
			}

			System.out.println("NEWS items is : " + NewsJSONArray.size());
			System.out.println("Video items is : " + VideoJSONArray.size());
			System.out.println("Photos items is : " + PhotosJSONArray.size());

		} catch (Exception e) {
			e.printStackTrace();

		}

		return this;
	}

	public FrontdoorAPI News_getarticlevalues()

	{
		int articlecount = NewsJSONArray.size();
		for (int i = 0; i < articlecount; i++) {
			JSONObject article = NewsJSONArray.get(i);
			String articleName=(String) article.get("socialTitle");
			reportStep(i + ": " + String.format(AppConstants.ARTICLE_NAME, articleName), "INFO");
			System.out.println("*******************************************************************");
			Iterator<String> key = article.keys();
			ArrayList<String> actualKeyName = new ArrayList<String>();

			actualKeyName.add("pageType");
			actualKeyName.add("link");
			actualKeyName.add("omnitureTitle");
			actualKeyName.add("uri");
			actualKeyName.add("contentItemType");
			actualKeyName.add("socialTitle");

			while (key.hasNext()) {
				String currentDynamicKey = (String) key.next();
				for (int h = 0; h < actualKeyName.size(); h++) {
					if (currentDynamicKey.equalsIgnoreCase(actualKeyName.get(h))) {
						String currentArticle = article.getString(currentDynamicKey);
						if (currentArticle.isEmpty()) {
							reportStep(String.format(AppConstants.IS_EMPTY, currentDynamicKey), "FAIL");
						} else if (currentArticle.equalsIgnoreCase("null")) {
							reportStep(String.format(AppConstants.IS_NULL, currentDynamicKey), "FAIL");
						} else {
							reportStep("The Keyname is:" + currentDynamicKey + " - " + currentArticle, "PASS");
						}
					} else {
						if (currentDynamicKey.equalsIgnoreCase("thumbnail")) {
							boolean stop = false;
							JSONObject articleThumbnail = article.getJSONObject("thumbnail");
							Iterator<String> keys1 = articleThumbnail.keys();
							while (keys1.hasNext()) {
								String currentDynamicKey1 = (String) keys1.next();
								if (currentDynamicKey1.equalsIgnoreCase("src")
										|| currentDynamicKey1.equalsIgnoreCase("resizeUrl")) {
									String currentThumbnail = articleThumbnail.getString(currentDynamicKey1);
									if (currentThumbnail.isEmpty()) {
										reportStep(String.format(AppConstants.IS_EMPTY, currentDynamicKey1), "FAIL");
									} else if (currentThumbnail.equalsIgnoreCase("null")) {
										reportStep(String.format(AppConstants.IS_NULL, currentDynamicKey1), "FAIL");
									} else {
										reportStep("The Keyname is:" + currentDynamicKey1 + " - " + currentThumbnail, "PASS");
										stop = true;
									}
								}
							}							
							if (stop) {
								break;
							}
						}
					}
				}
			}
		}
		return this;

	}

	public FrontdoorAPI News_thumbnail()

	{
		return this;

	}

	public FrontdoorAPI Video_getvalues() {

		int videocount = VideoJSONArray.size();
		for (int i = 0; i < videocount; i++) {
			JSONObject video = VideoJSONArray.get(i);
			String videoName=(String) video.get("socialTitle");
			System.out.println("VIDEO *******************************************************************");
			reportStep(i + ": " + String.format(AppConstants.ARTICLE_NAME, videoName), "INFO");
			Iterator<String> key = video.keys();

			ArrayList<String> Videokeyname = new ArrayList<String>();
			Videokeyname.add("pageType");
			Videokeyname.add("link");
			Videokeyname.add("omnitureTitle");
			Videokeyname.add("uri");
			Videokeyname.add("contentItemType");
			Videokeyname.add("socialTitle");


			while (key.hasNext()) {
				String videodynamickey = (String) key.next();
				for (int j = 0; j < Videokeyname.size(); j++) {
					if (videodynamickey.equalsIgnoreCase(Videokeyname.get(j))) {
						String currentvideo=video.getString(videodynamickey);
						if (currentvideo.isEmpty()) {
							reportStep(String.format(AppConstants.IS_EMPTY, videodynamickey), "FAIL");
						} else if (currentvideo.equalsIgnoreCase("null")) {
							reportStep(String.format(AppConstants.IS_NULL, videodynamickey), "FAIL");
						} else {
							reportStep("The Keyname is:" + videodynamickey + " - " + currentvideo, "PASS");
						}
					} else {
						if (videodynamickey.equalsIgnoreCase("thumbnail")) {

							boolean stop1 = false;
							JSONObject video_thumbnail = video.getJSONObject("thumbnail");
							Iterator<String> key1 = video_thumbnail.keys();

							while (key1.hasNext()) {
								String videodynamickey1 = (String) key1.next();
								if (videodynamickey1.equalsIgnoreCase("src")
										|| videodynamickey1.equalsIgnoreCase("resizeUrl")) {
									String currentVdThumbnail=video_thumbnail.getString(videodynamickey1);
									if (currentVdThumbnail.isEmpty()) {
										reportStep(String.format(AppConstants.IS_EMPTY, currentDynamicKey1), "FAIL");
									} else if (currentVdThumbnail.equalsIgnoreCase("null")) {
										reportStep(String.format(AppConstants.IS_NULL, currentDynamicKey1), "FAIL");
									} else {
										reportStep("The Keyname is:" + videodynamickey1 + " - " + currentVdThumbnail, "PASS");
										stop1 = true;
									}
								}
							}
							if (stop1) {
								break;
							}
						}
					}
				}
			}
		}

		return this;
	}

	public FrontdoorAPI Photos_getvalues() {

		int Photocount = PhotosJSONArray.size();
		for (int i = 0; i < Photocount; i++) {

			JSONObject photos = PhotosJSONArray.get(i);
			String photoName=(String) photos.get("publishTitle");
			System.out.println("PHOTOS *******************************************************************");
			reportStep(i + ": " + String.format(AppConstants.ARTICLE_NAME, photoName), "INFO");
			Iterator<String> key = photos.keys();

			ArrayList<String> Photoskeyname = new ArrayList<String>();
			Photoskeyname.add("pageTitle");
			Photoskeyname.add("link");
			Photoskeyname.add("omnitureTitle");
			Photoskeyname.add("uri");
			Photoskeyname.add("contentItemType");
			Photoskeyname.add("socialTitle");
			Photoskeyname.add("src");

			while (key.hasNext()) {
				String photosdynamickey = (String) key.next();
				for (int j = 0; j < Photoskeyname.size(); j++) {
					if (photosdynamickey.equalsIgnoreCase(Photoskeyname.get(j))) {
						String currentphoto=photos.getString(photosdynamickey);
						if(currentphoto.isEmpty()) {
							reportStep(String.format(AppConstants.IS_EMPTY, currentphoto), "FAIL");
						}else if(currentphoto.equalsIgnoreCase("null")){
							reportStep(String.format(AppConstants.IS_NULL, currentphoto), "FAIL");
						}
						else {
							reportStep("The Keyname is:" + photosdynamickey + " - " + currentphoto, "PASS");
						}
					} else {
						if (photosdynamickey.equalsIgnoreCase("thumbnail")) {

							boolean stop2 = false;
							JSONObject Photos_thumbnail = photos.getJSONObject("thumbnail");
							Iterator<String> Key1 = Photos_thumbnail.keys();

							while (Key1.hasNext()) {
								String photosdynamickey1 = (String) Key1.next();
								if (photosdynamickey1.equalsIgnoreCase("src")
										|| photosdynamickey1.equalsIgnoreCase("resizeUrl")) {
									String currentphotothumbnail=Photos_thumbnail.getString(photosdynamickey1);
									if(currentphotothumbnail.isEmpty()) {
										reportStep(String.format(AppConstants.IS_EMPTY,currentphotothumbnail), "FAIL");
									}else if(currentphotothumbnail.equalsIgnoreCase("null")) {
										reportStep(String.format(AppConstants.IS_NULL, currentphotothumbnail), "FAIL");
									}else {
										reportStep("The Keyname is:" + photosdynamickey1 + " - " + currentphotothumbnail, "PASS");
										stop2=true;
									}
								}
							}
							if (stop2) {
								break;
							}

						}

					}

				}
			}
		}

		return this;
	}

}
