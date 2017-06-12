package fastl;

public class Phi {

	private double a;
	private double b;
	private double c;
	private double d;
	private double p;
	private double e;
	private double f;
	private double g;
	private double h;
	private int[][] matrix = new int[2][2];
	
	public void setVectors(int[][] _matrix, int _c1, int _c2){
		for(int i=0;i<2;i++){
			matrix[i][0] = _matrix[i][_c1];
			//if(matrixO2[i][0]==0)matrixO2[i][0]=0.1;
			matrix[i][1] = _matrix[i][_c2];
			//if(matrixO2[i][1]==0)matrixO2[i][1]=0.1;
		}
	}
	
	public double getPhi(){
		a = matrix[0][0];
		b = matrix[0][1];
		c = matrix[1][0];
		d = matrix[1][1];
		if(a==0&&b==0){
			a = 0.001;
			b = 0.001;
		}else if(c==0&&d==0){
			c = 0.001;
			d = 0.001;
		}
		e = a + b;
		f = c + d;
		g = a + c;
		h = b + d;
		p = (a*d - b*c);
		
		p = p/Math.sqrt(e*f*g*h);
		
		return p>=0?p:-p;
	}
	
}
