/**
 *
 */
package jp.co.fd.hadoop.comparator;

import jp.co.fd.hadoop.bean.GeneralKeyBean;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * @author YIDATEC
 *
 */
public class BillGroupIdComparator extends WritableComparator {

	public BillGroupIdComparator() {
		super(GeneralKeyBean.class, true);
	}
	@SuppressWarnings("unchecked")
    @Override
	public int compare(WritableComparable w1, WritableComparable w2){
    	GeneralKeyBean key1 = (GeneralKeyBean) w1;
    	GeneralKeyBean key2 = (GeneralKeyBean) w2;
    	int result = 0;
    	result = key1.getBillGroupId().compareTo(key2.getBillGroupId());
    	return result;
	}
}
