package futuredata.search;

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

import futuredata.config.HttpConfig;

public class EdinetSearcher {

    private static final Logger LOG = LoggerFactory.getLogger(EdinetSearcher.class);

    public String search(HttpConfig httpConfig) {

        ProtocolSocketFactory factory = new DummySSLProtocolSocketFactory();
        Protocol https = new Protocol("https", factory, 443);
        Protocol.registerProtocol("https", https);
        try {
            HttpClient httpClient = new HttpClient();

            httpClient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
            httpClient.getParams().setParameter("http.useragent",
                    "Mozilla/5.0 (Windows NT 6.2; rv:18.0) Gecko/20100101 Firefox/18.0");

            GetMethod getMethod = new GetMethod(httpConfig.getHttpUrl());

            httpClient.executeMethod(getMethod);

            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                LOG.error("検索失敗: " + getMethod.getStatusLine());

                return "0";
            }
            InputStream in = getMethod.getResponseBodyAsStream();
            byte[] responseBody = Tools.readAll(in);
            String response = new String(responseBody, "UTF-8");
            LOG.info("検索結果\n：" + response);

            return response;

        } catch (IOException | NumberFormatException e) {
            LOG.error("実行失敗：", e);

            return "0";
        }
    }
}
