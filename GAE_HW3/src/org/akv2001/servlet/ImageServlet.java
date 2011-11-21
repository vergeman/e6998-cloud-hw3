package org.akv2001.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;



import org.akv2001.datastore.MemCacheManager;
import org.akv2001.datastore.PersistManager;
import org.akv2001.datastore.QueryManager;
import org.akv2001.datastore.Woeid;
import org.akv2001.fetch.FetchRequest;
import org.akv2001.fetch.PrepareWoeid;
import org.akv2001.mail.MailManager;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;


@SuppressWarnings("serial")
public class ImageServlet extends HttpServlet {
	
	
	ArrayList<Woeid> coldest;
	ArrayList<Woeid> hottest;
	ArrayList<Woeid> all;
	

	HashMap<String, Woeid> woeid_map;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String key = req.getParameter("id");
		
		if (key != null) {
			System.out.println(key);

			PersistManager pm = new PersistManager();
			Woeid w = pm.get(key);

			if (w != null) {
				resp.setContentType("image/png");
				resp.getOutputStream().write(w.getImg().getBytes());
			}
		}
		

	}
}
