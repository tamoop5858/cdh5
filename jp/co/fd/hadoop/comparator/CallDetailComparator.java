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
public class CallDetailComparator extends WritableComparator {

	public CallDetailComparator() {
		super(GeneralKeyBean.class, true);
	}
	@SuppressWarnings("unchecked")
    @Override
	public int compare(WritableComparable w1, WritableComparable w2){

    	GeneralKeyBean key1 = (GeneralKeyBean) w1;
    	GeneralKeyBean key2 = (GeneralKeyBean) w2;

    	int result = 0;
    	result = key1.getBillGroupId().compareTo(key2.getBillGroupId());
    	if (result == 0) {
    		result = key1.getBillMonth().compareTo(key2.getBillMonth());
    	}
    	if (result == 0) {
    		result = key1.getBillCycleId().compareTo(key2.getBillCycleId());
    	}
    	System.out.println("####" + key1.getBeanId() + ":" + key2.getBeanId() + "####" + key1.getBillGroupId() + ":" + key2.getBillGroupId() +
    			"####" + key1.getBillMonth() + ":" + key2.getBillMonth() + "###" + key1.getBillCycleId() + ":" + key2.getBillCycleId());
    	return result;
	}
}
