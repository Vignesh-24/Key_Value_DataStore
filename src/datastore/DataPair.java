package datastore;

import org.json.simple.JSONObject;

public class DataPair {
	
	private String key;
	private JSONObject value;
	private int TTL;	//Time To Live
	private DataPair next;
	
	public DataPair(String key,JSONObject value,int ttl) {
		this.key=key;
		this.value=value;
		this.next=null;
		this.TTL=ttl;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public JSONObject getValue() {
		return value;
	}

	public void setValue(JSONObject value) {
		this.value = value;
	}

	public int getTTL() {
		return TTL;
	}

	public void setTTL(int tTL) {
		TTL = tTL;
	}

	public DataPair getNext() {
		return next;
	}

	public void setNext(DataPair next) {
		this.next = next;
	}

	

}
