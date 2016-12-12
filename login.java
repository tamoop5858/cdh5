Index: Http.java
===================================================================
--- Http.java   (revision 1482896)
+++ Http.java   (working copy)
@@ -17,25 +17,19 @@
 package org.apache.nutch.protocol.httpclient;
  
 // JDK imports
+import java.io.IOException;
 import java.io.InputStream;
-import java.io.IOException;
 import java.net.URL;
 import java.util.ArrayList;
 import java.util.Collection;
+import java.util.HashMap;
 import java.util.HashSet;
+import java.util.Map;
+import java.util.Set;
  
 import javax.xml.parsers.DocumentBuilderFactory;
 import javax.xml.parsers.ParserConfigurationException;
-import org.xml.sax.SAXException;
-import org.w3c.dom.Document;
-import org.w3c.dom.Element;
-import org.w3c.dom.NodeList;
-import org.w3c.dom.Node;
  
-// Commons Logging imports
-import org.slf4j.Logger;
-import org.slf4j.LoggerFactory;
-
 // HTTP Client imports
 import org.apache.commons.httpclient.Header;
 import org.apache.commons.httpclient.HostConfiguration;
@@ -45,15 +39,23 @@
 import org.apache.commons.httpclient.auth.AuthScope;
 import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
 import org.apache.commons.httpclient.protocol.Protocol;
-
+import org.apache.commons.lang.StringUtils;
+import org.apache.hadoop.conf.Configuration;
+import org.apache.nutch.net.protocols.Response;
+import org.apache.nutch.protocol.ProtocolException;
+import org.apache.nutch.protocol.http.api.HttpBase;
 // Nutch imports
 import org.apache.nutch.storage.WebPage;
 import org.apache.nutch.storage.WebPage.Field;
-import org.apache.nutch.net.protocols.Response;
-import org.apache.nutch.protocol.ProtocolException;
-import org.apache.nutch.protocol.http.api.HttpBase;
-import org.apache.hadoop.conf.Configuration;
 import org.apache.nutch.util.NutchConfiguration;
+// Commons Logging imports
+import org.slf4j.Logger;
+import org.slf4j.LoggerFactory;
+import org.w3c.dom.Document;
+import org.w3c.dom.Element;
+import org.w3c.dom.Node;
+import org.w3c.dom.NodeList;
+import org.xml.sax.SAXException;
  
 /**
  * This class is a protocol plugin that configures an HTTP client for Basic,
@@ -89,6 +91,7 @@
  
    private static final Collection<WebPage.Field> FIELDS = new HashSet<WebPage.Field>();
  
+   private static HttpFormAuthConfigurer formConfigurer;
    static {
        FIELDS.add(WebPage.Field.MODIFIED_TIME);
        FIELDS.add(WebPage.Field.HEADERS);
@@ -98,7 +101,7 @@
    public Collection<Field> getFields() {
        return FIELDS;
    }
-   
+
    /**
     * Returns the configured HTTP client.
     * 
@@ -244,6 +247,11 @@
     * @throws IOException
     *             If any I/O error occurs.
     */
+   /**
+    * @throws ParserConfigurationException
+    * @throws SAXException
+    * @throws IOException
+    */
    private static synchronized void setCredentials()
            throws ParserConfigurationException, SAXException, IOException {
  
@@ -282,6 +290,13 @@
                    continue;
                }
  
+               String authMethod = credElement.getAttribute("authMethod");
+               // read http form post auth info
+               if (StringUtils.isNotBlank(authMethod)) {
+                   formConfigurer = readFormAuthConfigurer(credElement,
+                           authMethod);
+                   continue;
+               }
                String username = credElement.getAttribute("username");
                String password = credElement.getAttribute("password");
  
@@ -356,6 +371,95 @@
    }
  
    /**
+    * <auth-configuration> <credentials authMethod="formAuth"
+    * loginUrl="loginUrl" loginFormId="loginFormId" loginRedirect="true">
+    * <loginPostData> <field name="username" value="user1"/> </loginPostData>
+    * <additionalPostHeaders> <field name="header1" value="vaule1"/>
+    * </additionalPostHeaders> <removedFormFields> <field name="header1"/>
+    * </removedFormFields> </credentials> </auth-configuration>
+    */
+   private static HttpFormAuthConfigurer readFormAuthConfigurer(
+           Element credElement, String authMethod) {
+       if ("formAuth".equals(authMethod)) {
+           HttpFormAuthConfigurer formConfigurer = new HttpFormAuthConfigurer();
+
+           String str = credElement.getAttribute("loginUrl");
+           if (StringUtils.isNotBlank(str)) {
+               formConfigurer.setLoginUrl(str.trim());
+           } else {
+               throw new IllegalArgumentException("Must set loginUrl.");
+           }
+           str = credElement.getAttribute("loginFormId");
+           if (StringUtils.isNotBlank(str)) {
+               formConfigurer.setLoginFormId(str.trim());
+           } else {
+               throw new IllegalArgumentException("Must set loginFormId.");
+           }
+           str = credElement.getAttribute("loginRedirect");
+           if (StringUtils.isNotBlank(str)) {
+               formConfigurer.setLoginRedirect(Boolean.parseBoolean(str));
+           }
+
+           NodeList nodeList = credElement.getChildNodes();
+           for (int j = 0; j < nodeList.getLength(); j++) {
+               Node node = nodeList.item(j);
+               if (!(node instanceof Element))
+                   continue;
+
+               Element element = (Element) node;
+               if ("loginPostData".equals(element.getTagName())) {
+                   Map<String, String> loginPostData = new HashMap<String, String>();
+                   NodeList childNodes = element.getChildNodes();
+                   for (int k = 0; k < childNodes.getLength(); k++) {
+                       Node fieldNode = childNodes.item(k);
+                       if (!(fieldNode instanceof Element))
+                           continue;
+
+                       Element fieldElement = (Element) fieldNode;
+                       String name = fieldElement.getAttribute("name");
+                       String value = fieldElement.getAttribute("value");
+                       loginPostData.put(name, value);
+                   }
+                   formConfigurer.setLoginPostData(loginPostData);
+               } else if ("additionalPostHeaders".equals(element.getTagName())) {
+                   Map<String, String> additionalPostHeaders = new HashMap<String, String>();
+                   NodeList childNodes = element.getChildNodes();
+                   for (int k = 0; k < childNodes.getLength(); k++) {
+                       Node fieldNode = childNodes.item(k);
+                       if (!(fieldNode instanceof Element))
+                           continue;
+
+                       Element fieldElement = (Element) fieldNode;
+                       String name = fieldElement.getAttribute("name");
+                       String value = fieldElement.getAttribute("value");
+                       additionalPostHeaders.put(name, value);
+                   }
+                   formConfigurer
+                           .setAdditionalPostHeaders(additionalPostHeaders);
+               } else if ("removedFormFields".equals(element.getTagName())) {
+                   Set<String> removedFormFields = new HashSet<String>();
+                   NodeList childNodes = element.getChildNodes();
+                   for (int k = 0; k < childNodes.getLength(); k++) {
+                       Node fieldNode = childNodes.item(k);
+                       if (!(fieldNode instanceof Element))
+                           continue;
+
+                       Element fieldElement = (Element) fieldNode;
+                       String name = fieldElement.getAttribute("name");
+                       removedFormFields.add(name);
+                   }
+                   formConfigurer.setRemovedFormFields(removedFormFields);
+               }
+           }
+
+           return formConfigurer;
+       } else {
+           throw new IllegalArgumentException("Unsupported authMethod: "
+                   + authMethod);
+       }
+   }
+
+   /**
     * If credentials for the authentication scope determined from the specified
     * <code>url</code> is not already set in the HTTP client, then this method
     * sets the default credentials to fetch the specified <code>url</code>. If
@@ -367,6 +471,17 @@
     */
    private void resolveCredentials(URL url) {
  
+       if (formConfigurer != null) {
+           HttpFormAuthentication formAuther = new HttpFormAuthentication(
+                   formConfigurer, client, this);
+           try {
+               formAuther.login();
+           } catch (Exception e) {
+               throw new RuntimeException(e);
+           }
+
+           return;
+       }
        if (defaultUsername != null && defaultUsername.length() > 0) {
  
            int port = url.getPort();
Index: HttpFormAuthConfigurer.java
===================================================================
--- HttpFormAuthConfigurer.java (revision 0)
+++ HttpFormAuthConfigurer.java (working copy)
@@ -0,0 +1,87 @@
+package org.apache.nutch.protocol.httpclient;
+
+import java.util.HashMap;
+import java.util.HashSet;
+import java.util.Map;
+import java.util.Set;
+
+public class HttpFormAuthConfigurer {
+   private String loginUrl;
+   private String loginFormId;
+   /**
+    * The data posted to login form, such as username(or email), password
+    */
+   private Map<String, String> loginPostData;
+   /**
+    * In case we need add additional headers.
+    */
+   private Map<String, String> additionalPostHeaders;
+   private boolean loginRedirect;
+   /**
+    * Used when we need remove some form fields.
+    */
+   private Set<String> removedFormFields;
+
+   public HttpFormAuthConfigurer() {
+   }
+
+   public String getLoginUrl() {
+       return loginUrl;
+   }
+
+   public HttpFormAuthConfigurer setLoginUrl(String loginUrl) {
+       this.loginUrl = loginUrl;
+       return this;
+   }
+
+   public String getLoginFormId() {
+       return loginFormId;
+   }
+
+   public HttpFormAuthConfigurer setLoginFormId(String loginForm) {
+       this.loginFormId = loginForm;
+       return this;
+   }
+
+   public Map<String, String> getLoginPostData() {
+       return loginPostData == null ? new HashMap<String, String>()
+               : loginPostData;
+   }
+
+   public HttpFormAuthConfigurer setLoginPostData(
+           Map<String, String> loginPostData) {
+       this.loginPostData = loginPostData;
+       return this;
+   }
+
+   public Map<String, String> getAdditionalPostHeaders() {
+       return additionalPostHeaders == null ? new HashMap<String, String>()
+               : additionalPostHeaders;
+   }
+
+   public HttpFormAuthConfigurer setAdditionalPostHeaders(
+           Map<String, String> additionalPostHeaders) {
+       this.additionalPostHeaders = additionalPostHeaders;
+       return this;
+   }
+
+   public boolean isLoginRedirect() {
+       return loginRedirect;
+   }
+
+   public HttpFormAuthConfigurer setLoginRedirect(boolean redirect) {
+       this.loginRedirect = redirect;
+       return this;
+   }
+
+   public Set<String> getRemovedFormFields() {
+       return removedFormFields == null ? new HashSet<String>()
+               : removedFormFields;
+   }
+
+   public HttpFormAuthConfigurer setRemovedFormFields(
+           Set<String> removedFormFields) {
+       this.removedFormFields = removedFormFields;
+       return this;
+   }
+}
\ No newline at end of file
Index: HttpFormAuthentication.java
===================================================================
--- HttpFormAuthentication.java (revision 0)
+++ HttpFormAuthentication.java (working copy)
@@ -0,0 +1,200 @@
+package org.apache.nutch.protocol.httpclient;
+
+import java.io.IOException;
+import java.io.UnsupportedEncodingException;
+import java.net.CookieHandler;
+import java.net.CookieManager;
+import java.util.ArrayList;
+import java.util.HashMap;
+import java.util.HashSet;
+import java.util.List;
+import java.util.Map;
+import java.util.Map.Entry;
+import java.util.Set;
+
+import org.apache.commons.httpclient.Header;
+import org.apache.commons.httpclient.HttpClient;
+import org.apache.commons.httpclient.NameValuePair;
+import org.apache.commons.httpclient.methods.GetMethod;
+import org.apache.commons.httpclient.methods.PostMethod;
+import org.apache.commons.io.IOUtils;
+import org.jsoup.Jsoup;
+import org.jsoup.nodes.Document;
+import org.jsoup.nodes.Element;
+import org.jsoup.select.Elements;
+import org.slf4j.Logger;
+import org.slf4j.LoggerFactory;
+
+public class HttpFormAuthentication {
+   private static final Logger LOGGER = LoggerFactory
+           .getLogger(HttpFormAuthentication.class);
+   private static Map<String, String> defaultLoginHeaders = new HashMap<String, String>();
+
+   static {
+       defaultLoginHeaders.put("User-Agent", "Mozilla/5.0");
+       defaultLoginHeaders
+               .put("Accept",
+                       "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
+       defaultLoginHeaders.put("Accept-Language", "en-US,en;q=0.5");
+       defaultLoginHeaders.put("Connection", "keep-alive");
+       defaultLoginHeaders.put("Content-Type",
+               "application/x-www-form-urlencoded");
+   }
+
+   private HttpClient client;
+   private HttpFormAuthConfigurer authConfigurer = new HttpFormAuthConfigurer();
+   private String cookies;
+
+   public HttpFormAuthentication(HttpFormAuthConfigurer authConfigurer,
+           HttpClient client, Http http) {
+       this.authConfigurer = authConfigurer;
+       this.client = client;
+       defaultLoginHeaders.put("Accept", http.getAccept());
+       defaultLoginHeaders.put("Accept-Language", http.getAcceptLanguage());
+       defaultLoginHeaders.put("User-Agent", http.getUserAgent());
+   }
+
+   public HttpFormAuthentication(String loginUrl, String loginForm,
+           Map<String, String> loginPostData,
+           Map<String, String> additionalPostHeaders,
+           Set<String> removedFormFields) {
+       this.authConfigurer.setLoginUrl(loginUrl);
+       this.authConfigurer.setLoginFormId(loginForm);
+       this.authConfigurer
+               .setLoginPostData(loginPostData == null ? new HashMap<String, String>()
+                       : loginPostData);
+       this.authConfigurer
+               .setAdditionalPostHeaders(additionalPostHeaders == null ? new HashMap<String, String>()
+                       : additionalPostHeaders);
+       this.authConfigurer
+               .setRemovedFormFields(removedFormFields == null ? new HashSet<String>()
+                       : removedFormFields);
+       this.client = new HttpClient();
+   }
+
+   public void login() throws Exception {
+       // make sure cookies is turn on
+       CookieHandler.setDefault(new CookieManager());
+       String pageContent = httpGetPageContent(authConfigurer.getLoginUrl());
+       List<NameValuePair> params = getLoginFormParams(pageContent);
+       sendPost(authConfigurer.getLoginUrl(), params);
+   }
+
+   private void sendPost(String url, List<NameValuePair> params)
+           throws Exception {
+       PostMethod post = null;
+       try {
+           if (authConfigurer.isLoginRedirect()) {
+               post = new PostMethod(url) {
+                   @Override
+                   public boolean getFollowRedirects() {
+                       return true;
+                   }
+               };
+           } else {
+               post = new PostMethod(url);
+           }
+           // we can't use post.setFollowRedirects(true) as it will throw
+           // IllegalArgumentException:
+           // Entity enclosing requests cannot be redirected without user
+           // intervention
+           setLoginHeader(post);
+           post.addParameters(params.toArray(new NameValuePair[0]));
+           int rspCode = client.executeMethod(post);
+           if (LOGGER.isDebugEnabled()) {
+               LOGGER.debug("rspCode: " + rspCode);
+               LOGGER.debug("\nSending 'POST' request to URL : " + url);
+
+               LOGGER.debug("Post parameters : " + params);
+               LOGGER.debug("Response Code : " + rspCode);
+               for (Header header : post.getRequestHeaders()) {
+                   LOGGER.debug("Response headers : " + header);
+               }
+           }
+           String rst = IOUtils.toString(post.getResponseBodyAsStream());
+           LOGGER.debug("login post result: " + rst);
+       } finally {
+           if (post != null) {
+               post.releaseConnection();
+           }
+       }
+   }
+
+   private void setLoginHeader(PostMethod post) {
+       Map<String, String> headers = new HashMap<String, String>();
+       headers.putAll(defaultLoginHeaders);
+       // additionalPostHeaders can overwrite value in defaultLoginHeaders
+       headers.putAll(authConfigurer.getAdditionalPostHeaders());
+       for (Entry<String, String> entry : headers.entrySet()) {
+           post.addRequestHeader(entry.getKey(), entry.getValue());
+       }
+       post.addRequestHeader("Cookie", getCookies());
+   }
+
+   private String httpGetPageContent(String url) throws IOException {
+
+       GetMethod get = new GetMethod(url);
+       try {
+           for (Entry<String, String> entry : authConfigurer
+                   .getAdditionalPostHeaders().entrySet()) {
+               get.addRequestHeader(entry.getKey(), entry.getValue());
+           }
+           client.executeMethod(get);
+           Header cookieHeader = get.getResponseHeader("Set-Cookie");
+           if (cookieHeader != null) {
+               setCookies(cookieHeader.getValue());
+           }
+           String rst = IOUtils.toString(get.getResponseBodyAsStream());
+           return rst;
+       } finally {
+           get.releaseConnection();
+       }
+
+   }
+
+   private List<NameValuePair> getLoginFormParams(String pageContent)
+           throws UnsupportedEncodingException {
+       List<NameValuePair> params = new ArrayList<NameValuePair>();
+       Document doc = Jsoup.parse(pageContent);
+       Element loginform = doc.getElementById(authConfigurer.getLoginFormId());
+       if (loginform == null) {
+           throw new IllegalArgumentException("No form exists: "
+                   + authConfigurer.getLoginFormId());
+       }
+       Elements inputElements = loginform.getElementsByTag("input");
+       // skip fields in removedFormFields or loginPostData
+       for (Element inputElement : inputElements) {
+           String key = inputElement.attr("name");
+           String value = inputElement.attr("value");
+           if (authConfigurer.getLoginPostData().containsKey(key)
+                   || authConfigurer.getRemovedFormFields().contains(key)) {
+               // value = loginPostData.get(key);
+               continue;
+           }
+           params.add(new NameValuePair(key, value));
+       }
+       // add key and value in loginPostData
+       for (Entry<String, String> entry : authConfigurer.getLoginPostData()
+               .entrySet()) {
+           params.add(new NameValuePair(entry.getKey(), entry.getValue()));
+       }
+       return params;
+   }
+
+   public String getCookies() {
+       return cookies;
+   }
+
+   public void setCookies(String cookies) {
+       this.cookies = cookies;
+   }
+
+   public boolean isRedirect() {
+       return authConfigurer.isLoginRedirect();
+   }
+
+   public void setRedirect(boolean redirect) {
+       this.authConfigurer.setLoginRedirect(redirect);
+   }
+
+}


********************************************************************************************************************************************************************************

Index: conf/nutch-default.xml
===================================================================
--- conf/nutch-default.xml  (revision 919153)
+++ conf/nutch-default.xml  (working copy)
@@ -128,6 +128,15 @@
 </property>
  
 <property>
+  <name>http.cookie.login.page</name>
+  <value></value>
+  <description>URL of the login page to derive the cookies from. Cookies
+  will be stored upon initialization and re-initialized upon expiration.
+  Any URL request attributes will be sent to POSTed to the page.
+  NOTE: This currently only works for protocol-httpclient.</description>
+</property>
+
+<property>
   <name>http.timeout</name>
   <value>10000</value>
   <description>The default network timeout, in milliseconds.</description>
Index: src/plugin/lib-http/src/java/org/apache/nutch/protocol/http/api/HttpBase.java
===================================================================
--- src/plugin/lib-http/src/java/org/apache/nutch/protocol/http/api/HttpBase.java   (revision 919153)
+++ src/plugin/lib-http/src/java/org/apache/nutch/protocol/http/api/HttpBase.java   (working copy)
@@ -127,7 +127,10 @@
   
   /** Do we use HTTP/1.1? */
   protected boolean useHttp11 = false;
-  
+
+  /** URL of the login page to derive cookies from. */
+  protected String cookieLoginPage = null; 
+
   /** Skip page if Crawl-Delay longer than this value. */
   protected long maxCrawlDelay = -1L;
  
@@ -166,6 +169,7 @@
         this.maxCrawlDelay = (long)(conf.getInt("fetcher.max.crawl.delay", -1) * 1000);
         // backward-compatible default setting
         this.byIP = conf.getBoolean("fetcher.threads.per.host.by.ip", true);
+        this.cookieLoginPage = conf.get("http.cookie.login.page");
         this.useHttp11 = conf.getBoolean("http.useHttp11", false);
         this.robots.setConf(conf);
         this.checkBlocking = conf.getBoolean(Protocol.CHECK_BLOCKING, true);
@@ -329,7 +333,11 @@
   public boolean getUseHttp11() {
     return useHttp11;
   }
-  
+
+  public String getCookieLoginPage() {
+    return cookieLoginPage;
+  }
+
   private String blockAddr(URL url, long crawlDelay) throws ProtocolException {
      
     String host;
Index: src/plugin/protocol-httpclient/src/java/org/apache/nutch/protocol/httpclient/Http.java
===================================================================
--- src/plugin/protocol-httpclient/src/java/org/apache/nutch/protocol/httpclient/Http.java  (revision 919153)
+++ src/plugin/protocol-httpclient/src/java/org/apache/nutch/protocol/httpclient/Http.java  (working copy)
@@ -19,6 +19,7 @@
 // JDK imports
 import java.io.InputStream;
 import java.io.IOException;
+import java.net.MalformedURLException;
 import java.net.URL;
 import java.util.ArrayList;
 import javax.xml.parsers.DocumentBuilderFactory;
@@ -150,7 +151,7 @@
    */
   protected Response getResponse(URL url, CrawlDatum datum, boolean redirect)
     throws ProtocolException, IOException {
-    resolveCredentials(url);
+    resolveCredentials(url, redirect);
     return new HttpResponse(this, url, datum, redirect);
   }
  
@@ -342,9 +343,11 @@
    * client.
    *
    * @param url URL to be fetched
+   * @param redirect Follow redirects if and only if true
+   * @throws IOException 
+   * @throws MalformedURLException 
    */
-  private void resolveCredentials(URL url) {
-
+  private void resolveCredentials(URL url, boolean redirect) throws MalformedURLException, IOException {
     if (defaultUsername != null && defaultUsername.length() > 0) {
  
       int port = url.getPort();
@@ -363,6 +366,10 @@
               + url.getHost() + "; port: " + port
               + "; found for url: " + url);
  
+        // (Re-) initialize any cookie-based authorization
+
+        resolveCookieCredentials(redirect);
+
         // Credentials are already configured, so do nothing and return
         return;
       }
@@ -382,7 +389,30 @@
       client.getState().setCredentials(
           serverAuthScope, serverCredentials);
     }
+
+    // (Re-) initialize any cookie-based authorization
+
+    resolveCookieCredentials(redirect);
   }
+  
+  /**
+   * (Re-)initializes the cookies if a login page was given.
+   * 
+   * XXX: Note that we also choose whether to redirect for login pages
+   * 
+   * @param redirect Follow redirects if and only if true
+   * @throws IOException 
+   * @throws MalformedURLException 
+   */
+  private void resolveCookieCredentials(boolean redirect) throws MalformedURLException, IOException {
+    if (cookieLoginPage != null) {
+      try {
+          new HttpCookieAuthentication(this, new URL(cookieLoginPage), redirect);
+      } catch (Exception e) {
+          LOG.error("Cookie-based authentication failed; cookies will not be present for this request but an attempt to retrieve them will be made for the next one.");
+      }
+    }
+  }
  
   /**
    * Returns an authentication scope for the specified
Index: src/plugin/protocol-httpclient/src/java/org/apache/nutch/protocol/httpclient/HttpCookieAuthentication.java
===================================================================
--- src/plugin/protocol-httpclient/src/java/org/apache/nutch/protocol/httpclient/HttpCookieAuthentication.java  (revision 0)
+++ src/plugin/protocol-httpclient/src/java/org/apache/nutch/protocol/httpclient/HttpCookieAuthentication.java  (revision 0)
@@ -0,0 +1,105 @@
+/**
+ * Licensed to the Apache Software Foundation (ASF) under one or more
+ * contributor license agreements.  See the NOTICE file distributed with
+ * this work for additional information regarding copyright ownership.
+ * The ASF licenses this file to You under the Apache License, Version 2.0
+ * (the "License"); you may not use this file except in compliance with
+ * the License.  You may obtain a copy of the License at
+ *
+ *     http://www.apache.org/licenses/LICENSE-2.0
+ *
+ * Unless required by applicable law or agreed to in writing, software
+ * distributed under the License is distributed on an "AS IS" BASIS,
+ * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
+ * See the License for the specific language governing permissions and
+ * limitations under the License.
+ */
+package org.apache.nutch.protocol.httpclient;
+
+import java.io.IOException;
+import java.net.URL;
+
+import org.apache.commons.httpclient.Cookie;
+import org.apache.commons.httpclient.HttpVersion;
+import org.apache.commons.httpclient.cookie.CookiePolicy;
+import org.apache.commons.httpclient.methods.PostMethod;
+import org.apache.commons.httpclient.params.HttpMethodParams;
+
+/**
+ * An implementation of authentication through cookies. Currently
+ * performs a POST on the given URL, adding any resulting cookies
+ * to the client.
+ * 
+ * Cookies must be checked against their expiration dates with each
+ * request to ensure they haven't expired.
+ *
+ * @author Jasper van Veghel <jasper@seajas.com>
+ */
+public class HttpCookieAuthentication  {
+  /**
+   * POSTs to the given <code>url</code> and stores the resulting cookies.
+   *
+   * @param http                An instance of the implementation class
+   *                            of this plugin
+   * @param url                 URL to be fetched
+   * @param followRedirects     Whether to follow redirects; follows
+   *                            redirect if and only if this is true
+   * @return                    HTTP cookie authentication
+   * @throws IOException        When an error occurs
+   */
+  HttpCookieAuthentication(Http http, URL url, boolean followRedirects) throws IOException {
+    // Add cookies if we haven't retrieved any yet or if they've expired
+
+    if (Http.getClient().getState() != null && Http.getClient().getState().getCookies().length > 0) {
+      boolean expired = false;
+
+      for (Cookie cookie : Http.getClient().getState().getCookies()) {
+        if (cookie.isExpired()) {
+          expired = true;
+      
+          break;
+        }
+      }
+
+      // We have cookies and none of them have expired yet; don't get new ones
+
+      if (!expired)
+        return;
+    }
+
+    // Prepare POST method for HTTP request
+
+    PostMethod method = new PostMethod(url.toString());
+    method.setFollowRedirects(followRedirects);
+    method.setDoAuthentication(true);
+
+    // Set HTTP parameters
+
+    HttpMethodParams parameters = method.getParams();
+
+    if (http.getUseHttp11()) {
+      parameters.setVersion(HttpVersion.HTTP_1_1);
+    } else {
+      parameters.setVersion(HttpVersion.HTTP_1_0);
+    }
+
+    parameters.makeLenient();
+    parameters.setContentCharset("UTF-8");
+    parameters.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
+    parameters.setBooleanParameter(HttpMethodParams.SINGLE_COOKIE_HEADER, true);
+
+    try {
+      int code = Http.getClient().executeMethod(method);
+
+      if (code == 200 && Http.LOG.isTraceEnabled()) {
+        Http.LOG.trace("url: " + url +
+            "; status code: " + code +
+            "; cookies received: " + Http.getClient().getState().getCookies().length);
+      } else {
+          Http.LOG.error("Unable to retrieve login page; code = " + code);
+      }
+    } finally {
+      method.releaseConnection();
+    }
+  }
+}
\ No newline at end of file

+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++