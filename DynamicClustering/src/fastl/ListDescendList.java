package fastl;

public class ListDescendList {

	public ListAscendListElement[] list = null;
	public int size;
	public int idx;
	public int low;
	public int high;
	public int id;
	public ListAscendListElement p = new ListAscendListElement();
	
	private double Thre = 0.01;
	
	public ListDescendList(int _size){
		list = new ListAscendListElement[_size];
		size = _size;
		low = 0;
		high = 0;
		idx = 0;
		for(int i=0;i<size;i++){
			list[i] = new ListAscendListElement();
		}
	}
	
	public void add(int _id1, int _id2, double _val){
		if(_val<Thre)return;
		low = 0;
		high = idx;
		id = idx;
		while(low < high){
			id = (low + high)/2;
			if(_val>list[id].value){
				high = id;
			}else if(_val<list[id].value){
				low = id + 1;
			}else{
				break;
			}
		}
		id = (low+high)/2;
		if(idx<list.length){
			for(int i=idx;i>id;i--){
				list[i].set(list[i-1].id1, list[i-1].id2, list[i-1].value);
			}
			idx++;
			list[id].set(_id1, _id2, _val);
		}else{
			if(id>=idx)return;
			for(int i=idx-1;i>id;i--){
				list[i].set(list[i-1].id1, list[i-1].id2, list[i-1].value);
			}
			list[id].set(_id1, _id2, _val);
		}
	}
	
	public void clean(){
		low = 0;
		high = 0;
		idx = 0;
	}

	public ListAscendListElement getTop(boolean[] _arrNotEmpty){
		int i = 0;
		for(;i<idx;i++){
			if(_arrNotEmpty[list[i].id1]&&_arrNotEmpty[list[i].id2]){
				break;
			}
		}
		if(i>=idx){
			idx = 0;
			return null;
		}else{
			p.set(list[i].id1, list[i].id2, list[i].value);
		}
		for(int j=i+1;j<idx;j++){
			list[j-i-1].set(list[j].id1, list[j].id2, list[j].value);
		}
		idx -= (i+1);
		return p;
	}
	
	private int count;
	
	public ListAscendListElement getTop(){
		if(idx == 0)return null;
		count = 0;
		p.set(list[0].id1, list[0].id2, list[0].value);
		for(int i=0;i<idx;i++){
			if(list[i].id1 == p.id1||list[i].id1 == p.id2||list[i].id2 == p.id1||list[i].id2 == p.id2){
				count++;
				list[i].id1 = -1;
			}
		}
		low = 0;
		high = 0;
		for(int i=0;i<idx-count;i++){
			if(list[i].id1 == -1){
				for(int j = high;j<idx;j++){
					if(list[j].id1!=-1){
						high = j;
						break;
					}
				}
				list[i].set(list[high].id1, list[high].id2, list[high].value);
				list[high].id1 = -1;
				high++;
			}
		}
		idx -= count;
		return p;
	}
	
}
