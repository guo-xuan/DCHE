package mtif;

import java.util.HashMap;

public class ChiSquare2{
//public class ChiSquare extends Thread {
	
	private static HashMap<Long, Double> arrChiValue = new HashMap<Long, Double>();
	private static long[] arrEle4 = new long[4];
	private static long temp4ArrEle4;
	private double[][] matrixO2 = new double[2][2];
	private double[] sumR2 = new double[2];
	private double[] sumC2 = new double[2];
	private double sum2;
	private double[][] matrixE2 = new double[2][2];
	private double chi2;
	
	private static double expectedVal = 0.002;
	
	public double calculateChi2(){
		//chi2 = this.checkValue();
		//if(chi2!=-1)return chi2;
		for(int i=0;i<2;i++){
			sumR2[i] = 0;
			sumC2[i] = 0;
		}
		sum2 = 0;
		for(int i=0;i<2;i++){
			sumR2[i] = matrixO2[i][0] + matrixO2[i][1];
			sumC2[i] = matrixO2[0][i] + matrixO2[1][i];
			sum2 += sumC2[i];
		}
		//calculate the expect value
		for(int i=0;i<2;i++){
			for(int j=0;j<2;j++){
				matrixE2[i][j] = (sumR2[i]*sumC2[j]/sum2);
			}
		}
		//calculate the chi-square
		chi2 = 0;
		for(int i=0;i<2;i++){
			for(int j=0;j<2;j++){
				if(matrixE2[i][j]==0){
					chi2 += ((Math.pow(matrixO2[i][j] - expectedVal, 2)/expectedVal));
				}else{
					chi2 += ((Math.pow(matrixO2[i][j] - matrixE2[i][j], 2)/matrixE2[i][j]));
				}
			}
		}
		return chi2;
	}
	
	public double checkValue(){
		arrEle4[0] = (long) matrixO2[0][0];
		arrEle4[1] = (long) matrixO2[0][1];
		arrEle4[2] = (long) matrixO2[1][0];
		arrEle4[3] = (long) matrixO2[1][1];
		//sort the array
		for(int i=0;i<3;i++){
			for(int j=i+1;j<4;j++){
				if(arrEle4[i]>arrEle4[j]){
					temp4ArrEle4 = arrEle4[i];
					arrEle4[i] = arrEle4[j];
					arrEle4[j] = temp4ArrEle4;
				}
			}
		}
		//get the id
		temp4ArrEle4 = 0;
		for(int i = 0;i<4;i++){
			temp4ArrEle4 = temp4ArrEle4 << 15;
			temp4ArrEle4 += arrEle4[i];
		}
		if(arrChiValue.containsKey(temp4ArrEle4)){
			return arrChiValue.get(temp4ArrEle4);
		}
		return -1;
	}
	
	public double getChi2(){
		return chi2;
	}
	
	public static double pValue(double degree, double critical){
		double p = 0;
		if(critical/2>(degree/2+1)){
			p = MyGamma.regularizedGammaQ(degree/2, critical / 2, 10e-50, Integer.MAX_VALUE);
		}else{
			p = 1 - MyGamma.regularizedGammaP(degree/2, critical / 2, 10e-50, Integer.MAX_VALUE);
		}
		return p;
	}
	
	public void run(){
		this.calculateChi2();
	}
	
	/**
	 * 
	 * @param _matrix 2*n vector
	 * @param _c1
	 * @param _c2
	 */
	public void setVectors(int[][] _matrix, int _c1, int _c2){
		for(int i=0;i<2;i++){
			matrixO2[i][0] = _matrix[i][_c1];
			//if(matrixO2[i][0]==0)matrixO2[i][0]=0.1;
			matrixO2[i][1] = _matrix[i][_c2];
			//if(matrixO2[i][1]==0)matrixO2[i][1]=0.1;
		}
		if(matrixO2[0][0]==0&&matrixO2[0][1]==0){
			matrixO2[0][0] = expectedVal;
			matrixO2[0][1] = expectedVal;
		}
		if(matrixO2[1][0]==0&&matrixO2[1][1]==0){
			matrixO2[1][0] = expectedVal;
			matrixO2[1][1] = expectedVal;
		}
	}
	
	public static void main(String[] args){
		
		double c = ChiSquare2.pValue(26, 95.94);
		double d = c * 166167000;
		System.out.println(c+"\t"+d);
		Chi chiCal = new Chi();
		int[][] matrix = {{100,5,41},{0,0,72}};
		ChiSquare2 chi = new ChiSquare2();
		chi.setVectors(matrix, 0, 1);
		System.out.println("chi: "+chi.calculateChi2());
		int size = 3;
		boolean[] arrEmpty = new boolean[size];
		for(int i=0;i<size;i++)arrEmpty[i] = true;
		double x = chiCal.cal(matrix, arrEmpty, size);
		System.out.println(x);
		System.out.println(ChiSquare2.pValue(2, x));
	}
}
