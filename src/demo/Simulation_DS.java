package demo;

import org.json.simple.JSONObject;
import datastore.DataStore;


public class Simulation_DS {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		DataStore ds=new DataStore("E:\\Eclipse java\\Data_Store\\src");
		JSONObject jsonObject=new JSONObject();
		//Arbitrary JSON object for Simulation
		jsonObject.put("demo", "value");
		
		
		//Creating five keyvalue pairs
		ds.create("lulu", jsonObject,2);
		ds.create("msdhoni",jsonObject);
		ds.create("vicky", jsonObject);
		ds.create("lulu", jsonObject);
		ds.create("vciky", jsonObject);
		ds.create("vckyi", jsonObject);
		
		
		//fetching all Pairs
		ds.getAll();
		
		//Getting Pair using Key
		System.out.println(ds.get("vckyi"));
		System.out.println(ds.get("vicky"));
		System.out.println(ds.get("vcyi"));
		
		//Sleeping for TTL Tests
		System.out.println("Main Thread sleeeping for 3 secs for TTL Test");
		Thread.sleep(4000);
		
		//Checking Key with characters > 32
		ds.create("vckyishdkhsdkhdkshkdhskdhskhdkhsdkshdjdshadjshjdhjahdshshadhshsdahdshdsjhdj", jsonObject);
		
		//Again Fetching all Entries
		ds.getAll();
		
		//Finally Saving evrythng to the file
		ds.saveAndClose();
		
	

	}

}
