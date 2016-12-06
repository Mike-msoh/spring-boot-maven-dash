package com.ibm.dshik.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ibm.json.java.JSONObject;

@RestController
public class HelloController {
	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
	private Gson gson;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @RequestMapping(value = "/home", method = RequestMethod.GET)
	 */
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String home() {
		logger.info("Welcome hello REST Service !.");
		gson = new Gson();
		
		JSONObject vcapApp = getVcapApp();
		if(vcapApp != null)
			logger.info("VCAP_APPLICATION [{}]", gson.toJson(vcapApp));
		else 
			logger.info("VCAP_APPLICATION [{}]", "NULL");
		return "<H1>Spring Boot Application [hello] from com.ibm.dshik </H1>";
	}
	
	private JSONObject getVcapApp() {
		String vcap = System.getenv("VCAP_APPLICATION");
		if (vcap == null) return null;
		JSONObject vcapObject = null;
		try {
			vcapObject = JSONObject.parse(vcap);
		} catch (IOException e) {
			String message = "Error parsing VCAP_APPLICATION: ";
			//logger.log(Level.SEVERE, message + e.getMessage(), e);
			logger.info("{}", message + e.getMessage(), e);
		}
		return vcapObject;
	}
}
