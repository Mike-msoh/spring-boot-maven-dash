package com.ibm.dshik.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @RequestMapping(value = "/home", method = RequestMethod.GET)
	 */
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String home() {
		logger.info("Welcome hello REST Service !.");
		
		return "<H1>Spring Boot Application [hello] from com.ibm.dshik </H1>";
	}
}
