package com.futuredata.bigdata.gora.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;

import org.apache.gora.query.Result;
import org.apache.nutch.storage.UrlInfo;
import org.apache.nutch.storage.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.futuredata.bigdata.gora.dao.UrlInfoDao;
import com.futuredata.bigdata.gora.dao.WebPageInfoDao;
import com.futuredata.bigdata.gora.output.OutputFileManager;

public class WebPageService {

    private static final Logger log = LoggerFactory.getLogger(WebPageService.class);
    
    private UrlInfoDao urlInfoDao = new UrlInfoDao();
    private WebPageInfoDao webPageInfoDao = new WebPageInfoDao();

    public void printCrawledWebPage() throws Exception {

	// 1. read crawled urlInfo by urlInfoDao
	Result<String, UrlInfo> urlInfoResult = urlInfoDao.queryALl();

	while (urlInfoResult.next()) {

	    String urlInfoKey = urlInfoResult.getKey();
	    UrlInfo urlInfo = urlInfoResult.get();

	    // 2. read crawled webpageinfo by webPageInfoDao
	    // TODO
	    String webPageInfoKey = urlInfoKey;

	    WebPage webPage = webPageInfoDao.queryByKey(webPageInfoKey);

	    // 3. write crawled webpageinfo by webPageInfoDao
	    writeWebPageToFile(webPageInfoKey, webPage);

	    // 4. delete crawled urlInfo by urlInfoDao
	    urlInfoDao.deleteByKey(urlInfoKey);
	}
    }

    private void writeWebPageToFile(String webPageKey, WebPage webPage) {

	OutputFileManager outputFileManager = new OutputFileManager();
	
	String filePath = outputFileManager.getOutputFilePath(webPageKey + ".txt");
	
	ByteBuffer contentByteBuffer = webPage.getContent();
	
	File file = new File(filePath);

        try (PrintStream ps = new PrintStream(new FileOutputStream(file))) {

            ps.append(new String(contentByteBuffer.array()));

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
    }
}
