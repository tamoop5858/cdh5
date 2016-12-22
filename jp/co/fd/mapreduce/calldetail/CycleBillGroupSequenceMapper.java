/**
 *
 */
package jp.co.fd.mapreduce.calldetail;


import jp.co.fd.hadoop.base.SequenceMapper;
import jp.co.fd.hadoop.bean.GeneralKeyBean;
import jp.co.fd.hadoop.bean.GeneralValueBean;
import jp.co.fd.hadoop.common.CommonConst;
import jp.co.fd.hadoop.common.exception.IllegalDataException;
import jp.co.fd.hadoop.common.exception.ProcessException;

import org.apache.hadoop.conf.Configuration;



/**
 * @author YIDATEC
 *
 */
public class CycleBillGroupSequenceMapper extends SequenceMapper {


	public CycleBillGroupSequenceMapper() {
		super(CallDetailControl.mapReduceId, CallDetailControl.mapReduceName);
	}
	@Override
    public void setupEx(Context context, Configuration conf) throws IllegalDataException, ProcessException {
	}
	@Override
	public boolean setBeanInfo(GeneralKeyBean key, GeneralValueBean value, Context context) throws IllegalDataException,
        ProcessException {
		key.setSortType(CommonConst.SORT_TYPE_CALL_DETAIL);
		return true;
	}
}
