package szenczewski.cse355.project.convexhull;

public class Point
{
	private int x;
	private int y;
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int GetX()
	{
		return x;
	}
	
	public int GetY()
	{
		return y;
	}
	
	@Override
	public String toString()
	{
		String s = "[";

		s += x;
		s += ",";
		s += y;
		s += "]";
		
		return s;
	}
	
	public void Multiply(int n)
	{
		x *= n;
		y *= n;
	}
}
