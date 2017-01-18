package com.futuredata.bigdata.gora.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.futuredata.bigdata.gora.config.ConfigManager;
import com.futuredata.bigdata.gora.service.WebPageService;

public class WebPageAction {

    private static final Logger log = LoggerFactory.getLogger(WebPageAction.class);
    
    public void printCrawledWebPage() {

	WebPageService service = new WebPageService();

	try {
	    
	    service.printCrawledWebPage();
	} catch (Exception e) {
	    
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) throws Exception {

	ConfigManager.getInstance().getConfigFormXMLFile();
	
	WebPageAction action = new WebPageAction();

	action.printCrawledWebPage();
    }
}
