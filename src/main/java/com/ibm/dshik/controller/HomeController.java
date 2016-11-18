package com.ibm.dshik.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ibm.dshik.vo.Book;

@RestController
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
//    @javax.ws.rs.core.Context
//    private HttpServletRequest request;
    
	/**
	 * Simply selects the home view to render by returning its name.
	 * @RequestMapping(value = "/home", method = RequestMethod.GET)
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String root() {
		logger.info("Welcome root REST Service !.");
		
		return "<H1>Spring Boot Application [/] from com.ibm.dshik </H1>";
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home() {
		logger.info("Welcome home REST Service !.");
		
		return "<H1>Spring Boot Application [home] from com.ibm.dshik </H1>";
	}
	
	@RequestMapping(value = "/booklist", method = RequestMethod.GET)
	public String booklist(@Context HttpServletRequest request, @PathParam("userId") String userId) {
		logger.info("Welcome booklist REST Service !.");
		
		logger.info("GET pathParam userId :" + userId);
		
		HttpSession session =  request.getSession();
		String user = (String)session.getAttribute(userId);
		System.out.println("session user : " + user + " of " + userId);
		if(user == null) {
			return "Failed, there is no session info";
		}
		
		String lookupName = null;
		String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
		if (VCAP_SERVICES != null) {
			try {
				JSONObject vcap = new JSONObject(System.getenv("VCAP_SERVICES"));
				Gson gson = new Gson();
				logger.info("VCAP_SERVICES [{}]", gson.toJson(vcap));
				//JSONObject credentials = (JSONObject)((JSONObject)((JSONArray)vcap.get("dashDB")).get(0)).get("credentials");
				if(vcap.get("dashDB") != null) {
					JSONObject dashDB0 = (JSONObject)((JSONArray)vcap.get("dashDB")).get(0);
					String luName = (String)dashDB0.getString("name");
					if(luName != null) {
						lookupName = "jdbc/"+luName;
					}
				}
				
				logger.info("VCAP_SERVICES lookupName [{}]", lookupName);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		DataSource dataSource  =  null ; 
		
		try  { 
			javax.naming.Context ctx = new InitialContext (); 
            if(lookupName != null) {
            	dataSource = (DataSource )ctx.lookup(lookupName); 
            } else {
            	dataSource = (DataSource )ctx.lookup( "jdbc/dashDB-gx" ); 
            }
            
        }  catch  (NamingException e)  { 
            e.printStackTrace (); 
        }

		List<Book> booklist = new ArrayList<Book>();
		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement("select TITLE, TYPE, PRICE, ISBN from BOOKLIST");
			ResultSet rs = stmt.executeQuery();
			
			
			while(rs.next()) {
				logger.info("book : {}, {}, {}, {}", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				Book book = new Book();
				book.setTitle(rs.getString(1));
				book.setType(rs.getString(2));
				book.setPrice(rs.getString(3));
				book.setIsbn(rs.getString(4));
				
				booklist.add(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Gson gson = new Gson();
		return gson.toJson(booklist);
		//return "<H1>Spring Boot Application [home] from com.ibm.dshik </H1>";
	}
}
