/**
 *
 */
package jp.co.fd.hadoop.partitioner;

import jp.co.fd.hadoop.bean.GeneralKeyBean;
import jp.co.fd.hadoop.bean.GeneralValueBean;

import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @author YIDATEC
 *
 */
public class BillGroupIdPartitioner extends Partitioner<GeneralKeyBean, GeneralValueBean>{

	public BillGroupIdPartitioner() {

	}

	@Override
	public int getPartition(GeneralKeyBean key, GeneralValueBean value, int numPartitions) {
		if (numPartitions <= 1) {
			return 0;
		}
		int hashCode = 0;
		int partition = 0;
		hashCode = key.getBillGroupId().hashCode();
		partition = hashCode % numPartitions;

		return partition;
	}

}
