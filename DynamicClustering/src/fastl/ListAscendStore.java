package fastl;

import java.io.FileWriter;
import java.io.IOException;

public class ListAscendStore {

	public ListMutipleElement[] list;
	public int idx = 0;
	private int low = 0;
	private int high = 1;
	private int id = 0;
	
	public static String enter = "\n";
	
	public ListAscendStore(int _n, int _size){
		list = new ListMutipleElement[_n];
		for(int i=0;i<_n;i++){
			list[i] = new ListMutipleElement(_size);
		}
		idx = 0;
	}
	
	public void add(int[] _comb, double _val){
		low = 0;
		high = idx;
		id = idx;
		while(low < high){
			id = (low + high)/2;
			if(_val<list[id].value){
				high = id;
			}else if(_val>list[id].value){
				low = id+1;
			}else{
				break;
			}
		}
		id = (low+high)/2;
		if(id<idx){
			if(list[id].equal(_comb)){
				return;
			}
		}
		if(idx<list.length){
			for(int i=idx;i>id;i--){
				list[i].Set(list[i-1].list, list[i-1].value);
			}
			idx++;
			list[id].Set(_comb, _val);
		}else{
			if(id>=idx)return;
			for(int i=idx-1;i>id;i--){
				list[i].Set(list[i-1].list, list[i-1].value);
			}
			list[id].Set(_comb, _val);
		}
	}
	
	public void clean(){
		idx = 0;
	}
	
	public ListMutipleElement getTop(){
		if(idx==0)return null;
		return list[0];
	}
	
	public void printAll(){
		for(int i=0;i<idx;i++){
			list[i].print();
		}
	}
	
	public void printAll(FileWriter fwR) throws IOException{
		for(int i=0;i<idx;i++){
			fwR.write(Integer.toString(i+1)+"\t");
			fwR.write(list[i].getString());
			fwR.write(enter);
		}
	}
	
	public void printAll(FileWriter fwR, double _cv) throws IOException{
		for(int i=0;i<idx;i++){
			if(list[i].value > _cv)break;
			fwR.write(Integer.toString(i+1)+"\t");
			fwR.write(list[i].getString());
			fwR.write(enter);
		}
	}
}
