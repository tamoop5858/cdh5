package com.futuredata.bigdata.gora.config;

public class FuturedataGoraConfig {

    private String outputFilepath;

    private String zookeeper;

    public String getOutputFilepath() {
	return outputFilepath;
    }

    public void setOutputFilepath(String outputFilepath) {
	this.outputFilepath = outputFilepath;
    }

    public String getZookeeper() {
	return zookeeper;
    }

    public void setZookeeper(String zookeeper) {
	this.zookeeper = zookeeper;
    }

}
