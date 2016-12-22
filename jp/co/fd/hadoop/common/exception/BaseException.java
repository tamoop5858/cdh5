/**
 *
 */
package jp.co.fd.hadoop.common.exception;

/**
 * @author YIDATEC
 *
 */
public class BaseException extends Exception {

	private static final long serialVersionUID = -7657135644585032100L;

	public BaseException() {
		super();
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}
}
