package com.futuredata.bigdata.gora.dao;

import com.futuredata.bigdata.gora.config.ConfigManager;

public class ConnectionManager {

    private String hbaseZookeeperQuorum;

    public ConnectionManager() {

	hbaseZookeeperQuorum = ConfigManager.getInstance().getConfigFormXMLFile().getZookeeper();
    }

    public String getHbaseZookeeperQuorum() {
	return hbaseZookeeperQuorum;
    }

    public void setHbaseZookeeperQuorum(String hbaseZookeeperQuorum) {
	this.hbaseZookeeperQuorum = hbaseZookeeperQuorum;
    }

}
