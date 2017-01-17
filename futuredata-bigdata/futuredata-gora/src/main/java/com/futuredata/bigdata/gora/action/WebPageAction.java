package com.futuredata.bigdata.gora.action;

import com.futuredata.bigdata.gora.service.WebPageService;

public class WebPageAction {

    private void printCrawledWebPage() {

	WebPageService service = new WebPageService();

	service.printCrawledWebPage();

    }

    public static void main(String[] args) throws Exception {

	WebPageAction action = new WebPageAction();

	action.printCrawledWebPage();
    }
}
