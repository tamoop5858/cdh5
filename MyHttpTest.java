import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

public class MyHttpTest {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws UnrecoverableKeyException
	 * @throws KeyManagementException
	 */
	@SuppressWarnings({ "deprecation", "resource" })
	public static void main(String[] args) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(ClientPNames.COOKIE_POLICY,
					CookiePolicy.BROWSER_COMPATIBILITY);
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
							"Mozilla/5.0 (Windows NT 6.2; rv:18.0) Gecko/20100101 Firefox/18.0");
			String PostFir = "https://disclosure.edinet-fsa.go.jp/E01EW/BLMainController.jsp?uji.verb=W1E63011CXW1E6A011DSPSch&uji.bean=ee.bean.parent.EECommonSearchBean&TID=W1E63011&PID=W1E63011&SESSIONKEY=1482035441088&lgKbn=2&pkbn=0&skbn=1&dskb=&askb=&dflg=0&iflg=&preId=1&mul=&fls=on&cal=1&era=H&yer=&mon=&pfs=4&row=100&idx=0&str=&kbn=1&flg=&syoruiKanriNo=";
			// 获得密匙库
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			FileInputStream instream = new FileInputStream(new File("d:\\edinet.keystore"));
			// 密匙库的密码
			trustStore.load(instream, "changeit".toCharArray());
			// 注册密匙库
			SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
			// 不校验域名
			socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme sch = new Scheme("https", 443, socketFactory);
			client.getConnectionManager().getSchemeRegistry().register(sch);
			HttpPost httppost1 = new HttpPost(PostFir);
			HttpResponse response1 = client.execute(httppost1);
			HttpEntity resEntity1 = response1.getEntity();
			System.out.println(EntityUtils.toString(resEntity1, "Shift_JIS"));
		} catch (Throwable tb) {
			tb.printStackTrace();
		}

	}
}
