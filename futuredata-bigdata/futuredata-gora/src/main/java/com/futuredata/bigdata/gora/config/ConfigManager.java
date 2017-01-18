package com.futuredata.bigdata.gora.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static ConfigManager instance = null;

    private static FuturedataGoraConfig config;

    public static synchronized ConfigManager getInstance() {

	if (instance == null) {

	    instance = new ConfigManager();
	}
	return instance;
    }

    public FuturedataGoraConfig getConfig() {

	if (config == null) {

	    config = getConfigFormXMLFile();
	}
	return config;
    }

    public FuturedataGoraConfig getConfigFormXMLFile() {

	Properties props = new Properties();

	try {
	    props.load(new FileInputStream("src/main/resources/futuredata-gora-config.properties"));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	String outputFilepath = props.getProperty("output.filepath");

	String zookeeper = props.getProperty("hbase.zookeeper.quorum");

	FuturedataGoraConfig futuredataGoraConfig = new FuturedataGoraConfig();

	futuredataGoraConfig.setOutputFilepath(outputFilepath);

	futuredataGoraConfig.setZookeeper(zookeeper);

	return futuredataGoraConfig;
    }

}
