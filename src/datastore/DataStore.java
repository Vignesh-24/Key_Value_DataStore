package datastore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class DataStore {
	

	private String filepath;
	private ArrayList<DataPair> hashtable;
	private long file_size=0;
	private TTL_Monitor monitor;
	private int flag=0;
	
	
	
	// Inner class for monitoring Time-To-Live(TTL)
	private class TTL_Monitor implements Runnable {
		
		PriorityQueue<DataPair> queue;
		int timer=0;
		DataPair victim=null;
		
		public TTL_Monitor() {
			// TODO Auto-generated constructor stub
			 queue=new PriorityQueue<>(5, new TTL_Comparator()) ;
			 System.out.println("TTL Monitor Started");
		}
		
		
		
		@Override
		public void run() {
			
			while(flag==1) {
				try {
					Thread.sleep(1000);
					timer++;
					if(victim==null && !queue.isEmpty())
						victim=queue.poll();
					if(victim!=null) {
						if(victim.getTTL() <= timer) {
							delete(victim.getKey());
							victim=null;
						}
							
					}
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		

	}
	
	
	
	
	
	
	public DataStore(String path) {
		
		this.filepath=path+"\\Data.json";
		this.initialize_table();
		createFile(filepath);
		this.monitor=new TTL_Monitor();
		this.flag=1;
		
		//Starting thread to monitor TTL
		Thread thread = new Thread(this.monitor);  
		thread.start();  
	
	}
	
	
	
	public DataStore() {
		
		this.filepath="Data.json";
		this.initialize_table();
		createFile(filepath);
		this.monitor=new TTL_Monitor();
		this.flag=1;
		
		//Starting thread to monitor TTL
		Thread thread = new Thread(this.monitor);  
		thread.start();  
		
	}
	
	
	
	
	
	private void createFile(String path) {
		File file=new File(path);
		try {
			
			if(file.createNewFile())
				System.out.println("File Created Successfully: "+path);
			
			else {
				System.out.println("File Already Exists...!");
				System.out.println("Loading Data from Existing File...!");
				
				
				if(this.LoadData(file)) {
					System.out.println("Data Loaded Sucessfully");
				}
				
				
				else {
					System.out.println("Load Unsuccessfull... Creating New File");
					file.delete();
					file.createNewFile();
					System.out.println("File Created Successfully: "+path);
				}
				
				
					
			}
			
		
		} catch (IOException io) {
			System.out.println("I/O Exception Occurred..!" +"\n");
		} catch(SecurityException se) {
			System.out.println("Security Exception Occurred! Please Provide rights to create File in the specified path or provide a new location!!");
		}
		
		this.file_size=file.length();
		
	}
	
	
	
	
	
	
	private void initialize_table() {
		this.hashtable=new ArrayList<>();
		for(int i=0;i<32;i++)
			this.hashtable.add(null);
			
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	private Boolean LoadData(File file) {
		JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(file))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray pairList = (JSONArray) obj;
            pairList.forEach( pair -> this.parseDataPair( (JSONObject) pair));
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            return false;
        } catch (IOException e) {
            return false;
           
        } catch (ParseException e) {
            System.out.println("Parse Exception");
            return false;
        }
		
		return true;
		
	}
	
	
	
	
	
	
	private void parseDataPair(JSONObject data) {
		JSONObject pair= (JSONObject) data.get("pair");
		this.create(pair.get("key").toString(), (JSONObject) pair.get("val"), -1);
		
	}
	
	
	
	

	/** Function to find index of bucket, index calculated as hashCode(key)& (n-1) **/
	private int getHashIndex(String key) {
		return key.hashCode()&31;
		
	}
	
	
	
	
	
	/** To implement Thread Safety, "synchronized" method is used **/
	public synchronized Boolean create(String key, JSONObject value,int TTL) {
		try {
			
			if(key.length()>32) {
				System.out.println("ERROR:Maximum Key length 32 exceeded!!!");
				return false;
			}
			
			if(!((key != null) && (!key.equals("")) && (key.matches("^[a-zA-Z]*$")))) {
				System.out.println("ERROR: Key Should Contain only Alphabets!!!");
				return false;
			}
			
			//Estimating New Object Size as 16KB and checking whether 1GB Limit will exceed or not 
			if ((this.file_size+(16*1024)) > 1024*1024*1024) {
				System.out.println("Error File Size Limit Exceeded!! No more Insertion can be done!!");
				return false;
			}
				
			DataPair dp=new DataPair(key, value, TTL);
			//If TTL is set, adding DataPair to the TTL_Monitor Queue
			if(TTL!=-1) {
				dp.setTTL(dp.getTTL()+this.monitor.timer);
				this.monitor.queue.add(dp);
				System.out.println("Key: "+key+" is added to Monitor queue");
			}
			
			int index=this.getHashIndex(key);
			DataPair itr=hashtable.get(index);
			
			// if the index is free, add there itself
			if(itr==null) {
				hashtable.set(index, dp);
				System.out.println("Insertion Sucessfull with Key: "+key);
				return true;
			}
			
			//If the index occupied already, iterate until find empty space
			while(itr.getNext() != null && !itr.getKey().equals(key)) 
				itr=itr.getNext();
			
			if(itr.getKey().equals(key)) {
				System.out.println("Key Already Exists");
				return false;
			}
			
			itr.setNext(dp);
			System.out.println("Insertion Sucessfull with Key: "+key);
			return true;
			
			
			
		} catch (Exception ex) {
			System.out.println("Creation Failed: "+ex.toString());
			return false;
		}
		
		
		
	}
	
	
	
	
	//To provide Optional TTL parameters
	public synchronized Boolean create(String key, JSONObject value) {
		return this.create(key, value, -1);
	}
	
	
	
	
	public synchronized Boolean delete(String key) {
		
		int index=this.getHashIndex(key);
		System.out.println("Deleting Record with Key : "+key);
		DataPair itr=hashtable.get(index);
		if(itr!=null && itr.getKey().equals(key)) {
			hashtable.set(index, itr.getNext());
			//Garbage Collector will take care of deleting content from memory if reference is null
			itr=null;	
			System.out.println("Record with key : "+key+" Deleted..!");
			this.file_size-=(16*1024);
			return true;
		}
		
		DataPair prevDataPair=null;
		while(itr!=null && !itr.getKey().equals(key)){
			prevDataPair=itr;
			itr=itr.getNext();
		}
		
		if(itr==null) {
			System.out.println("No Record Found");
			return false;
		}
		
		else {
			prevDataPair.setNext(itr.getNext());
			itr=null;
		}
		this.file_size-=(16*1024);
		System.out.println("Record with key : "+key+" Deleted..!");
		return true;
		
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	private Boolean save(File filename) {
		
		JSONObject js;
		JSONArray pairlist=new JSONArray();
		for(int index=0;index<32;index++) {
			DataPair dp=hashtable.get(index);
			while(dp!=null) {
				js=new JSONObject();
				js.put("key",dp.getKey());
				js.put("val",dp.getValue());
				JSONObject obj=new JSONObject();
				obj.put("pair", js);
				pairlist.add(obj);
				dp=dp.getNext();
			}
			
		}
		
		System.out.println("Saving to the File....!");
		try (FileWriter file = new FileWriter(filename)) {
			 
            file.write(pairlist.toString());
            file.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
		System.out.println("Save Successfull");
		return true;
		
	}
	
	
	
	
	
	
	public synchronized JSONObject get(String key) {
		
		int index=this.getHashIndex(key);
		System.out.println("Get Request for Key: "+key);
		DataPair dpr=this.hashtable.get(index);
		while(dpr!=null && !dpr.getKey().equals(key)) 
			dpr=dpr.getNext();
		
		if(dpr==null)
			System.out.println("Key Does not Exist");
		else
			return dpr.getValue();
		
		return new JSONObject();
			
		
	}
	
	
	
	
	
	
	//Utility Function to read JSON from STANDARD INPUT
	public JSONObject readJSON() {
		Scanner scanner=new Scanner(System.in);
		StringBuffer sb=new StringBuffer("");
		System.out.println("Enter JSON Data: ");
		while(scanner.hasNextLine()) {
			String newLine=scanner.nextLine();
			if (newLine.isEmpty()) {
                break;
            }
			sb.append(newLine);
		}
		
		try {
			Object obj = new JSONParser().parse(sb.toString());
			JSONObject js=(JSONObject) obj;
			scanner.close();
			return js;
		} catch (ParseException e) {
			System.out.print("Provide valid JSON");
			scanner.close();
			return null;
		} 
	
	}
	
	public void saveAndClose() {
		this.flag=0;
		System.out.println("Deleting All TTL Enabled Data Pairs");
		for(DataPair dp: this.monitor.queue)
			this.delete(dp.getKey());
	    if(this.monitor.victim != null)
	    	this.delete(this.monitor.victim.getKey());
		System.out.println("ALL TTL Enabled Data Pairs Deleted");
		this.save(new File(this.filepath));
	}
	
	
	
	public void getAll() {
		System.out.println("--------------------------------");
		System.out.println("Reading all Key_Value Pairs.....");
		System.out.println("--------------------------------");
		for(int i=0;i<32;i++) {
			
			DataPair dPair=hashtable.get(i);
			while(dPair != null) {
				System.out.println("KEY: "+dPair.getKey() +"\tVALUE: "+dPair.getValue().toJSONString());
				dPair=dPair.getNext();
			}
		}
		System.out.println("--------------------------------");
	}
	
	
	
	
}
