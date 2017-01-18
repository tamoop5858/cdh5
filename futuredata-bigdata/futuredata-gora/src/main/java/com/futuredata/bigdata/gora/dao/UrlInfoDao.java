package com.futuredata.bigdata.gora.dao;

import java.io.IOException;

import org.apache.gora.query.Query;
import org.apache.gora.query.Result;
import org.apache.gora.store.DataStore;
import org.apache.gora.store.DataStoreFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.storage.UrlInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlInfoDao {

    private static final Logger log = LoggerFactory.getLogger(UrlInfoDao.class);

    private DataStore<String, UrlInfo> urlInfoDataStore;

    public UrlInfoDao() {
	
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

	urlInfoDataStore = DataStoreFactory.getDataStore(String.class, UrlInfo.class, conf);
    }

    public void add(String key) {

    }

    public void deleteByKey(String key) throws Exception {

	urlInfoDataStore.delete(key);

	log.info("UrlInfo with key:" + key + " deleted");

	urlInfoDataStore.flush();
    }

    public void deleteAll() throws Exception {

	Query<String, UrlInfo> query = urlInfoDataStore.newQuery();
	Result<String, UrlInfo> result = query.execute();

	while (result.next()) {

	    String resultKey = result.getKey();
	    urlInfoDataStore.delete(resultKey);

	    log.info("UrlInfo with key:" + resultKey + " deleted");
	}

	urlInfoDataStore.flush();
    }

    public void update(String key) {

    }

    public UrlInfo queryByKey(String key) {

	return urlInfoDataStore.get(key);
    }

    public Result<String, UrlInfo> queryALl() {

	Query<String, UrlInfo> query = urlInfoDataStore.newQuery();

	Result<String, UrlInfo> result = query.execute();

	return result;
    }

}
