package com.futuredata.bigdata.gora.dao;

import java.io.IOException;

import org.apache.gora.store.DataStore;
import org.apache.gora.store.DataStoreFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.storage.UrlInfo;

public class UrlInfoDao {

    private DataStore<String, UrlInfo> UurlInfoDataStore;

    public UrlInfoDao() {
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

	ConnectionManager connectionManager = new ConnectionManager();
	conf.set("hbase.zookeeper.quorum", connectionManager.getHbaseZookeeperQuorum());

	UurlInfoDataStore = DataStoreFactory.getDataStore(String.class, UrlInfo.class, conf);
    }

    private void add(String key) {

    }

    private void delete(String key) {

    }

    private void update(String key) {

    }

    private void query(String key) {

    }

}
