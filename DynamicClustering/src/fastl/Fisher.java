package fastl;

public class Fisher {

	private int a;
	private int b;
	private int c;
	private int d;
	private double p;
	private int j;
	private int i;
	private int l;
	private int[][] matrix = new int[2][2];
	
	public void setVectors(int[][] _matrix, int _c1, int _c2){
		for(int i=0;i<2;i++){
			matrix[i][0] = _matrix[i][_c1];
			//if(matrixO2[i][0]==0)matrixO2[i][0]=0.1;
			matrix[i][1] = _matrix[i][_c2];
			//if(matrixO2[i][1]==0)matrixO2[i][1]=0.1;
		}
	}
	
	public double twoSide(){
		/*if(matrix[0][0]==0&&matrix[0][1]==0){
			return 0;
		}else if(matrix[1][0]==0&&matrix[1][1]==0){
			return 0;
		}*/
		double p = fisher();
		double current = p;
		if(a>c){
			if(c==0||b==0)return p;
			for(i=c-1;i>=0;i--){
				current = current*b*c/((a+1)*(d+1));
				p += current;
				a++;
				c--;
				b--;
				d++;
				if(c==0||b==0)break;
			}
		}else{
			if(a==0||d==0)return p;
			for(i=a-1;i>=0;i--){
				current = current*a*d/((c+1)*(b+1));
				p += current;
				a--;
				c++;
				b++;
				d--;
				if(a==0||d==0)break;
			}
		}
		
		return p;
	}
	
	public double fisher(){
		p = 1;
		a = matrix[0][0]+matrix[0][1];
		b = matrix[1][0]+matrix[1][1];
		j = a + b;
		c = matrix[0][0]+matrix[1][0];
		d = matrix[0][1]+matrix[1][1];
		if(a<=b&&a<=c&&a<=d){
			c = matrix[0][1]; d = matrix[0][0]; a = matrix[1][1]; b = matrix[1][0];
		}else if(b<=a&&b<=c&&b<=d){
			c = matrix[1][0]; d = matrix[1][1]; a = matrix[0][0]; b = matrix[0][1];
		}else if(c<=a&&c<=b&&c<=d){
			c = matrix[0][0]; d = matrix[1][0]; a = matrix[0][1]; b = matrix[1][1];
		}else{
			c = matrix[1][1]; d = matrix[0][1]; a = matrix[1][0]; b = matrix[0][0];
		}
		l = d;
		for(i = (a + 1);i<= a + c;i++){
			p = p * i;
			if(p>1){
				p = p/j;
				j = j - 1;
			}
		}
		for(i = (c + 1);i<= c + d;i++){
			p = p * i;
			if(p>1){
				p = p/j;
				j = j - 1;
			}
		}
		for(i = (b + 1);i<= b + d;i++){
			p = p * i;
			if(p>1&&j>(a+b)){
				p = p/j;
				j = j - 1;
			}
			if(p>1){
				p = p/l;
				l = l - 1;
			}
		}
		while(j>(a+b)){
			p = p/j;
			j = j - 1;
		}
		while(l>1){
			p = p/l;
			l = l - 1;
		}
		return p;
	}
	
	public static void main(String[] args){
		int[][] matrix = {{5,5},{2,0}};
		
		Fisher fish = new Fisher();
		fish.setVectors(matrix, 0, 1);
		System.out.println(fish.twoSide());
	}
	
}
