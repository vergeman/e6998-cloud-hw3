package org.akv2001.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;



import org.akv2001.datastore.MemCacheManager;
import org.akv2001.datastore.QueryManager;
import org.akv2001.datastore.Woeid;
import org.akv2001.fetch.FetchRequest;
import org.akv2001.fetch.PrepareWoeid;
import org.akv2001.mail.MailManager;


@SuppressWarnings("serial")
public class HW3Servlet extends HttpServlet {
	
	static final String filename = "./WOEID_Codes.txt";
	static final String weather_url = "http://weather.yahooapis.com/forecastrss";
	
	static final String name_from = "Alan HW3 Cloud";
	static final String email_from = "alan.verga@gmail.com";
	static final String name_to = "Alan Verga";
	static final String email_to = "alan.verga@gmail.com";
	
	
	ArrayList<Woeid> coldest;
	ArrayList<Woeid> hottest;
	ArrayList<Woeid> all;
	
	/*
	 * broken links 2347568 2347567 12590237 240485035
	 */
	HashMap<String, Woeid> woeid_map;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		/* check cache / db */
		MemCacheManager mem_mgr = new MemCacheManager();

		if (mem_mgr.isValid() ) {
			
			coldest = mem_mgr.getCachedWoeidList("coldest");
			hottest = mem_mgr.getCachedWoeidList("hottest");
			all = mem_mgr.getCachedWoeidList("all");
			
		} 
		else 
		{
			
			/*prepare requests*/
			
			System.out.println("not cached values");
			PrepareWoeid woeid_req = new PrepareWoeid();

			woeid_req.loadFile(filename);

			/*
			 * we do operations internally of the fetches since we want to
			 * leverage asynchronicity and do operations while waiting for
			 * responses. Makes for unintuitive code paths, but faster
			 */

			/* fetch rss info, parse, return as Woeid key-obj map */
			woeid_map = woeid_req.fetchWoeids(weather_url);

			/*
			 * fetch associated image url, manipulate image and save objects to
			 * data store
			 */
			FetchRequest fetch_img = new FetchRequest();
			fetch_img.fetchImgAsync(woeid_map);

			/* get top 5 hottest and coldest locales */
			 QueryManager qm = new QueryManager(); 
			 coldest = qm.getColdest(5); 
			 hottest = qm.getHottest(5);
			 all = qm.getAll();
			 
			/* cache them */
			mem_mgr.cacheWoeidList(coldest, "coldest");
			mem_mgr.cacheWoeidList(hottest, "hottest");
			mem_mgr.cacheWoeidList(all, "all");
			 
			/* send mail */
			System.out.println("Sending mail");
			MailManager mm = new MailManager(email_from, name_from, 
					email_to, name_to, "Temperatures");

			mm.BuildMessage(coldest, hottest, all);
			mm.PrintMessage();
			mm.Send();

		}
		
		/*forward to JSP*/
		req.setAttribute("coldest", coldest);
		req.setAttribute("hottest", hottest);
		req.setAttribute("all", all);
		
		String destination = "/index.jsp";
		RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);

		try {
			rd.forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		}

	}
}
