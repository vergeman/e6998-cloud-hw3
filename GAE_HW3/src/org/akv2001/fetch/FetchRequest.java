package org.akv2001.fetch;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


import org.akv2001.datastore.PersistManager;
import org.akv2001.datastore.Woeid;
import org.akv2001.img.ImageManager;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.apphosting.api.ApiProxy.ApiDeadlineExceededException;


/*
 * class to make generic URL requests and 
 * store results in String - String mapping
 */

public class FetchRequest {

	private HashMap<String, URL> urlMap;
	private HashMap<String, Future<HTTPResponse>> responses;
	private URLFetchService fetcher;
	HashMap<String, String> results;
	HashMap<String, Woeid> woeids_map;

	public FetchRequest() {
		urlMap = new HashMap<String, URL>();
		responses = new HashMap<String, Future<HTTPResponse>>();
		fetcher = URLFetchServiceFactory.getURLFetchService();
		results = new HashMap<String, String>();
		woeids_map = new HashMap<String, Woeid>();
	}

	/*
	 * populates http request holder with a key, and String based url
	 */
	public void addRequest(String key, String url) {
		try {
			this.urlMap.put(key, new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * asynchronously fetches url content for Woeids
	 */
	public HashMap<String, Woeid> fetchWoeidAsync() {
		int count = 0;
		
		/* query */
		for (String key : urlMap.keySet()) {

			try {
			
				Future<HTTPResponse> HTTP_response = fetcher.fetchAsync(urlMap.get(key));
				responses.put(key, HTTP_response);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		

		/* check for results */
		Entry<String, Future<HTTPResponse>> response;
		
		while (responses.size() > 0) {

			for (Iterator<Entry<String, Future<HTTPResponse>>> it = responses.entrySet().iterator(); it.hasNext();) {
				
				response = it.next();
				
				if (response.getValue().isDone()) {
					
					try {
						
						/* we have results, so parse and persist them */
						String res = new String(response.getValue().get().getContent());
						String key = response.getKey();
						Woeid w = new Woeid();
						w.setKey(key);
						
						results.put(key, res);
						
						ParseXML parser = new ParseXML(res, w);

						parser.parse();

						woeids_map.put(key, w);
						
						it.remove(); // pop from iterator so won't be checked

					} catch (ApiDeadlineExceededException e) {
						//e.printStackTrace();
						count++;
					} catch (InterruptedException e) {
						//e.printStackTrace();
						count++;
					} catch (ExecutionException e) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							//e1.printStackTrace();
						}
						//e.printStackTrace();
						count++;
					}
				}
			}
			// break on repeated failures (malformed input, etc)
			if (count > 10 ) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}
				System.out.println("fail, breaking");
				break;
				//count = 0;
			}
			
		}

		System.out.println("Finished async requests");
		return woeids_map;
	}

	
	/* fetches images */
	
	/*
	 * asynchronously fetches url content for Woeids
	 */
	public void fetchImgAsync(HashMap<String, Woeid> woeid_map ) {
		int count = 0;
		
		/* query */
		for (String key : woeid_map.keySet()) {

			try {
				if (woeid_map.get(key).getImg_url() != null) {
					Future<HTTPResponse> HTTP_response = fetcher.fetchAsync(new URL(woeid_map.get(key).getImg_url()));
					responses.put(key, HTTP_response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		

		/* check for results */
		Entry<String, Future<HTTPResponse>> response;
		
		while (responses.size() > 0) {

			for (Iterator<Entry<String, Future<HTTPResponse>>> it = responses.entrySet().iterator(); it.hasNext();) {
				
				response = it.next();

				if (response.getValue().isDone()) {
					
					try {
						
						String key = response.getKey();
						String type = null;
						/* we have results, so parse and persist them */
						
						byte[] res = response.getValue().get().getContent();
						
						for (HTTPHeader header : response.getValue().get().getHeaders()) {
							if (header.getName().equalsIgnoreCase("content-type")) {
								type = header.getValue();
								break;
							}
								
						}
						ImageManager imgr = new ImageManager(res, type);
						Image res_img = imgr.rotate_and_resize(90, 300, 300);
						
						/*associate image*/
						woeid_map.get(key).setImg(res_img.getImageData());
						woeid_map.get(key).setImg_type(type);
						
						/*timestamp our save*/
						woeid_map.get(key).setTimestamp(new Date());
						
						/*save*/
						PersistManager pm = new PersistManager();
						pm.save(woeid_map.get(key));
					
						/* pop from iterator so won't be checked */
						it.remove();

					} catch (ApiDeadlineExceededException e) {
						//e.printStackTrace();
						count++;
					} catch (InterruptedException e) {
						//e.printStackTrace();
						count++;
					} catch (ExecutionException e) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							//e1.printStackTrace();
						}
						//e.printStackTrace();
						count++;
					}
				}
			}
			// break on repeated failures (malformed input, etc)
			if (count > 10 ) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}
				System.out.println("fail, breaking");
				break;
			}
			
		}

	}
	
	
	
}
