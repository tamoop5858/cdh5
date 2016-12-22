/**
 *
 */
package jp.co.fd.mapreduce.calldetail;

import java.io.IOException;

import jp.co.fd.hadoop.base.BaseReducer;
import jp.co.fd.hadoop.bean.GeneralKeyBean;
import jp.co.fd.hadoop.bean.GeneralValueBean;
import jp.co.fd.hadoop.common.exception.IllegalDataException;
import jp.co.fd.hadoop.common.exception.ProcessException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;


/**
 * @author YIDATEC
 *
 */
public class BmReferenceReducer extends BaseReducer<GeneralKeyBean, GeneralValueBean> {

	private final String referencePrefix = "rfc";
	private Text outputText = new Text();
	public BmReferenceReducer() {
		super(CallDetailControl.mapReduceId, CallDetailControl.mapReduceName);

	}
	@Override
	public void setupEx(Context context, Configuration conf)
			throws IOException, InterruptedException, ProcessException,
			IllegalDataException {

		System.out.println("setupEx===========reducer===========");
	}


	@Override
	public void reduceEx(GeneralKeyBean key, Iterable<GeneralValueBean> values, Context context)
			throws IOException, InterruptedException, ProcessException,
			IllegalDataException {
        // ���̓��R�[�h�̏���
        for (GeneralValueBean generalValueBean : values) {
        	String value = generalValueBean.getBmReferenceValueBean().getParameter_value_desc();
//        	outputText.set(value);
//        	multiOutputs.write(referencePrefix, null, outputText, referencePrefix);
        	multiOutputs.write(referencePrefix, key, generalValueBean, referencePrefix);
        }

	}

	@Override
	public void cleanupEx(Context context, Configuration conf)
			throws ProcessException, IllegalDataException {

		System.out.println("cleanupEx===========reducer===========");
	}

}
