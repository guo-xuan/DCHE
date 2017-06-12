package mtif;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DC {

	public static void main(String[] args) throws IOException{

		//test();
		String folderPath = "D:\\Code\\SNP\\Model\\AdditiveModel_3rd\\6\\";//"D:\\Research\\Research-Data\\SNP\\AMD\\";//"/home/xguo9/SNP/Model5/2000/";
		String prename = "simdata";
		String postname = ".txt";
		int st = 1, ed = 101;
		int nSNP = 1000;//90449;//1000;
		int nSample = 1600;
		int nCase = 800;
		String resultsPath = "";
		String resultsName = "0.4_1600";
		int[] answers = {0, 1, 2, 3};
		double cv = (1.5E-03);
		if(args.length==11){
			folderPath = args[0];
			st = Integer.parseInt(args[1]);
			ed = Integer.parseInt(args[2]);
			nSNP = Integer.parseInt(args[3]);
			nSample = Integer.parseInt(args[4]);
			nCase = Integer.parseInt(args[5]);
			resultsPath = args[6];
			resultsName = args[7];
			answers[0] = Integer.parseInt(args[8]);
			answers[1] = Integer.parseInt(args[9]);
			cv = Double.parseDouble(args[10]);
			//cv = cv / ((nSNP*(nSNP-1)*(nSNP-2))/(3*2));
			Exhaustion.funcSNP = answers;
			//Exhaustion.critialValue = cv;
		}else{
			System.out.println("java -jar DC.jar " +
					"Model_folder_path " +
					"start_order " +
					"end_order " +
					"nSNP " +
					"nSample " +
					"nCase " +
					"Results_Path " +
					"Results_File_Name " +
					"funSNP1 " +
					"funSNP2 " +
					"critical_value");
			System.out.println("java -jar DC.jar " +
					"/home/xguo9/SNP/false/800/ " +
					"0 " +
					"100 " +
					"2000 " +
					"800 " +
					"400 " +
					"/home/xguo9/SNP/false/ChiDC/resultshighorder/ " +
					"false0 " +
					"0 " +
					"1 " +
					"0.00340311151758776");
			//return;
		}
		String filepath;
		for(int i=st;i<ed;i++){
			System.out.println("Process "+i);
			Exhaustion objE = new Exhaustion(nSample, nSNP, nCase);
			//cv = cv / ((nSNP*(nSNP-1))/(2));
			double[] arrCV = new double[1]; arrCV[0] = cv;
			objE.setCV(arrCV);
			filepath = folderPath + prename + i + postname;
			//filepath = folderPath + prename + postname;
			objE.readData(filepath);
			objE.twoSearch();
			//objE.threeSearch();
			//objE.fourSearch();
			FileWriter fwR = new FileWriter(resultsPath+resultsName+".txt", true);
			objE.checkResult(fwR);
			//objE.checkResultsThree(fwR);
			fwR.flush();
			fwR.close();
			//objE.checkResult();
			//objE.printTwoAll();
			//objE.twoSearch2();
			//objE.printTwoAll();
			//FileWriter fwR = new FileWriter("falseRate.txt", true);
			//fwR.write(i+"\t");
			//objE.printTwoOne(fwR);
			//fwR.flush();
			//fwR.close();
			//objE.threeSearch();
			//fwR = new FileWriter(resultsPath+resultsName+"_3.txt", true);
			//objE.checkResultsThree(fwR);
			//fwR.flush();
			//fwR.close();
			//objE.printThreeAll();
			//objE.fourSearch();
			//fwR = new FileWriter(resultsPath+resultsName+"_4.txt", true);
			//objE.checkResultsFour(fwR);
			//fwR.flush();
			//fwR.close();
		}
		System.out.println("Over");
		
	}
	
	public static void test(){
		String filename = "D:\\Research\\2013_0709_SNP\\Results\\RA\\2_SNPs.txt";
		String filepath = "D:\\Code\\SNP\\RealDataConvert\\Data_narac_filted.txt";
		String mapname = "D:\\Code\\SNP\\RealDataConvert\\Data_narac.map";
		Exhaustion objE = new Exhaustion(868+1194, 487678, 868);
		objE.readData(filepath);
		System.out.println("end reading");
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			BufferedReader brN = new BufferedReader(new FileReader(mapname));
			String[] names = new String[487678];
			String inputLine;
			int i = 0;
			while((inputLine=brN.readLine())!=null){
				names[i] = inputLine;
				i++;
			}
			brN.close();
			FileWriter fw = new FileWriter("results_2.txt");
			int[] combinations = new int[2];
			String[] arrStr;
			String space = " ";
			String tab = "\t";
			String enter = "\n";
			double value = 0;
			i=0;
			while((inputLine=br.readLine())!=null){
				arrStr = inputLine.split(space);
				combinations[0] = Integer.parseInt(arrStr[0]);
				combinations[1] = Integer.parseInt(arrStr[1]);
				//combinations[2] = Integer.parseInt(arrStr[2]);
				//combinations[3] = Integer.parseInt(arrStr[3]);
				value = objE.testSearch(combinations);
				i++;
				System.out.println("processing "+i);
				fw.write(names[combinations[0]]+tab+names[combinations[1]]+tab+value);
				fw.write(enter);
				fw.flush();
			}
			br.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
