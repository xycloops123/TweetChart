package com.cloud.tweetchart;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class storedataServlet extends HttpServlet {
	
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		
				JsonParser parser = new JsonParser();
				
				FileInputStream fstream = new FileInputStream("ows.json");
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
				  int i=0, cnt=0;
				
				  
			  while ((strLine = br.readLine()) != null)   {
				    i++;
				  	Object obj = parser.parse(strLine);
				  	JsonObject jsonObject = (JsonObject) obj;
					  String str = JSONparse(jsonObject);
					  String[] tweets = str.split("gdm.nyc");
						
					  if (Double.parseDouble(tweets[1]) > 0.0){
						  System.out.println(tweets[0]);
						  Entity tweets_datastore = new Entity("tweets");
						  tweets_datastore.setProperty("tweet_text", tweets[0]);
						  tweets_datastore.setProperty("tweet_lat", tweets[1]);
						  tweets_datastore.setProperty("tweet_longi", tweets[2]);
						  tweets_datastore.setProperty("tweet_tag", tweets[3]);
						  tweets_datastore.setProperty("tweet_time", tweets[4]);
						  datastore.put(tweets_datastore);
						  cnt++;
					  }
					 
			  }
			  in.close();
			  System.out.println ("Total line scan : " + i);	
			  System.out.println ("Proper Data line : " + cnt);	
	
		
		
		
		
		resp.setContentType("text/plain");
		resp.getWriter().println("Total line scan : " + cnt);
		
		
	}	
	
	public static String JSONparse(JsonObject jsonObject) throws IOException{
	 	
		// tweet main text
	 		String tweet_text = jsonObject.get("text").toString();
		//System.out.println(tweet_text);

		// tweet latitude and longitude 
			JsonElement tweet_lat;
			JsonElement tweet_longi;
			
	 	   	JsonElement location = jsonObject.get("location");
	 	   	JsonObject  tweet_location = location.getAsJsonObject();
		    tweet_lat = tweet_location.get("lat");
		    tweet_longi = tweet_location.get("lng");
		    //System.out.println(tweet_lat + "," + tweet_longi);

		// tweet tag
			String tweet_tag = null;
	 	   	JsonElement tag = jsonObject.get("entities");
	 	   	JsonObject  entities = tag.getAsJsonObject();
				for(Object hashObj : (JsonArray)entities.get("hashtags")){
					JsonObject hashJson = (JsonObject)hashObj;
					tweet_tag = hashJson.get("text").toString().toLowerCase();
				
				}
				try{
					tweet_tag = tweet_tag.split("\"")[1];
				}catch (Exception e){ }	
			//System.out.println(tweet_tag);
			
		
		// tweet timestamp
			JsonElement tweet_time;
			tweet_time = jsonObject.get("timestamp");
	        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");       
	        String tweet_timestamp = dt1.format(tweet_time.getAsLong());
	        //System.out.println(tweet_timestamp);
        
        return tweet_text+ "gdm.nyc" +tweet_lat + "gdm.nyc" + tweet_longi+ "gdm.nyc" +tweet_tag + "gdm.nyc" +tweet_timestamp;

 }
	
	
}