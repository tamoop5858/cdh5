import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class MyHttpClientGetPost {

	public static void main(String[] args) {
		//doGet();
		doPost();
	}
	
	public static void doGet() {
		String uriAPI = "http://www.mlit.go.jp/nega-inf/cgi-bin/fmenu.cgi?jigyoubunya=ikkyuu";
		HttpGet httpRequst = new HttpGet(uriAPI);
		try {
			// 使用DefaultHttpClient类的execute方法发送HTTP GET请求，并返回HttpResponse对象。
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				System.out.println(EntityUtils.toString(httpEntity, "Shift_JIS"));
			} else {
				System.out.println(httpResponse.getStatusLine().getStatusCode());
				
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpRequst.abort();
		}
	}

	public static void doPost() {
		String uriAPI = "http://www.mlit.go.jp/nega-inf/cgi-bin/fsearch.cgi";
		HttpPost httpRequst = new HttpPost(uriAPI);
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(); 
		BasicNameValuePair p1 = new BasicNameValuePair("jigyoubunya", "ikkyuu");
		params.add(p1);
		try {
			httpRequst.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);  
            if(httpResponse.getStatusLine().getStatusCode() == 200)  
            {  
                HttpEntity httpEntity = httpResponse.getEntity();  
                System.out.println(EntityUtils.toString(httpEntity, "Shift_JIS"));
            } else {
            	System.out.println(httpResponse.getStatusLine().getStatusCode());
            }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpRequst.abort();
		}
	}
}
