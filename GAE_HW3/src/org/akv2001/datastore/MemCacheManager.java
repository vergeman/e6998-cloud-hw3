package org.akv2001.datastore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MemCacheManager {
	static int cacheExpiry = 20; //sec
	static int dbExpiry = 1; //min
	MemcacheService syncCache;

	public MemCacheManager() {
		syncCache = MemcacheServiceFactory.getMemcacheService();
	}
	
	/*caches list obj - namespace serves as key, cacheExpiry as refresh time*/
	public void cacheWoeidList(ArrayList<Woeid> woeids, String namespace) {
		if (namespace != null) {
			syncCache = MemcacheServiceFactory.getMemcacheService(namespace);
		} else {
			syncCache = MemcacheServiceFactory.getMemcacheService();
		}
		try {
			syncCache.put(namespace, woeids,
					Expiration.byDeltaSeconds(cacheExpiry));
		} catch (MemcacheServiceException m) {
			/* some of our lists are > 1 mb so can't be cached */
			m.printStackTrace();
		}
	}

	public boolean isValid() {

		if (needsUpdate()) {
			return false;
		}

		
		return true;

	}

	
	private boolean needsUpdate() {
		syncCache = MemcacheServiceFactory.getMemcacheService("all");
		/*check if db is still valid - dbExpiry*/
		QueryManager qm = new QueryManager();
		Woeid w = qm.getOldest();
		
		/*if we have no value, then db is empty*/
		if (w == null) {
			System.out.println("db is empty");
			return true;
		}

		Calendar wcal = Calendar.getInstance();
		wcal.setTime(w.getTimestamp());

		wcal.add(Calendar.MINUTE, dbExpiry);
		
		Calendar cal = Calendar.getInstance();

		
		/*
		 * if db is stale by more than a minute, 
		 * we need to indicate a full fetch and 
		 * by returning null to the caller as our result
		 */
		if (cal.after(wcal) ) {
			System.out.println("stale db");
			return true;
		}
		
		return false;

	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Woeid> getCachedWoeidList(String namespace) {
		/*set namespace*/
		syncCache = MemcacheServiceFactory.getMemcacheService(namespace);
		
		/*check cache*/
		List<Woeid> value = (List<Woeid>) syncCache.get(namespace);
		
		/*if cache is expired*/
		if (value == null) {

			/*get from database*/
			System.out.println("expired cache - loading db: " + namespace);
			
			QueryManager qm = new QueryManager();
			
			/*otherwise we refresh cache from db*/
			ArrayList<Woeid> coldest = qm.getColdest(5);
			ArrayList<Woeid> hottest = qm.getHottest(5);
			ArrayList<Woeid> all = qm.getAll();
			
			cacheWoeidList(coldest, "coldest");													
			cacheWoeidList(hottest, "hottest");			
			cacheWoeidList(all, "all");			
			
			syncCache = MemcacheServiceFactory.getMemcacheService(namespace);
			
			/*this is hacky but all is a special case > 1mb so isn't cacheable as a list*/
			if (namespace.equals("all")) {
				value = all;
			}
			else {
				value = new ArrayList<Woeid>( (List<Woeid>) syncCache.get(namespace) );
			}

		} 
		
		return new ArrayList<Woeid>(value);
	}
	

	
	
}
