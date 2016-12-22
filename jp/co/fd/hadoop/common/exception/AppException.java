/**
 *
 */
package jp.co.fd.hadoop.common.exception;

/**
 * @author YIDATEC
 *
 */
public class AppException extends BaseException {


	private static final long serialVersionUID = -6065922276406886346L;
	private String message = null;
	private String[] logArgs = null;



	public AppException() {
		super();
	}
	public AppException(Throwable cause) {
		super(cause);
	}

	public AppException(String message, String[] logArgs) {
		super();
		this.setMessage(message);
		this.setLogArgs(logArgs);
	}

	public AppException(Throwable cause, String message, String[] logArgs) {
		super(cause);
		this.setMessage(message);
		this.setLogArgs(logArgs);
	}
	public final String createErrorMessage(String mapReduceId, String mapReduceName) {
		StringBuffer sb = new StringBuffer();
		sb.append(message);
		sb.append(".");
		sb.append(mapReduceId);
		sb.append(".");
		sb.append(mapReduceName);
		sb.append(".");

		if (logArgs != null) {
			for (String log : logArgs) {
				sb.append(".");
				sb.append(log);
			}
		}
		String result = sb.toString();
		sb = null;

		return result;
	}
	public void setLogArgs(String[] logArgs) {
		this.logArgs = logArgs;
	}
	public String[] getLogArgs() {
		return logArgs;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}


}
