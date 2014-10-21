package com.cloud.tweetchart;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

@SuppressWarnings("serial")
public class getFilterdata extends HttpServlet {
	
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	    
		String key = req.getParameter("filter");
	    byte[] value;
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	    
	    value = (byte[]) syncCache.get(key); // read from cache
	    if (value == null) {
	    	
	        syncCache.put(key, value);
	      }
	    
	    System.out.println(key + value);
		
		String filter_str = req.getParameter("filter");
		
		Filter hashtagfilter = new FilterPredicate("tweet_tag",FilterOperator.EQUAL,filter_str.toLowerCase());
		
		
		// extract data from google datastore
		
		// Use class Query to assemble a query
		Query q = new Query("tweets").setFilter(hashtagfilter);
		
		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);
		FetchOptions options = FetchOptions.Builder.withChunkSize(5000);
		
	
		
		resp.setContentType("text/plain");
		for (Entity result : pq.asIterable()) {
		  String t_text = (String) result.getProperty("tweet_text");
		  String t_time = (String) result.getProperty("tweet_time");
		  String t_tag = (String) result.getProperty("tweet_tag");
		  String t_lat = (String) result.getProperty("tweet_lat");
		  String t_lng = (String) result.getProperty("tweet_longi");
		  
		  resp.getWriter().println(t_text + "," + t_time + "," + t_tag + ","  + t_lat + "," + t_lng + ";");
		}
		
		
		
	}
	public static void add_to_Datastore(String tweet_text, String tweet_time, double tweet_lat, double tweet_longi, String tweet_tag){

		Entity tweets = new Entity("tweets");
		tweets.setProperty("tweet_text", tweet_text);
		tweets.setProperty("tweet_time", tweet_time);
		tweets.setProperty("tweet_lat", tweet_lat);
		tweets.setProperty("tweet_longi", tweet_longi);
		tweets.setProperty("tweet_tag", tweet_tag);
		datastore.put(tweets); 	

		
	}
	
	
	
}
