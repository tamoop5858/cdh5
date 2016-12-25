package com.apdplat.demo.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apdplat.search.DummySSLProtocolSocketFactory;
import org.apdplat.search.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NegaSearcher {
    private static final Logger LOG = LoggerFactory.getLogger(NegaSearcher.class);


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

            PostMethod postMethod = new PostMethod(url);
            NameValuePair jigyoubunya = new NameValuePair("jigyoubunya", "ikkyuu");
            NameValuePair[] pairs = {jigyoubunya};
            postMethod.setRequestBody(pairs);
            httpClient.executeMethod(postMethod);

            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode != HttpStatus.SC_OK) {
                LOG.error("検索失敗: " + postMethod.getStatusLine());
            }
            InputStream in = postMethod.getResponseBodyAsStream();
            byte[] responseBody = Tools.readAll(in);
            String response = new String(responseBody, "UTF-8");
            LOG.info("検索結果\n：" + response);


        } catch (IOException | NumberFormatException e) {
            LOG.error("実行失敗：", e);
        }
    }

    public static void main(String args[]) {

		String url = "http://www.mlit.go.jp/nega-inf/cgi-bin/fsearch.cgi";

        NegaSearcher searcher = new NegaSearcher();
        searcher.search(url);

    }
}
