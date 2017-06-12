package mtif;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Score {
	
	private static int mask = 3;
	/**
	 * new motif evaluation method
	 * @param matrix
	 * @param combinations
	 * @param lastD
	 * @param matrixP
	 * @param degree
	 * @return
	 */
	public static double motifEvaluationCor(byte[][] matrix, int[] combinations, int lastD, 
			double[][] matrixP, double degree){
		int row = matrix.length;
		int column = combinations.length;
		int id = 0;
		int value = 0;
		//count the individuals on the case and control sets
		RecordCase.clear();
		RecordHealth.clear();
		for(int i=0;i<=lastD;i++){
			id = 0;
			for(int j=0;j<column;j++){
				id = id<<2;
				id += matrix[i][combinations[j]];
			}
			if(RecordCase.containsKey(id)){
				value = RecordCase.get(id);
				value ++;
				RecordCase.put(id, value);
			}else{
				value = 1;
				RecordCase.put(id, value);
			}
			if(!RecordHealth.containsKey(id)){
				RecordHealth.put(id, 0);
			}
		}
		for(int i=lastD+1;i<row;i++){
			id = 0;
			for(int j=0;j<column;j++){
				id = id<<2;
				id += matrix[i][combinations[j]];
			}
			if(RecordHealth.containsKey(id)){
				value = RecordHealth.get(id);
				value ++;
				RecordHealth.put(id, value);
			}else{
				value = 1;
				RecordHealth.put(id, value);
			}
			if(!RecordCase.containsKey(id)){
				RecordCase.put(id, 0);
			}
		}
		//calculate the p-value for all combinations
		double result = 0;
		Entry<Integer, Integer> entry = null;
		int D = 0;
		int H = 0;
		double pv = 0;
		double temp = 0;
		int key = 0;
		int val = 0;
		double p = 1;
		for(Iterator<Entry<Integer, Integer>> ite = RecordCase.entrySet().iterator();ite.hasNext();){
			entry = ite.next();
			D = entry.getValue();
			H = RecordHealth.get(entry.getKey());
			
			if((((double)D)/countD)<(((double)H)/countH))continue;
			
			key = entry.getKey();
			p = 1;
			
			for(int i=combinations.length-1;i>-1;i--){
				val = key&mask; key = key>>2;
				p*=matrixP[combinations[i]][val];
			}
			
			pv = logPValue(D+H, H, p, degree);
			temp = -(Math.log(pv));//System.out.println(temp);
			result += temp;
			//result += -(Math.log(pv));
		}
		return result;
	}
	
	/**
	 * for the correlation p-value
	 * @param C
	 * @param dH
	 * @param p
	 * @param degree
	 * @return
	 */
	private static double logPValue(int C, int dH, double p, double degree){
		double logH, logD;
		int idx = (int) (p/degree);
		logH = arrLog[idx];
		idx = (int) ((1-p)/degree);
		logD = arrLog[idx];
		double result = 0;
		double product = 0;
		for(int i=0;i<=dH;i++){
			product = 0;
			for(int k=0;k<i;k++){
				product += arrLogTotal[C - i + 1 + k];
				product -= arrLogTotal[k+1];
				product += logH;
			}
			for(int k=0;k<C-i;k++){
				product += logD;
			}
			result += Math.pow(Math.E, product);
		}
		return result;
	}

	/**
	 * calculate p-value
	 * @param HS the ratio of health individuals and the C
	 * @param DS the ratio of case individuals and the C
	 * @param C the total count of individuals having the combinations of specific SNPs
	 * @param dH the total count of individuals with combination C in the control set
	 * @return
	 */
	public static double pValue(double HS, double DS, int C, int dH){
		double result = 0;
		int i = 1;
		int di = 0;
		int j = 0;
		result += Math.pow(DS, C);
		double product = 1;
		for(;i<=dH;i++){
			di = C - i; //for disease
			j = 0;
			product = 1;
			for(int k=1;k<=i;k++){
				product *= C - i + 1 + k - 1;
				product /= k;
				product *= HS;
				if(j<di){
					j++;
					product *= DS;
				}
			}
			for(int k=j;k<di;k++){
				product *=DS;
			}
			result += product;
		}
		return result;
	}
	
	public static double[] arrLog;
	/**
	 * given the degree of accuracy to generate a set of log value
	 * @param degree
	 */
	public static void generateLogValue(double degree){
		int n = (int) (1/degree);
		arrLog = new double[n+1];
		arrLog[0] = Math.log(degree - (degree/10));
		for(int i=1;i<n;i++){
			arrLog[i] = Math.log(i*degree);
		}
		arrLog[n] = 0;
	}
	
	private static double logHS;
	private static double logDS;
	private static double[] arrLogTotal;
	private static double ratioHS;
	private static double ratioDS;
	private static double countH;
	private static double countD;
	
	public static void initLog(double HS, double DS, int total){
		ratioHS = HS;
		ratioDS = DS;
		logHS = Math.log(HS);
		logDS = Math.log(DS);
		arrLogTotal = new double[total+1];
		for(int i=1;i<=total;i++){
			arrLogTotal[i] = Math.log(i);
		}
		countH = ratioHS * total;
		countD = ratioDS * total;
	}
	
	public static double logPValue(int C, int dH){
		double result = 0;
		double product = 0;
		for(int i=0;i<=dH;i++){
			product = 0;
			for(int k=0;k<i;k++){
				product += arrLogTotal[C - i + 1 + k];
				product -= arrLogTotal[k+1];
				product += logHS;
			}
			for(int k=0;k<C-i;k++){
				product += logDS;
			}
			result += Math.pow(Math.E, product);
		}
		return result;
	}
	
	private static HashMap<Integer, Integer> RecordCase = new HashMap<Integer, Integer>();
	private static HashMap<Integer, Integer> RecordHealth = new HashMap<Integer, Integer>();
	
	/**
	 * use the minimum p-value as evaluation value
	 * @param matrix
	 * @param combinations
	 * @param lastD
	 * @param HS
	 * @param DS
	 * @return
	 */
	public static double motifEvaluation(byte[][] matrix, int[] combinations, int lastD, double HS, double DS, int limit){
		
		int row = matrix.length;
		int column = combinations.length;
		int id = 0;
		int value = 0;
		//count the individuals on the case and control sets
		RecordCase.clear();
		RecordHealth.clear();
		for(int i=0;i<=lastD;i++){
			id = 0;
			for(int j=0;j<column;j++){
				id = id<<2;
				id += matrix[i][combinations[j]];
			}
			if(RecordCase.containsKey(id)){
				value = RecordCase.get(id);
				value ++;
				RecordCase.put(id, value);
			}else{
				value = 1;
				RecordCase.put(id, value);
			}
			if(!RecordHealth.containsKey(id)){
				RecordHealth.put(id, 0);
			}
		}
		for(int i=lastD+1;i<row;i++){
			id = 0;
			for(int j=0;j<column;j++){
				id = id<<2;
				id += matrix[i][combinations[j]];
			}
			if(RecordHealth.containsKey(id)){
				value = RecordHealth.get(id);
				value ++;
				RecordHealth.put(id, value);
			}else{
				value = 1;
				RecordHealth.put(id, value);
			}
			if(!RecordCase.containsKey(id)){
				RecordCase.put(id, 0);
			}
		}
		//calculate the p-value for all combinations
		double result = 0;
		Entry<Integer, Integer> entry = null;
		int D = 0;
		int H = 0;
		double pv = 0;
		double temp = 0;
		for(Iterator<Entry<Integer, Integer>> ite = RecordCase.entrySet().iterator();ite.hasNext();){
			entry = ite.next();
			D = entry.getValue();
			H = RecordHealth.get(entry.getKey());
			if((((double)D)/countD)<(((double)H)/countH))continue;
			pv = logPValue(D+H, H);
			temp = -(Math.log(pv));//System.out.println(temp);
			result += temp;
			//result += -(Math.log(pv));
		}
		return result;
	}
	
	/**
	 * use the minimum p-value as evaluation value
	 * @param matrix
	 * @param combinations
	 * @param lastD
	 * @param HS
	 * @param DS
	 * @return
	 */
	public static double motifEvaluation2(byte[][] matrix, int[] combinations, int lastD, double HS, double DS, int limit){
		double result = Double.MAX_VALUE;
		int row = matrix.length;
		int column = combinations.length;
		int id = 0;
		int value = 0;
		//count the individuals on the case and control sets
		RecordCase.clear();
		RecordHealth.clear();
		for(int i=0;i<=lastD;i++){
			id = 0;
			for(int j=0;j<column;j++){
				id = id<<2;
				id += matrix[i][combinations[j]];
			}
			if(RecordCase.containsKey(id)){
				value = RecordCase.get(id);
				value ++;
				RecordCase.put(id, value);
			}else{
				value = 1;
				RecordCase.put(id, value);
			}
			if(!RecordHealth.containsKey(id)){
				RecordHealth.put(id, 0);
			}
		}
		for(int i=lastD+1;i<row;i++){
			id = 0;
			for(int j=0;j<column;j++){
				id = id<<2;
				id += matrix[i][combinations[j]];
			}
			if(RecordHealth.containsKey(id)){
				value = RecordHealth.get(id);
				value ++;
				RecordHealth.put(id, value);
			}else{
				value = 1;
				RecordHealth.put(id, value);
			}
			if(!RecordCase.containsKey(id)){
				RecordCase.put(id, 0);
			}
		}
		//calculate the p-value for all combinations
		Entry<Integer, Integer> entry = null;
		int D = 0;
		int H = 0;
		double pv = 0;
		for(Iterator<Entry<Integer, Integer>> ite = RecordCase.entrySet().iterator();ite.hasNext();){
			entry = ite.next();
			D = entry.getValue();
			H = RecordHealth.get(entry.getKey());
			if(H>limit||((((double)D)/countD)<(((double)H)/countH))){
				pv = 1;
			}else{
				//pv = pValue(HS, DS, D+H, H);
				pv = logPValue(D+H, H);
			}
			if(pv<result)result = pv;
		}
		return result;
	}
	
	public static void main(String[] args){
		initLog(0.5, 0.5, 800);
		double a = -Math.log(logPValue(21, 10));
		double b = -Math.log(logPValue(38, 10));
		double c = -Math.log(logPValue(21, 10));
		double d = -Math.log(logPValue(30, 10));
		System.out.println(a+b+c+2*d);
		a = -Math.log(logPValue(24, 10));
		b = -Math.log(logPValue(35, 10));
		c = -Math.log(logPValue(24, 10));
		d = -Math.log(logPValue(34, 10));
		//double e = -Math.log(logPValue(1,0));
		System.out.println(a + b+ c+d);
		//System.out.println(c);
		//System.out.println(d);
	}
}
