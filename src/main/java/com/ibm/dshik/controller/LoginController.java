package com.ibm.dshik.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
    //@Context
    //private HttpServletRequest request;
    
	/**
	 * Simply selects the home view to render by returning its name.
	 * @RequestMapping(value = "/home", method = RequestMethod.GET)
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@Context HttpServletRequest request, @RequestBody String creds) {
		System.out.println("-> api/service/login api");
		HttpSession session =  request.getSession();
		
		logger.info("POST creds :" + creds);
		
		try {
			JSONObject credentials = new JSONObject(creds);
			String userID = credentials.getString("user_id");
			String password = credentials.getString("password");
			
			String user = (String)session.getAttribute(userID);
			System.out.println("session user : " + user + " of " + userID);
			
			if (userID.equals("admin") && password.equals("password")) {
				if(user == null) {
					session.setAttribute("admin", userID);
					return "Successful!!, you are now logged in as " + userID;
				} else {
					return "Already you are logged in as " + userID;
				}
			} else {
				return "Failed";
			}

		} catch (JSONException e) {

			e.getStackTrace();
			return "Failed";

		}
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout(@Context HttpServletRequest request, @RequestBody String creds) {
		System.out.println("-> api/service/logout api");
		HttpSession session =  request.getSession();
		
		try {
			JSONObject credentials = new JSONObject(creds);
			String userID = credentials.getString("user_id");
			String password = credentials.getString("password");
			
			String user = (String)session.getAttribute(userID);
			System.out.println("logout request session user is " + user + " of " + userID);
			
			if (user != null) {
				session.removeAttribute(user);
				return "Successful!!, you are now logged out as " + userID;
			} else {
				return "Failed, there is no session of " + userID;
			}

		} catch (JSONException e) {

			e.getStackTrace();
			return "Failed";

		}
	}
}
