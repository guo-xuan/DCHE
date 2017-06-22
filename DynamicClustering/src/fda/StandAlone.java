package fda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mtif.Exhaustion;
import mtif.ParExhaustion;

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

	private static ArrayList<String> asGwasFiles = new ArrayList<String>();
	private static int iOrder = 0;
	private static String sOutputFile;
	private static int iNumTotalSamples;
	private static int iNumCases;
	private static int iNumSnps;

	public static void main(String[] args) {

		parse_option(args);
		setDataSize();
		ParExhaustion.loadingData(asGwasFiles);
		ParExhaustion.setOrder(iOrder);

		int iPoolSize = 5;
		ExecutorService taskList = Executors.newFixedThreadPool(iPoolSize);
		for (int i = 0; i < 5; i++) {
			taskList.execute(new ParExhaustion());
		}

		if (true) {
			return;
		}

		int nSample = 0;
		int nCases = 0;
		int nSNPs = 0;
		int order = 2;
		double[] alpha = new double[1];
		int[] sizeList = new int[1];
		String fileDataset = "";

		try {
			File f = new File(parameterFile);
			if (!f.exists()) {
				System.out.println("DCHEparameters.txt file does not exist.");
				return;
			}
			BufferedReader br = new BufferedReader(new FileReader(parameterFile));
			String inputLine = null;
			String[] arrStr;
			while ((inputLine = br.readLine()) != null) {
				if (inputLine.startsWith(strnSample)) {
					inputLine = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf("#")).trim();
					nSample = Integer.parseInt(inputLine);
					continue;
				}
				if (inputLine.startsWith(strnCases)) {
					inputLine = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf("#")).trim();
					nCases = Integer.parseInt(inputLine);
					continue;
				}
				if (inputLine.startsWith(strnSNPs)) {
					inputLine = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf("#")).trim();
					nSNPs = Integer.parseInt(inputLine);
					continue;
				}
				if (inputLine.startsWith(strOrder)) {
					inputLine = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf("#")).trim();
					order = Integer.parseInt(inputLine);
					continue;
				}
				if (inputLine.startsWith(strAlpha)) {
					inputLine = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf("#")).trim();
					arrStr = inputLine.split(",");
					alpha = new double[arrStr.length];
					for (int i = 0; i < arrStr.length; i++) {
						alpha[i] = Double.parseDouble(arrStr[i].trim());
					}
					continue;
				}
				if (inputLine.startsWith(strList)) {
					inputLine = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf("#")).trim();
					arrStr = inputLine.split(",");
					sizeList = new int[arrStr.length];
					for (int i = 0; i < arrStr.length; i++) {
						sizeList[i] = Integer.parseInt(arrStr[i].trim());
					}
					continue;
				}
			}
			br.close();
			if (alpha.length != sizeList.length || alpha.length != (order - 1)) {
				System.out.println("The format of DCHEparameters.txt file is not correct.");
				// return;
			}
			// get input file
			f = new File(inputFile);
			if (!f.exists()) {
				System.out.println("DCHEinputFile.txt file does not exist.");
				return;
			}
			br = new BufferedReader(new FileReader(inputFile));
			inputLine = null;
			while ((inputLine = br.readLine()) != null) {
				fileDataset = inputLine.trim();
				break;
			}
			if (fileDataset.isEmpty()) {
				System.out.println("DCHEinputFile.txt file does not exist.");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// run DCHE

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
		if (order == 1) {
			System.out.println("Start one locus test:");
			objE.oneSearch();
			System.out.println();
			System.out.println("Finish one locus test and writing into files.");
			objE.writeResults(outputFile);
		}
		System.out.println("DCHE Over");
	}

	public static void parse_option(String[] args) {
		asGwasFiles.clear();
		for (int i = 0; i < args.length; i += 2) {
			if (args[i].charAt(1) == 'i') {
				if (!asGwasFiles.contains(args[i + 1])) {
					asGwasFiles.add(args[i + 1]);
				}
			} else if (args[i].compareTo("-n") == 0) {
				iOrder = Integer.parseInt(args[i + 1]);
			} else if (args[i].compareTo("-o") == 0) {
				sOutputFile = args[i + 1];
			}
		}

	}

	public static void setDataSize() {

		if (asGwasFiles.size() == 0) {
			System.out.println("No data file provided.");
			System.exit(1);
		}

		// get the number of samples, number of cases
		iNumTotalSamples = 0;
		iNumCases = 0;
		iNumSnps = 0;
		boolean bIsSnp = false;
		String sLine;
		char c = ' ';
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(asGwasFiles.get(0)));
			while ((sLine = br.readLine() ) != null){
				if(sLine.charAt(0)=='1'){
					iNumCases += 1;
				}
				iNumTotalSamples += 1;
			}
			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// get number of SNPs
		ArrayList<Integer> aiSnpIdxInEachFile = new ArrayList<Integer>();
		for (int i = 0; i < asGwasFiles.size(); i++) {
			bIsSnp = false;
			try {
				br = new BufferedReader(new FileReader(asGwasFiles.get(i)));
				aiSnpIdxInEachFile.add(iNumSnps);
				sLine = br.readLine();
				for (int j = 0; j < sLine.length(); j++) {
					c = sLine.charAt(j);
					if (!bIsSnp) {
						if (c >= '0' && c <= '9') {
							bIsSnp = true;
							iNumSnps += 1;
						}
					} else {
						if (c < '0' || c > '9') {
							bIsSnp = false;
						}
					}
				}
				// minus one, the first column is the label column
				iNumSnps--;
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		ParExhaustion.initilizeMemory(iNumCases, iNumTotalSamples, iNumSnps, aiSnpIdxInEachFile);
	}
	

}
