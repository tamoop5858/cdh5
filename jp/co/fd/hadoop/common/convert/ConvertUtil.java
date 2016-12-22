/**
 *
 */
package jp.co.fd.hadoop.common.convert;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

import jp.co.fd.hadoop.common.CommonConst;

/**
 * @author YIDATEC
 *
 */
public class ConvertUtil {


	private static ThreadLocal<CharsetEncoder> encoderUTF8Factory =
			new ThreadLocal<CharsetEncoder>() {
		protected CharsetEncoder initialValue () {
			return Charset.forName(CommonConst.CHARSET_UTF8).newEncoder().onMalformedInput(CodingErrorAction.REPORT)
					.onUnmappableCharacter(CodingErrorAction.REPORT);
		}
	};
	private static ThreadLocal<CharsetEncoder> encoderSJISFactory =
			new ThreadLocal<CharsetEncoder>() {
		protected CharsetEncoder initialValue () {
			return Charset.forName(CommonConst.CHARSET_MS932).newEncoder().onMalformedInput(CodingErrorAction.REPORT)
					.onUnmappableCharacter(CodingErrorAction.REPORT);
		}
	};
	private ConvertUtil() {

	}
	/**
	 *
	 * @param str
	 * @return
	 * @throws CharacterCodingException
	 */
	public static byte[] encodeSJIS(String str) throws CharacterCodingException{
		CharsetEncoder encoder = encoderSJISFactory.get();
		encoder.onMalformedInput(CodingErrorAction.REPLACE);
		encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
		ByteBuffer bytes = encoder.encode(CharBuffer.wrap(str.toCharArray()));

		encoder.onMalformedInput(CodingErrorAction.REPORT);
		encoder.onUnmappableCharacter(CodingErrorAction.REPORT);

		byte[] endoded = new byte[bytes.limit()];
		System.arraycopy(bytes.array(), 0, endoded, 0, endoded.length);
		return endoded;
	}
	public static byte[] encodeUTF8(String str) throws CharacterCodingException{
		CharsetEncoder encoder = encoderUTF8Factory.get();

		encoder.onMalformedInput(CodingErrorAction.REPLACE);
		encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);

		ByteBuffer bytes = encoder.encode(CharBuffer.wrap(str.toCharArray()));

		encoder.onMalformedInput(CodingErrorAction.REPORT);
		encoder.onUnmappableCharacter(CodingErrorAction.REPORT);

		byte[] endoded = new byte[bytes.limit()];
		System.arraycopy(bytes.array(), 0, endoded, 0, endoded.length);
		return endoded;
	}
}
