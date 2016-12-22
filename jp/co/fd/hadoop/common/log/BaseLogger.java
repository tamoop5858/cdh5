/**
 *
 */
package jp.co.fd.hadoop.common.log;

import jp.co.fd.hadoop.common.CommonConst;
import jp.co.fd.hadoop.common.util.MessageUtil;

/**
 * @author YIDATEC
 *
 */
public class BaseLogger {


		private BaseLogger() {
			// なし
		}

		public static void printDebugMessage(String msgId) {
			// logger確認し、Debugメッセージ出力
			printDebugMessage(msgId, null);
		}

		public static void printDebugMessage(String msgId, Object[] param) {
			// logger確認し、Debugメッセージ出力
			if (CommonLogger.getInstance().canWrite()) {
				CommonLogger.getInstance().debug(MessageUtil.getMessage(msgId, param));

			} else {
				printMessage(msgId, param);
			}
		}

		public static void printInfoMessage(String msgId) {
			// logger確認し、Infoメッセージ出力
			printInfoMessage(msgId, null);
		}

		public static void printInfoMessage(String msgId, Object[] param) {
			// logger確認し、Infoメッセージ出力
			if (CommonLogger.getInstance().canWrite()) {
				CommonLogger.getInstance().info(MessageUtil.getMessage(msgId, param));

			} else {
				printMessage(msgId, param);
			}
		}

		public static void printWarnMessage(String msgId) {
			// logger確認し、Warnメッセージ出力
			printWarnMessage(msgId, null);
		}

		public static void printWarnMessage(String msgId, Object[] param) {
			// logger確認し、Warnメッセージ出力
			if (CommonLogger.getInstance().canWrite()) {
				CommonLogger.getInstance().warn(MessageUtil.getMessage(msgId, param));

			} else {
				printMessage(msgId, param);
			}
		}

		public static void printErrorMessage(String msgId) {
			printErrorMessage(msgId, null);
		}

		public static void printErrorMessage(String msgId, Object[] param) {
			// logger確認し、Errorメッセージ出力
			if (CommonLogger.getInstance().canWrite()) {
				CommonLogger.getInstance().error(MessageUtil.getMessage(msgId, param));

			} else { // loggerが指定されてない場合は、標準出力
				printMessage(msgId, param);
			}
		}

		public static void printMessage(String msgId) {
			printMessage(msgId, null);
		}

		public static void printMessage(String msgId, Object[] param) {
			printMessage(msgId, param, null);
		}

		public static void printMessage(String msgId, Object[] param, Throwable ex) {
			StringBuilder sb = new StringBuilder();
			sb.append(MessageUtil.getMessage(msgId, param));

			// 例外が指定されている場合
			if (ex != null) {
				sb.append(CommonConst.LINE_SEPARATER).append(getStackTrace(ex));
			}

			// 標準出力
			System.out.println(sb.toString());
		}

		public static void printMessage(Throwable ex) {
			// logger確認し、Errorメッセージ出力
			if (CommonLogger.getInstance().canWrite()) {
				CommonLogger.getInstance().error(getStackTrace(ex));

			} else { // loggerが指定されてない場合は、標準出力
				printMessage(getStackTrace(ex), null);
			}
		}

		public static String getStackTrace(Throwable ex) {
			// メッセージ用
			StringBuilder sb = new StringBuilder();

			// 例外NULL確認
			if (ex != null) {
				// メッセージ追加
				sb.append(ex.getMessage());
				// StackTrace情報の追加
				StackTraceElement[] stackTrace = ex.getStackTrace();
				for (StackTraceElement element : stackTrace) {
					sb.append(" at ");
					sb.append(element.getClassName());
					sb.append(".");
					sb.append(element.getMethodName());
					sb.append("(");
					sb.append(element.getFileName());
					sb.append(":");
					sb.append(element.getLineNumber());
					sb.append(")");
				}
			}
			// 作成した文字列返却
			return sb.toString();
		}
}
