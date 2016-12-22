/**
 *
 */
package jp.co.fd.hadoop.base;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jp.co.fd.hadoop.common.CommonConst;
import jp.co.fd.hadoop.common.exception.BaseException;
import jp.co.fd.hadoop.common.exception.IllegalDataException;
import jp.co.fd.hadoop.common.exception.ProcessException;
import jp.co.fd.hadoop.common.log.CommonLogger;
import jp.co.fd.hadoop.common.util.ArgUtil;
import jp.co.fd.hadoop.common.util.DateUtil;
import jp.co.fd.hadoop.common.util.GeneralOutputsUtil;
import jp.co.fd.hadoop.common.util.HDFSUtil;
import jp.co.fd.hadoop.common.util.PropertyUtil;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author YIDATEC
 * @param <KEYIN>
 * @param <VALUEIN>
 * @param <KEYOUT>
 * @param <VALUEOUT>
 *
 */
public class BaseControl extends BaseConfiguredTool {


	private String mapReduceId = null;
	private String mapReduceName = null;
	private String baseJobName = null;

	public long startDate = 0;
	public String startDateString = null;
	private String baseName = null;

	private CommonLogger appLogger = null;
	private CommonLogger capLogger = null;

	private int appExitCode = 0;
	private int errorExitCode = 1;

	private BaseControlUtility baseControlUtility = null;


	private long inputTotalRecord = 0;
	private long outputTotalRecord = 0;
	private long invalidTotalRecord = 0;
	private long invalidMapTotalRecord = 0;
	private long invalidReduceTotalRecord = 0;
	private long errorTotalRecord = 0;
	private long errorMapTotalRecord = 0;
	private long errorReduceTotalRecord = 0;

	private long tempTotalPathFileSize = 0;
	private long startDateJob = 0;
	private String workRootPathCache = null;
	private String tempOutputPathCache = null;

	int currentCount = 0;

	public BaseControl(String mapReduceId, String mapReduceName) {
		this.mapReduceId = mapReduceId;
		this.mapReduceName = mapReduceName;
		this.baseJobName = mapReduceId;
	}
	public int executeMapReduce(String[] args, BaseControl control) {
		// ���O
		//�J�n���O�̏o��
		BaseControlUtility.outputAppStartLog(mapReduceId, mapReduceName, args);
		if (args.length < 1) {
			ProcessException exception = new ProcessException();
			exception.setMessage("�p�����[�^������Ȃ��B");
			BaseControlUtility.outputConsoleErrorLog(mapReduceId, mapReduceName, exception);
		}
		String logFile = null;
		String localHddRoot = null;

		//�Ɩ����O�t�@�C�����̐ݒ�
		startDate = System.currentTimeMillis();

		try {
			localHddRoot = PropertyUtil.getInstance().getStringProperty("local.root");
		} catch (IllegalDataException e1) {
			localHddRoot = "/sbtm/";
		} catch (ProcessException e1) {
			localHddRoot = "/sbtm/";
		}
		SimpleDateFormat sdf = DateUtil.DATE_FORMAT.get();
		sdf.applyPattern("yyyyMMddHHmmss");
		startDateString = sdf.format(new Date(startDate));

		StringBuilder sb = new StringBuilder();
		sb.append(startDateString).append("_");
		sb.append(UUID.randomUUID());


		baseName = sb.toString();

		sb.setLength(0);
		sb.append(localHddRoot);
		if (!(localHddRoot.endsWith(CommonConst.FILE_SEPARATER))) {
			sb.append(CommonConst.FILE_SEPARATER);
		}
		sb.append("log");
		sb.append(CommonConst.FILE_SEPARATER);
		sb.append("APP_");
		sb.append(baseName);
		sb.append(".log");

		logFile = sb.toString();
		//�Ɩ����K�[�̐���
		appLogger  = CommonLogger.createLogger(logFile, CommonConst.BASE_LOG_PATTERN);
		//�L���p�V�e�B���O�t�@�C�����̐ݒ�

		//�L���p�V�e�B���K�[�̐���
		sb.setLength(0);
		sb.append(localHddRoot);
		if (!(localHddRoot.endsWith(CommonConst.FILE_SEPARATER))) {
			sb.append(CommonConst.FILE_SEPARATER);
		}
		sb.append("log");
		sb.append(CommonConst.FILE_SEPARATER);
		sb.append("CAP_");
		sb.append(baseName);
		sb.append(".log");

		capLogger  = CommonLogger.createLogger(sb.toString(),CommonConst.BASE_LOG_PATTERN);

		//���[�e�B���e�B�N���X�̐���
		baseControlUtility = new BaseControlUtility( appLogger, capLogger);

		baseControlUtility.outputTraceLog(ArgUtil.makeArgs("�����J�n","executeMapReduce"));
		int returnValue = 0;
		try {
			returnValue = ToolRunner.run(control, args);
			if (returnValue == BaseConfiguredTool.RETURN_CODE_ERROR) {
				setExitCode(errorExitCode);
				ProcessException e = new ProcessException();
				e.setMessage("MapReduce���s�G���[���������܂����B");
				BaseControlUtility.outputConsoleErrorLog(mapReduceId, mapReduceName, e);
			}
		} catch (BaseException e) {
			setExitCode(errorExitCode);
//			e.printStackTrace();
			BaseControlUtility.outputConsoleErrorLog(mapReduceId, mapReduceName, e);

		} catch (IOException e) {
			setExitCode(errorExitCode);
			e.printStackTrace();
			BaseControlUtility.outputConsoleErrorLog(mapReduceId, mapReduceName, e);

		} catch (InterruptedException e) {
			setExitCode(errorExitCode);
			e.printStackTrace();
			BaseControlUtility.outputConsoleErrorLog(mapReduceId, mapReduceName, e);

		} catch (ClassNotFoundException e) {
			setExitCode(errorExitCode);
			e.printStackTrace();
			BaseControlUtility.outputConsoleErrorLog(mapReduceId, mapReduceName, e);

		} catch (Exception e) {
			setExitCode(errorExitCode);
			e.printStackTrace();
			BaseControlUtility.outputConsoleErrorLog(mapReduceId, mapReduceName, e);

		}

		try {

			//HDFS��̃��O�����O�t�@�C���̃��[�J���Ɏ��W����
			baseControlUtility.collectHDFSLogs(getConf(), baseName, logFile);
			baseControlUtility.deleteHDFSLogs(getConf(), baseName);
			outputCapLogAll();
		} catch (ProcessException e) {
			setExitCode(errorExitCode);
			BaseControlUtility.outputConsoleErrorLog(mapReduceId, mapReduceName, e);
		}
		//�����I�����O�̏o��

		//TODO
		BaseControlUtility.outputAppEndLog(mapReduceId, mapReduceName, returnValue, 0, 0, 0, 0);

		return returnValue;
	}

	private void outputCapLogAll() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}
	@Override
	public boolean preProcess(String[] args) throws BaseException {
		baseControlUtility.outputTraceLog(ArgUtil.makeArgs("Start","preProcess"));
		boolean isExecuteMapReduce = true;
		Configuration conf = getConf();
		//MapReduce�����O�o�͐�̐ݒ�
		StringBuilder sb = new StringBuilder();
		sb.append("/hdfs/log/temp");
		sb.append(baseJobName);
		sb.append("/");
		try {
			HDFSUtil.mkdir(conf,sb.toString());
		} catch (IOException e) {

		}

		isExecuteMapReduce = preProcessEx(args, conf);

		baseControlUtility.outputTraceLog(ArgUtil.makeArgs("End","preProcess"));

		return isExecuteMapReduce;
	}

	public boolean preProcessEx(String[] args, Configuration conf)
			throws BaseException {
		return true;
	}
	@Override
	public Job configJob(String[] args)
			throws BaseException, IOException {
		baseControlUtility.outputTraceLog(ArgUtil.makeArgs("Start","configJob"));
		int jobCount = getCurrentCount();
		Configuration conf = getConf();
		Job job = null;
		switch (jobCount) {
		case 1:
			startDateJob = System.currentTimeMillis();
			job = configJob1st(conf, args);
			break;
		case 2:
			startDateJob = System.currentTimeMillis();
			job = configJob2nd(conf, args);
			break;
		case 3:
			startDateJob = System.currentTimeMillis();
			job = configJob3rd(conf, args);
			break;
		default:
			break;
		}
		if (job == null) {
			return null;
		}
		//�SMapReduce����Conf�̐ݒ�
		Configuration jobConf = job.getConfiguration();
		jobConf.set("MapRed_log_baseName", baseName);
		jobConf.set("map_reduce_id", mapReduceId);
		jobConf.set("map_reduce_name", mapReduceName);

		jobConf.setLong("map_reduce_start_date", startDate);
		jobConf.set("map_reduce_start_dateString", startDateString);

		baseControlUtility.outputTraceLog(ArgUtil.makeArgs("Start","configJob"));
		return job;
	}
	@Override
	public boolean postProcess(Job job) throws BaseException {
		int jobCount = getCurrentCount();
		baseControlUtility.outputTraceLog(ArgUtil.makeArgs("Start","postProcess"));

		Configuration conf = getConf();
		//�J�E���^���̎擾�E�J�E���^�t�@�C���̍폜
		baseControlUtility.readCountFiles(conf, tempOutputPathCache);

		boolean postResult = false;
		switch (jobCount) {
		case 1:
			postResult = postprocess1st(job, conf);
			break;
		case 2:
			postResult = postprocess2nd(job, conf);
			break;
		case 3:
			postResult = postprocess3rd(job, conf);
			break;
		default:
			break;
		}
		baseControlUtility.outputTraceLog(ArgUtil.makeArgs("End","postProcess"));
		return postResult;
	}
	public Job configJob1st(Configuration conf, String[] args)
			throws IllegalDataException, ProcessException {
		return null;
	}

	public boolean postprocess1st(Job job, Configuration conf)
			throws IllegalDataException, ProcessException {
		return false;
	}

	public Job configJob2nd(Configuration conf, String[] args)
			throws IllegalDataException, ProcessException {
		return null;
	}

	public boolean postprocess2nd(Job job, Configuration conf)
			throws IllegalDataException, ProcessException {
		return false;
	}

	public Job configJob3rd(Configuration conf, String[] args)
			throws IllegalDataException, ProcessException {
		return null;
	}

	public boolean postprocess3rd(Job job, Configuration conf)
			throws IllegalDataException, ProcessException {
		return false;
	}


	public void setReducerOutputPath(Job job, String outputPath){
		FileOutputFormat.setOutputPath(job, new Path(outputPath));
		tempOutputPathCache = outputPath;

	}

	/**
	 * @param currentCount
	 *  �Z�b�g���� currentCount
	 */
	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}

	private void setExitCode(int i) {
		// TODO

	}

	public Job createNewJob(Configuration conf) throws ProcessException {
		return baseControlUtility.createNewJob(conf);
	}

	public void checkAndKillJob(String jobName) throws ProcessException {
		JobConf jobconf = new JobConf(getConf());
		baseControlUtility.checkAndKillJob(jobconf, jobName);
	}
	public String cleanAndFixWorkDir(String mapreduceId, String id) throws ProcessException {
		return baseControlUtility.cleanAndFixWorkDir(getConf(), mapreduceId, id);
	}
	public String createOutputDirName(String workRootPath) throws ProcessException {
		return baseControlUtility.createOutputDirName(getConf(), workRootPath);
	}
	public String createOutputDir(String workRootPath) throws ProcessException {
		return baseControlUtility.createOutputDir(getConf(), workRootPath);
	}

	public String createOutTempDirName(String workRootPath) throws ProcessException {
		return baseControlUtility.createOutTempDirName(getConf(), workRootPath);
	}
	public String createOutTempDirNameReference(String workRootPath) throws ProcessException {
		return baseControlUtility.createOutTempDirNameReference(getConf(), workRootPath);
	}
	public void setMapReduceNum(Job job, Configuration conf, String mapReduceId, int jobCount)
		throws IllegalDataException, ProcessException {
		StringBuffer sb  = new StringBuffer();
		sb.append(CommonConst.MAP_NUM_BASENAME);
		sb.append(CommonConst.STRING_DOT);
		sb.append(mapReduceId);
		sb.append(CommonConst.STRING_DOT);
		sb.append(jobCount);

		int mapNum = PropertyUtil.getInstance().getIntProperty(sb.toString());

		sb.setLength(0);
		sb.append(CommonConst.REDUCE_NUM_BASENAME);
		sb.append(CommonConst.STRING_DOT);
		sb.append(mapReduceId);
		sb.append(CommonConst.STRING_DOT);
		sb.append(jobCount);
		int reduceNum = PropertyUtil.getInstance().getIntProperty(sb.toString());

		if ((mapNum < 1 && mapNum != -1) || (reduceNum < 1 && reduceNum != -1)) {
			ProcessException exception = new ProcessException();
			exception.setMessage("mapReduce ���s��" + mapNum + reduceNum);
			throw exception;
		}

		if (mapNum != -1) {
			conf.setInt(CommonConst.CONF_KEY_MAPNUM, mapNum);
		}
		if (reduceNum != -1) {
			job.setNumReduceTasks(reduceNum);
		}
	}


	public List<String> addMultiInputFile(Job job, String basePath,
			String prefix, Class inputFormatClass, Class mapperClass) throws IllegalDataException, ProcessException {
		return baseControlUtility.addMultiInputFile(getConf(), job, basePath, prefix, inputFormatClass, mapperClass);
	}
	public void addMultiOutputPath(Job job,String prefix, Class<? extends OutputFormat> outputFormatClass,
			Class keyBean, Class valueBean) throws ProcessException{
		GeneralOutputsUtil.addNameOutPut(job, prefix, outputFormatClass, keyBean, valueBean);

	}
	public String getKeyMapReduce(String baseKeyname, String mapReduceId, int jobCount) throws ProcessException {
		if (baseKeyname == null || baseKeyname.isEmpty() || mapReduceId == null || mapReduceId.isEmpty()) {
			ProcessException exception = new ProcessException();
			exception.setMessage("getKeyMapReduce error");
			exception.setLogArgs(ArgUtil.makeArgs(baseKeyname, mapReduceId));
			throw exception;
		}
		StringBuffer sb  = new StringBuffer();
		sb.append(baseKeyname);
		sb.append(CommonConst.STRING_DOT);
		sb.append(mapReduceId);
		sb.append(CommonConst.STRING_DOT);
		sb.append(jobCount);

		return sb.toString();
	}

	public Map<String, String> getXmlInfo(Configuration conf, String path) throws ProcessException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		Map<String, String> map = new HashMap<String, String>();
		try {
			DocumentBuilder builder = fac.newDocumentBuilder();
			FSDataInputStream inputStream = HDFSUtil.getInputStream(conf, path);
			if (inputStream == null) {
				ProcessException exception = new ProcessException();
				exception.setMessage("xml file is not exitst");
				throw exception;
			}
			Document doc = builder.parse(inputStream);
			Node rootNode = doc.getFirstChild();
			NodeList logFileList = rootNode.getChildNodes();
			for (int i = 1; i < logFileList.getLength(); i += 2) {
				Node curNode = logFileList.item(i);
				NamedNodeMap nammap = curNode.getAttributes();
				map.put(nammap.item(0).getNodeValue(), nammap.item(1).getNodeValue());
			}
		} catch (ParserConfigurationException e) {
			ProcessException exception = new ProcessException(e);
			exception.setMessage("xml reader error");
			throw exception;
		} catch (SAXException e) {
			ProcessException exception = new ProcessException(e);
			exception.setMessage("xml reader error");
			throw exception;
		} catch (IOException e) {
			ProcessException exception = new ProcessException(e);
			exception.setMessage("xml reader error");
			throw exception;
		}
		return map;
	}
	public void mergeCallDetailFile(Configuration conf, String pathFrom, String prefix, String pathTo, String fileExt)
		throws ProcessException {
		SimpleDateFormat sdf = DateUtil.DATE_FORMAT.get();
		sdf.applyPattern("yyyyMMddHHmmss");
		String outputDateString = sdf.format(new Date(startDate));
		baseControlUtility.mergeCallDetailFile(conf, pathFrom, prefix, outputDateString, pathTo, fileExt);
	}
    protected void cleanWorkDir(String workRootPath) throws ProcessException {
    	baseControlUtility.cleanWorkDir(getConf(), workRootPath);
    }
}
