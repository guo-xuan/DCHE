package mtif;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import fastl.Datag;
import fastl.ListAscendStore;
import fastl.ListMutipleElement;

public class Exhaustion {

	public static int[] funcSNP = { 0, 1, 2, 3 };
	// public static double critialValue =
	// 3.0145381508104504504504504504505E-9;//40.0634;//37.2344;//34.0687;
	public static int ksize = 2;
	public static int[] combination;
	public static double pv;
	public static Datag SNPData;
	public static int nSample;
	public static int nSNP;
	public static int nCase;

	private int countConstant = 10000;
	private int countEvaluation = 0;
	public boolean flagPrint = false;
	private double[] arrCV = new double[3];
	private ListAscendStore[] arrListLocus = new ListAscendStore[3];
	// private ListAscendStore listTwoLocus = new ListAscendStore(1, 2);
	// private ListAscendStore listThreeLocus = new ListAscendStore(1, 3);
	// private ListAscendStore listFourLocus = new ListAscendStore(1, 4);
	private EveTwoLoci eve = new EveTwoLoci();
	private int accuarcy = 0;

	private int accuarcy2 = 0;

	private String enter = "\n";

	private double[][] dlSingleChiSquareTest;

	public Exhaustion(int _nSample, int _nSNP, int _nCase) {
		nCase = _nCase;
		nSample = _nSample;
		nSNP = _nSNP;
		SNPData = new Datag(_nCase, _nSample - _nCase, _nSNP);
		int[] list = { 200, 50, 10 };
		this.setList(list);
		double[] alpha = { 1.5E-03, 1.2E-07, 1.0E-21 };
		this.setCV(alpha);
	}

	public Exhaustion(int _nSample, int _nSNP, int _nCase, double[] _alpha, int[] _list) {
		nCase = _nCase;
		nSample = _nSample;
		nSNP = _nSNP;
		SNPData = new Datag(_nCase, _nSample - _nCase, _nSNP);
		this.setCV(_alpha);
		this.setList(_list);
	}

	public void checkResult() {
		ListMutipleElement pair = arrListLocus[0].getTop();
		if (pair.list[0] == funcSNP[0] && pair.list[1] == funcSNP[1]) {
			System.out.print("t");
			if (pair.value > arrCV[0]) {
				System.out.print("f");
			}
			this.accuarcy++;
		} else if (pair.list[1] == funcSNP[0] && pair.list[0] == funcSNP[1]) {
			System.out.print("t");
			if (pair.value > arrCV[0]) {
				System.out.print("f");
			}
			this.accuarcy++;
		} else {
			System.out.print("f");
		}
		System.out.print("\t");
		arrListLocus[0].list[0].print();
	}

	public void checkResult(FileWriter fwR) throws IOException {
		ListMutipleElement pair = arrListLocus[0].getTop();
		pair.Sort();
		if (pair.list[0] == funcSNP[0] && pair.list[1] == funcSNP[1]) {
			fwR.write("t");
			if (pair.value > arrCV[0]) {
				fwR.write("f");
			}
		} else {
			fwR.write("f");
		}
		fwR.write("\t");
		fwR.write(pair.getString());
		fwR.write("\n");
	}

	public void checkResultsThree(FileWriter fwR) throws IOException {
		ListMutipleElement pair = arrListLocus[1].getTop();
		pair.Sort();
		if (pair.list[0] == funcSNP[0] && pair.list[1] == funcSNP[1] && pair.list[2] == funcSNP[2]) {
			fwR.write("t");
			if (pair.value > arrCV[1]) {
				fwR.write("f");
			}
		} else {
			fwR.write("f");
		}
		fwR.write("\t");
		fwR.write(pair.getString());
		fwR.write("\n");
	}

	public void checkResultsFour(FileWriter fwR) throws IOException {
		ListMutipleElement pair = arrListLocus[2].getTop();
		pair.Sort();
		if (pair.list[0] == funcSNP[0] && pair.list[1] == funcSNP[1] && pair.list[2] == funcSNP[2]
				&& pair.list[3] == funcSNP[3]) {
			fwR.write("t");
			if (pair.value > arrCV[2]) {
				fwR.write("f");
			}
		} else {
			fwR.write("f");
		}
		fwR.write("\t");
		fwR.write(pair.getString());
		fwR.write("\n");
	}

	public void clean() {
		this.accuarcy = 0;
		this.accuarcy2 = 0;
	}

	public static int combinatorial(int _n, int _x) {
		int result = 1;
		for (int i = 0; i < _x; i++) {
			result *= (_n - i);
		}
		for (int i = 0; i < _x; i++) {
			result /= (i + 1);
		}
		return result;
	}

	public double testSearch(int[] combinations) {
		double value = eve.evaluate(combinations, SNPData);
		return value;
	}

	public void oneSearch() {
		dlSingleChiSquareTest = new double[nSNP][];
		int[] combinations = new int[1];
		for (int i = 0; i < nSNP; i++) {
			combinations[0] = i;
			dlSingleChiSquareTest[i] = eve.evaluateOneSnp(combinations, SNPData);
		}
	}

	public void twoSearch() {
		arrListLocus[0].clean();
		this.countEvaluation = 0;
		int[] combinations = new int[2];
		double value = 0;
		for (int i = 0; i < nSNP; i++) {
			// i = 50327;
			combinations[0] = i;
			for (int j = i + 1; j < nSNP; j++) {
				// j = 23385;
				combinations[1] = j;
				// int[] comb = {37680, 50327}; combinations = comb;
				// combinations[0] = 0; combinations[1] = 999;
				if (flagPrint) {
					if (this.countEvaluation % countConstant == 0)
						System.out.print(this.countEvaluation + " ");
				}
				this.countEvaluation++;
				value = eve.evaluate(combinations, SNPData);
				// value = eve.evaluate2(combinations, SNPData);
				// value = eve.evaluate3(combinations, SNPData);
				arrListLocus[0].add(combinations, value);
			}
		}
	}

	public void twoSearch2() {
		arrListLocus[0].clean();
		int[] combinations = new int[2];
		double value = 0;
		for (int i = 0; i < nSNP; i++) {
			combinations[0] = i;
			for (int j = i + 1; j < nSNP; j++) {
				combinations[1] = j;
				// combinations[0] = 0; combinations[1] = 999;
				// value = eve.evaluate(combinations, SNPData);
				value = eve.evaluate2(combinations, SNPData);
				arrListLocus[0].add(combinations, value);
			}
		}
	}

	public void threeSearch() {
		arrListLocus[1].clean();
		this.countEvaluation = 0;
		int[] combinations = new int[3];
		double value = 0;
		for (int i = 0; i < arrListLocus[0].idx; i++) {
			for (int j = 0; j < nSNP; j++) {
				combinations[0] = arrListLocus[0].list[i].list[0];
				combinations[1] = arrListLocus[0].list[i].list[1];
				if (combinations[0] != j && combinations[1] != j) {
					combinations[2] = j;
					// combinations[0] = 0; combinations[1] = 1; combinations[2]
					// = 2;
					Sort(combinations);
					if (flagPrint) {
						if (this.countEvaluation % countConstant == 0)
							System.out.print(this.countEvaluation + " ");
					}
					this.countEvaluation++;
					value = eve.evaluate(combinations, SNPData);
					// value = eve.evaluate2(combinations, SNPData);
					arrListLocus[1].add(combinations, value);
				}
			}
		}
	}

	public void fourSearch() {
		arrListLocus[2].clean();
		this.countEvaluation = 0;
		int[] combinations = new int[4];
		double value = 0;
		for (int i = 0; i < arrListLocus[1].idx; i++) {
			for (int j = 0; j < nSNP; j++) {
				combinations[0] = arrListLocus[1].list[i].list[0];
				combinations[1] = arrListLocus[1].list[i].list[1];
				combinations[2] = arrListLocus[1].list[i].list[2];
				if (combinations[0] != j && combinations[1] != j && combinations[2] != j) {
					combinations[3] = j;
					Sort(combinations);
					if (flagPrint) {
						if (this.countEvaluation % countConstant == 0)
							System.out.print(this.countEvaluation + " ");
					}
					this.countEvaluation++;
					value = eve.evaluate(combinations, SNPData);
					arrListLocus[2].add(combinations, value);
				}
			}
		}
	}

	public void output(FileWriter fwR) throws IOException {
		fwR.write("Total " + this.accuarcy + "\n");
		fwR.write("Another " + this.accuarcy2 + "\n");
	}

	public void output() {
		System.out.println("Total " + this.accuarcy);
	}

	public void printTwoAll() {
		arrListLocus[0].printAll();
	}

	public void printThreeAll() {
		arrListLocus[1].printAll();
	}

	public void printTwo(FileWriter fwR) throws IOException {
		arrListLocus[0].printAll(fwR);
	}

	public void printTwoOne(FileWriter fwR) throws IOException {
		if (arrListLocus[0].idx == 0) {
			fwR.write(enter);
			return;
		}
		fwR.write(arrListLocus[0].list[0].getString());
		fwR.write(enter);
	}

	public void printThree(FileWriter fwR) throws IOException {
		arrListLocus[1].printAll(fwR);
	}

	public void printFour(FileWriter fwR) throws IOException {
		arrListLocus[2].printAll(fwR);
	}

	private static int idxSort = -1;
	private static int tempSort = 0;

	public static void Sort(int[] _arr) {
		idxSort = -1;
		tempSort = 0;
		for (int i = 0; i < _arr.length; i++) {
			idxSort = i;
			for (int j = i + 1; j < _arr.length; j++) {
				if (_arr[j] < _arr[idxSort]) {
					idxSort = j;
				}
			}
			if (idxSort != i) {
				tempSort = _arr[i];
				_arr[i] = _arr[idxSort];
				_arr[idxSort] = tempSort;
			}
		}
	}

	/**
	 * only read the second column through the last column
	 * 
	 * @param path
	 * @param _row
	 * @param _col
	 * @param _lastD
	 */
	public void readData(String path) {
		String inputLine = null;
		SNPData.clean();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while ((inputLine = br.readLine()) != null) {
				SNPData.add(inputLine);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setCV(double[] _alpha) {
		arrCV = new double[_alpha.length];
		for (int i = 0; i < _alpha.length; i++) {
			arrCV[i] = _alpha[i] / combinatorial(nSNP, i + 2);
		}
	}

	public void setList(int[] _list) {
		arrListLocus = new ListAscendStore[_list.length];
		for (int i = 0; i < _list.length; i++) {
			arrListLocus[i] = new ListAscendStore(_list[i], i + 2);
		}
	}

	public void writeResults(String _fileName, int _order) {
		try {
			FileWriter fwR = new FileWriter(_fileName, true);
			arrListLocus[_order].printAll(fwR, arrCV[_order]);
			fwR.flush();
			fwR.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeResults(String _fileName) {
		try {
			FileWriter fwR = new FileWriter(_fileName, true);
			for (int i = 0; i < nSNP; i++) {
				fwR.write(Integer.toString(i + 1) + "\t");
				fwR.write(Double.toString(dlSingleChiSquareTest[i][0]) + "\t");
				fwR.write(Double.toString(dlSingleChiSquareTest[i][1]));
				fwR.write(enter);
			}

			fwR.flush();
			fwR.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test() {
		int id = 0;
		String tab = "\t";
		String ca = "1";
		int[] arrIntCase = new int[50];
		int[] arrIntControl = new int[50];
		try {
			BufferedReader br = new BufferedReader(new FileReader("test_0_4.txt"));
			String inputLine = null;
			String[] arrStr = null;
			while ((inputLine = br.readLine()) != null) {
				arrStr = inputLine.split(tab);
				id = 0;
				for (int i = 1; i < 3; i++) {
					id = (id << 2) + Integer.parseInt(arrStr[i]);
				}
				if (arrStr[0].compareTo(ca) == 0) {
					arrIntCase[id]++;
				} else {
					arrIntControl[id]++;
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
