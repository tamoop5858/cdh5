package jp.co.fd.hadoop.common.util;

import java.text.SimpleDateFormat;

public class DateUtil {
	public static final ThreadLocal<SimpleDateFormat> DATE_FORMAT= new ThreadLocal<SimpleDateFormat>() {
		public SimpleDateFormat initialValue() {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.setLenient(false);
			return sdf;
		}

	};
}
