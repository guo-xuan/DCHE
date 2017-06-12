package fda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import mtif.Exhaustion;

public class StandAlone {

	private static String parameterFile = "DCHEparameters.txt";
	private static String inputFile = "DCHEinputFile.txt";
	private static String outputFile = "DCHESingleResults.txt";
	private static String strnSample = "[NO.SAMPLES]";
	private static String strnCases = "[NO.CASES]";
	private static String strnSNPs = "[NO.SNPS]";
	private static String strOrder = "[ORDER]";
	private static String strAlpha = "[ALPHA0]";
	private static String strList = "[SIZELIST]";
	
	public static void main(String[] args){
		
		int nSample = 0;
		int nCases = 0;
		int nSNPs = 0;
		int order = 2;
		double[] alpha = new double[1];
		int[] sizeList = new int[1];
		String fileDataset = "";
		
		try {
			File f = new File(parameterFile);
			if(!f.exists()){
				System.out.println("DCHEparameters.txt file does not exist.");
				return;
			}
			BufferedReader br = new BufferedReader(new FileReader(parameterFile));
			String inputLine = null;
			String[] arrStr;
			while((inputLine=br.readLine())!=null){
				if(inputLine.startsWith(strnSample)){
					inputLine = inputLine.substring(inputLine.indexOf("]")+1, inputLine.indexOf("#")).trim();
					nSample = Integer.parseInt(inputLine);
					continue;
				}
				if(inputLine.startsWith(strnCases)){
					inputLine = inputLine.substring(inputLine.indexOf("]")+1, inputLine.indexOf("#")).trim();
					nCases = Integer.parseInt(inputLine);
					continue;
				}
				if(inputLine.startsWith(strnSNPs)){
					inputLine = inputLine.substring(inputLine.indexOf("]")+1, inputLine.indexOf("#")).trim();
					nSNPs = Integer.parseInt(inputLine);
					continue;
				}
				if(inputLine.startsWith(strOrder)){
					inputLine = inputLine.substring(inputLine.indexOf("]")+1, inputLine.indexOf("#")).trim();
					order = Integer.parseInt(inputLine);
					continue;
				}
				if(inputLine.startsWith(strAlpha)){
					inputLine = inputLine.substring(inputLine.indexOf("]")+1, inputLine.indexOf("#")).trim();
					arrStr = inputLine.split(",");
					alpha = new double[arrStr.length];
					for(int i=0;i<arrStr.length;i++){
						alpha[i] = Double.parseDouble(arrStr[i].trim());
					}
					continue;
				}
				if(inputLine.startsWith(strList)){
					inputLine = inputLine.substring(inputLine.indexOf("]")+1, inputLine.indexOf("#")).trim();
					arrStr = inputLine.split(",");
					sizeList = new int[arrStr.length];
					for(int i=0;i<arrStr.length;i++){
						sizeList[i] = Integer.parseInt(arrStr[i].trim());
					}
					continue;
				}
			}
			br.close();
			if(alpha.length!=sizeList.length||alpha.length!=(order - 1)){
				System.out.println("The format of DCHEparameters.txt file is not correct.");
				// return;
			}
			//get input file
			f = new File(inputFile);
			if(!f.exists()){
				System.out.println("DCHEinputFile.txt file does not exist.");
				return;
			}
			br = new BufferedReader(new FileReader(inputFile));
			inputLine = null;
			while((inputLine=br.readLine())!=null){
				fileDataset = inputLine.trim();
				break;
			}
			if(fileDataset.isEmpty()){
				System.out.println("DCHEinputFile.txt file does not exist.");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//run DCHE
		
		Exhaustion objE = new Exhaustion(nSample, nSNPs, nCases, alpha, sizeList);
		System.out.println("Begin reading dataset.");
		objE.readData(fileDataset);
		System.out.println("Finish reading dataset.");
		objE.flagPrint = true;
		try {
			FileWriter fwR = new FileWriter(outputFile, false);
			fwR.write("Index\tChi-square\tP_value\n");
			fwR.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(order==1){
			System.out.println("Start one locus test:");
			objE.oneSearch();
			System.out.println();
			System.out.println("Finish one locus test and writing into files.");
			objE.writeResults(outputFile);
		}
		System.out.println("DCHE Over");
	}
	
}
