package mtif;

import java.util.Comparator;

public class SNP {

	private int[] arrComb;
	private double score;
	
	public SNP(int[] combination, double _s){
		arrComb = new int[combination.length];
		for(int i=0;i<combination.length;i++){
			arrComb[i] = combination[i];
		}
		score = _s;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int[] getArrComb() {
		return arrComb;
	}

	public void setArrComb(int[] arrComb) {
		this.arrComb = arrComb;
	}
	
	public void print(){
		for(int i=0;i<arrComb.length;i++){
			System.out.print(arrComb[i]+" ");
		}
		System.out.println(score);
	}
	
}

class SortByScore implements Comparator<SNP>{

	@Override
	public int compare(SNP s1, SNP s2) {
		if(s1.getScore()<s2.getScore()){
			return 1;
		}else if(s1.getScore()==s2.getScore()){
			return 0;
		}
		return -1;
	}
	
}
