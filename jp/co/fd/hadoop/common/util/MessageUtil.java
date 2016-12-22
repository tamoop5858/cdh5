package jp.co.fd.hadoop.common.util;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import jp.co.fd.hadoop.common.log.BaseLogger;

public class MessageUtil {
	private static final String MESSAGE_PROPERTIES = "sbb";
	private static Map<String, String> msgMap = new HashMap<String, String>();

	static {
		ResourceBundle resouce = ResourceBundle.getBundle(MESSAGE_PROPERTIES);
		Enumeration<String> keys = resouce.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			msgMap.put(key, resouce.getString(key));
		}
	}
	private MessageUtil(){

	}
	public static String getMessage(String msgId, Object[] param) {
		String msg = msgId;
		if (msgMap != null && msgMap.containsKey(msgId)){
			if (param != null) {
				msg = MessageFormat.format(msgMap.get(msgId), param);
			} else {
				msg = msgMap.get(msgId);
			}
		} else {
			if (param != null) {
				msg = MessageFormat.format(msgId, param);
			}
		}
		return msg;
	}

	public static String getMessage(String msgId) {
		return getMessage(msgId, null);
	}
	public static String getMessage(Exception ex) {
		return BaseLogger.getStackTrace(ex);
	}
}
