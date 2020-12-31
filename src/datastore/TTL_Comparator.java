package datastore;

import java.util.Comparator;

/** class to Enable Sorting based on TTL value **/
public class TTL_Comparator implements Comparator<DataPair> {

	@Override
	public int compare(DataPair dp1, DataPair dp2) {
		//To enable Sorting using TTL value
		return dp1.getTTL()<dp2.getTTL()?1:-1;
	}

}
