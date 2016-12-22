package jp.co.fd.hadoop.common.log;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import jp.co.fd.hadoop.common.CommonConst;
import jp.co.fd.hadoop.common.util.MessageUtil;

import org.apache.log4j.Appender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.spi.LoggingEvent;

public class CommonLogger {

		/** �N���X�� */
		private static final String FQCN = CommonLogger.class.getName();

		/** PAD���K�[ */
		private static final String COMMON_LOGGER = "COMMON_LOGGER";

		/** PAD�A�y���_�[ */
		private static final String COMMON_APPENDER = "COMMON_APPENDER";

		/** ���K�[�C���f�b�N�X�ԍ� */
		private static int index = 1;

		/** ���K�[ */
		private Logger logger = null;

		/** �A�y���_�[ */
		private Appender appender = null;

		/** �C���X�^���X */
		private static CommonLogger instance = CommonLogger.getDefaultLogger();

		public static CommonLogger getInstance() {
			return instance;
		}

		private CommonLogger() {
			// �����Ȃ�
		}

		private static CommonLogger getDefaultLogger() {
			// �V�X�e���v���p�e�B~���O���x���擾
			String level = System.getProperty(CommonConst.LOGGER_LEVEL);
			// ���ϐ��ɒ�`����ĂȂ��ꍇ
			if (level == null) {
				// PDA�v���p�e�B�t�@�C������擾
				level = MessageUtil.getMessage(CommonConst.LOGGER_LEVEL);
			}

			// �V�X�e���v���p�e�B����o�̓p�^�[���擾
			String logPattern = System.getProperty(CommonConst.LOGGER_OUTPUT_PATTERN);
			// ���ϐ��ɒ�`����ĂȂ��ꍇ
			if (logPattern == null) {
				// PDA�v���p�e�B�t�@�C������o��
				logPattern = MessageUtil.getMessage(CommonConst.LOGGER_OUTPUT_PATTERN);
			}
			// ���O�p�^�[�����ݒ肳��ĂȂ��ꍇ
			if (logPattern == null || logPattern.length() == 0) {
				// �p�^�[�����w�肳��ĂȂ����߁A�f�B�t�H���g�l���p
				logPattern = CommonConst.DEFAULT_LOG_PATTERN;
			}

			// �V�X�e���v���p�e�B���烍�O�o�̓A�y���_�[�擾
			String appenderStr = System.getProperty(CommonConst.LOGGER_OUTPUT_APPENDER);
			// ���ϐ��ɒ�`����ĂȂ��ꍇ
			if (appenderStr == null) {
				// PDA�v���p�e�B�t�@�C������擾
				appenderStr = MessageUtil.getMessage(CommonConst.LOGGER_OUTPUT_APPENDER);
			}
			// ���O�p�^�[�����ݒ肳��ĂȂ��ꍇ
			if (appenderStr == null || appenderStr.length() == 0) {
				// �p�^�[�����w�肳��ĂȂ����߁A�f�B�t�H���g�l���p
				appenderStr = CommonConst.FILE_APPENDER;
			}

			// �V�X�e���v���p�e�B���烍�O�o�͐�擾
			String filename = System.getProperty(CommonConst.LOGGER_FILE_NAME);
			// ���ϐ��ɒ�`����ĂȂ��ꍇ
			if (filename == null) {
				// PDA�v���p�e�B�t�@�C������擾
				filename = MessageUtil.getMessage(CommonConst.LOGGER_FILE_NAME);
			}
			// �t�@�C�������w�肳��Ă���ꍇ�̂݃��K�[����
			if (filename != null && 0 < filename.length()) {
				// ���K�[�쐬
				return createLogger(filename, appenderStr, logPattern, Level.toLevel(level));
			} else {
				return new CommonLogger();
			}
		}

		public boolean canWrite() {
			return (logger != null && appender != null);
		}

		public void debug(Object message) {
			debug(message, null);
		}

		public void debug(Object message, Throwable throwable) {
			// DEBUG���O�o�͗L���m�F
			if (logger != null && logger.isDebugEnabled()) {
				writeLog(Level.DEBUG, message, throwable);
			}
		}

		public void info(Object message) {
			info(message, null);
		}

		public void info(Object message, Throwable throwable) {
			// INFO���O�o�͗L���m�F
			writeLog(Level.INFO, message, throwable);
		}

		public void warn(Object message) {
			warn(message, null);
		}

		public void warn(Object message, Throwable throwable) {
			// WARN���O�o�͗L���m�F
			writeLog(Level.WARN, message, throwable);
		}

		public void error(Object message) {
			error(message, null);
		}

		public void error(Object message, Throwable throwable) {
			// ERROR���O�o�͗L���m�F
			writeLog(Level.ERROR, message, throwable);
		}

		public String getLogPattern() {
			// ���O�p�^�[���擾
			String logPattern = CommonConst.DEFAULT_LOG_PATTERN.replaceAll(CommonConst.HOSTNAME_PATTERN, getHostname());
			// �A�y���_�[�m�F
			if (appender != null) {
				// ���C�A�E�g�擾
				Layout layout = appender.getLayout();
				// ���C�A�E�g�����݂��A�p�^�[�����C�A�E�g�̏ꍇ
				if (layout != null && layout instanceof PatternLayout) {
					logPattern = ((PatternLayout) layout).getConversionPattern();
				}
			}
			return logPattern;
		}

		public static CommonLogger createLogger(String filename) {
			// ���K�[�쐬
			return createLogger(filename, CommonConst.FILE_APPENDER, CommonConst.DEFAULT_LOG_PATTERN, Level.toLevel(null));
		}

		public static CommonLogger createLogger(String filename, String pattern) {
			// ���K�[�쐬
			return createLogger(filename, CommonConst.FILE_APPENDER, pattern, Level.toLevel(null));
		}

		public static CommonLogger createLogger(String filename, String appenderStr, String pattern, Level level) {
			// ���K�[�C���X�^���X����
			CommonLogger commonLogger = new CommonLogger();

			// �t�@�C����NULL�m�F
			if (filename != null && !filename.isEmpty()) {
				// �t�@�C���쐬
				File file = new File(filename);
				// �f�B���N�g�������݂��Ȃ��ꍇ�͏����Ȃ�
				if (file == null || file.getParentFile() == null || !file.getParentFile().isDirectory()) {
					return commonLogger;
				}

				try {
					// �p�^�[���m�F
					if (pattern.contains(CommonConst.HOSTNAME_PATTERN)) {
						// �z�X�g���u��
						pattern = pattern.replaceAll(CommonConst.HOSTNAME_PATTERN, getHostname());
					}
					// Appender�擾
					commonLogger.makeAppender(filename, appenderStr, pattern);

					// Logger�擾
					commonLogger.logger = Logger.getLogger(COMMON_LOGGER.concat(String.valueOf(CommonLogger.index++)));

					// �A�y���_�[�ǉ�
					commonLogger.logger.addAppender(commonLogger.appender);

					// ���O���x���ݒ�
					commonLogger.logger.setLevel(level);
				} catch (IOException e) {
					// ���O�o��
					BaseLogger.printMessage(e);
					// ������
					commonLogger.logger = null;
					commonLogger.appender = null;
				}
			}
			return commonLogger;
		}

		private void makeAppender(String filename, String appenderStr, String pattern) throws IOException {
			// �A�y���_�[�쐬
			if (0 <= CommonConst.DAILY_ROLLING_FILE_APPENDER.indexOf(appenderStr)) { // DailyRollingFileAppender�̏ꍇ
				appender = new DailyRollingFileAppender(new PatternLayout(pattern), filename, CommonConst.DEFAULT_DATE_ROLLING_PATTERN);

				// ���t�p�^�[���ݒ�
				String datePattern = System.getProperty(CommonConst.LOGGER_FILE_ROLLING_POSTFIX);
				if (datePattern != null && 0 < datePattern.length()) {
					((DailyRollingFileAppender) appender).setDatePattern(datePattern);
				}

			} else if (0 <= CommonConst.ROLLING_FILE_APPENDER.indexOf(appenderStr)) { // RollingFileAppender�̏ꍇ
				appender = new RollingFileAppender(new PatternLayout(pattern), filename);

				// MaxSize�ݒ�
				((RollingFileAppender) appender).setMaxFileSize(System.getProperty(CommonConst.LOGGER_FILE_MAX_SIZE,
						CommonConst.DEFAULT_LOG_MAX_SIZE));

				// MaxBackupIndex�ݒ�
				int maxBackups = 1;
				try {
					maxBackups = Integer.valueOf(System.getProperty(CommonConst.LOGGER_FILE_MAX_BACKUPS, CommonConst.DEFAULT_LOG_BACKUPS));
				} catch (NumberFormatException e) {
					// ���O�o��
					BaseLogger.printMessage(e);
				}
				((RollingFileAppender) appender).setMaxBackupIndex(maxBackups);
			} else { // ���̑��iFileAppender���܂ށj
				appender = new FileAppender(new PatternLayout(pattern), filename);
			}

			// �A�y���_�[���ݒ�
			appender.setName(COMMON_APPENDER);
		}

		private void writeLog(Level level, Object message, Throwable throwable) {
			// ���K�[NULL�m�F
			if (logger != null && appender != null) {
				// ���O���b�Z�[�W�ǉ�
				appender.doAppend(new LoggingEvent(FQCN, logger, level, message, throwable));
			}
		}

		private static String getHostname() {
			String hostname = "";
			try {
				// �z�X�g���擾
				hostname = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				// ���O�o��
				BaseLogger.printMessage(e);
			}
			// �z�X�g���ԋp
			return hostname;
		}

		public void close() {
			// �A�y���_�[�ݒ�L���m�F
			if (appender != null) {
				// �N���[�Y����
				appender.close();
				// ������
				appender = null;
			}
		}

}
