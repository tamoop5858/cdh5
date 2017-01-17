package com.futuredata.bigdata.gora.output;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.futuredata.bigdata.gora.config.ConfigManager;
import com.futuredata.bigdata.gora.config.FuturedataGoraConfig;

public class OutputFileManager {

    public String getOutputFilePath(String defaultFileName) {

	FuturedataGoraConfig config = ConfigManager.getInstance().getConfig();

	String fileName = getFileNameByRule(defaultFileName);

	return config.getOutputFilepath() + "\\" + fileName;
    }

    private String getFileNameByRule(String name) {

	if (name == null || name == "") {

	    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	    name = df.format(new Date());
	}

	return name;
    }

}
