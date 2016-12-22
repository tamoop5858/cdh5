/**
 *
 */
package jp.co.fd.hadoop.common.log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import jp.co.fd.hadoop.common.CommonConst;
import jp.co.fd.hadoop.common.util.HDFSUtil;
import jp.co.fd.hadoop.common.util.MessageUtil;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.TaskAttemptID;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author YIDATEC
 *
 */
public class HDFSLogger {

	/** HDFS�̐ݒ��� */
	private Configuration conf = null;

	/** �o�̓X�g���[�� */
	private OutputStream os = null;

	/** �p�^�[���R���o�[�^�[ */
	private PatternConverter converter = null;

	/** ���K�[ */
	private Logger logger = null;

	/** ���O�t�@�C���p�X */
	private String path = null;

	/** �o�͍ő�s�� */
	private int maxLine = 0;

	/** ���݂̃��O�o�͍s�� */
	private int currentLine = 0;

	/** �t�@�C�������L�� */
	private boolean created = false;

	/** ���O�t�H�[�}�b�g�ϊ��L�� */
	private boolean formatChange = false;


	public HDFSLogger(org.apache.hadoop.mapreduce.Mapper.Context mapContext, int maxLine, boolean formatChange) {
		this(mapContext, null, maxLine, formatChange);
	}

	public HDFSLogger(org.apache.hadoop.mapreduce.Mapper.Context mapContext, String prefix, int maxLine,
			boolean formatChange) {
		if (mapContext != null) {
			conf = mapContext.getConfiguration();
			init(mapContext.getTaskAttemptID(), prefix);
		}
		this.maxLine = maxLine;
		this.formatChange = formatChange;
	}

	public HDFSLogger(org.apache.hadoop.mapreduce.Reducer.Context reducerContext,
			int maxLine, boolean formatChange) {

		this(reducerContext, null, maxLine, formatChange);
	}

	public HDFSLogger(org.apache.hadoop.mapreduce.Reducer.Context reducerContext, String prefix,
			int maxLine, boolean formatChange) {
		if (reducerContext != null) {
			conf = reducerContext.getConfiguration();
			init(reducerContext.getTaskAttemptID(), prefix);
		}
		this.maxLine = maxLine;
		this.formatChange = formatChange;
	}

	public void create() throws IOException {
		if (!created) {
			os = HDFSUtil.getOutputStream(conf, path);
			if (os == null) {
				throw new IOException("File Create Failed");
			}
			created = true;
		}
	}

	public boolean debug(String msg) {
		return write(msg, Level.DEBUG);
	}

	public boolean info(String msg) {
		return write(msg, Level.INFO);
	}

	public boolean warn(String msg) {
		return write(msg, Level.WARN);
	}

	public boolean error(String msg) {
		return write(msg, Level.ERROR);
	}

	public boolean write(String msg) {
		return write(msg, Level.ALL);
	}

	public boolean write(String msg, Object[] param) {
		return write(MessageUtil.getMessage(msg, param), Level.ALL);
	}

	public boolean write(String msg, boolean isCheckMaxLine) {
		// �o�͍ő�s���`�F�b�N�t���O��ݒ肵write���\�b�h�����s
		return write(msg, Level.ALL, isCheckMaxLine);
	}

	public void close() throws IOException {
		// �N���[�Y����
		HDFSUtil.closeAll(os);
	}

	public static boolean collect(Configuration conf, String input, String output, boolean overwrite, boolean toLocal)
		throws IOException {
		// �o�͗L��
		boolean writed = false;
		if(conf != null && input != null && output != null) {
			InputStream is = null;
			OutputStream os = null;

			List<Path> list = HDFSUtil.getFilePathList(conf, input);
			if (list != null) {
				try {
					if (toLocal) {
						os = new FileOutputStream(output, overwrite);
					} else {
						os = HDFSUtil.getOutputStream(conf, output, overwrite);
					}
					for (Path path : list) {
						is = HDFSUtil.getInputStream(conf, path.toUri().getPath());
						HDFSUtil.copyBytes(conf, is, os, false);
						is.close();
					}
					writed = true;
				} finally {
					HDFSUtil.closeAll(is,os);
				}
			}
		}

		return writed;
	}

	public static boolean collect(Configuration conf, String input, String output, boolean overwrite)
		throws IOException {
		// HDFS��Ƀ��O�o��
		return collect(conf, input, output, overwrite, false);
	}

	public static int collect(Configuration conf, String input) throws IOException {
		int records = 0;
		if(conf != null && input != null) {
			List<Path> list = HDFSUtil.getFilePathList(conf, input);
			if (list != null) {
				BufferedReader br = null;
				try {
					for (Path path : list) {
						br = new BufferedReader(new InputStreamReader(HDFSUtil.getInputStream(conf,
								path.toUri().getPath())));
						String line = null;
						while  ((line = br.readLine()) != null) {
							BaseLogger.printWarnMessage(line);
							records++;
						}
						br.close();
					}
				} finally {
					HDFSUtil.closeAll(br);
				}
			}
		}
		return records;
	}

	private void init(TaskAttemptID id, String prefix) {
		String fileName = null;
		if (prefix == null) {
			fileName = conf.get(CommonConst.HDFS_LOG_FILE_PATH_AND_PREFIX);
		} else {
			fileName = prefix;
		}
		StringBuilder sb = new StringBuilder();
		if (fileName != null && !fileName.isEmpty()) {
			sb.append(fileName).append(CommonConst.FILE_NAME_CONNECTOR);
		} else {
			sb.append(conf.get(CommonConst.HDFS_LOG_FILE_PATH, conf.get(CommonConst.MAPRED_OUTPUT_DIR,"")));
			sb.append(CommonConst.FILE_SEPARATER);
		}
		sb.append(id);
		sb.append(CommonConst.LOG_FILE_EXT);
		path = sb.toString();
		converter = new PatternParser(CommonLogger.getInstance().getLogPattern()).parse();
		logger = Logger.getLogger(CommonConst.HDFS_LOG);
	}

	private String format(String msg, Level level) {
		StringBuffer sbuf = new StringBuffer();
		if (converter != null) {
			LoggingEvent event = new LoggingEvent(HDFSLogger.class.getName(), logger, level, msg, null);
			for (PatternConverter c = converter; c != null; c = c.next) {
				c.format(sbuf, event);
			}
		} else {
			sbuf.append(msg);
		}
		// ���b�Z�[�W�ԋp
		return sbuf.toString();
	}

	private boolean write(String msg, Level level) {
		// �o�͍ő�s���`�F�b�N�t���O��true��ݒ肵write���\�b�h�����s
		return write(msg, level, true);
	}

	private boolean write(String msg, Level level, boolean isCheckMaxLine) {
		boolean writed = false;
		boolean checkMaxLineResult = false;
		if (!created) {
			try {
				create();
			} catch (IOException e) {
				BaseLogger.printMessage(e);
			}
		}
		if (isCheckMaxLine) {
			if (currentLine < maxLine) {
				checkMaxLineResult = true;
			} else {
				checkMaxLineResult = false;
			}
		} else {
			checkMaxLineResult = true;
		}
		if (os != null && msg != null && checkMaxLineResult) {
			try {
				if (formatChange) {
					os.write(format(msg,level).getBytes(CommonConst.CHARSET_UTF8));
				} else {
					os.write(msg.getBytes(CommonConst.CHARSET_UTF8));
					os.flush();
				}
				os.write(CommonConst.LINE_SEPARATER.getBytes(CommonConst.CHARSET_UTF8));
				currentLine++;
				writed = true;
			} catch (IOException e) {
				BaseLogger.printMessage(e);
			}
		}
		// �o�͌��ʕԋp
		return writed;
	}
}
