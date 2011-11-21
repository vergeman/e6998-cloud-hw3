package org.akv2001.fetch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.akv2001.datastore.Woeid;

/*
 * loads WOEID's preps for Fetch
 */
public class PrepareWoeid {

	private ArrayList<String> woeids;
	HashMap<String, Woeid> results;
	
	
	public PrepareWoeid() {
		this.woeids = new ArrayList<String>();
	}

	
	/*
	 * wrap around generic YRequest functions, builds URL String requests
	 */
	public HashMap<String, Woeid> fetchWoeids(String baseurl) {
		
		FetchRequest req = new FetchRequest();
		
		for (String s : woeids) {
			req.addRequest(s, baseurl + "?w=" + s + "&u=c");
		}
		System.out.println("loaded url requests");
		
		results = req.fetchWoeidAsync();
		return results;
	}
	
	
	
	
	/*
	 * loads WOEID's from file into arrayList
	 */
	public void loadFile(String filename) {
	
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			
			String s;
			while ((s = br.readLine()) != null) {
				woeids.add(s);
			}
			
			fr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	public ArrayList<String> getWoeids() {
		return woeids;
	}

	public void setWoeids(ArrayList<String> woeids) {
		this.woeids = woeids;
	}
}
