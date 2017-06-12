package fastl;

public class Datag {

	public int[][] iCase;
	public int[][] iCont;
	public int nEleCase;
	public int nEleCont;
	public int nSNP;
	private int idxCase = 0;
	private int idxCont = 0;
	private static int len = 32;
	private static int mask = 1;
	private static String strCase = "1";
	private static int[] wordbit = new int[256];
	private static String SPLIT = " ";
	private int[] tempCase;
	private int[] tempCont;
	
	public Datag(int _nCase, int _nCont, int _nSNP){
		nEleCase = (int) Math.ceil(((double)_nCase)/((double)len));
		nEleCont = (int) Math.ceil(((double)_nCont)/((double)len));
		iCase = new int[_nSNP*3][nEleCase];
		iCont = new int[_nSNP*3][nEleCont];
		tempCase = new int[nEleCase];
		tempCont = new int[nEleCont];
		nSNP = _nSNP;
		precompute();
	}
	
	public void add(String _str){
		String[] arrStr = _str.split(SPLIT);
		int temp = 0;
		if(arrStr[0].equals(strCase)){
			for(int i=0;i<nSNP;i++){
				temp = Integer.parseInt(arrStr[i+1]);
				if(temp>=3)continue;
				iCase[i*3+temp][idxCase/len] |= (mask<<(idxCase%len));
			}
			idxCase++;
		}else{
			for(int i=0;i<nSNP;i++){
				temp = Integer.parseInt(arrStr[i+1]);
				if(temp>=3)continue;
				iCont[i*3+temp][idxCont/len] |= (mask<<(idxCont%len));
			}
			idxCont++;
		}
	}
	
	private static int bitCount(int _i)
    {
        _i = (_i & 85) + ((_i >> 1) & 85);
        _i = (_i & 51) + ((_i >> 2) & 51);
        _i = (_i & 15) + ((_i >> 4) & 15);
        return _i;
    }
	
	public void clean(){
		idxCase = 0;
		idxCont = 0;
		for(int i=0;i<iCase.length;i++){
			for(int j=0;j<iCase[0].length;j++){
				iCase[i][j] = 0;
			}
		}
		for(int i=0;i<iCont.length;i++){
			for(int j=0;j<iCont[0].length;j++){
				iCont[i][j] = 0;
			}
		}
	}
	
	private static int getCount(int[] _arr)
    {
        int result = 0;
        for (int i = 0; i < _arr.length; i++)
        {
            result += wordbit[_arr[i] & 255] + wordbit[(_arr[i] >> 8) & 255] + wordbit[(_arr[i] >> 16) & 255] + wordbit[(_arr[i] >> 24) & 255];
        }
        return result;
    }
	
	public int getDistribution(int _id1, int _id2, int[][] _matrix, boolean[] _arrNotEmpty){
		
		int _size = 0;
		for(int j=0;j<3;j++){
			for(int k=0;k<3;k++){
				for(int i=0;i<nEleCase;i++){
					tempCase[i] = iCase[_id1*3+j][i]&iCase[_id2*3+k][i];
				}
				for(int i=0;i<nEleCont;i++){
					tempCont[i] = iCont[_id1*3+j][i]&iCont[_id2*3+k][i];
				}
				_matrix[0][_size] = getCount(tempCase);
				_matrix[1][_size] = getCount(tempCont);
				if(_matrix[0][_size]!=0||_matrix[1][_size]!=0){
					_arrNotEmpty[_size] = true;
					_size++;
				}
			}
		}
		return _size;
	}
	
	private int[] arrStatus = new int[5];
    private int idxStatus = 0;
    private boolean flagStatus = true;
	
	public int getDistribution(int[] _comb, int _size, int[][] _matrix, boolean[] _arrNotEmpty){
		
		int idx = 0;
        int nComb = 0;

        nComb = (int)Math.pow(3, _size);
        for (int i = 0; i < this.arrStatus.length; i++)
        {
            arrStatus[i] = 0;
        }

        do
        {
            //calculate distribution
            for (int i = 0; i < nEleCase; i++)
            {
                this.tempCase[i] = -1;
            }
            for (int i = 0; i < nEleCont; i++)
            {
                this.tempCont[i] = -1;
            }
            for (int i = 0; i < _size; i++)
            {

                for (int j = 0; j < nEleCase; j++)
                {
                    this.tempCase[j] &= this.iCase[_comb[i] * 3 + arrStatus[i]][j];
                }
                for (int j = 0; j < nEleCont; j++)
                {
                    this.tempCont[j] &= this.iCont[_comb[i] * 3 + arrStatus[i]][j];
                }
               
            }
            _matrix[0][idx] = getCount(this.tempCase);
            _matrix[1][idx] = getCount(this.tempCont);
            if (_matrix[0][idx] != 0 || _matrix[1][idx] != 0)
            {
            	_arrNotEmpty[idx] = true;
                idx++;
            }
            nComb--;
            if (nComb == 0) break;
            //update status
            idxStatus = _size - 1;
            while (flagStatus)
            {
                arrStatus[idxStatus]++;
                if (arrStatus[idxStatus] > 2)
                {
                    arrStatus[idxStatus] = 0;
                    idxStatus--;
                }
                else
                {
                    flagStatus = false;
                }
            }
            flagStatus = true;
        } while (nComb > 0);

        return idx;
	}
	
	private static void precompute()
    {
        for (int i = 0; i < wordbit.length; i++)
        {
            wordbit[i] = bitCount(i);
        }
    }
}
