package fastl;

public class ListAscendListElement implements Comparable<ListAscendListElement>{

	public int id1;
	public int id2;
	public double value;
	
	public void set(int _id1, int _id2, double _value){
		id1 = _id1;
		id2 = _id2;
		value = _value;
	}


	@Override
	public int compareTo(ListAscendListElement arg0) {
		// TODO Auto-generated method stub
		if(value>arg0.value){
			return 1;
		}else if(value<arg0.value){
			return -1;
		}
		return 0;
	}
}
