package futuredata.search;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

import futuredata.config.HttpConfig;
import futuredata.config.HttpPara;

public class NegaSearcher {

    private static final Logger LOG = LoggerFactory.getLogger(NegaSearcher.class);

    public String search(HttpConfig httpConfig) {

        ProtocolSocketFactory factory = new DummySSLProtocolSocketFactory();
        Protocol https = new Protocol("https", factory, 443);
        Protocol.registerProtocol("https", https);
        try {
            HttpClient httpClient = new HttpClient();

            httpClient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
            httpClient.getParams().setParameter("http.useragent",
                    "Mozilla/5.0 (Windows NT 6.2; rv:18.0) Gecko/20100101 Firefox/18.0");

            PostMethod postMethod = new PostMethod(httpConfig.getHttpUrl());

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();

            List<HttpPara> httpParaList = httpConfig.getParaList();

            if (httpParaList != null && !httpParaList.isEmpty()) {

                for (HttpPara httpPara : httpParaList) {

                    pairs.add(new NameValuePair(httpPara.getParaKey(), httpPara.getParaValue()));
                }
                postMethod.setRequestBody(pairs.toArray(new NameValuePair[httpParaList.size()]));
            }

            httpClient.executeMethod(postMethod);

            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode != HttpStatus.SC_OK) {
                LOG.error("検索失敗: " + postMethod.getStatusLine());

                return "0";
            }
            InputStream in = postMethod.getResponseBodyAsStream();
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
