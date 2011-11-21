package org.akv2001.fetch;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.akv2001.datastore.Woeid;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
 * XML SAX parser based off of 
 * http://totheriver.com/learn/xml/xmltutorial.html#6.1.2
 */

public class ParseXML extends DefaultHandler {
	
	SAXParserFactory spf;
	SAXParser sp;
	String doc;
	Woeid woeid;
	
	boolean first_forecast = true;
	boolean second_description = false;
	
	Pattern p_img = Pattern.compile("<img src=\"(.*)\"");
	Matcher m;
	
	String tempVal;
	
	public ParseXML(String doc, Woeid w) {
		this.spf = SAXParserFactory.newInstance();
		
		this.doc = doc;
		this.woeid = w;
		
		try {
			sp = spf.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void parse() {

			//parse the doc 
			try {
				
				InputSource iSource = new InputSource(new StringReader(doc));
				iSource.setEncoding("UTF-8");
				
				sp.parse(iSource, this);
				
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}


	}
	
	/*callbacks*/
	
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		tempVal = "";

		try {
			if (qName.equalsIgnoreCase("yweather:location")) {
				woeid.setCity(attributes.getValue("city"));
				woeid.setRegion(attributes.getValue("region"));
				woeid.setCountry(attributes.getValue("country"));
			}

			if (qName.equalsIgnoreCase("yweather:atmosphere")) {
				woeid.setHumidity(Double.parseDouble(attributes
						.getValue("humidity")));
				woeid.setVisibility(Double.parseDouble(attributes
						.getValue("visibility")));
			}

			if (qName.equalsIgnoreCase("yweather:astronomy")) {
				woeid.setSunrise(attributes.getValue("sunrise"));
				woeid.setSunset(attributes.getValue("sunset"));
			}

			if (qName.equalsIgnoreCase("yweather:condition")) {
				woeid.setCurr_text(attributes.getValue("text"));
				woeid.setTemp(Double.parseDouble(attributes.getValue("temp")));
				woeid.setDate(attributes.getValue("date"));
			}

			if (qName.equalsIgnoreCase("yweather:forecast") && first_forecast) {

				woeid.setLow(Double.parseDouble(attributes.getValue("low")));
				woeid.setHigh(Double.parseDouble(attributes.getValue("high")));
				woeid.setForecast_text(attributes.getValue("text"));

				first_forecast = false;
			}
		} catch (NumberFormatException n) {
			System.out.println("empty string ");
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName,
			String qName) throws SAXException {

		/* to get img data*/
			if(qName.equalsIgnoreCase("description") ) {

				if (!second_description) {
					second_description = true;
					return;
				}
				
				m = p_img.matcher(tempVal);
				
				if (m.find()) {
					woeid.setImg_url(m.group(1));
				}
				
			}
			
		/*title data*/
			if(qName.equalsIgnoreCase("title") ) {
				woeid.setTitle(tempVal);
			}

		}



	public static void main(String args[]) {
		String test = "<img src=\"http://l.yimg.com/a/i/us/we/52/33.gif\"/><br />adfadfasd";
		System.out.println(test);
		Pattern p_img = Pattern.compile("<img src=\"(.*)\"");
		Matcher m;

		m = p_img.matcher(test);
		m.find();
		System.out.println("count: " + m.groupCount());
		System.out.println(m.group(1));

	}

}
