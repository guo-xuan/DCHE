package mtif;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import fastl.Datag;

public class ParExhaustion implements Runnable {

	public static Datag SNPData = null;
	public static ArrayList<Integer> aiSnpIdxInEachFile = new ArrayList<Integer>();
	public static int iNumTotalSamples = 0;
	public static int iNumSnps = 0;
	public static int iOrder = 2;
	
	private int[] iaSnpTasks;
	private EveTwoLoci eve;
	
	public ParExhaustion(ArrayList<Integer> _aiSnps){
		iaSnpTasks = new int[_aiSnps.size()];
		for(int i=0;i<_aiSnps.size();i++){
			iaSnpTasks[i] = _aiSnps.get(i);
		}
	}

	public static void initilizeMemory(int _nCase, int _nSample, int _nSNP, ArrayList<Integer> _aiSnpIdxInEachFile) {
		iNumTotalSamples = _nSample;
		iNumSnps = _nSNP;
		SNPData = new Datag(_nCase, _nSample - _nCase, _nSNP);
		for (int i = 0; i < _aiSnpIdxInEachFile.size(); i++) {
			aiSnpIdxInEachFile.add(_aiSnpIdxInEachFile.get(i));
		}
	}

	public static void loadingData(ArrayList<String> _asGwasFiles) {
		ArrayList<BufferedReader> aBr = new ArrayList<BufferedReader>();
		BufferedReader br;

		try {
			for (int i = 0; i < _asGwasFiles.size(); i++) {
				br = new BufferedReader(new FileReader(_asGwasFiles.get(i)));
				aBr.add(br);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String sLine;
		ArrayList<String> asLines = new ArrayList<String>();
		try {
			for (int i = 0; i < iNumTotalSamples; i++) {
				asLines.clear();
				for (int j = 0; j < aBr.size(); j++) {
					sLine = aBr.get(j).readLine();
					asLines.add(sLine);
				}
				SNPData.add(asLines);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// close file reader
		try {
			for (int i = 0; i < aBr.size(); i++) {
				aBr.get(i).close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if(iOrder == 2){
			eve = new EveTwoLoci(SNPData);
			if(aiSnpIdxInEachFile.size()==1){
				testOneFile();
			}else{
				testTwoFiles();
			}
		}
	}
	
	public static void setOrder(int _iOrder){
		iOrder = _iOrder;
	}
	
	private void testOneFile(){
		int[] combinations = new int[2];
		for(int i=0;i<iaSnpTasks.length;i++){
			combinations[0] = iaSnpTasks[i];
			for(int j=i+1;j<iNumSnps;j++){
				combinations[1] = j;
				eve.evaluate2Par(combinations, SNPData);
			}
		}
	}
	
	private void testTwoFiles(){
		int[] combinations = new int[2];
		for(int i=0;i<iaSnpTasks.length;i++){
			combinations[0] = iaSnpTasks[i];
			for(int j=aiSnpIdxInEachFile.get(1);j<iNumSnps;j++){
				combinations[1] = j;
				eve.evaluate2Par(combinations, SNPData);
			}
		}
	}

}
