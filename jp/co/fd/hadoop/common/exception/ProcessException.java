/**
 *
 */
package jp.co.fd.hadoop.common.exception;

/**
 * @author YIDATEC
 *
 */
public class ProcessException extends AppException {

	private static final long serialVersionUID = 6531173299704328466L;

	public ProcessException() {
		super();
	}

	public ProcessException(String message, String[] logArgs) {
		super(message, logArgs);
	}

	public ProcessException(Throwable cause, String message, String[] logArgs) {
		super(cause, message, logArgs);
	}

	public ProcessException(Throwable cause) {
		super(cause);
	}
}
