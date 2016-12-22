package jp.co.fd.hadoop.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CharacterCodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;

import jp.co.fd.hadoop.common.CommonConst;
import jp.co.fd.hadoop.common.convert.ConvertUtil;
import jp.co.fd.hadoop.common.exception.IllegalDataException;
import jp.co.fd.hadoop.common.exception.ProcessException;
import jp.co.fd.hadoop.common.log.CommonLogger;
import jp.co.fd.hadoop.common.util.ArgUtil;
import jp.co.fd.hadoop.common.util.CounterID;
import jp.co.fd.hadoop.common.util.HDFSUtil;

public class BaseControlUtility {

	private  static CommonLogger appLogger = null;
	private HashMap<Long, Long> counterValueTotal = new HashMap<Long, Long>();

	public BaseControlUtility(CommonLogger appLogger, CommonLogger capLogger) {
		BaseControlUtility.setAppLogger(appLogger);
	}

	private static void setAppLogger(CommonLogger appLogger) {
		BaseControlUtility.appLogger = appLogger;
	}

	public String cleanAndFixWorkDir(Configuration conf, String mapReduceId,
			String id) throws ProcessException{
		if (conf == null || mapReduceId == null || mapReduceId.isEmpty() || id == null ||
				id.isEmpty()) {
			throw new ProcessException();
		}
		String workRootPathtop = "/hdfs/data/sbtm/".concat(id).concat("/");

		String pathInput = "/hdfs/data/sbtm/".concat(id).concat("/input/");
		String pathRunning = "/hdfs/data/sbtm/".concat(id).concat("/running/");
		String pathOutput = "/hdfs/data/sbtm/".concat(id).concat("/output/");
		String pathFinish = "/hdfs/data/sbtm/".concat(id).concat("/finish/");
		String pathOld = "/hdfs/data/sbtm/".concat(id).concat("/old/");

		boolean isExistInput = false;
		boolean isExistRunning = false;
		boolean isExistOutput = false;
		boolean isExistFinish = false;
		boolean isExistOld = false;
		String currentCheckDir = null;

		currentCheckDir = pathInput;
		try {
			currentCheckDir = pathInput;
			isExistInput = HDFSUtil.isPathExist(conf, pathInput);
			currentCheckDir = pathRunning;
			isExistRunning = HDFSUtil.isPathExist(conf, pathRunning);
			currentCheckDir = pathOutput;
			isExistOutput = HDFSUtil.isPathExist(conf, pathOutput);
			currentCheckDir = pathFinish;
			isExistFinish = HDFSUtil.isPathExist(conf, pathFinish);
			currentCheckDir = pathOld;
			isExistOld = HDFSUtil.isPathExist(conf, pathOld);
		} catch (IOException e) {
			ProcessException exception = new ProcessException(e);
			exception.setMessage("cleanAndFixWorkDir error");
			exception.setLogArgs(ArgUtil.makeArgs(id, currentCheckDir));
			throw exception;
		}
		if (isExistFinish || isExistOld) {
			return null;
		}

		if (isExistInput && isExistRunning) {
			ProcessException exception = new ProcessException();
			exception.setMessage("cleanAndFixWorkDir error");
			exception.setLogArgs(ArgUtil.makeArgs(id, pathInput, pathRunning));
			throw exception;
		}

		if (!isExistInput && !isExistRunning) {
			ProcessException exception = new ProcessException();
			exception.setMessage("cleanAndFixWorkDir error");
			exception.setLogArgs(ArgUtil.makeArgs(id, pathInput, pathRunning));
			throw exception;
		}
		if (isExistOutput) {
			deleteFile(conf, pathOutput);
		} else {
			try {
				HDFSUtil.mkdir(conf, pathOutput);
			} catch (IOException e) {
				ProcessException exception = new ProcessException(e);
				exception.setMessage("cleanAndFixWorkDir error");
				exception.setLogArgs(ArgUtil.makeArgs(id, pathOutput));
				throw exception;
			}
		}
		if (isExistInput) {
			try {
				HDFSUtil.move(conf, pathInput, pathRunning);
			} catch (IOException e) {
				ProcessException exception = new ProcessException(e);
				exception.setMessage("cleanAndFixWorkDir error");
				exception.setLogArgs(ArgUtil.makeArgs(id, pathOutput));
				throw exception;
			}
		}
		return workRootPathtop;
	}
	private void deleteFile(Configuration conf, String filePath) throws ProcessException{
		if (conf == null || filePath == null || filePath.isEmpty()) {
			throw new ProcessException();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(filePath);
		if (filePath.endsWith("/")) {
			sb.append("/");
		}
		sb.append(CommonConst.FILE_PATTERN_ALL);

		try {
			List<String> fileList = HDFSUtil.getFileList(conf, sb.toString());
			if (fileList != null && !fileList.isEmpty()) {
				for (String file : fileList) {
					HDFSUtil.del(conf, file);
				}
			}
		} catch (IOException e) {
			ProcessException exception = new ProcessException(e);
			exception.setMessage("deleteFile error");
			exception.setLogArgs(ArgUtil.makeArgs(filePath));
			throw exception;
		}


	}
	public boolean returnCleanupWorkDir(Configuration conf, String mapReduceId,int serviceId,
		String outputPath) throws ProcessException{
		return true;
	}
	/**
	 *
	 * @param conf
	 * @param mapReduceId
	 * @return
	 * @throws ProcessException
	 */
	public String createOutputDir(Configuration conf, String workRootPath) throws ProcessException{
		if (conf == null || workRootPath == null || workRootPath.isEmpty()) {
			throw new ProcessException();
		}
		String outputPath = workRootPath.concat("output/");
		try {
			if (HDFSUtil.isPathExist(conf, outputPath)) {
				return outputPath;
			}
			HDFSUtil.mkdir(conf, outputPath);
		} catch (IOException e) {
			ProcessException processException = new ProcessException(e);
			throw processException;
		}
		outputTraceLog(ArgUtil.makeArgs("�o�̓f�B���N�g���m��", outputPath));
		return outputPath;
	}
	public String createOutputDirName(Configuration conf, String workRootPath) throws ProcessException{
		if (conf == null || workRootPath == null || workRootPath.isEmpty()) {
			throw new ProcessException();
		}
		String outputPath = workRootPath.concat("running/output/");
		return outputPath;
	}
	public String createOutTempDirName(Configuration conf, String workRootPath) throws ProcessException{
		if (conf == null || workRootPath == null || workRootPath.isEmpty()) {
			throw new ProcessException();
		}
		String tempPath = workRootPath.concat("running/").concat("temp_output/");

		return tempPath;
	}
	public String createOutTempDirNameReference(Configuration conf, String workRootPath) throws ProcessException{
		if (conf == null || workRootPath == null || workRootPath.isEmpty()) {
			throw new ProcessException();
		}
		String tempPath = workRootPath.concat("running/").concat("temp_output_Reference/");

		return tempPath;
	}
	/**
	 *
	 * @param conf
	 * @param workRootPath
	 * @return
	 * @throws ProcessException
	 */
	public long cleanWorkDir(Configuration conf, String workRootPath) throws ProcessException{
		if (conf == null || workRootPath == null || workRootPath.isEmpty()) {
			throw new ProcessException();
		}
		if (workRootPath.endsWith("/")) {
			workRootPath.concat("/");
		}
		StringBuffer sb = new StringBuffer();
		sb.append(workRootPath);
		sb.append("running");
		sb.append("/*");
		String tempDirPattern = sb.toString();
		long totalSize = 0;
		try {
			List<String> tempDirList = HDFSUtil.getDirList(conf, tempDirPattern);
			if (tempDirList != null && tempDirList.size() > 0) {
				for (String tempDir : tempDirList) {
					totalSize += HDFSUtil.getFileSize(conf, tempDir);
					HDFSUtil.del(conf, tempDir);
				}
			}
		} catch (IOException e) {
			ProcessException processException = new ProcessException(e);
			throw processException;
		}
		return totalSize;
	}

	/**
	 *
	 * @param mapReduceId
	 * @param mapReduceName
	 * @param args
	 */
	public static void outputAppStartLog(String mapReduceId, String mapReduceName, String[] args) {
		if (mapReduceId == null || mapReduceId.isEmpty() || mapReduceName == null ||
				mapReduceName.isEmpty() || args == null){
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("�����J�n,");
		sb.append(mapReduceId);
		sb.append(CommonConst.STRING_COMMA);
		sb.append(mapReduceName);
		sb.append(CommonConst.STRING_COMMA);

		for (int i = 0; i < args.length; i++) {
			sb.append(args[i]);
			sb.append(CommonConst.STRING_COMMA);
		}
		sb.append(CommonConst.LINE_SEPARATER);
		outputConsoleLog(System.out, sb.toString());
	}
	public static void outputConsoleErrorLog(String mapReduceId, String mapReduceName, Exception exception) {
		if (mapReduceId == null || mapReduceId.isEmpty() || mapReduceName == null ||
				mapReduceName.isEmpty() || exception == null){
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(CommonConst.LINE_SEPARATER);
		sb.append(mapReduceId);
		sb.append(CommonConst.STRING_COMMA);
		sb.append(mapReduceName);
		sb.append(CommonConst.STRING_COMMA);
		sb.append(exception.getMessage());

		outputConsoleLog(System.out, sb.toString());
	}
	/**
	 *
	 * @param mapReduceId
	 * @param mapReduceName
	 * @param args
	 */
	public static void outputAppEndLog(String mapReduceId, String mapReduceName, int exitCode,
			long inputTotal, long outTotal, long invildTotal, int errorTotal){
		if (mapReduceId == null || mapReduceId.isEmpty() || mapReduceName == null ||
				mapReduceName.isEmpty()){
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(CommonConst.LINE_SEPARATER);
		sb.append("�����I��,");
		sb.append(mapReduceId);
		sb.append(CommonConst.STRING_COMMA);
		sb.append(mapReduceName);
		sb.append(CommonConst.STRING_COMMA);
		sb.append(exitCode);
		sb.append(CommonConst.STRING_COMMA);

		sb.append(inputTotal);
		sb.append(CommonConst.STRING_COMMA);
		sb.append(outTotal);
		sb.append(CommonConst.STRING_COMMA);
		sb.append(invildTotal);
		sb.append(CommonConst.STRING_COMMA);
		sb.append(errorTotal);

		outputConsoleLog(System.out, sb.toString());

		if (appLogger != null) {
			appLogger.info(sb.toString());
		}
	}
	/**
	 *
	 * @param outputStream
	 * @param logMessage
	 */
	private static void outputConsoleLog(PrintStream outputStream,
			String logMessage) {
		if (outputStream == null || logMessage == null || logMessage.isEmpty()) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(CommonConst.CONSOLE_LOG_DATE_PATTERN);

		StringBuffer sb = new StringBuffer();
		sb.append(sdf.format(Calendar.getInstance().getTime()));
		sb.append(CommonConst.STRING_COMMA);
		String hostName = null;
		InetAddress localAdress;
		try {
			localAdress = InetAddress.getLocalHost();
			if (localAdress != null) {
				hostName = localAdress.getHostName();
			}
		} catch (UnknownHostException e1) {
		}

		if (hostName == null) {
			hostName = "";
		}
		sb.append(hostName);
		sb.append(CommonConst.STRING_COMMA);
		sb.append(logMessage);
		try {
			byte[] outputBytes = ConvertUtil.encodeSJIS(sb.toString());
			outputStream.write(outputBytes, 0, outputBytes.length);
		} catch (CharacterCodingException e) {
			outputStream.print(sb.toString());
		}
	}

	public  void outputTraceLog(String[] args){

		if ((args == null) || (args.length <1)) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(args[i]);
		}
		appLogger.info(sb.toString());
	}

	public String getPublicDirFilePrefix(int fileId, List<String> fileList) throws ProcessException{
		return null;
	}

	public List<String> addMultiInputFile(Configuration conf, Job job, String basePath,
			String prefix, Class<? extends InputFormat> inputFormatClass, Class<? extends Mapper> mapperClass)
			throws IllegalDataException, ProcessException {

		if (job == null || basePath == null || inputFormatClass == null || mapperClass == null) {
			ProcessException exception = new ProcessException();
			exception.setMessage("addMultiInputFile error");
			throw exception;
		}
		List<String> fileList = null;

		StringBuffer sb = new StringBuffer();
		sb.append(basePath);
		if (!(basePath.endsWith(CommonConst.FILE_SEPARATER))) {
			sb.append(CommonConst.FILE_SEPARATER);
		}
		if (prefix != null) {
			sb.append(prefix);
			sb.append(CommonConst.FILE_SEPARATER);
		}

		try {
			 fileList = HDFSUtil.getFileList(conf, sb.toString());
			 if (fileList != null && fileList.size() >= 1) {
				 for (String file : fileList) {
					 MultipleInputs.addInputPath(job, new Path(file), inputFormatClass, mapperClass);
				 }
			 }
		} catch (IOException e) {
			ProcessException exception = new ProcessException(e);
			exception.setMessage("addMultiInputFile error");
			throw exception;
		}
		return fileList;
	}



	public Job createNewJob(Configuration conf) throws ProcessException{
		if (conf == null) {
			throw new ProcessException();
		}
		Job job = null;
		try {
			job = new Job(conf);
		} catch (IOException e) {
			ProcessException exception = new ProcessException(e);
			throw exception;
		}
		return job;
	}
	/**
	 *
	 * @param conf
	 * @param jobName
	 * @throws Exception
	 */
	public void checkAndKillJob(JobConf conf , String jobName) throws ProcessException {
		if (conf == null || jobName == null || jobName.isEmpty()) {
			throw new ProcessException();
		}
		String jobId = null;

		try {
			jobId = HDFSUtil.searchJob(conf,jobName);
			if (jobId == null || jobId.isEmpty()) {
				return;
			}
			HDFSUtil.killJob(conf, jobId);
		} catch (IOException e) {
			ProcessException processException = new ProcessException(e);
			throw processException;
		}
		outputTraceLog(ArgUtil.makeArgs("�W���u�����I�����܂����B",jobName, jobId));
	}

	public void readSequenceFile(Configuration conf,Path inputPathFile, Writable keyBean,
			Writable valueBean) throws ProcessException{
		if (conf == null || inputPathFile == null || keyBean == null || valueBean == null) {
			throw new ProcessException();
		}
		Reader reader = null;
		try {
			FileSystem fs = FileSystem.get(conf);
			reader = new Reader(fs, inputPathFile, conf);
			if (!(reader.next(keyBean, valueBean))) {
				throw new ProcessException();
			}
		} catch (IOException e) {
			ProcessException exception = new ProcessException(e);
			throw exception;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					ProcessException exception = new ProcessException(e);
					throw exception;
				} finally {
					reader = null;
				}
			}
		}
	}

	public void writeSequenceFile(Configuration conf,String outputPathFile, Writable keyBean,
			Writable valueBean,Class keyClass, Class valueClass ) throws ProcessException{
		if (conf == null || outputPathFile == null || keyBean == null || valueBean == null) {
			throw new ProcessException();
		}
		Writer writer = null;
		try {
			FileSystem fs = FileSystem.get(conf);
			writer = SequenceFile.createWriter(fs, conf, new Path(outputPathFile), keyClass, valueClass);
			writer.append(keyBean, valueBean);
		} catch (IOException e) {
			ProcessException exception = new ProcessException(e);
			throw exception;
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					ProcessException exception = new ProcessException(e);
					throw exception;
				} finally {
					writer = null;
				}
			}
		}
	}

	public long getCount(Job job, CounterID counterId) throws ProcessException{

		if (job == null || counterId == null) {
			throw new ProcessException();
		}
		long count = 0;
		try {
			Counters counters = job.getCounters();
			Counter counter = null;
			if (counters != null) {
				counter = counters.findCounter(counterId);
			}
			if (counter != null) {
				count = counter.getValue();
			}
		} catch (IOException e) {
			ProcessException exception = new ProcessException(e);
			throw exception;
		}
		return count;
	}

	public long getCountCorrect(Configuration conf, CounterID counterId, int countType,String baseName)
			throws ProcessException{
		if(conf == null || counterId == null) {
			ProcessException exception = new ProcessException();
			throw exception;
		}
		long curretValue = 0;
		Long currentValueObject = null;
		currentValueObject = counterValueTotal.get(Long.valueOf(counterId.getId()));
		if (currentValueObject != null) {
			curretValue = currentValueObject.longValue();
		}
		return curretValue;
	}

	public void  collectHDFSLogs(Configuration conf, String baseName, String logFile)
			throws ProcessException{
		if(baseName == null || baseName.isEmpty() || logFile ==null || logFile.isEmpty()) {
			return;
		}
		if(conf == null) {
			ProcessException exception = new ProcessException();
			throw exception;
		}
		if (appLogger == null || !(appLogger.canWrite())) {
			ProcessException exception = new ProcessException();
			throw exception;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("/hdfs/log/");
		sb.append(baseName);
		sb.append(CommonConst.FILE_SEPARATER);
		sb.append(baseName);
		sb.append(".log");

		List<Path> list = null;
		try {
			list = HDFSUtil.getFilePathList(conf, sb.toString());
		} catch (IOException e) {
			ProcessException exception = new ProcessException(e);
			throw exception;
		}
		if (list == null || list.size() < 1) {
			return;
		}
		BufferedReader br = null;
		for (Path path : list) {
			try {
				br = new BufferedReader(
						new InputStreamReader(HDFSUtil.getInputStream(conf, path.toUri().getPath())));
				String line = null;
				while ((line = br.readLine()) != null) {
					appLogger.error(line);
				}
			} catch (IOException e) {
				ProcessException exception = new ProcessException(e);
				throw exception;
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						ProcessException exception = new ProcessException(e);
						throw exception;
					} finally {
						br = null;
					}
				}
			}
		}

	}

	public void  deleteHDFSLogs(Configuration conf, String baseName) throws ProcessException{
		if(baseName == null || baseName.isEmpty()) {
			return;
		}
		if(conf == null) {
			ProcessException exception = new ProcessException();
			throw exception;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("/hdfs/log/");
		sb.append(baseName);
		sb.append(CommonConst.FILE_SEPARATER);
		try {
			HDFSUtil.del(conf, sb.toString());
		} catch (IOException e) {
			ProcessException exception = new ProcessException(e);
			throw exception;
		}
	}
	public void  readCountFiles(Configuration conf, String tempOutputpath) throws ProcessException{
		if(conf == null || tempOutputpath == null || tempOutputpath.isEmpty()) {
			ProcessException exception = new ProcessException();
			throw exception;
		}
		if (tempOutputpath.endsWith("/")) {
			tempOutputpath.concat("/");
		}
		readCountFiles(conf, tempOutputpath , 1, counterValueTotal);
	}
	private void  readCountFiles(Configuration conf, String tempOutputpath,
			int countType, HashMap<Long, Long> countMap) throws ProcessException{
		if(conf == null || tempOutputpath == null || tempOutputpath.isEmpty()) {
			ProcessException exception = new ProcessException();
			throw exception;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(tempOutputpath);
		sb.append("CountTotal_");
		sb.append("/*");
		List<String> counterFileList = null;
		HashMap<Long, List<String>> counterIdMap = new HashMap<Long, List<String>>();

		try {
			counterFileList = HDFSUtil.getFileList(conf, sb.toString());
			for (String fileName : counterFileList) {
				if (fileName.endsWith("/")) {
					continue;
				}
				int slashPos = fileName.lastIndexOf("/");
				if (slashPos >= 0) {
					fileName = fileName.substring(slashPos+ 1);
				}
				String[] fileNames = fileName.split("_");
				if (fileNames.length < 3) {
					ProcessException exception = new ProcessException();
					throw exception;
				}
				Long counterId = null;
				try {
					counterId = Long.valueOf(fileNames[1]);
				} catch (NumberFormatException e) {
					ProcessException exception = new ProcessException();
					throw exception;
				}
				List<String> fileList = counterIdMap.get(counterId);
				if (fileList == null) {
					fileList = new ArrayList<String>();
					counterIdMap.put(counterId, fileList);
				}
				fileList.add(fileName);
			}
		} catch (IOException e) {
			ProcessException exception = new ProcessException();
			throw exception;
		}

		//
		for (Long counterId :counterIdMap.keySet()) {
			long currentValue = 0;
			List<String> readCounterFileList = counterIdMap.get(counterId);
			for (String readCounterFile : readCounterFileList) {
				IntWritable keyBean = new IntWritable();
				LongWritable valueBean = new LongWritable();
				readSequenceFile(conf, new Path(tempOutputpath, readCounterFile), keyBean, valueBean);
				currentValue += valueBean.get();
			}
			countMap.put(counterId, currentValue);
		}
		try {
			if (counterFileList != null) {
				for (String fileName : counterFileList) {
						HDFSUtil.del(conf, fileName);
				}
			}
		} catch (IOException e) {
			ProcessException exception = new ProcessException();
			throw exception;
		}

	}

	public void mergeCallDetailFile(Configuration conf, String pathFrom, String prefix,
			String outputDateString, String pathTo, String fileExt) throws ProcessException {
		if ((conf == null) || (pathFrom == null) || (pathFrom.isEmpty())
				|| (pathTo == null) || (pathTo.isEmpty())) {
			ProcessException exception = new ProcessException();
			exception.setMessage("mergeCallDetailFile error");
			throw exception;
		}

        StringBuffer sb = new StringBuffer();

        sb.append(pathFrom);

        if (!(pathFrom.endsWith(CommonConst.FILE_SEPARATER))) {
            sb.append(CommonConst.FILE_SEPARATER);
        }

        if ((prefix != null) && ((!prefix.isEmpty()))) {
            sb.append(prefix);
            sb.append(CommonConst.FILE_SEPARATER);
            sb.append(prefix);
        } else {
            sb.append(CommonConst.FILE_PREFIX_PART);
        }
        sb.append(CommonConst.PATTERN_ALL);
        List<String> inputFileList = null;
        try {
            inputFileList = HDFSUtil.getFileList(conf, sb.toString());
        } catch (IOException ioException) {
			ProcessException exception = new ProcessException(ioException);
			exception.setMessage("mergeCallDetailFile error");
			throw exception;
        }
        sb.setLength(0);
        sb.append(pathTo);
        if (!(pathFrom.endsWith(CommonConst.FILE_SEPARATER))) {
            sb.append(CommonConst.FILE_SEPARATER);
        }
        sb.append(prefix);
        sb.append(outputDateString);
        sb.append(fileExt);
        String outputFileName = sb.toString();
        InputStream is = null;
        OutputStream os = null;
        try {
            os = HDFSUtil.getOutputStream(conf, outputFileName);
            if ((inputFileList != null) && (0 < inputFileList.size())) {
                for (String inputFile : inputFileList) {
                    is = HDFSUtil.getInputStreamEx(conf, inputFile);
                    HDFSUtil.copyBytes(conf, is, os, false);
                    is.close();
                    is = null;
                }
            }
            os.close();
            os = null;
        } catch (IOException ioException) {
            ProcessException exception = new ProcessException(ioException);
            exception.setMessage("mergeCallDetailFile error");
            throw exception;

        } finally {
            ProcessException closeException = null;
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioException) {
                    ProcessException exception = new ProcessException(ioException);
                    exception.setMessage("mergeCallDetailFile error");
                } finally {
                    is = null;
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ioException) {
                    ProcessException exception = new ProcessException(ioException);
                    exception.setMessage("mergeCallDetailFile error");
                } finally {
                    os = null;
                }
            }
            if (closeException != null) {
                throw closeException;
            }
        }
    }
}
