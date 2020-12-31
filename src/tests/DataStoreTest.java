package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import datastore.DataStore;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;


@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class DataStoreTest {
	DataStore ds;
	JSONObject jsonObject;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}
	@SuppressWarnings("unchecked")
	@BeforeAll
	void setUp() throws Exception {
		ds=new DataStore();
		jsonObject=new JSONObject();
		jsonObject.put("keyname", "JSONval");
		
	}
	@AfterAll
	void tearDown() throws Exception {
		ds.saveAndClose();
	}
	
	@Test
	@Order(2)
	void testCreateStringJSONObjectInt() throws InterruptedException {
		Boolean actual=ds.create("vickyttl", jsonObject,2);
		assertEquals(true, actual);
		Thread.sleep(3000);
		//Inserting same record to test TTL deletion
		actual=ds.create("vickyttl", jsonObject,1);
		assertEquals(true, actual);
		
	}

	@Test
	@Order(1)
	void testCreateStringJSONObject() {
		Boolean actual=ds.create("vicky", jsonObject);
		assertEquals(true, actual);
		actual=ds.create("vicky1", jsonObject);
		assertEquals(false, actual);
		actual=ds.create("lulu", jsonObject);
		assertEquals(true, actual);
		actual=ds.create("vicky1hajdjaddggsgdjgjdgsagdgsgdggdsgjhhgshdshdgdhgdg", jsonObject);
		assertEquals(false, actual);
	}

	@Test
	@Order(4)
	void testDelete() {
		Boolean actual=ds.delete("lulu");
		assertEquals(true, actual);
		actual=ds.delete("Nothing");
		assertEquals(false, actual);
	}

	
	@Test
	@Order(3)
	void testGet() {
		
		assertEquals(true,jsonObject.toString().equals(ds.get("vicky").toString()));
		assertEquals(false,jsonObject.toString().equals(ds.get("vickyyy").toString()));
	}

}
