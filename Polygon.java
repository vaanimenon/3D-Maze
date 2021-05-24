import java.awt.Polygon;
public class Polygon{
	private int[] x,y;

	public Polygon(int[] x,int[] y){
		this.x=x;
		this.y=y;
	}
	public int[] getX(){
		return x;
	}
	public int[] getY(){
		return y;
	}
	//maybe make a method to return a polygon....it will simplify things
	public Polygon getPoly(){
		return new Polygon(y,x,x.length);
	}
}
