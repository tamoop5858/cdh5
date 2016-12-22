package jp.co.fd.hadoop.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.TaskInputOutputContext;
import org.apache.hadoop.util.ReflectionUtils;

public class GeneralOutputsUtil<KEYOUT, VALUEOUT> {
	private static final String BASE_OUTPUT_NAME = "mapreduce.output.basename";
	private static final String MULTIPLE_OUTPUT = "mapreduce.multipleoutputs";
	private static final String MO_PREFIX = "mapreduce.multipleoutputs.namedOutput";

	private Map<String, TaskAttemptContext> taskContexts = new HashMap<String, TaskAttemptContext>();
	private TaskInputOutputContext<?, ?, KEYOUT, VALUEOUT> context = null;
	private Set<String> nameOutputs = null;
	private Map<String, RecordWriter<?,?>> recordWriters = null;

	public GeneralOutputsUtil(TaskInputOutputContext<?, ?, KEYOUT, VALUEOUT> context) {
		this.context = context;
		nameOutputs = Collections.unmodifiableSet(new HashSet<String>(GeneralOutputsUtil.getNameOutputsList(context)));
		recordWriters = new HashMap<String, RecordWriter<?,?>>();
	}
	public static void addNameOutPut(Job job,String namedOutput, Class<? extends OutputFormat> outputFormatClass,
			Class<?> keyClass, Class<?> valueClass) {
		checkNameOutputName(job, namedOutput, true);
		Configuration conf = job.getConfiguration();

		conf.set(MULTIPLE_OUTPUT, conf.get(MULTIPLE_OUTPUT, "").concat(" ").concat(namedOutput));
		conf.setClass(MO_PREFIX + namedOutput + ".format", outputFormatClass, OutputFormat.class);
		conf.setClass(MO_PREFIX + namedOutput + ".key", keyClass, Object.class);
		conf.setClass(MO_PREFIX + namedOutput + ".value", valueClass, Object.class);
	}
	public <K, V> void write(K key, V value, String baseOutputPath) throws IOException, InterruptedException {
		checkNameOutputPath(baseOutputPath);
		TaskAttemptContext taskContext = new TaskAttemptContext(context.getConfiguration(), context.getTaskAttemptID());
		//TaskAttemptContext taskContext = new TaskAttemptContextImpl(context.getConfiguration(), context.getTaskAttemptID());
		getRecordWriter(taskContext, baseOutputPath, baseOutputPath).write(key, value);

	}

	public <K, V> void write(String namedOutput, K key, V value, String baseOutputPath)
			throws IOException, InterruptedException {
		checkNameOutputName(context, namedOutput, false);
		checkNameOutputPath(baseOutputPath);

		if (!nameOutputs.contains(namedOutput)) {
			throw new IllegalArgumentException("Undefined named output '" + namedOutput + "'");
		}
		TaskAttemptContext taskContext = getTaskAttemptContext(namedOutput);

		getRecordWriter(taskContext, namedOutput, baseOutputPath).write(key, value);
	}
	public void close() throws IOException, InterruptedException {
		for (RecordWriter writer : recordWriters.values()) {
			writer.close(context);
		}
	}
	private static void checkNameOutputName(JobContext job, String nameOutput, boolean isDefined) {
		List<String> definedChannels = getNameOutputsList(job);
		if (isDefined && definedChannels.contains(nameOutput)) {
			throw new IllegalArgumentException("Named output" + nameOutput + "already Defined");
		} else if (!isDefined && !definedChannels.contains(nameOutput)) {
			throw new IllegalArgumentException("Named output" + nameOutput + "not Defined");
		}
	}
	private static void checkNameOutputPath(String nameOutput) throws IllegalArgumentException {
		if (nameOutput == null) {
			throw new IllegalArgumentException("output name cannot be part");
		}
	}
	private static List<String> getNameOutputsList(JobContext job) {
		List<String> names = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(job.getConfiguration().get(MULTIPLE_OUTPUT,"")," ");
		while (st.hasMoreTokens()) {
			names.add(st.nextToken());
		}
		return names;
	}
	private static Class<? extends OutputFormat<?, ?>> getNamedOutputFamartClass(JobContext job, String nameOutput) {
		return (Class<? extends OutputFormat<?, ?>>)
		job.getConfiguration().getClass(MO_PREFIX + nameOutput + ".format", null, OutputFormat.class);
	}
	private static Class<?> getNamedOutputKeyClass(JobContext job, String nameOutput) {
		return job.getConfiguration().getClass(MO_PREFIX + nameOutput + ".key", null, WritableComparable.class);
	}
	private static Class<? extends Writable> getNamedOutputValueClass(JobContext job, String nameOutput) {
		return job.getConfiguration().getClass(MO_PREFIX + nameOutput + ".value", null, Writable.class);
	}
	private RecordWriter getRecordWriter(TaskAttemptContext taskContext, String nameOutput, String baseFileName)
			throws IOException, InterruptedException {
		RecordWriter writer = recordWriters.get(nameOutput);

		if (writer == null) {
			taskContext.getConfiguration().set(BASE_OUTPUT_NAME, nameOutput.concat("/").concat(baseFileName));
			try {
				writer = ((OutputFormat) ReflectionUtils.newInstance(taskContext.getOutputFormatClass(),
						taskContext.getConfiguration())).getRecordWriter(taskContext);
			} catch (ClassNotFoundException e) {
				throw new IOException(e);
			}
			recordWriters.put(nameOutput, writer);
		}
		return writer;
	}
	private TaskAttemptContext getTaskAttemptContext(String nameOutput) throws IOException {
		TaskAttemptContext taskContext = taskContexts.get(nameOutput);
		if (taskContext != null) {
			return taskContext;
		}
		Job job = new Job(context.getConfiguration());
		job.setOutputFormatClass(getNamedOutputFamartClass(job, nameOutput));
		job.setOutputKeyClass(getNamedOutputKeyClass(job, nameOutput));
		job.setOutputValueClass(getNamedOutputValueClass(job, nameOutput));
		taskContext = new TaskAttemptContext(job.getConfiguration(), context.getTaskAttemptID());
		//taskContext = new TaskAttemptContextImpl(job.getConfiguration(), context.getTaskAttemptID());
		taskContexts.put(nameOutput, taskContext);
		return taskContext;
	}
}

