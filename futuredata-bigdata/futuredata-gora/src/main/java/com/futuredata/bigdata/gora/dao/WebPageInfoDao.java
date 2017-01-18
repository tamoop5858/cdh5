package com.futuredata.bigdata.gora.dao;

import java.io.IOException;

import org.apache.gora.query.Query;
import org.apache.gora.query.Result;
import org.apache.gora.store.DataStore;
import org.apache.gora.store.DataStoreFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.storage.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebPageInfoDao {

    private static final Logger log = LoggerFactory.getLogger(WebPageInfoDao.class);
    
    private DataStore<String, WebPage> webPageDataStore;

    public WebPageInfoDao() {
	
	try {
	    
	    init();
	} catch (IOException ex) {
	    
	    throw new RuntimeException(ex);
	}
    }

    private void init() throws IOException {

	Configuration conf = new Configuration();

	ConnectionManager connectionManager = new ConnectionManager();
	conf.set("hbase.zookeeper.quorum", connectionManager.getHbaseZookeeperQuorum());

	webPageDataStore = DataStoreFactory.getDataStore(String.class, WebPage.class, conf);
    }

    public void add(String key) {

    }

    public void delete(String key) {

    }

    public void update(String key) {

    }

    public WebPage queryByKey(String key) {

	return webPageDataStore.get(key);
    }
    
    public Result<String, WebPage> queryAll() {

	Query<String, WebPage> query = webPageDataStore.newQuery();
	
	Result<String, WebPage> result = query.execute();
	
	return result;
    }

}
