package org.apache.gora.tutorial.log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import org.apache.avro.util.Utf8;
import org.apache.gora.query.Query;
import org.apache.gora.query.Result;
import org.apache.gora.store.DataStore;
import org.apache.gora.store.DataStoreFactory;
import org.apache.gora.tutorial.log.generated.Pageview;
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.storage.UrlInfo;
import org.apache.nutch.storage.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LogManager is the tutorial class to illustrate the basic {@link DataStore}
 * API usage. The LogManager class is used to parse the web server logs in
 * combined log format, store the data in a Gora compatible data store, query
 * and manipulate the stored data.
 *
 * <p>
 * In the data model, keys are the line numbers in the log file, and the values
 * are Pageview objects, generated from
 * <code>gora-tutorial/src/main/avro/pageview.json</code>.
 *
 * <p>
 * See the tutorial.html file in docs or go to the <a
 * href="http://gora.apache.org/docs/current/tutorial.html"> web site</a>for
 * more information.
 * </p>
 */
public class UrlInfoManager {

	private static final Logger log = LoggerFactory
			.getLogger(UrlInfoManager.class);

	private DataStore<Long, Pageview> dataStore;
	private DataStore<String, UrlInfo> dataStoreUrlInfo;
	private DataStore<String, WebPage> dataStoreWebPage;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MMM/yyyy:HH:mm:ss Z");

	public UrlInfoManager() {
		try {
			init();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private void init() throws IOException {
		// Data store objects are created from a factory. It is necessary to
		// provide the key and value class. The datastore class is optional,
		// and if not specified it will be read from the properties file
		Configuration conf = new Configuration();
		// conf.set("hbase.zookeeper.quorum","192.168.3.53:2181");
		dataStore = DataStoreFactory.getDataStore(Long.class, Pageview.class,
				conf);
		dataStoreUrlInfo = DataStoreFactory.getDataStore(String.class,
				UrlInfo.class, conf);
		dataStoreWebPage = DataStoreFactory.getDataStore(String.class,
				WebPage.class, conf);
	}

	/**
	 * Parses a log file and store the contents at the data store.
	 *
	 * @param input
	 *            the input file location
	 */
	private void parse(String input) throws IOException, ParseException,
			Exception {
		log.info("Parsing file:" + input);
		BufferedReader reader = new BufferedReader(new FileReader(input));
		long lineCount = 0;
		try {
			String line = reader.readLine();
			do {
				Pageview pageview = parseLine(line);

				if (pageview != null) {
					// store the pageview
					storePageview(lineCount++, pageview);
				}

				line = reader.readLine();
			} while (line != null);

		} finally {
			reader.close();
		}
		log.info("finished parsing file. Total number of log lines:"
				+ lineCount);
	}

	/** Parses a single log line in combined log format using StringTokenizers */
	private Pageview parseLine(String line) throws ParseException {
		StringTokenizer matcher = new StringTokenizer(line);
		// parse the log line
		String ip = matcher.nextToken();
		matcher.nextToken(); // discard
		matcher.nextToken();
		long timestamp = dateFormat.parse(matcher.nextToken("]").substring(2))
				.getTime();
		matcher.nextToken("\"");
		String request = matcher.nextToken("\"");
		String[] requestParts = request.split(" ");
		String httpMethod = requestParts[0];
		String url = requestParts[1];
		matcher.nextToken(" ");
		int httpStatusCode = Integer.parseInt(matcher.nextToken());
		int responseSize = Integer.parseInt(matcher.nextToken());
		matcher.nextToken("\"");
		String referrer = matcher.nextToken("\"");
		matcher.nextToken("\"");
		String userAgent = matcher.nextToken("\"");

		// construct and return pageview object
		Pageview pageview = new Pageview();
		pageview.setIp(new Utf8(ip));
		pageview.setTimestamp(timestamp);
		pageview.setHttpMethod(new Utf8(httpMethod));
		pageview.setUrl(new Utf8(url));
		pageview.setHttpStatusCode(httpStatusCode);
		pageview.setResponseSize(responseSize);
		pageview.setReferrer(new Utf8(referrer));
		pageview.setUserAgent(new Utf8(userAgent));

		return pageview;
	}

	/** Stores the pageview object with the given key */
	private void storePageview(long key, Pageview pageview) throws IOException,
			Exception {
		log.info("Storing Pageview in: " + dataStore.toString());
		dataStore.put(key, pageview);
	}

	/** Fetches a single pageview object and prints it */
	private void get(String key) throws IOException, Exception {
		WebPage webpage;
		if (key.equals("all")) {
			Query<String, UrlInfo> query = dataStoreUrlInfo.newQuery();
			Result<String, UrlInfo> result = query.execute();
			while (result.next()) { // advances the Result object and breaks if
									// at end
				String resultKey = result.getKey(); // obtain current key
				webpage = dataStoreWebPage.get(resultKey);
				printContent(resultKey, webpage);
			}
		} else {
			webpage = dataStoreWebPage.get(key);
			printContent(key, webpage);
		}
	}

	/** Queries and prints a single pageview object */
	private void query(String key) throws IOException, Exception {
		// Queries are constructed from the data store
		Query<String, UrlInfo> query = dataStoreUrlInfo.newQuery();
		if (!key.equals("all")) {
			query.setKey(key);
		}

		Result<String, UrlInfo> result = query.execute(); // Actually executes
															// the query.
		// alternatively dataStore.execute(query); can be used

		printResult(result);
	}

	/** Deletes the pageview with the given line number */
	private void delete(String key) throws Exception {
		if (key.equals("all")) {
			Query<String, UrlInfo> query = dataStoreUrlInfo.newQuery();
			Result<String, UrlInfo> result = query.execute();
			while (result.next()) { // advances the Result object and breaks if
									// at end
				String resultKey = result.getKey(); // obtain current key
				dataStoreUrlInfo.delete(resultKey);
				log.info("pageview with key:" + resultKey + " deleted");
			}
		} else {
			dataStoreUrlInfo.delete(key);
			log.info("pageview with key:" + key + " deleted");
		}
		dataStore.flush(); // write changes may need to be flushed before
							// they are committed

	}

	private void printResult(Result<String, UrlInfo> result)
			throws IOException, Exception {

		while (result.next()) { // advances the Result object and breaks if at
								// end
			String resultKey = result.getKey(); // obtain current key
			UrlInfo resultUrlInfo = result.get(); // obtain current value object

			// print the results
			System.out.println(resultKey + ":");
			printUrlInfo(resultUrlInfo);
		}

		System.out.println("Number of UrlInfo from the query:"
				+ result.getOffset());
	}

	/** Pretty prints the pageview object to stdout */
	private void printUrlInfo(UrlInfo urlinfo) {
		if (urlinfo == null) {
			System.out.println("No result to show");
		} else {
			System.out.println(urlinfo.toString());
		}
	}

	private void printContent(String key, WebPage webpage) {
		if (webpage == null) {
			System.out.println("No result to show");
		} else {
			ByteBuffer contentByteBuffer = webpage.getContent();
			if (contentByteBuffer == null){
				System.out.println("Content IS null");
			}else{
				System.out.println(new String(contentByteBuffer.array()));
			}
		}
	}

	private void close() throws IOException, Exception {
		// It is very important to close the datastore properly, otherwise
		// some data loss might occur.
		if (dataStore != null)
			dataStore.close();

		if (dataStoreUrlInfo != null)
			dataStoreUrlInfo.close();

		if (dataStoreWebPage != null)
			dataStoreWebPage.close();
	}

	private static final String USAGE = "LogManager -parse <input_log_file>\n"
			+ "           -get <lineNum>\n" + "           -query <lineNum>\n"
			+ "           -query <startLineNum> <endLineNum>\n"
			+ "           -delete <lineNum>\n"
			+ "           -deleteByQuery <startLineNum> <endLineNum>\n";

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println(USAGE);
			System.exit(1);
		}

		UrlInfoManager manager = new UrlInfoManager();

		if ("-parse".equals(args[0])) {
			manager.parse(args[1]);
		} else if ("-get".equals(args[0])) {
			manager.get(args[1]);
		} else if ("-query".equals(args[0])) {
			if (args.length == 2)
				manager.query(args[1]);
		} else if ("-delete".equals(args[0])) {
			manager.delete(args[1]);
		} else {
			System.err.println(USAGE);
			System.exit(1);
		}

		manager.close();
	}

}