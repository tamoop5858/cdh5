package jp.co.fd.hadoop.common.util;

public class ArgUtil {
	public static String[] makeArgs(String... args){
		if (args == null) {
			return null;
		}
		String[] strList = new String[args.length];
		for (int i = 0; i < args.length; i++){
			strList[i] = args[i];
		}
		return strList;
	}
}
