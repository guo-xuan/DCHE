package mtif;

import fastl.Datag;
import fastl.Fisher;
import fastl.ListAscendList;
import fastl.ListAscendListElement;
import fastl.ListDescendList;
import fastl.Phi;

public class EveTwoLoci {

	private boolean[] arrEmpty = new boolean[243];
	private int[][] matrix = new int[2][243];// 0 case 1 control
	private ListAscendList listFast = new ListAscendList(100);
	private ChiSquare2 calculator = new ChiSquare2();
	private Chi chiCal = new Chi();

	private static int numGroup = 3;
	private static int numGroupBound = 6;

	private ListDescendList listF = new ListDescendList(100);
	private Fisher fish = new Fisher();

	private Phi phi = new Phi();

	private int[] arrStatus = new int[5];
	private int[] tempCase;
	private int[] tempControl;
	
	public EveTwoLoci() {
	}

	public EveTwoLoci(Datag SNPData) {
		tempCase = new int[SNPData.nEleCase];
		tempControl = new int[SNPData.nEleCont];
	}

	public double evaluate3(int[] combinations, Datag _SNPData) {
		listFast.Thre = 0.05;
		int n = 0;
		int i = 9;
		n = _SNPData.getDistribution(combinations, combinations.length, matrix, arrEmpty);
		// from the back to front
		listFast.clean();
		for (i = 1; i < n; i++) {
			if (!arrEmpty[i])
				continue;
			for (int j = 0; j < i; j++) {
				if (!arrEmpty[j])
					continue;
				phi.setVectors(matrix, j, i);
				// listF.add(j, i, fish.fisher());
				listFast.add(j, i, phi.getPhi());
			}
		}
		// listFast.sort();
		ListAscendListElement pair;
		double result = 1;
		double temp;
		i = n;
		while (n > numGroup) {
			// combine first
			// pair = listFast.getTop(arrEmpty);
			pair = listFast.getTop();
			if (pair == null) {
				if (result == 1) {
					result = chiCal.cal(matrix, arrEmpty, i);
					result = ChiSquare2.pValue(n - 1, result);
				}
				return result;
			}
			matrix[0][i] = matrix[0][pair.id1] + matrix[0][pair.id2];
			matrix[1][i] = matrix[1][pair.id1] + matrix[1][pair.id2];
			arrEmpty[i] = true;
			arrEmpty[pair.id1] = false;
			arrEmpty[pair.id2] = false;
			n--;
			if (n <= numGroupBound) {
				temp = chiCal.cal(matrix, arrEmpty, i + 1);
				temp = ChiSquare2.pValue(n - 1, temp);
				if (temp < result)
					result = temp;
			}
			if (n <= numGroup)
				break;
			for (int j = 0; j < i; j++) {
				if (!arrEmpty[j])
					continue;
				phi.setVectors(matrix, j, i);
				// listF.add(j, i, fish.fisher());
				listFast.add(j, i, phi.getPhi());
			}
			// listFast.sort();
			i++;
		}

		// calculate the final chisquare value;
		// return chiCal.cal(matrix, arrEmpty);
		return result;
	}

	public double evaluate2(int[] combinations, Datag _SNPData) {
		int n = 0;
		int i = 9;
		n = _SNPData.getDistribution(combinations, combinations.length, matrix, arrEmpty);
		// from the back to front
		listF.clean();
		for (i = 1; i < n; i++) {
			if (!arrEmpty[i])
				continue;
			for (int j = 0; j < i; j++) {
				if (!arrEmpty[j])
					continue;
				fish.setVectors(matrix, j, i);
				listF.add(j, i, fish.fisher());
				// listF.add(j, i, fish.twoSide());
			}
		}
		// listFast.sort();
		ListAscendListElement pair;
		double result = 1;
		double temp;
		i = n;
		while (n > numGroup) {
			// combine first
			// pair = listF.getTop(arrEmpty);
			pair = listF.getTop();
			if (pair == null) {
				if (result == 1) {
					result = chiCal.cal(matrix, arrEmpty, i);
					result = ChiSquare2.pValue(n - 1, result);
				}
				return result;
			}
			matrix[0][i] = matrix[0][pair.id1] + matrix[0][pair.id2];
			matrix[1][i] = matrix[1][pair.id1] + matrix[1][pair.id2];
			arrEmpty[i] = true;
			arrEmpty[pair.id1] = false;
			arrEmpty[pair.id2] = false;
			n--;
			if (n <= numGroupBound) {
				temp = chiCal.cal(matrix, arrEmpty, i + 1);
				temp = ChiSquare2.pValue(n - 1, temp);
				if (temp < result)
					result = temp;
			}
			if (n <= numGroup)
				break;
			for (int j = 0; j < i; j++) {
				if (!arrEmpty[j])
					continue;
				fish.setVectors(matrix, j, i);
				listF.add(j, i, fish.fisher());
				// listF.add(j, i, fish.twoSide());
			}
			// listFast.sort();
			i++;
		}

		// calculate the final chisquare value;
		// return chiCal.cal(matrix, arrEmpty);
		return result;
	}

	public double evaluate(int[] combinations, Datag _SNPData) {
		int n = 0;
		int i = 9;
		n = _SNPData.getDistribution(combinations, combinations.length, matrix, arrEmpty);
		double result = 1;
		if (n <= numGroup && n >= 2) {
			result = chiCal.cal(matrix, arrEmpty, i);
			result = ChiSquare2.pValue(n - 1, result);
			return result;
		}
		// from the back to front
		listFast.clean();
		for (i = 1; i < n; i++) {
			if (!arrEmpty[i])
				continue;
			for (int j = 0; j < i; j++) {
				if (!arrEmpty[j])
					continue;
				calculator.setVectors(matrix, j, i);
				listFast.add(j, i, calculator.calculateChi2());
			}
		}
		// listFast.sort();
		ListAscendListElement pair;

		double temp;
		i = n;
		while (n > numGroup) {
			// combine first
			// pair = listFast.getTop(arrEmpty);
			pair = listFast.getTop();
			if (pair == null) {
				if (result == 1) {
					result = chiCal.cal(matrix, arrEmpty, i);
					result = ChiSquare2.pValue(n - 1, result);
				}
				return result;
			}
			matrix[0][i] = matrix[0][pair.id1] + matrix[0][pair.id2];
			matrix[1][i] = matrix[1][pair.id1] + matrix[1][pair.id2];
			arrEmpty[i] = true;
			arrEmpty[pair.id1] = false;
			arrEmpty[pair.id2] = false;
			n--;
			if (n <= numGroupBound) {
				temp = chiCal.cal(matrix, arrEmpty, i + 1);
				temp = ChiSquare2.pValue(n - 1, temp);
				if (temp < result)
					result = temp;
			}
			if (n <= numGroup)
				break;
			for (int j = 0; j < i; j++) {
				if (!arrEmpty[j])
					continue;
				calculator.setVectors(matrix, j, i);
				listFast.add(j, i, calculator.calculateChi2());
			}
			// listFast.sort();
			i++;
		}

		// calculate the final chisquare value;
		// return chiCal.cal(matrix, arrEmpty);
		return result;
	}

	public double evaluate2Par(int[] combinations, Datag _SNPData) {
		int n = 0;
		int i = 9;
		n = _SNPData.getDistributionPar(combinations, combinations.length, matrix, arrEmpty, arrStatus, tempCase,
				tempControl);
		double result = 1;
		// if n < numGroup
		if (n >= 2 && n <= numGroup) {
			result = chiCal.cal(matrix, arrEmpty, i);
			result = ChiSquare2.pValue(n - 1, result);
			return result;
		}
		// from the back to front
		listFast.clean();
		for (i = 1; i < n; i++) {
			if (!arrEmpty[i])
				continue;
			for (int j = 0; j < i; j++) {
				if (!arrEmpty[j])
					continue;
				calculator.setVectors(matrix, j, i);
				listFast.add(j, i, calculator.calculateChi2());
			}
		}
		// listFast.sort();
		ListAscendListElement pair;

		double temp;
		i = n;
		while (n > numGroup) {
			// combine first
			// pair = listFast.getTop(arrEmpty);
			pair = listFast.getTop();
			if (pair == null) {
				if (result == 1) {
					result = chiCal.cal(matrix, arrEmpty, i);
					result = ChiSquare2.pValue(n - 1, result);
				}
				return result;
			}
			matrix[0][i] = matrix[0][pair.id1] + matrix[0][pair.id2];
			matrix[1][i] = matrix[1][pair.id1] + matrix[1][pair.id2];
			arrEmpty[i] = true;
			arrEmpty[pair.id1] = false;
			arrEmpty[pair.id2] = false;
			n--;
			if (n <= numGroupBound) {
				temp = chiCal.cal(matrix, arrEmpty, i + 1);
				temp = ChiSquare2.pValue(n - 1, temp);
				if (temp < result)
					result = temp;
			}
			if (n <= numGroup){
				break;
			}
			for (int j = 0; j < i; j++) {
				if (!arrEmpty[j])
					continue;
				calculator.setVectors(matrix, j, i);
				listFast.add(j, i, calculator.calculateChi2());
			}
			// listFast.sort();
			i++;
		}

		// calculate the final chisquare value;
		// return chiCal.cal(matrix, arrEmpty);
		return result;
	}

	public double[] evaluateOneSnp(int[] combinations, Datag _SNPData) {
		int n = 0;
		int i = 3;
		n = _SNPData.getDistribution(combinations, combinations.length, matrix, arrEmpty);
		for (int j = 0; j < i; j++) {
			if (arrEmpty[j]) {
				if (matrix[0][j] == 0 && matrix[1][j] == 0) {
					arrEmpty[j] = false;
				}
			}
		}
		double[] result = { 0, 1 };
		if (n > 0) {
			result[0] = chiCal.cal(matrix, arrEmpty, i);
			result[1] = ChiSquare2.pValue(n - 1, result[0]);
		}
		return result;

	}

}

class Chi {

	private double sum1;
	private double sum2;
	private double sum3;
	private double sum;
	private double ex;
	private double result;

	public double cal(int[][] matrix, boolean[] arr) {
		sum1 = 0;
		sum2 = 0;
		result = 0;
		for (int i = 0; i < matrix[0].length; i++) {
			if (arr[i]) {
				sum1 += matrix[0][i];
				sum2 += matrix[1][i];
			}
		}
		sum = sum1 + sum2;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i]) {
				sum3 = matrix[0][i] + matrix[1][i];
				ex = sum3 * sum1 / sum;
				result += ((Math.pow(matrix[0][i] - ex, 2)) / ex);
				ex = sum3 * sum2 / sum;
				result += ((Math.pow(matrix[1][i] - ex, 2)) / ex);
			}
		}
		return result;
	}

	public double cal(int[][] _matrix, boolean[] _arr, int _size) {
		sum1 = 0;
		sum2 = 0;
		result = 0;
		for (int i = 0; i < _size; i++) {
			if (_arr[i]) {
				sum1 += _matrix[0][i];
				sum2 += _matrix[1][i];
			}
		}
		sum = sum1 + sum2;
		for (int i = 0; i < _size; i++) {
			if (_arr[i]) {
				sum3 = _matrix[0][i] + _matrix[1][i];
				ex = sum3 * sum1 / sum;
				result += ((Math.pow(_matrix[0][i] - ex, 2)) / ex);
				ex = sum3 * sum2 / sum;
				result += ((Math.pow(_matrix[1][i] - ex, 2)) / ex);
			}
		}
		return result;
	}

}
