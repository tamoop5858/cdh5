package com.futuredata.bigdata.gora.service;

import org.apache.gora.tutorial.log.UrlInfoManager;

import com.futuredata.bigdata.gora.dao.UrlInfoDao;
import com.futuredata.bigdata.gora.dao.WebPageInfoDao;

public class WebPageService {

    private UrlInfoDao urlInfoDao = new UrlInfoDao();
    private WebPageInfoDao webPageInfoDao = new WebPageInfoDao();

    public void printCrawledWebPage() {

	// 1. read crawled urlInfo by urlInfoDao

	// 2. delete crawled urlInfo by urlInfoDao

	// 3. read crawled webpageinfo by webPageInfoDao

	// 4. write crawled webpageinfo by webPageInfoDao

    }

    private void readUrlInfo() {

    }

    private void deleteUrlInfo() {

    }

    private void readWebPageinfo() {

    }

    private void writeWebPageinfo() {

    }

    private void writePageContentToFile() {

    }

}
