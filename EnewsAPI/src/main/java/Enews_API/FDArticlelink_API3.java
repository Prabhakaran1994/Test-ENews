package Enews_API;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
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

import Enews_WDMethods.ProjectMethods;

public class FDArticlelink_API3 extends ProjectMethods {



	private String result="";
	private ArrayList<JSONObject> NewsJSONArray = new ArrayList<JSONObject>();

	public FDArticlelink_API3(ExtentTest test) {
		this.test=test;
	}

	public FDArticlelink_API3 Articleapi(String api) {
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
						//System.out.println(l + "--> "+ Articletype);

						if(Articletype.equalsIgnoreCase("NEWS")) {
							//		    				JSONArray news1 =(JSONArray) articlelength.get(l);
							//		    				System.out.println(news1.length());
							JSONObject news1=  (JSONObject) articlelength.get(l);
							NewsJSONArray.add(news1);
						}
					}
				}
				else{
					reportStep("The Widget name is:"+" " + widget, "INFO");
				}
			}
			System.out.println("NEWS items is : " +NewsJSONArray.size()); 
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public FDArticlelink_API3 articlelink() throws ClientProtocolException, IOException, URISyntaxException {

		int Articlecount=NewsJSONArray.size();
		for(int i=0;i<Articlecount;i++) {
			JSONObject article=NewsJSONArray.get(i);
			reportStep(i+": "+"The article name is:"+" "+article.get("socialTitle") , "INFO");
			System.out.println("*******************************************************************");
			String link=article.getString("link");

			if(!link.isEmpty()){	
				if(!link.equalsIgnoreCase("null")){

					String ArticleJSONlink=link;
					//	ArticleJSONlink=Headerlink;
					URIBuilder Header=new URIBuilder(ArticleJSONlink);
					Header.addParameter("version", "v2");

					String apilink="https://eol-feeds.eonline.com"+Header.build();

					System.out.println(apilink);
					//System.out.println(ArticleJSONlink);
					reportStep("The Articlelink is:"+link+" - ", "PASS");
					//Articlelist.add(ArticleJSONlink);
					api(apilink);
				}
				else {
					reportStep("The Articlelink is not present :"+link+" - ", "FAIL");
				}
			}

		}
		return this;

	}

	public FDArticlelink_API3 api(String api) {
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
					Article_getvalues();
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

	public FDArticlelink_API3 Article_getvalues() {

		try {

			commonkeys();
			heroimage();
			author();
			Thumbnail();
			Potraitthumbnail();
			Segments();
			Categories();
		}
		catch(Exception E) {
			//E.printStackTrace();
		}
		return this;
	}


	public FDArticlelink_API3 commonkeys() {
		try {

			JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
			JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");


			if(article.has("article"))
			{
				JSONObject articleItem = article.getJSONObject("article");
				Iterator<String> key=articleItem.keys();
				ArrayList<String> Actualkeyname=new ArrayList<String>();

				Actualkeyname.add("shortTitle");
				Actualkeyname.add("summary");
				Actualkeyname.add("relatedContentRef");
				Actualkeyname.add("thumbnailSource");
				Actualkeyname.add("pageType");
				Actualkeyname.add("socialTitle");
				Actualkeyname.add("omnitureTitle");
				Actualkeyname.add("uri");
				Actualkeyname.add("title");
				Actualkeyname.add("displayPublishedDate");
				System.out.println("ARTICLE GENERAL KEYS");
				System.out.println("********************");
				while(key.hasNext()) {
					String Currentdynamickey=key.next();
					for(int i=0;i<Actualkeyname.size();i++) {

						if(Currentdynamickey.equalsIgnoreCase(Actualkeyname.get(i))) {

							if(!Currentdynamickey.contains("displayPublishedDate"))
							{
								if(!articleItem.getString(Currentdynamickey).isEmpty()) {
									if(!articleItem.getString(Currentdynamickey).equalsIgnoreCase("null")) {
										reportStep("The Keyname is:"+Currentdynamickey+" - "+articleItem.getString(Currentdynamickey), "PASS");
									}
									else {
										reportStep("The Keyname is:"+Currentdynamickey+" - "+" having NULL value ", "FAIL");
									}
								}
								else {
									reportStep("The Keyname is:"+Currentdynamickey+" - "+"having EMPTY value ", "FAIL");
								}
							}
							else {
								if(Currentdynamickey.equalsIgnoreCase("displayPublishedDate"))
								{
									long date= articleItem.getLong(Currentdynamickey);
									if(date!=0)
									{								
										SimpleDateFormat publisheddate = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
										String dateText = publisheddate.format(date);

										reportStep("The keyname is:"+Currentdynamickey+ " "+dateText, "PASS");
									}
								}
							}
						}
					}

				}	
			}
			else
			{
				reportStep("The object article is not found for this article", "WARN");
			}
			System.out.println("********************");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return this;
	}


	public FDArticlelink_API3 heroimage() {


		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");

		if(articleItem.has("heroImage"))
		{
			JSONObject Heroimage= articleItem.getJSONObject("heroImage");
			Iterator<String> key=articleItem.keys();
			String heroimagekey="heroImage";
			System.out.println("HERO IMAGE CHECK");
			System.out.println("******************");
			while(key.hasNext()) {
				String Currentdynamickey=key.next();
				if(Currentdynamickey.equalsIgnoreCase(heroimagekey))
				{
					if(!Heroimage.getString("src").isEmpty()) {
						if(!Heroimage.getString("src").equalsIgnoreCase("null")) {
							reportStep("The Heroimage Keyname is: src"+ ""+" - "+Heroimage.getString("src"), "PASS");
						}
						else {
							reportStep("The Heroimage Keyname is: src is having NULL value", "FAIL");
						}
					}
					else {
						reportStep("The Heroimage Keyname is: src is having Empty value", "FAIL");
					}
				}
			}	

		}
		else
		{
			reportStep("The object Hero image is not found for this article", "WARN");
		}
		System.out.println("******************");
		return this;
	}



	public FDArticlelink_API3 author() {

		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");

		if(articleItem.has("authors")) {
			JSONArray authorname=articleItem.getJSONArray("authors");
			int authorkey=authorname.length();
			for(int i=0;i<authorkey;i++) {
				JSONObject Keyvalue_authors=(JSONObject) authorname.get(i);
				Iterator<String> key=Keyvalue_authors.keys();
				String Author="name";
				System.out.println("AUTHOR CHECK");
				System.out.println("*************************");
				while(key.hasNext()) {
					String Currentdynamickey=key.next();
					if(Currentdynamickey.equalsIgnoreCase(Author)) {
						if(!Keyvalue_authors.getString("name").isEmpty()) {
							if(!Keyvalue_authors.getString("name").equalsIgnoreCase("null")) {
								reportStep("The Author Keyname is: name"+ ""+" - "+Keyvalue_authors.getString("name"), "PASS");
							}
							else {
								reportStep("The Author Keyname name is having NULL value", "FAIL");
							}
						}
						else {
							reportStep("The Author Keyname name is having EMPTY value", "FAIL");
						}
					}
				}

			}
		}
		else
		{
			reportStep("The object Authors is not found for this article", "WARN");
		}
		System.out.println("*************************");
		return this;
	}


	public FDArticlelink_API3 Thumbnail() {

		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");

		if(articleItem.has("thumbnail")){
			JSONObject Keyvalue_thumbnail=articleItem.getJSONObject("thumbnail");
			Iterator<String> key1=Keyvalue_thumbnail.keys();
			ArrayList<String> Thumbnailkeyname=new ArrayList<String>();
			Thumbnailkeyname.add("src");
			Thumbnailkeyname.add("resizeUrl");
			System.out.println("THUMBNAIL CHECK");
			System.out.println("****************************");
			while(key1.hasNext()) {
				String Currentdynamickey1=(String) key1.next();
				for(int j=0;j<Thumbnailkeyname.size();j++) {
					if(Currentdynamickey1.equalsIgnoreCase(Thumbnailkeyname.get(j))) {
						if(!Keyvalue_thumbnail.getString(Currentdynamickey1).isEmpty()) {
							if(!Keyvalue_thumbnail.getString(Currentdynamickey1).equalsIgnoreCase("null")) {
								reportStep("The Thumbnail Keyname is:"+Currentdynamickey1+" - "+Keyvalue_thumbnail.getString(Currentdynamickey1), "PASS");

							}
							else {
								reportStep("The Thumbnail Keyname is:"+Currentdynamickey1+" - "+"having NULL value", "FAIL");
							}
						}
						else {
							reportStep("The Thumbnail Keyname is:"+Currentdynamickey1+" - "+"having EMPTY value", "FAIL");
						}
					}
				}
			}
		}
		else
		{
			reportStep("The object Thumbnail is not found for this article", "WARN");
		}
		System.out.println("****************************");
		return this;
	}



	public FDArticlelink_API3 Potraitthumbnail() {
		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");

		if(articleItem.has("portraitThumbnail")) {
			JSONObject Keyvalue_potraitthumbnail=articleItem.getJSONObject("portraitThumbnail");
			Iterator<String> key1=Keyvalue_potraitthumbnail.keys();
			ArrayList<String> Thumbnailkeyname=new ArrayList<String>();
			Thumbnailkeyname.add("src");
			Thumbnailkeyname.add("resizeUrl");
			System.out.println("POTRAITTHUMBNAIL CHECK");
			System.out.println("****************************");
			while(key1.hasNext()) {
				String Currentdynamickey1=(String) key1.next();
				for(int j=0;j<Thumbnailkeyname.size();j++) {
					if(Currentdynamickey1.equalsIgnoreCase(Thumbnailkeyname.get(j))) {
						if(!Keyvalue_potraitthumbnail.getString(Currentdynamickey1).isEmpty()) {
							if(!Keyvalue_potraitthumbnail.getString(Currentdynamickey1).equalsIgnoreCase("null")) {
								reportStep("The PotraitThumbnail Keyname is:"+Currentdynamickey1+" - "+Keyvalue_potraitthumbnail.getString(Currentdynamickey1), "PASS");

							}
							else {
								reportStep("The PotraitThumbnail Keyname is:"+Currentdynamickey1+" - "+"having NULL value", "FAIL");
							}
						}
						else {
							reportStep("The PotraitThumbnail Keyname is:"+Currentdynamickey1+" - "+"having EMPTY value", "FAIL");
						}
					}
				}
			}
		}
		else
		{
			reportStep("The object Potraitthumbnail is not found for this article", "WARN");
		}
		System.out.println("****************************");
		return this;
	}


	public FDArticlelink_API3 Categories() {

		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");

		if(articleItem.has("categories")) {
			JSONArray Categoryname=articleItem.getJSONArray("categories");
			int categorykey=Categoryname.length();
			for(int i=0;i<categorykey;i++) {
				JSONObject Keyvalue_categories=(JSONObject) Categoryname.get(i);
				Iterator<String> categorieskey=Keyvalue_categories.keys();
				String Keyss="key";
				System.out.println("CATEGORIES CHECK");
				System.out.println("*********************************");
				while(categorieskey.hasNext()) {
					String Currentdynamickey=categorieskey.next();
					if(Currentdynamickey.equalsIgnoreCase(Keyss)) {
						if(!Keyvalue_categories.getString("key").isEmpty()) {
							if(!Keyvalue_categories.getString("key").equalsIgnoreCase("null")) {
								reportStep("The Categories Keyname is:"+ i +" : "+Currentdynamickey+" - "+Keyvalue_categories.getString(Currentdynamickey), "PASS");
							}
							else {
								reportStep("The Categories Keyname :"+Currentdynamickey+" - "+" is having NULL value", "FAIL");
							}
						}
						else {
							reportStep("The Categories Keyname :"+Currentdynamickey+" - "+" is having EMPTY value", "FAIL");
						}
					}
				}
			}
		}
		else{
			reportStep("The object Categories is not found for this article", "WARN");
		}
		System.out.println("*********************************");
		return this;
	}


	public FDArticlelink_API3 Segments() {

		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");
		System.out.println("SEGMENT CHECK");
		System.out.println("****************");
		if(articleItem.has("segments")) {
			JSONArray Segmentsname=articleItem.getJSONArray("segments");
			int segmentkey=Segmentsname.length();

			for(int i=0;i<segmentkey;i++) {
				JSONObject Keyvalue_segments=(JSONObject) Segmentsname.get(i);
				Iterator<String> key=Keyvalue_segments.keys();

				ArrayList<String> Segmentskeyname=new ArrayList<String>();
				Segmentskeyname.add("text");
				Segmentskeyname.add("contentUrl");
				Segmentskeyname.add("pageType");
				Segmentskeyname.add("link");
				Segmentskeyname.add("textBlockType");
				Segmentskeyname.add("html");
				//Segmentskeyname.add("@id");

				while(key.hasNext()) {
					String Currentdynamickey=key.next();

					for(int j=0;j<Segmentskeyname.size();j++) {
						if(Currentdynamickey.equalsIgnoreCase(Segmentskeyname.get(j))) {
							if(!Keyvalue_segments.getString(Currentdynamickey).isEmpty()) {
								if(!Keyvalue_segments.getString(Currentdynamickey).equalsIgnoreCase("null")) {
									reportStep("The Segments Keyname is:"+ i +" : "+Currentdynamickey+" - "+Keyvalue_segments.getString(Currentdynamickey), "PASS");
									Segments_Objects(Keyvalue_segments.getString(Currentdynamickey),i);
								}
								else {
									reportStep("The Segments Keyname :"+Currentdynamickey+" - "+" is having NULL value", "FAIL");
								}
							}
							else {
								reportStep("The Segments Keyname :"+Currentdynamickey+" - "+" is having EMPTY value", "INFO");
							}
						}
					}
				}
				System.out.println("**********************************************");
			}
		}
		else{
			reportStep("The object Segments is not found for this article", "WARN");
		}

		//System.out.println("********************");
		return this;
	}


	public FDArticlelink_API3 Segments_Objects(String Objects,int i) {
		try {
			if(Objects.equals("videoWithText") || Objects.equals("videoOnly")) {
				System.out.println("SEGMENT VIDEO CHECK");
				System.out.println("**********************");
				video(i);
				Video_Thumbnail(i);
				//System.out.println("**********************");
			}
			else if(Objects.equals("ad")) {
				System.out.println("SEGMENT AD CHECK");
				System.out.println("**********************");
				Ad();
				//	System.out.println("**********************");
			}
			else if(Objects.equals("photoWithText") || Objects.equals("photoOnly")) {
				System.out.println("SEGMENT PHOTO CHECK");
				System.out.println("**********************");
				System.out.println(i);
				Segments_Phototext(i);
				
				//System.out.println("**********************");
			}
			else if(Objects.equals("callToAction")) {
				System.out.println("SEGMENT CTA CHECK");
				System.out.println("**********************");
				Segments_Thumbnail(i);
				//System.out.println("**********************");
			}
			else if(Objects.equals("verticalGallery")) {
				System.out.println("SEGMENT VERTICALGALLERY CHECK");
				System.out.println("**********************");
				Verticalgallery(i);
				VG_GalleryItems(i);
				//	System.out.println("**********************");

			}
			else if(Objects.equals("eCommerce")) {
				System.out.println("SEGMENT ECOMMERCE CHECK");
				System.out.println("**********************");
				eCommerce(i);
				/*System.out.println("ECOMMERCE THUMBNAIL CHECKING");
				System.out.println("*************************");*/
				eCommerceThumbnail(i);
				/*System.out.println("ECOMMERCE OFFERS CHECKING");
				System.out.println("*************************");*/
				eCommerceOffers(i);
			}

		}
		catch(Exception E) {

		}
		return this;
	}


	public FDArticlelink_API3 video(int i) {
		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");
		JSONArray Segmentsname=articleItem.getJSONArray("segments");
		int segmentkey=Segmentsname.length();
			JSONObject Keyvalue_segments=(JSONObject) Segmentsname.get(i);

			if(Keyvalue_segments.has("video")) {
				JSONObject videoItem = Keyvalue_segments.getJSONObject("video");
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
					for(int i1=0;i1<Actualkeyname.size();i1++) {
						if(Currentdynamickey.equalsIgnoreCase(Actualkeyname.get(i1))) {
							if(!videoItem.getString(Currentdynamickey).isEmpty()) {
								if(!videoItem.getString(Currentdynamickey).equalsIgnoreCase("null")) {
									reportStep("The segments video keyname is : "+Currentdynamickey+" - "+videoItem.getString(Currentdynamickey), "PASS");
								}
								else {
									reportStep("The segments video Keyname is: "+Currentdynamickey+" - "+" having NULL value ", "FAIL");
								}
							}
							else {
								reportStep("The segments video Keyname is: "+Currentdynamickey+" - "+" having EMPTY value ", "FAIL");
							}
						}
					}
				}
			}
			/*else
		{
			reportStep("The object Video is not found", "WARN");
		}*/
			

		return this;
	}




	public FDArticlelink_API3 Video_Thumbnail(int i) {
		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");
		JSONArray Segmentsname=articleItem.getJSONArray("segments");
		int segmentkey=Segmentsname.length();

			JSONObject Keyvalue_segments=(JSONObject) Segmentsname.get(i);

			if(Keyvalue_segments.has("video")) {
				JSONObject Keyvalue_Video=Keyvalue_segments.getJSONObject("video");

				if(Keyvalue_Video.has("largeThumbnail")) {

					JSONObject VideoThumbnail_KeyItems=Keyvalue_Video.getJSONObject("largeThumbnail");
					Iterator<String> key=VideoThumbnail_KeyItems.keys();

					ArrayList<String> Thumbnailkeyname=new ArrayList<String>();
					Thumbnailkeyname.add("src");
					Thumbnailkeyname.add("resizeUrl");

					while(key.hasNext()) {
						String Currentdynamickey1=(String) key.next();
						for(int j=0;j<Thumbnailkeyname.size();j++) {
							if(Currentdynamickey1.equalsIgnoreCase(Thumbnailkeyname.get(j))) {
								if(!VideoThumbnail_KeyItems.getString(Currentdynamickey1).isEmpty()) {
									if(!VideoThumbnail_KeyItems.getString(Currentdynamickey1).equalsIgnoreCase("null")) {
										reportStep("The Video Largethumbnail Keyname is:"+Currentdynamickey1+" - "+VideoThumbnail_KeyItems.getString(Currentdynamickey1), "PASS");

									}
									else {
										reportStep("The Video Largethumbnail Keyname is:"+Currentdynamickey1+" - "+"having NULL value", "FAIL");
									}
								}
								else {
									reportStep("The Video Largethumbnail Keyname is:"+Currentdynamickey1+" - "+"having EMPTY value", "FAIL");
								}
							}
						}
					}
				}
				/*else
				{
					reportStep("The Video Largethumbnail Keyname is not found for this video item", "WARN");
				}*/
			}
		
		return this;
	}



	public FDArticlelink_API3 Ad() {
		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");
		JSONArray Segmentsname=articleItem.getJSONArray("segments");
		int segmentkey=Segmentsname.length();
		for(int i=0;i<segmentkey;i++) {
			JSONObject Keyvalue_segments=(JSONObject) Segmentsname.get(i);

			if(Keyvalue_segments.has("ad")) {
				JSONObject AdItem = Keyvalue_segments.getJSONObject("ad");
				Iterator<String> key=AdItem.keys();
				ArrayList<String> Actualkeyname=new ArrayList<String>();

				Actualkeyname.add("adType");
				Actualkeyname.add("slot");
				Actualkeyname.add("sizes");

				while(key.hasNext()) {
					String Currentdynamickey=key.next();
					for(int j=0;j<Actualkeyname.size();j++) {
						if(Currentdynamickey.equalsIgnoreCase(Actualkeyname.get(j))) {
							if(!AdItem.getString(Currentdynamickey).isEmpty()) {
								if(!AdItem.getString(Currentdynamickey).equalsIgnoreCase("null")) {
									reportStep("The segments Ad keyname is : "+Currentdynamickey+" - "+AdItem.getString(Currentdynamickey), "PASS");
								}
								else {
									reportStep("The segments Ad keyname is : "+Currentdynamickey+" - "+ "having NULL value", "FAIL");
								}
							}
							else {
								reportStep("The segments Ad keyname is : "+Currentdynamickey+" - "+ "having EMPTY value", "FAIL");
							}
						}
					}
				}
			}
			/*else {
				reportStep("The segments AD is not found for this article", "WARN");
			}*/
		}		
		return this;
	}

	public FDArticlelink_API3 Segments_Phototext(int i) {
		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");
		JSONArray Segmentsname=articleItem.getJSONArray("segments");
		int segmentkey=Segmentsname.length();

		//boolean Stop=false;
		
			JSONObject Keyvalue_segments=(JSONObject) Segmentsname.get(i);

			if(Keyvalue_segments.has("image")) {
				JSONObject ImageItem = Keyvalue_segments.getJSONObject("image");
				Iterator<String> key=ImageItem.keys();
				ArrayList<String> Actualkeyname=new ArrayList<String>();

				Actualkeyname.add("src");
				Actualkeyname.add("resizeUrl");

				while(key.hasNext()) {
					String Currentdynamickey=key.next();
					for(int j=0;j<Actualkeyname.size();j++) {
						if(Currentdynamickey.equalsIgnoreCase(Actualkeyname.get(j))) {
							if(!ImageItem.getString(Currentdynamickey).isEmpty()) {
								if(!ImageItem.getString(Currentdynamickey).equalsIgnoreCase("null")) {
									reportStep("The segments Photo keyname is : "+Currentdynamickey+" - "+ImageItem.getString(Currentdynamickey), "PASS");
									//Stop=true;
								}
								else {
									reportStep("The segments Photo keyname is : "+Currentdynamickey+" - "+ "having NULL value", "FAIL");
								}
							}
							else {
								reportStep("The segments Photo keyname is : "+Currentdynamickey+" - "+ "having EMPTY value", "WARN");
							}
						}
					}
				}
				/*if(Stop) {
					break;
				}*/
			}		
				
		return this;
	}


	public FDArticlelink_API3 Segments_Thumbnail(int i) {
		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");
		JSONArray Segmentsname=articleItem.getJSONArray("segments");
		int segmentkey=Segmentsname.length();
		boolean Stop=false;
			JSONObject Keyvalue_segments=(JSONObject) Segmentsname.get(i);

			if(Keyvalue_segments.has("thumbnail")) {

				JSONObject Thumbnail_Item = Keyvalue_segments.getJSONObject("thumbnail");
				Iterator<String> key=Thumbnail_Item.keys();
				ArrayList<String> Actualkeyname=new ArrayList<String>();

				Actualkeyname.add("src");
				Actualkeyname.add("resizeUrl");

				while(key.hasNext()) {
					String Currentdynamickey=key.next();
					for(int j=0;j<Actualkeyname.size();j++) {
						if(Currentdynamickey.equalsIgnoreCase(Actualkeyname.get(j))) {
							if(!Thumbnail_Item.getString(Currentdynamickey).isEmpty()) {
								if(!Thumbnail_Item.getString(Currentdynamickey).equalsIgnoreCase("null")) {
									reportStep("The segments CTA Thumbnail keyname is : "+Currentdynamickey+" - "+Thumbnail_Item.getString(Currentdynamickey), "PASS");
								}
								else {
									reportStep("The segments CTA Thumbnail keyname is : "+Currentdynamickey+" - "+ "having NULL value", "FAIL");
								}
							}
							else {
								reportStep("The segments CTA Thumbnail keyname is : "+Currentdynamickey+" - "+ "having EMPTY value", "FAIL");
							}
						}
					}
				}

			}		
		return this;
	}


	public FDArticlelink_API3 Verticalgallery(int i) {

		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");
		JSONArray Segmentsname=articleItem.getJSONArray("segments");
		int segmentkey=Segmentsname.length();
			JSONObject Keyvalue_segments=(JSONObject) Segmentsname.get(i);

			if(Keyvalue_segments.has("gallery")) {
				JSONObject Thumbnail_VG = Keyvalue_segments.getJSONObject("gallery");
				Iterator<String> key=Thumbnail_VG.keys();

				ArrayList<String> Actualkeyname=new ArrayList<String>();
				Actualkeyname.add("uri");
				Actualkeyname.add("contentItemType");
				Actualkeyname.add("publishTitle");
				Actualkeyname.add("link");
				Actualkeyname.add("pageType");
				Actualkeyname.add("analyticsSection");

				while(key.hasNext()) {
					String Currentdynamickey=key.next();

					for(int n=0;n<Actualkeyname.size();n++) {
						if(Currentdynamickey.equalsIgnoreCase(Actualkeyname.get(n))) {
							if(!Thumbnail_VG.getString(Currentdynamickey).isEmpty()) {
								if(!Thumbnail_VG.getString(Currentdynamickey).equalsIgnoreCase("null")) {
									reportStep("The segments VerticalGallery keyname is : "+Currentdynamickey+" - "+Thumbnail_VG.getString(Currentdynamickey), "PASS");
								}
								else {
									reportStep("The segments VerticalGallery keyname is : "+Currentdynamickey+" - "+ "having NULL value", "FAIL");
								}
							}
							else {
								reportStep("The segments VerticalGallery keyname is : "+Currentdynamickey+" - "+ "having EMPTY value", "FAIL");
							}
						}
					}
				}

			}

		
		return this;
	}


	public FDArticlelink_API3 VG_GalleryItems(int i) {

		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");
		JSONArray Segmentsname=articleItem.getJSONArray("segments");
		int segmentkey=Segmentsname.length();

			JSONObject Keyvalue_segments=(JSONObject) Segmentsname.get(i);

			if(Keyvalue_segments.has("gallery")) {
				JSONObject Keyvalue_VG=Keyvalue_segments.getJSONObject("gallery");


				JSONArray VG_KeyItems=Keyvalue_VG.getJSONArray("galleryItems");
				int VG_keys=VG_KeyItems.length();

				for(int j=0;j<VG_keys;j++) {

					JSONObject keyvalue_VG_GalleryItem=(JSONObject) VG_KeyItems.get(j);
					Iterator<String> key=keyvalue_VG_GalleryItem.keys();

					ArrayList<String> Actualkeyname=new ArrayList<String>();
					Actualkeyname.add("uri");
					Actualkeyname.add("title");
					Actualkeyname.add("socialTitle");

					while(key.hasNext()) {
						String Currentdynamickey=key.next();
						for(int k=0;k<Actualkeyname.size();k++) {
							if(Currentdynamickey.equalsIgnoreCase(Actualkeyname.get(k))) {
								if(!keyvalue_VG_GalleryItem.getString(Currentdynamickey).isEmpty()) {
									if(!keyvalue_VG_GalleryItem.getString(Currentdynamickey).equalsIgnoreCase("null")) {
										reportStep("The segments VG_GAlleryItem keyname is: "+ j +" : "+Currentdynamickey+" - "+keyvalue_VG_GalleryItem.getString(Currentdynamickey), "PASS");

									}
									else {
										reportStep("The segments VG_GAlleryItem keyname is : "+ j +" : "+Currentdynamickey+" - "+"having NULL value", "FAIL");
									}
								}
								else {
									reportStep("The segments VG_GAlleryItem keyname is : "+ j +" : "+Currentdynamickey+" - "+"having EMPTY value", "FAIL");
								}
							}
						}
					}
				}
			}
		

		return this;
	}



	public FDArticlelink_API3 eCommerce(int i) {
		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");
		JSONArray Segmentsname=articleItem.getJSONArray("segments");
		int segmentkey=Segmentsname.length();
			JSONObject Keyvalue_segments=(JSONObject) Segmentsname.get(i);

			if(Keyvalue_segments.has("productOfferContent")) {
				boolean stop=false;
				JSONObject eCproductoffer_KeyItems=Keyvalue_segments.getJSONObject("productOfferContent");
				Iterator<String> key=eCproductoffer_KeyItems.keys();

				ArrayList<String> Actualkeyname=new ArrayList<String>();
				Actualkeyname.add("offerId");
				Actualkeyname.add("itemTitle");
				Actualkeyname.add("itemText");

				while(key.hasNext()) {
					String Currentdynamickey=key.next();

					for(int n=0;n<Actualkeyname.size();n++) {
						if(Currentdynamickey.equalsIgnoreCase(Actualkeyname.get(n))) {
							if(!eCproductoffer_KeyItems.getString(Currentdynamickey).isEmpty()) {
								if(!eCproductoffer_KeyItems.getString(Currentdynamickey).equalsIgnoreCase("null")) {
									reportStep("The segments ECommerce keyname is : "+Currentdynamickey+" - "+eCproductoffer_KeyItems.getString(Currentdynamickey), "PASS");
									stop=true;
								}
								else {
									reportStep("The segments ECommerce keyname is : "+Currentdynamickey+" - "+ "having NULL value", "FAIL");
								}
							}
							else {
								reportStep("The segments ECommerce keyname is : "+Currentdynamickey+" - "+ "having EMPTY value", "FAIL");
							}
						}
					}

				}


			}
		
		return this;
	}


	public FDArticlelink_API3 eCommerceThumbnail(int i) {
		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");
		JSONArray Segmentsname=articleItem.getJSONArray("segments");
		int segmentkey=Segmentsname.length();
			JSONObject Keyvalue_segments=(JSONObject) Segmentsname.get(i);

			if(Keyvalue_segments.has("productOfferContent")) {
				
				JSONObject eCproductoffer_KeyItems=Keyvalue_segments.getJSONObject("productOfferContent");
				if(eCproductoffer_KeyItems.has("thumbnail")) {
					JSONObject eCommerceThumbail=eCproductoffer_KeyItems.getJSONObject("thumbnail");
					Iterator<String> key=eCommerceThumbail.keys();
					ArrayList<String> Actualkeyname=new ArrayList<String>();
					Actualkeyname.add("src");
					Actualkeyname.add("resizeUrl");


					while(key.hasNext()) {
						String Currentdynamickey=key.next();

						for(int n=0;n<Actualkeyname.size();n++) {
							if(Currentdynamickey.equalsIgnoreCase(Actualkeyname.get(n))) {
								if(!eCommerceThumbail.getString(Currentdynamickey).isEmpty()) {
									if(!eCommerceThumbail.getString(Currentdynamickey).equalsIgnoreCase("null")) {
										reportStep("The segments ECommerce Thumbnail keyname is : "+Currentdynamickey+" - "+eCommerceThumbail.getString(Currentdynamickey), "PASS");
										
									}
									else {
										reportStep("The segments ECommerce Thumbnail keyname is : "+Currentdynamickey+" - "+ "having NULL value", "FAIL");
									}
								}
								else {
									reportStep("The segments ECommerce Thumbnail keyname is : "+Currentdynamickey+" - "+ "having EMPTY value", "FAIL");
								}
							}
							

						}

					}
					
				}
			}
		
			return this;
	}

	public FDArticlelink_API3 eCommerceOffers(int i) {
		JSONObject Bodykeyvalue=new JSONObject(result).getJSONObject("body");
		JSONObject article= Bodykeyvalue.getJSONObject("featuredContent");
		JSONObject articleItem = article.getJSONObject("article");
		JSONArray Segmentsname=articleItem.getJSONArray("segments");
		int segmentkey=Segmentsname.length();
			boolean stop=false;
			JSONObject Keyvalue_segments=(JSONObject) Segmentsname.get(i);

			if(Keyvalue_segments.has("productOfferContent")) {

				JSONObject eCproductoffer_KeyItems=Keyvalue_segments.getJSONObject("productOfferContent");
				JSONArray eCommerceThumbail=eCproductoffer_KeyItems.getJSONArray("offers");
				int offersname=eCommerceThumbail.length();
				for(int j=0;j<offersname;j++) {

					JSONObject Keyvalueoffers=(JSONObject) eCommerceThumbail.get(j);
					Iterator<String> key=Keyvalueoffers.keys();

					ArrayList<String> Actualkeyname=new ArrayList<String>();
					Actualkeyname.add("currentPrice");
					//	Actualkeyname.add("originalPrice");
					Actualkeyname.add("text");


					while(key.hasNext()) {
						String Currentdynamickey=key.next();

						for(int n=0;n<Actualkeyname.size();n++) {
							if(Currentdynamickey.equalsIgnoreCase(Actualkeyname.get(n))) {
								if(!Keyvalueoffers.getString(Currentdynamickey).isEmpty()) {
									if(!Keyvalueoffers.getString(Currentdynamickey).equalsIgnoreCase("null")) {
										reportStep("The segments ECommerce Offers is : "+Currentdynamickey+" - "+Keyvalueoffers.getString(Currentdynamickey), "PASS");
									}
									else {
										reportStep("The segments ECommerce Offers is : "+Currentdynamickey+" - "+ "having NULL value", "FAIL");
									}
								}
								else {
									reportStep("The segments ECommerce Offers is : "+Currentdynamickey+" - "+ "having EMPTY value", "FAIL");
								}
							}


						}

					}
					


				}

			}
		
		return this; 
	}
	 
}


