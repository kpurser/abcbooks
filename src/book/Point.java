package book;
public class Point  extends java.awt.Point implements Comparable{

	public Point(int x, int y) {
		super(x, y);
	}

	@Override
	public int compareTo(Object arg0) {
		Point p = (Point) arg0;
		if(this.x < p.x){
			return - 5;
		}
		if(this.x > p.x){
			return 5;
		}
		if(this.x == p.x){
			if(this.y < p.y){
				return - 1;
			}
			if(this.y > p.y){
				return 1;
			}
		}
		return 0;
	}
	
	public int x(){
		return this.x;
	}
	
	public int y(){
		return this.y;
	}

}
