package jp.co.fd.hadoop.common.util;

public enum CounterID {
	INPUT_TOTAL_RECORD(1),
	INPUT_TOTAL_RECORD_MAP(2),
	ERROR_TOTAL_MAP_RECORD(3),
	ERROR_TOTAL_RECORD_RECORD(4),
	INVALID_TOTAL_MAP_RECORD(5),
	INVALID_TOTAL_RECORD_RECORD(6),

	LOG_OUTPUT_FAIL_COUNTER(10);
	private  int id = 0;
	private CounterID(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
}
