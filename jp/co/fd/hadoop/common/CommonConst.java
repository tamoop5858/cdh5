package jp.co.fd.hadoop.common;

public class CommonConst {
	/**
	 *
	 */
	private CommonConst(){
	}

//	public static final String FILE_SEPARATER = System.getProperty("file.separator");
	public static final String FILE_SEPARATER = "/";
	public static final String LINE_SEPARATER = System.getProperty("line.separator");


	public static final String STRING_BLANK = "";
	public static final String STRING_COMMA = ",";
	public static final String STRING_DOT = ".";
	public static final String FILE_PATTERN_ALL = "/*";
	public static final String PATTERN_ALL = "*";
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String CHARSET_MS932 = "MS932";
	public static final String STRING_TAB = "\t";
	public static final String STRING_YES = "Y";
	public static final String STRING_NOT = "N";

	public static final String CONSOLE_LOG_DATE_PATTERN = "yyyy/MM/dd HH:mm:ss.SSS";
	public static final String BASE_LOG_PATTERN = "%d{yyyy/MM/dd HH:mm:ss.SSS},%h,%p,%X{type},%X{id},%X{cnt},%m%n";

	public static final String JOB_NAME_CALL_DETAIL = "SBTM_CallDetail";
	public static final String MAP_NUM_BASENAME = "MapReduce.MapNumTasks";
	public static final String REDUCE_NUM_BASENAME = "MapReduce.ReduceNumTasks";

	public static final String CONF_KEY_MAPNUM = "mapred.map.tasks";
	public static final String CONF_KEY_OUTPUT = "mapred.output.dir";

	public static final String MAPRED_CONFIG_HASH_PARTIION = "config.hashpartion";


	public static final String NEWLINE_CRLF = "\r\n";

	public static final String MAPRED_OUTPUT_COMPRESS = "mapred.output.compress";
	public static final String MAPRED_OUTPUT_COMPRESS_EXT = "mapred.output.compress.ext";
	public static final String MAPRED_OUTPUT_COMPRESS_TYPE = "mapred.output.compression.type";

	public static final String TEXTOUTPUT_FORMAT_SEPARATOR  = "mapred.textoutputformat.separator";
	public static final String TEXTOUTPUT_NEWLINE = "mapred.textoutputformat.newline";


	//���@

	public static final String OUTPUT_COMPRESS = "mapred.output.compress";

	public static final String COMPRESS_MAP_OUTPUT = "mapred.compress.map.output";

	public static final String OUTPUT_COMPRESSION_CODEC = "mapred.output.compression.codec";

	public static final String MAP_OUTPUT_COMPRESSION_CODEC = "mapred.map.output.compression.codec";

	public static final String MAP_TASKS_SPECULATIVE_EXECUTION = "mapred.map.tasks.speculative.execution";

	public static final String REDUCE_TASKS_SPECULATIVE_EXECUTION = "mapred.reduce.tasks.speculative.execution";

	public static final String JOB_QUEUE_NAME = "mapred.job.queue.name";

	public static final String KEY_COMPRESS_DEFAULT = "Key.Compress.Default";
	public static final String KEY_COMPRESS_SEQEXT = "Key.Compress.Seqext";
	public static final String KEY_COMPRESS_USEGZIP = "Key.Compress.UseGzip";

	//
	/** HDFS��̃��O�t�@�C���p�X */
	public static final String HDFS_LOG_FILE_PATH = "mapred.hdfs.log.file.path";

	/** HDFS��̃��O�t�@�C���p�X */
	public static final String HDFS_LOG_FILE_PATH_AND_PREFIX = "mapred.hdfs.log.file.prefix";

	/** Map/Reduce�o�͐�p�X */
	public static final String MAPRED_OUTPUT_DIR = "mapred.output.dir";
	/** HDFS���O�� */
	public static final String HDFS_LOG = "HDFS";

	/** �t�@�C�����ڑ��q */
	public static final String FILE_NAME_CONNECTOR = "_";

	/** ���O�t�@�C���g���q */
	public static final String LOG_FILE_EXT = ".log";

	public static final String TSV_FILE_EXT = ".tsv";
	public static final String FILE_PREFIX_PART = "part";
	//
	/** �z�X�g���u�������� */
	public static final String HOSTNAME_PATTERN = "%h";

	/** ���O�o�̓p�^�[�� */
	public static final String DEFAULT_LOG_PATTERN = "%d{yyyy/MM/dd HH:mm:ss.SSS},%h,%p,%m%n";

	/** ���t���[�����O�p�^�[�� */
	public static final String DEFAULT_DATE_ROLLING_PATTERN = "'.'yyyy-MM-dd";

	/** ���O�t�@�C���T�C�Y */
	public static final String DEFAULT_LOG_MAX_SIZE = "10MB";

	/** ���O�o�b�N�A�b�v�� */
	public static final String DEFAULT_LOG_BACKUPS = "1";

	/** �t�@�C���A�y���_�[ */
	public static final String FILE_APPENDER = "org.apache.log4j.FileAppender";

	/** �t�@�C���A�y���_�[ */
	public static final String ROLLING_FILE_APPENDER = "org.apache.log4j.RollingFileAppender";

	/** �t�@�C���A�y���_�[ */
	public static final String DAILY_ROLLING_FILE_APPENDER = "org.apache.log4j.DailyRollingFileAppender";

	/** �t�@�C���� */
	public static final String LOGGER_FILE_NAME = "logger.file.name";

	/** �t�@�C���g���q�� */
	public static final String LOGGER_FILE_ROLLING_POSTFIX = "logger.file.rolling.postfix";

	/** �t�@�C���T�C�Y�� */
	public static final String LOGGER_FILE_MAX_SIZE = "logger.file.max.size";

	/** �t�@�C���o�b�N�A�b�v���� */
	public static final String LOGGER_FILE_MAX_BACKUPS = "logger.file.max.backups";

	/** ���O���x�� */
	public static final String LOGGER_LEVEL = "logger.level";

	/** �p�^�[���� */
	public static final String LOGGER_OUTPUT_PATTERN = "logger.pattern";

	/** �A�y���_�[�� */
	public static final String LOGGER_OUTPUT_APPENDER = "logger.appender";

	//BeanID
	public static final byte BEAN_ID_BM_PARAMETER_RFRNC = 1;
	public static final byte BEAN_ID_CYCLE_BILL_GROUP = 2;
	public static final byte BEAN_ID_SUMMARIZE_BILL_GROUP = 3;
	public static final byte BEAN_ID_CDR_SBTM = 4;
	public static final byte BEAN_ID_CALL_DETAIL = 5;

	//RecordType
	public static final byte RECORD_TYPE_BM_PARAMETER_RFRNC = 1;
	public static final byte RECORD_TYPE_SUMMARIZE_BILL_GROUP = 2;
	public static final byte RECORD_TYPE_CYCLE_BILL_GROUP = 3;
	public static final byte RECORD_TYPE_CDR_SBTM = 4;
	public static final byte RECORD_TYPE_CALL_DETAIL = 5;
	//SortType
	public static final byte SORT_TYPE_BM_PARAMETER_RFRNC = 1;
	public static final byte SORT_TYPE_BILL_GROUP_ID= 2;
	public static final byte SORT_TYPE_CALL_DETAIL= 3;

	//conf
	public static final String PRAA_CALLDETAIL_BILL_MONTH = "PRAA_CALLDETAIL_BILL_MONTH";
	public static final String PRAA_CALLDETAIL_BILL_CYCLE_ID = "PRAA_CALLDETAIL_BILL_CYCLE_ID";
	public static final String PRAA_CALLDETAIL_TREATMENT_MODE = "CALLDETAIL_TREATMENT_MODE";
	public static final String PRAA_TREATMENT_SERIAL_NUMBER = "TREATMENT_SERIAL_NUMBER";
	public static final String OUT_TEMP_PATH_REFERENCE = "OUT_TEMP_PATH_REFERENCE";


}
