//package com.cloud.tweetchart;
//
//import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//import com.google.appengine.api.datastore.DatastoreService;
//import com.google.appengine.api.datastore.DatastoreServiceFactory;
//import com.google.appengine.api.datastore.Entity;
//
//
//public class sample {
//
//	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
//		JSONParser parser = new JSONParser();
//		try{
//				  FileInputStream fstream = new FileInputStream("C:/ows.json");
//				  DataInputStream in = new DataInputStream(fstream);
//				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
//				  String strLine;
//				  int i=0, cnt=0;
//			  while ((strLine = br.readLine()) != null)   {
//				  i++;
//				  try{
//					  Object obj = parser.parse(strLine);
//					  JSONObject jsonObject = (JSONObject) obj;
//					  String str = JSONparse(jsonObject);
//					  
//					  String[] tweets = str.split("myk.nyc");
//						
//					  if (Double.parseDouble(tweets[1]) > 0.0){
//						  System.out.println (str);
//						  cnt++;
//					  }
//				  }catch (Exception e){
//					  System.err.println("Error Inside: " + e.getMessage());
//					  }
//			  }
//			  in.close();
//			  System.out.println ("Total line scan : " + i);	
//			  System.out.println ("Proper Data line : " + cnt);	
//			  
//			    }
//		catch (Exception e){
//			  System.err.println("Error: " + e.getMessage());
//			 
//			  }
//     }
//     
// public static String JSONparse(JSONObject jsonObject) throws IOException{
//	 	// tweet main text
//	 	String tweet_text = (String) jsonObject.get("text");
//		//System.out.println(tweet_text);
//
//		// tweet latitude and longitude 
//		double tweet_lat = 0;
//		double tweet_longi = 0;
//		if(jsonObject.containsKey("location")){
//			JSONObject location = (JSONObject) jsonObject.get("location");
//			if(location.containsKey("lat")){
//				JSONObject tweet_location = (JSONObject) jsonObject.get("location");
//				try {
//					tweet_lat = (double) tweet_location.get("lat");
//					tweet_longi = (double) tweet_location.get("lng");
//				} catch (Exception e) {
//					tweet_lat = 0;
//					tweet_longi =0;
//				}
//			}
//		}	
//		//System.out.println(tweet_lat + "," + tweet_longi);
//
//		// tweet tag
//		String tweet_tag = null;
//		if(jsonObject.containsKey("entities")){
//			JSONObject entities = (JSONObject) jsonObject.get("entities");
//				if(entities.containsKey("hashtags")){
//					for(Object hashObj : (JSONArray)entities.get("hashtags")){
//						JSONObject hashJson = (JSONObject)hashObj;
//						tweet_tag = hashJson.get("text").toString().toLowerCase();
//						
//					}
//			}
//		}
//		//System.out.println(tweet_tag);
//		
//		long tweet_time = 0;
//		// tweet timestamp
//		if(jsonObject.containsKey("timestamp")){
//			tweet_time = (long) jsonObject.get("timestamp");
//		}
//		
//        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
//        //System.out.println(dt1.format(tweet_time));
//        
//        String tweet_time1 = dt1.format(tweet_time);
//
//        return tweet_text+ "myk.nyc" +tweet_lat + "myk.nyc" + tweet_longi+ "myk.nyc" +tweet_tag + "myk.nyc" +tweet_time1;
//
// }
// 
// public void addtoDatastore(){
//		// insert data into google datastore
//		String tweet_text = "'Goldfather";
//		String tweet_time  = "2014-12-12";
//		String tweet_lat  = "31.0000";
//		String tweet_longi = "-100.0000";
//		String tweet_tag = "#movie";
//		
//			
//			Entity tweets = new Entity("tweets");
//			tweets.setProperty("tweet_text", tweet_text);
//			tweets.setProperty("tweet_time", tweet_time);
//			tweets.setProperty("tweet_lat", tweet_lat);
//			tweets.setProperty("tweet_longi", tweet_longi);
//			tweets.setProperty("tweet_tag", tweet_tag);
//			datastore.put(tweets);
// }
//
//
//}