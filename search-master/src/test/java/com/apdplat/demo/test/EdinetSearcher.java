package com.apdplat.demo.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apdplat.search.DummySSLProtocolSocketFactory;
import org.apdplat.search.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EdinetSearcher {
    private static final Logger LOG = LoggerFactory.getLogger(EdinetSearcher.class);


    public void search(String url) {

    	ProtocolSocketFactory factory = new DummySSLProtocolSocketFactory();
    	Protocol https = new Protocol("https",factory,443);
    	Protocol.registerProtocol("https", https);
        try {
            HttpClient httpClient = new HttpClient();

            httpClient.getParams().setParameter("http.protocol.cookie-policy",
            		"compatibility");
            httpClient.getParams().setParameter( "http.useragent",
					"Mozilla/5.0 (Windows NT 6.2; rv:18.0) Gecko/20100101 Firefox/18.0");

            GetMethod getMethod = new GetMethod(url);

            httpClient.executeMethod(getMethod);

            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                LOG.error("検索失敗: " + getMethod.getStatusLine());
            }
            InputStream in = getMethod.getResponseBodyAsStream();
            byte[] responseBody = Tools.readAll(in);
            String response = new String(responseBody, "UTF-8");
            LOG.info("検索結果\n：" + response);


        } catch (IOException | NumberFormatException e) {
            LOG.error("実行失敗：", e);
        }
    }

    public static void main(String args[]) {

		String url = "https://disclosure.edinet-fsa.go.jp/E01EW/BLMainController.jsp?uji.verb=W1E63011CXW1E6A011DSPSch&uji.bean=ee.bean.parent.EECommonSearchBean&TID=W1E63011&PID=W1E63011&SESSIONKEY=1482035441088&lgKbn=2&pkbn=0&skbn=1&dskb=&askb=&dflg=0&iflg=&preId=1&mul=&fls=on&cal=1&era=H&yer=&mon=&pfs=4&row=100&idx=0&str=&kbn=1&flg=&syoruiKanriNo=";
		 url = "https://www.baidu.com";
		
        EdinetSearcher searcher = new EdinetSearcher();
        searcher.search(url);

    }
}
