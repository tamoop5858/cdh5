/**
 *
 */
package jp.co.fd.hadoop.common.exception;

/**
 * @author YIDATEC
 *
 */
public class IllegalDataException extends AppException {


	private static final long serialVersionUID = -9047451761895908960L;

	public IllegalDataException() {
		super();
	}

	public IllegalDataException(String message, String[] logArgs) {
		super(message, logArgs);
	}

	public IllegalDataException(Throwable cause, String message, String[] logArgs) {
		super(cause, message, logArgs);
	}

	public IllegalDataException(Throwable cause) {
		super(cause);
	}
}
