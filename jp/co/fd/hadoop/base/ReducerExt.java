package jp.co.fd.hadoop.base;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;

public class ReducerExt <KEYIN, VALUEIN, KEYOUT, VALUEOUT> extends
	Reducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {
	public ReducerExt() {
	}
	public void before(Context context) throws IOException, InterruptedException {
		setup(context);
	}
	public void execute(Context context) throws IOException, InterruptedException {
		while (context.nextKey()) {
			reduce(context.getCurrentKey(), context.getValues(), context);
		}
	}
	public void after(Context context) throws IOException, InterruptedException {
		cleanup(context);
	}
}
