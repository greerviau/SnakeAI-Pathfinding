public class Square {
	
	//X AND Y FOR THE SQUARE
	private int x;
	private int y;
	
	//CONSTRUCTOR FOR SQUARE
	public Square(int X, int Y) {
		//SET THE INITIAL X AND Y
		x = X;
		y = Y;
	}
	
	public double getDistance(int X, int Y) {
		double distance;
		double xdif = Math.abs(X-x);
		double ydif = Math.abs(Y-y);
		distance = xdif+ydif;
		return distance;
	}
	
	//GET X AND Y
	public int getX() { return x; }
	public int getY() { return y; }
	
	//SET THE X AND Y
	public void setX(int X) { x = X; }
	public void setY(int Y) { y = Y; }
	
	//ADD X AND Y VEL
	public void addX(int addX) { x += addX; }
	public void addY(int addY) { y += addY; }
}