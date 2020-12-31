# Datastore

- Datastore is a Java library for dealing with file based key-value pairs supporting the basic CRD (Create, Read and Delete) Operations.

- Implementation is pretty much similiar to [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html) in the sense that it uses **Hashtable** and **Seperate Chaining** Collision technique.

- It can be initialized using an optional file path. If one is not provided, it will reliably create itself in the reasonable location.

- Key string capped at 32 characters and Value must be a JSON object capped at 16KB.

- Every key supports setting a Time-To-Live property when it is created. This property is optional. If provided, it will be evaluated as an integer defining the number of seconds. Once the Time-To-Live for a key has expired, the key will no longer be available for Read or Delete operations.

- Only one process can access the datastore (local file) at a time.

- **Thread safe** as all CRD methods are Synchronized.

# ```Class DataStore```





### Constructors

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



## Usage

```java
DataStore ds=new DataStore();
```
```java
DataStore ds=new DataStore("E:\Java Projects");
```


## All Available Methods

```java
private void createFile(String path)
```
- This method will be automatically invoked by the constructor inorder to create a file. 

### Parameters:
`path` - the absolute path where file needs to be created / retrieved.



```java
private void initialize_table()
```
- Used to initialize a hashtable which will be invoked by the constructor

```java
private Boolean LoadData(File file)
```
- Loads data from the file saved in local storage.
- It will be invoked by the constructor automatically during instantiation if the file with the specified name already exists.

### Parameters:
`file` - the file from which data needs to be retrieved.



```java
private int getHashIndex(String key)
```
- This method finds the index of bucket ( hashtable ) in which data associated with the key needs to be stored.
- Index is calculated by **hashcode(key) & 31** ( Since only 32 buckets are present ).

### Parameters:
`key` - the key whose index to be found.

### Returns:
`int` - Index of the Key

```java
public synchronized Boolean create(String key, JSONObject value,int TTL)
```
- Associates the specified value with the specified key in this Datastore. 
- If the datastore previously contained a mapping for the key, then creation will get fail displaying appropriate message.

### Parameters:
`key` - key with which the specified value is to be associated.
`value` - value in the form of JSON associated with the specified key.
`ttl` - The Amount of time in seconds that the pair should be available to read / delete.

### Returns:
`true` - if the creation is successfull
`false` - if the creation is failed.



