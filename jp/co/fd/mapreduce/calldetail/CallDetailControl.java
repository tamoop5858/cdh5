/**
 *
 */
package jp.co.fd.mapreduce.calldetail;



import java.util.Map;

import jp.co.fd.hadoop.base.BaseControl;
import jp.co.fd.hadoop.base.BaseControlUtility;
import jp.co.fd.hadoop.bean.GeneralKeyBean;
import jp.co.fd.hadoop.bean.GeneralValueBean;
import jp.co.fd.hadoop.common.CommonConst;
import jp.co.fd.hadoop.common.exception.IllegalDataException;
import jp.co.fd.hadoop.common.exception.ProcessException;
import jp.co.fd.hadoop.common.util.PropertyUtil;
import jp.co.fd.hadoop.common.util.SequenceFileOutputFormatExt;
import jp.co.fd.hadoop.common.util.TextFileOutputFormatExt;
import jp.co.fd.hadoop.comparator.BillGroupIdComparator;
import jp.co.fd.hadoop.comparator.CallDetailComparator;
import jp.co.fd.hadoop.partitioner.BillGroupIdPartitioner;
import jp.co.fd.hadoop.partitioner.CallDetailPartitioner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;





/**
 * @author YIDATEC
 *
 */
public class CallDetailControl extends BaseControl{

	public static final String mapReduceId = "Calldetail";
	public static final String mapReduceName = "Calldetail";
	private static final int ARGS_COUNT = 2;
	private String callDelailId = null;
	private String jobName = null;

	private String workPath = null;
	private String outputPath = null;
	private String outTemCallDetailPath = null;
	private String outTempPath = null;
	private String outTempPathReference = null;

	private static final String dataInfoPath = "/hdfs/data/sbtm/current/database_info/";
	private static final String referencePath = "/hdfs/data/sbtm/current/reference/";
	private static final String cycleBillGroupPrefix = "cbg";
	private static final String referencePrefix = "rfc";
	private static final String summarizeBillGroupPrefix = "sbg";
	private static final String cdrPrefix = "cdr";

	private static final String innerPrefix = "inner";
	private static final String outerPrefix = "outer";

	String treatmentModePara = null;

	/**
	 *
	 * @param mapReduceId
	 * @param mapReduceName
	 */
	public CallDetailControl(String mapReduceId, String mapReduceName) {
		super(mapReduceId, mapReduceName);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		CallDetailControl control = new CallDetailControl(mapReduceId, mapReduceName);
		int status = control.executeMapReduce(args, control);
		System.exit(status);
	}
	/**
	 *
	 */
	@Override
	public boolean preProcessEx(String[] args, Configuration conf) throws IllegalDataException, ProcessException {

		if (args.length != ARGS_COUNT) {
			ProcessException exception = new ProcessException();
			exception.setMessage("�p�����[�^�̐����s���ł��B");
			BaseControlUtility.outputConsoleErrorLog(mapReduceId, mapReduceName, exception);
		}
		callDelailId = args[1];

		StringBuffer sb = new StringBuffer();
		sb.append(CommonConst.JOB_NAME_CALL_DETAIL);
		sb.append(callDelailId);
		jobName = sb.toString();

		checkAndKillJob(jobName);
		workPath = cleanAndFixWorkDir(mapReduceId, callDelailId);
		outputPath = createOutputDir(workPath);

		outTemCallDetailPath = createOutputDirName(workPath);
		outTempPath = createOutTempDirName(workPath);
		outTempPathReference = createOutTempDirNameReference(workPath);
		conf.set(CommonConst.OUT_TEMP_PATH_REFERENCE, outTempPathReference);

		Map<String, String> xmlMap = getXmlInfo(conf, "/hdfs/data/sbtm/current/xml/bm14t010.xml");
		String billMonthPara = xmlMap.get("billMonth");
		String billCycleIDPara = xmlMap.get("billCycleID");
		treatmentModePara = xmlMap.get("treatmentMode");
		String treatmentSerialNumberPara = xmlMap.get("treatmentSerialNumber");
		conf.set(CommonConst.PRAA_CALLDETAIL_BILL_MONTH, billMonthPara);
		conf.set(CommonConst.PRAA_CALLDETAIL_BILL_CYCLE_ID, billCycleIDPara);
		conf.set(CommonConst.PRAA_CALLDETAIL_TREATMENT_MODE, treatmentModePara);
		conf.set(CommonConst.PRAA_TREATMENT_SERIAL_NUMBER, treatmentSerialNumberPara);
		super.setExecCount(3);

		return true;
	}
	@Override
	public Job configJob1st(Configuration conf, String[] args) throws IllegalDataException, ProcessException {

		Job job = createNewJob(conf);
		setMapReduceNum(job, conf, mapReduceId, 1);
		job.setJarByClass(CallDetailControl.class);
		job.setJobName(jobName);
		addMultiInputFile(job, referencePath, null, TextInputFormat.class, BmReferenceMapper.class);

		job.setMapOutputKeyClass(GeneralKeyBean.class);
		job.setMapOutputValueClass(GeneralValueBean.class);

		conf.setBoolean(CommonConst.MAPRED_CONFIG_HASH_PARTIION, false);

		job.setReducerClass(BmReferenceReducer.class);

		job.setOutputKeyClass(GeneralKeyBean.class);
		job.setOutputValueClass(GeneralValueBean.class);
		conf.set(CommonConst.TEXTOUTPUT_NEWLINE, CommonConst.NEWLINE_CRLF);

		addMultiOutputPath(job, referencePrefix, SequenceFileOutputFormatExt.class, GeneralKeyBean.class,
				GeneralValueBean.class);

		setReducerOutputPath(job, outTempPathReference);


		String queueName = PropertyUtil.getInstance().getStringProperty(getKeyMapReduce(CommonConst.JOB_QUEUE_NAME, mapReduceId, 1));
//		setQueueName(job, queueName);

		super.compress = PropertyUtil.getInstance().getBooleanProperty(CommonConst.KEY_COMPRESS_DEFAULT);
		conf.setBoolean(CommonConst.MAPRED_OUTPUT_COMPRESS_EXT, PropertyUtil.getInstance().getBooleanProperty(CommonConst.KEY_COMPRESS_SEQEXT));
		super.useGzipCodec = PropertyUtil.getInstance().getBooleanProperty(CommonConst.KEY_COMPRESS_USEGZIP);
		return job;

	}
	@Override
	public boolean postprocess1st(Job job, Configuration conf)
			throws IllegalDataException, ProcessException {
		return true;
	}
	@Override
	public Job configJob2nd(Configuration conf, String[] args) throws IllegalDataException, ProcessException {
		Job job = createNewJob(conf);
		setMapReduceNum(job, conf, mapReduceId, 2);
		job.setJarByClass(CallDetailControl.class);
		job.setJobName(jobName);
		addMultiInputFile(job, dataInfoPath, cycleBillGroupPrefix, TextInputFormat.class, CycleBillGroupMapper.class);
		if (!treatmentModePara.equals("6")) {
			addMultiInputFile(job, dataInfoPath, summarizeBillGroupPrefix, TextInputFormat.class,
					SummarizeBillGroupMapper.class);
		}

		job.setMapOutputKeyClass(GeneralKeyBean.class);
		job.setMapOutputValueClass(GeneralValueBean.class);

		job.setPartitionerClass(BillGroupIdPartitioner.class);
		job.setGroupingComparatorClass(BillGroupIdComparator.class);
		conf.setBoolean(CommonConst.MAPRED_CONFIG_HASH_PARTIION, true);

		job.setReducerClass(CycleBillGroupReducer.class);

		job.setOutputKeyClass(GeneralKeyBean.class);
		job.setOutputValueClass(GeneralValueBean.class);
		conf.set(CommonConst.TEXTOUTPUT_NEWLINE, CommonConst.NEWLINE_CRLF);

		addMultiOutputPath(job, cycleBillGroupPrefix, SequenceFileOutputFormatExt.class, GeneralKeyBean.class,
				GeneralValueBean.class);

		setReducerOutputPath(job, outTempPath);

		conf.setBoolean(CommonConst.MAP_TASKS_SPECULATIVE_EXECUTION, PropertyUtil.getInstance().
				getBooleanProperty(getKeyMapReduce(CommonConst.MAP_TASKS_SPECULATIVE_EXECUTION, mapReduceId, 2)));
		conf.setBoolean(CommonConst.REDUCE_TASKS_SPECULATIVE_EXECUTION, PropertyUtil.getInstance().
				getBooleanProperty(getKeyMapReduce(CommonConst.REDUCE_TASKS_SPECULATIVE_EXECUTION, mapReduceId, 2)));

		String queueName = PropertyUtil.getInstance().getStringProperty(getKeyMapReduce(CommonConst.JOB_QUEUE_NAME, mapReduceId, 2));
//		setQueueName(job, queueName);

		super.compress = PropertyUtil.getInstance().getBooleanProperty(CommonConst.KEY_COMPRESS_DEFAULT);
		conf.setBoolean(CommonConst.MAPRED_OUTPUT_COMPRESS_EXT, PropertyUtil.getInstance().getBooleanProperty(CommonConst.KEY_COMPRESS_SEQEXT));
		super.useGzipCodec = PropertyUtil.getInstance().getBooleanProperty(CommonConst.KEY_COMPRESS_USEGZIP);
		return job;
	}
	@Override
	public boolean postprocess2nd(Job job, Configuration conf)
			throws IllegalDataException, ProcessException{

		System.out.println("This is control postprocess2nd!");
		return true;
	}
	@Override
	public Job configJob3rd(Configuration conf, String[] args) throws IllegalDataException, ProcessException {
		Job job = createNewJob(conf);
		setMapReduceNum(job, conf, mapReduceId, 3);
		job.setJarByClass(CallDetailControl.class);
		job.setJobName(jobName);
		addMultiInputFile(job, dataInfoPath, cdrPrefix, TextInputFormat.class, CdrSbtmMapper.class);

		addMultiInputFile(job, outTempPath, cycleBillGroupPrefix,
				SequenceFileInputFormat.class, CycleBillGroupSequenceMapper.class);


		job.setMapOutputKeyClass(GeneralKeyBean.class);
		job.setMapOutputValueClass(GeneralValueBean.class);

		job.setPartitionerClass(CallDetailPartitioner.class);
		job.setGroupingComparatorClass(CallDetailComparator.class);
		conf.setBoolean(CommonConst.MAPRED_CONFIG_HASH_PARTIION, true);

		job.setReducerClass(CdrSbtmReducer.class);

		job.setOutputKeyClass(GeneralKeyBean.class);
		job.setOutputValueClass(GeneralValueBean.class);
		conf.set(CommonConst.TEXTOUTPUT_NEWLINE, CommonConst.NEWLINE_CRLF);

		addMultiOutputPath(job, innerPrefix, TextFileOutputFormatExt.class, GeneralKeyBean.class,
				GeneralValueBean.class);
		addMultiOutputPath(job, outerPrefix, TextFileOutputFormatExt.class, GeneralKeyBean.class,
				GeneralValueBean.class);

		setReducerOutputPath(job, outTemCallDetailPath);

		conf.setBoolean(CommonConst.MAP_TASKS_SPECULATIVE_EXECUTION, PropertyUtil.getInstance().
				getBooleanProperty(getKeyMapReduce(CommonConst.MAP_TASKS_SPECULATIVE_EXECUTION, mapReduceId, 3)));
		conf.setBoolean(CommonConst.REDUCE_TASKS_SPECULATIVE_EXECUTION, PropertyUtil.getInstance().
				getBooleanProperty(getKeyMapReduce(CommonConst.REDUCE_TASKS_SPECULATIVE_EXECUTION, mapReduceId, 3)));

		String queueName = PropertyUtil.getInstance().getStringProperty(getKeyMapReduce(CommonConst.JOB_QUEUE_NAME, mapReduceId, 3));
//		setQueueName(job, queueName);

		super.compress = PropertyUtil.getInstance().getBooleanProperty(CommonConst.KEY_COMPRESS_DEFAULT);
		conf.setBoolean(CommonConst.MAPRED_OUTPUT_COMPRESS_EXT, PropertyUtil.getInstance().getBooleanProperty(CommonConst.KEY_COMPRESS_SEQEXT));
		super.useGzipCodec = PropertyUtil.getInstance().getBooleanProperty(CommonConst.KEY_COMPRESS_USEGZIP);
		return job;
	}
	@Override
	public boolean postprocess3rd(Job job, Configuration conf) throws IllegalDataException, ProcessException{

		System.out.println("This is control postprocess3nd!");
		mergeCallDetailFile(conf, outTemCallDetailPath, innerPrefix, outputPath, CommonConst.TSV_FILE_EXT);
		mergeCallDetailFile(conf, outTemCallDetailPath, outerPrefix, outputPath, CommonConst.TSV_FILE_EXT);
		cleanWorkDir(workPath);
		return true;
	}
}
