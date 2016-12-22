package jp.co.fd.hadoop.common.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobID;
import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.util.ReflectionUtils;


public class HDFSUtil {
	public static final String GZIP_CODEC_CLASS = "org.apache.hadoop.io.compress.GzipCodec";
	private HDFSUtil(){

	}
	public static FileSystem getFileSystem(Configuration conf) throws IOException {
		FileSystem fs = null;
		if (conf != null) {
			fs = FileSystem.get(conf);
		}
		return fs;
	}



	public static boolean isPathExist(Configuration conf, String path) throws IOException {
		boolean exist = false;
		if (conf != null && !(path.isEmpty())) {
			FileSystem fs = getFileSystem(conf);
			if (fs != null && fs.exists(new Path(path))) {
				exist = true;
			}
		}
		return exist;
	}

	public static boolean isDir(Configuration conf, String path) throws IOException {
		boolean isDir = false;
		if (isPathExist(conf, path)) {
			FileStatus status = getFileSystem(conf).getFileStatus(new Path(path));
			if (status !=null && status.isDir()) {
				isDir = true;
			}
		}
		return isDir;
	}

	public static List<String> getFileList(Configuration conf, String pathPattern) throws IOException {
		List<String> list = new ArrayList<String>();

		if (conf != null && !(pathPattern.isEmpty())) {
			FileSystem fs = getFileSystem(conf);
			FileStatus[] fileStatus = null;
			if (isDir(conf, pathPattern)) {
				fileStatus = fs.globStatus(new Path(new Path(pathPattern).toUri().getPath().concat("/*")));
			} else {
				fileStatus = fs.globStatus(new Path(pathPattern));
			}
			if (fileStatus != null) {
				for (FileStatus status : fileStatus) {
					if (!status.isDir()) {
						list.add(status.getPath().toUri().getPath());
					}
				}
			}
		}
		return list;
	}
	public static List<Path> getFilePathList(Configuration conf, String pathPattern) throws IOException {
		List<Path> list = new ArrayList<Path>();

		if (conf != null && !(pathPattern.isEmpty())) {
			FileSystem fs = getFileSystem(conf);
			FileStatus[] fileStatus = null;
			if (isDir(conf, pathPattern)) {
				fileStatus = fs.globStatus(new Path(new Path(pathPattern).toUri().getPath().concat("/*")));
			} else {
				fileStatus = fs.globStatus(new Path(pathPattern));
			}
			if (fileStatus != null) {
				for (FileStatus status : fileStatus) {
					if (!status.isDir()) {
						list.add(status.getPath());
					}
				}
			}
		}
		return list;
	}
	public static List<String> getAllFileList(Configuration conf, String path) throws IOException {
		List<String> list = new ArrayList<String>();

		List<Path> paths = getAllFilePathList(conf, path);
		if (paths != null) {
			for (Path file : paths) {
				list.add(file.toUri().getPath());
			}
		}
		return list;
	}
	public static List<Path> getAllFilePathList(Configuration conf, String path) throws IOException {
		List<Path> list = new ArrayList<Path>();

		if (conf != null && !(path.isEmpty())) {
			FileSystem fs = getFileSystem(conf);
			if (fs != null) {
				getFiles(list, fs, fs.getFileStatus(new Path(path)));
			}
		}
		return list;
	}
	public static List<String> getDirList(Configuration conf, String pathPattern) throws IOException {
		List<String> list = new ArrayList<String>();

		if (conf != null && !(pathPattern.isEmpty())) {
			FileSystem fs = getFileSystem(conf);
			if (fs != null) {
				FileStatus[] fileStatus = null;
				if (isDir(conf, pathPattern)) {
					fileStatus = fs.globStatus(new Path(new Path(pathPattern).toUri().getPath().concat("/*")));
				} else {
					fileStatus = fs.globStatus(new Path(pathPattern));
				}
				if (fileStatus != null) {
					for (FileStatus status : fileStatus) {
						if (status.isDir()) {
							list.add(status.getPath().toUri().getPath());
						}
					}
				}
			}
		}
		return list;
	}
	public static List<Path> getDirPathList(Configuration conf, String pathPattern) throws IOException {
		List<Path> list = new ArrayList<Path>();

		if (conf != null && !(pathPattern.isEmpty())) {
			FileSystem fs = getFileSystem(conf);
			if (fs != null) {
				FileStatus[] fileStatus = null;
				if (isDir(conf, pathPattern)) {
					fileStatus = fs.globStatus(new Path(new Path(pathPattern).toUri().getPath().concat("/*")));
				} else {
					fileStatus = fs.globStatus(new Path(pathPattern));
				}
				if (fileStatus != null) {
					for (FileStatus status : fileStatus) {
						if (status.isDir()) {
							list.add(status.getPath());
						}
					}
				}
			}
		}
		return list;
	}
	public static FileStatus[] getFileStatus(Configuration conf, String pathPattern) throws IOException {
		FileStatus[] fileStatus = null;

		if (conf != null && !(pathPattern.isEmpty())) {
			FileSystem fs = getFileSystem(conf);
			if (fs != null) {
				fileStatus = fs.globStatus(new Path(pathPattern));
			}
		}
		return fileStatus;
	}
	public static long getFileSize(Configuration conf, String path) throws IOException {
		long size = 0;

		if (conf != null && !(path.isEmpty())) {
			Path srcPath = new Path(path);

			FileSystem fileSystem = srcPath.getFileSystem(conf);
			Path[] pathItems = FileUtil.stat2Paths(fileSystem.globStatus(srcPath), srcPath);
			FileStatus[] items = fileSystem.listStatus(pathItems);

			if ((items != null) && (items.length != 0 || fileSystem.exists(srcPath))) {
				for (int i = 0; i< items.length; i++) {
					if (items[i].isDir()){
						size += fileSystem.getContentSummary(items[i].getPath()).getLength();
					} else {
						size += items[i].getLen();
					}
				}
			}
		}
		return size;
	}
	public static void mkdir(Configuration conf, String path) throws IOException{
		mkdir(conf, path, null);
	}
	/**
	 *
	 * @param conf
	 * @param path
	 * @param mode 777��3������
	 * @throws IOException
	 */
	public static void mkdir(Configuration conf, String path,String mode) throws IOException{
		if (conf != null && !(path.isEmpty())) {
			FileSystem fs = getFileSystem(conf);
			Path dir = new Path(path);
			if ((fs != null) && !(fs.exists(dir))) {
				if (!fs.mkdirs(dir)){
					throw new IOException("�f�B���N�g���������s�F" + path);
				} else {
					if (mode != null && !(mode.isEmpty())) {
						fs.setPermission(dir, new FsPermission(mode));
					}
				}
			}
		}
	}
	public static void move(Configuration conf, String from, String to) throws IOException{
		if (conf != null && !(from.isEmpty()) && !(to.isEmpty())) {
			FileSystem fs = getFileSystem(conf);
			if (fs == null){
				return;
			}
			Path fromPath = new Path(from);
			boolean renamed = false;

			if (isDir(conf, from)) {
				renamed = fs.rename(fromPath, new Path(to));
			} else {
				FileStatus[] status = fs.globStatus(fromPath);
				if (status != null) {
					if (1 < status.length && isPathExist(conf, to) && !isDir(conf, to)) {
						throw new IOException("�R�s�[�悪�������݂��Ă���B" + to);
					}
					for (int i = 0; i < status.length; i++) {
						renamed = fs.rename(status[i].getPath(), new Path(to));
						if (!renamed) {
							break;
						}
					}
				}
			}
			if (!renamed) {
				throw new IOException("�t�@�C���y�уf�B���N�g���̈ړ������s���܂����B" + from + "��" + to);
			}
		}
	}
	public static void del(Configuration conf, String path) throws IOException{
		if (isPathExist(conf, path)) {
			FileSystem fs = getFileSystem(conf);
			boolean deleted = fs.delete(new Path(path),true);
			if (!deleted) {
				throw new IOException("�t�@�C���y�уf�B���N�g���̍폜�����s���܂����B" + path);
			}
		}
	}
	public static void copyToLocal(Configuration conf, String from, String to) throws IOException{
		if (isPathExist(conf, from)) {
			FileSystem fs = getFileSystem(conf);
			fs.copyToLocalFile(new Path(from), new Path(to));
		}
	}
	public static void copyFromLocal(Configuration conf, String from, String to) throws IOException{
		if (from != null && to != null && !(from.isEmpty()) && !(to.isEmpty())) {
			File file = new File(from);
			if (file.exists()) {
				FileSystem fs = getFileSystem(conf);
				if (fs != null) {
					fs.copyFromLocalFile(new Path(from), new Path(to));
				} else {
					throw new IOException("���[�J������HDFS�ɃR�s�[�����s���܂����B" + from + "��" + to);
				}

			}
		}
	}
	public static void copy(Configuration conf, String from, String to) throws IOException{
		if (conf != null && !(from.isEmpty()) && !(to.isEmpty())) {
			Path fromPath = new Path(from);
			Path toPath = new Path(to);
			FileSystem srcFs = fromPath.getFileSystem(conf);
			FileSystem dstFs = toPath.getFileSystem(conf);

			Path[] srcs = FileUtil.stat2Paths(srcFs.globStatus(fromPath), fromPath);
			if (1 < srcs.length && isPathExist(conf, to) && !isDir(conf, to)) {
				throw new IOException("�R�s�[�悪�������݂��Ă���B" + to);
			}
			for (int i = 0; i < srcs.length; i++) {
				boolean result = FileUtil.copy(srcFs, srcs[i], dstFs, toPath, false, true, conf);
				if (!result) {
					throw new IOException("HDFS��R�s�[�����s���܂����B" + from + "��" + to);
				}
			}

		}
	}
	public static void copyBytes(Configuration conf, InputStream is, OutputStream  os) throws IOException{
		copyBytes(conf, is, os, true);
	}
	public static void copyBytes(Configuration conf, InputStream is, OutputStream  os, boolean close) throws IOException{
		if (conf != null && is != null && os != null) {
			IOUtils.copyBytes(is, os, conf, close);
		}
	}
	public static FSDataInputStream getInputStream(Configuration conf, String path) throws IOException{
		FSDataInputStream dis = null;
		if (isPathExist(conf, path)) {
			FileSystem fs = getFileSystem(conf);
			dis = fs.open(new Path(path));
		}
		return dis;
	}
	public static InputStream getInputStreamEx(Configuration conf, String path) throws IOException{
		FSDataInputStream dis = getInputStream(conf, path);
		if (dis != null) {
			Path file = new Path(path);
			CompressionCodecFactory factory = new CompressionCodecFactory(conf);
			CompressionCodec codec = factory.getCodec(file);

			if (codec != null) {
				return codec.createInputStream(dis);
			}
		}
		return dis;
	}

	public static FSDataOutputStream getOutputStream(Configuration conf, String path) throws IOException{
		return getOutputStream(conf, path, true);
	}
	public static FSDataOutputStream getOutputStream(Configuration conf, String path, boolean overwrite) throws IOException{
		FSDataOutputStream dos = null;
		if (conf != null && !(path.isEmpty())) {
			FileSystem fs = getFileSystem(conf);
			dos = fs.create(new Path(path), overwrite);
		}
		return dos;
	}
	public static void closeAll(Closeable... closeables) throws IOException{
		IOException ioException = null;
		for (java.io.Closeable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (IOException e) {
					if (ioException == null) {
						ioException = e;
					}
				}
			}
		}
		if (ioException != null) {
			throw ioException;
		}
	}
	public static String addHDFSFileToDistributedCache(Configuration conf, String path)
		throws IOException, URISyntaxException {
		String fragment = null;
		if (conf != null && !(path.isEmpty())) {
			FileSystem fs = getFileSystem(conf);
			Path destPath = new Path(path);
			if (fs != null && fs.exists(destPath)) {
				DistributedCache.addCacheFile(getPathURI(destPath, null), conf);
				DistributedCache.createSymlink(conf);
				fragment = destPath.getName();
			}
		}
		return fragment;
	}
	public static CompressionCodec getGzipCodec(Configuration conf) throws ClassNotFoundException {
		CompressionCodec codec = null;
		if (conf != null) {
			Class<?> codecClass = Class.forName(GZIP_CODEC_CLASS);
			codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);
		}
		return codec;
	}

	public static String searchJob(Configuration conf, String jobName)
			throws IOException {
		String jobId = null;
		if (conf != null && jobName != null && conf instanceof JobConf) {
			JobClient jobClient = new JobClient((JobConf) conf);
			JobStatus[] jobStatus = jobClient.jobsToComplete();
			if (jobStatus != null) {
				for (int i = 0; i < jobStatus.length; i++) {
					RunningJob job = jobClient.getJob(jobStatus[i].getJobID());
					if (jobName.equals(job.getJobName())) {
						jobId = jobStatus[i].getJobID().toString();
					}
				}
			}
		}
		return jobId;
	}
	public static void killJob(Configuration conf, String jobId) throws IOException {
		if (conf != null && jobId != null && conf instanceof JobConf) {
			JobClient jobClient = new JobClient((JobConf) conf);
			RunningJob job = jobClient.getJob(JobID.forName(jobId));
			if (job != null) {
				throw new IOException("�Y����Job�����݂��܂���B" + jobId);
			}
			job.killJob();
		}
	}

	public static boolean hasReadPermission(Configuration conf, String path) throws IOException {
		return checkPermission(conf, path, FsAction.READ);
	}
	public static boolean hasWritePermission(Configuration conf, String path) throws IOException {
		return checkPermission(conf, path, FsAction.WRITE);
	}
	private static boolean checkPermission(Configuration conf, String path, FsAction action) throws IOException {
		boolean isOk = false;
		if (isPathExist(conf, path)) {
			FileSystem fs = getFileSystem(conf);
			FileStatus status = fs.getFileStatus(new Path(path));
			FsPermission permission = status.getPermission();

			UserGroupInformation ugi = UserGroupInformation.getCurrentUser();
			if (ugi.getUserName().equals(status.getOwner())) {
				if (permission.getUserAction().implies(action));
				isOk = true;
			} else {
				String supergroup = conf.get("dfs.permissions.supergroup", "supergroup");
				String[] groups = ugi.getGroupNames();
				for (String group : groups) {
					if (supergroup.equals(group) || group.equals(status.getGroup())) {
						if (permission.getGroupAction().implies(action)) {
							isOk = true;
							break;
						}
					}
				}
			}
		}
		return isOk;
	}
	public static List<String> getRecords(Configuration conf, String path, int recordCount) throws IOException {
		List<String> records = new ArrayList<String>();
		if (!isPathExist(conf, path)) {
			return records;
		}
		BufferedReader br = null;
		try {
			if (0 < recordCount) {
				br = new BufferedReader(new InputStreamReader(getInputStream(conf, path)));
				int lines = 0;
				String line = "";
				while (lines < recordCount) {
					line = br.readLine();
					if (line == null) {
						break;
					}
					records.add(line);
					lines++;
				}
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
		return records;
	}
	private static URI getPathURI(Path destPath, String framents) throws URISyntaxException {
		URI pathUri = destPath.toUri();
		if (pathUri.getFragment() == null) {
			if (framents == null) {
				pathUri = new URI(pathUri.toString().concat("#").concat(destPath.getName()));
			} else {
				pathUri = new URI(pathUri.toString().concat("#").concat(framents));
			}
		}
		return pathUri;
	}
	private static void getFiles(List<Path> list, FileSystem fs, FileStatus file) throws IOException {
		if (file.isDir()) {
			FileStatus[] files = fs.listStatus(file.getPath());
			if (files != null) {
				for (FileStatus fileStatus : files) {
					if (fileStatus.isDir()) {
						getFiles(list, fs, fileStatus);
					} else {
						list.add(fileStatus.getPath());
					}
				}
			}
		} else {
			list.add(file.getPath());
		}
	}

	public static List<Writable> readSequenceFileList(Configuration conf, String inputFilePath) throws IOException {
		List<Writable> data = new ArrayList<Writable>();
		if (isDir(conf, inputFilePath)) {
			return data;
		}
		if (!isPathExist(conf, inputFilePath)) {
			return data;
		}
		Reader reader = null;
		try {

			FileSystem fs = FileSystem.get(conf);
			reader = new Reader(fs, new Path(inputFilePath), conf);
			Writable keyBean = (Writable) ReflectionUtils.newInstance(reader
					.getKeyClass(), conf);
			Writable valueBean = (Writable) ReflectionUtils.newInstance(reader
					.getValueClass(), conf);
			while (reader.next(keyBean, valueBean)) {
				data.add(valueBean);
				keyBean = (Writable) ReflectionUtils.newInstance(reader
						.getKeyClass(), conf);
				valueBean = (Writable) ReflectionUtils.newInstance(reader
						.getValueClass(), conf);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			finally {
			if (reader != null) {
				reader.close();
			}
			reader = null;
		}
		return data;
	}
    public static List<String> readRecords(Configuration conf, String path) throws IOException {
        List<String> records = new ArrayList<String>();
        if (!isPathExist(conf, path)) {
            return records;
        }
        BufferedReader br = null;
        try {
                br = new BufferedReader(new InputStreamReader(getInputStreamEx(conf, path)));
                String line = "";
                while (line != null) {
                    line = br.readLine();
                    records.add(line);
                }
        } finally {
            if (br != null) {
                br.close();
            }
        }

        return records;
    }
}
