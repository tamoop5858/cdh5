package jp.co.fd.hadoop.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import jp.co.fd.hadoop.common.exception.IllegalDataException;
import jp.co.fd.hadoop.common.exception.ProcessException;


public class PropertyUtil {
	private static PropertyUtil pdasg = null;
	private static Properties property = new Properties();

	private PropertyUtil(){

	}
	public static final synchronized PropertyUtil getInstance() throws ProcessException {
		if (pdasg == null) {
			pdasg = new PropertyUtil();
			pdasg.readPropertyFile();
		}
		return pdasg;
	}
	private final void readPropertyFile() throws ProcessException {
		String pathFileName = System.getProperty("sbtm.propertyFile");
		FileInputStream fs = null;
		try {
			
			fs = new FileInputStream(pathFileName);
			property.load(fs);
		} catch (FileNotFoundException e) {
			ProcessException processException = new ProcessException(e);
			throw processException;
		} catch (IOException e) {
			ProcessException processException = new ProcessException(e);
			throw processException;
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					ProcessException processException = new ProcessException(e);
					throw processException;
				} finally {
					fs = null;
				}
			}
		}
	}
	public final String getStringProperty(String key) throws IllegalDataException{
		String value = "";
		if (key != null) {
			value = property.getProperty(key);
			if (value == null) {
				throw new IllegalDataException("�Y���f�[�^�����݂��Ȃ�", null);
			}
		}
		return value;
	}
	public final boolean getBooleanProperty(String key) throws IllegalDataException{
		String value = getStringProperty(key);
		value = value.toLowerCase().trim();
		if (!(value.equals("true") || value.equals("false"))){
			throw new IllegalDataException("�Y���f�[�^��boolean�ɕϊ��ł��܂���", null);
		}
		return Boolean.parseBoolean(value);
	}
	public final int getIntProperty(String key) throws IllegalDataException{
		String value = getStringProperty(key);
		value = value.trim();
		int returnValue = Integer.parseInt(value);
		return returnValue;
	}
}
