package fastl;

public class ListMutipleElement {

	public int[] list;
	public double value;
	public static String tab = "\t";
	
	public ListMutipleElement(int _n){
		list = new int[_n];
	}
	
	public boolean equal(ListMutipleElement _ele){
		for(int i=0;i<list.length;i++){
			if(list[i]!=_ele.list[i]){
				return false;
			}
		}
		return true;
	}
	
	public boolean equal(int[] _comb){
		for(int i=0;i<list.length;i++){
			if(list[i]!= _comb[i]){
				return false;
			}
		}
		return true;
	}
	
	public String getString(){
		StringBuffer strBuf = new StringBuffer();
		for(int i=0;i<list.length;i++){
			strBuf.append(list[i]+tab);
		}
		strBuf.append(value);
		return strBuf.toString();
	}
	
	public void Set(int[] _comb, double _val){
		for(int i=0;i<list.length;i++){
			list[i] = _comb[i];
		}
		value = _val;
	}
	
	public void Sort(){
		int idx = -1;
		int temp = 0;
		for(int i=0;i<list.length;i++){
			idx = i;
			for(int j=i+1;j<list.length;j++){
				if(list[j]<list[idx]){
					idx = j;
				}
			}
			if(idx!=i){
				temp = list[i];
				list[i] = list[idx];
				list[idx] = temp;
			}
		}
	}
	
	public void print(){
		for(int i=0;i<list.length;i++){
			System.out.print(list[i]+"\t");
		}
		System.out.println(value);
	}
	
}
