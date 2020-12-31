# DATASTORE

Datastore is a Java library for dealing with file based key-value pairs supporting the basic CRD (Create, Read and Delete) Operations. `Implementation` is pretty much `similiar` to [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html) in the sense that it uses `Hashtable` and `Seperate Chaining Collision` technique. It can be initialized using an optional file path. If one is not provided, it will reliably create itself in the reasonable location. Key string capped at `32 characters` and Value must be a `JSON object capped at 16KB`. Every key supports `setting a Time-To-Live property` when it is created. This property is optional. If provided, it will be evaluated as an integer defining the number of seconds. Once the Time-To-Live for a key has expired, the key will no longer be available for Read or Delete operations. Only one process can access the datastore (local file) at a time. `Thread safe` as all CRD methods are Synchronized.

#

# ```Class DataStore```



### FIELDS

`private String filepath` - The name of the file datastored associated with.

`private ArrayList<DataPair> hashtable` - the hashtable of `size 32` (each index - head of linkedlist to handle collisions ( Seperate Chaining )).

`private long file_size` - holds the current file size.

`private TTL_Monitor monitor` - Inner class to monitor Time-To-Live Property.

`private int flag` - Used to start and stop TTL thread.

#

### CONSTRUCTORS

```java
public DataStore(String DIRECTORY_PATH)
```

- Constructs an empty Datastore with the default capacity of 32 and creates a file in the mentioned location if not present.
- In case the file already exists in the location, data from the file will be loaded into the datastore instance.
- In addition to that, Thread to monitor Time-To-Live Property will also be started.

#### Parameters:
`DIRECTORY_PATH` - the absolute path where file needs to be created / retrieved.

```java
public DataStore()
```

- Constructs an empty Datastore with the default capacity of 32 and creates a file in the default location as `data.json` if not present.
- In case the file already exists in the location, data from the file will be loaded into the datastore instance.
- In addition to that, Thread to monitor Time-To-Live Property will also be started.

#### Parameters:
`NOTHING`

#

## USAGE

```java
DataStore ds=new DataStore();
```
```java
DataStore ds=new DataStore("E:\Java Projects");
```

#

## PUBLIC METHODS AVAILABLE


```java
public synchronized Boolean create(String key, JSONObject value,int TTL)
```
- Associates the specified value with the specified key in this Datastore. 
- If the datastore previously contained a mapping for the key, then creation will get fail displaying appropriate message.

### Parameters:
`key` - key with which the specified value is to be associated.  

`value` - value in the form of JSON associated with the specified key.  

`ttl` - The Amount of time in seconds that the pair should be available to read / delete.  

( ```NOTE:```   ttl parameter is optional )


### Returns:
`true` - if the creation is successfull.

`false` - if the creation fails.  

#

```java
public synchronized Boolean delete(String key)
```
- Removes the mapping for the specified key from this datastore if present. 
- If the key is not present, then appropriate error message will be displayed.

### Parameters:
`key` - key whose mapping is to be removed from the datastore.


### Returns:
`true` - if the deletion is successfull. 

`false` - if the deletion fails.   

#

```java
public synchronized JSONObject get(String key)
```

- Returns the value to which the specified key is mapped, or empty JSON with appropriate error message if this map contains no mapping for the key.

### Parameters:
`key` - key whose mapping is to be retrieved from the datastore.

### Returns:
`JSON` - the value to which key is associated.  

#

```java
public void getAll()
```
- Fetches and prints all the available key-value mappings in the datastore.

### Parameters:
`NOTHING`

#

```java
public JSONObject readJSON()
```
- An Utility function to read JSON from STDIN ( Console )

### Parameters:
`NOTHING`

### Returns:
`JSON` - the input read from the console.  

#

```java
public void saveAndClose()
```
- This method is similiar to destructor which needs to be called before closing datastore.
- If this method is not called, new data will not be saved to the file.
- This method handles operation such as deleting TTL enabled key-value pairs and stopping TTL monitor thread.

### Parameters:
`NOTHING`

#


## USAGE

```java

JSONObject jsonObject=new JSONObject();
//Arbitrary JSON object for Simulation
jsonObject.put("demo", "value");
		
		
//Creating keyvalue pairs
ds.create("lulu", jsonObject,2);
ds.create("msdhoni",jsonObject);
ds.create("vicky", jsonObject);
ds.create("lulu", jsonObject);

		
//fetching all Pairs
ds.getAll();

//Getting Pair using Key
System.out.println(ds.get("vckyi"));
System.out.println(ds.get("vicky"));
System.out.println(ds.get("vcyi"));
    
//delete operation
ds.delete("vicky");
    
//Finally Saving evrythng to the file
ds.saveAndClose();

```



#

# UTILITY METHODS TO SUPPORT DATASTORE

```java
private void createFile(String path)
```
- This method will be automatically invoked by the constructor inorder to create a file. 

### Parameters:
`path` - the absolute path where file needs to be created / retrieved.

#

```java
private void initialize_table()
```
- Used to initialize a hashtable which will be invoked by the constructor

### Parameters:
`NOTHING`

#

```java
private Boolean LoadData(File file)
```
- Loads data from the file saved in local storage.
- It will be invoked by the constructor automatically during instantiation if the file with the specified name already exists.

### Parameters:
`file` - the file from which data needs to be retrieved.

### Returns:
`true` - if the load is successful.  

`false` - if load fails.   

#

```java
private Boolean save(File filename)
```

- Saves the entire contents of datastore to the file mentioned in the arguments.
- It will be invoked by saveandclose() method.

### Parameters:
`file` - the file to which data needs to be saved.

### Returns:
`true` - if the data is stored to the file.   

`false` - if save operation fails.  

#

```java
private int getHashIndex(String key)
```
- This method finds the index of bucket ( hashtable ) in which data associated with the key needs to be stored.
- Index is calculated by **hashcode(key) & 31** ( Since only 32 buckets are present ).

### Parameters:
`key` - the key whose index to be found.

### Returns:
`int` - Index of the Key



# ```Class DataPair```

### FIELDS

`private String key` - the key name

`private JSONObject value` - the value in JSON format

`private int TTL` - Time-To Live Property in seconds

`private DataPair next` - To hold next Datapair


### CONSTRUCTOR

```java
public DataPair(String key,JSONObject value,int ttl)
```


### METHODS AVAILABLE

```java
public String getKey()
```
```java
public void setKey(String key)
```
```java
public JSONObject getValue()
```
```java
public void setValue(JSONObject value)
```
```java
public int getTTL()
```
```java
public void setTTL(int tTL)
```
```java
public DataPair getNext()
```
```java
public void setNext(DataPair next)
```






