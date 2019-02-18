package szenczewski.cse355.project.convexhull;

public class AnglePoint extends Point implements Comparable<AnglePoint>
{
	private double angle;
	
	public AnglePoint(Point p, Point startingPoint)
	{
		super(p.GetX(), p.GetY());
		
		if (p == startingPoint)
		{
			angle = -100;
		}
		else
		{
			angle = Math.atan2(p.GetY() - startingPoint.GetY(), p.GetX() - startingPoint.GetX());
		}
	}
	
	public double GetAngle()
	{
		return angle;
	}
	
	@Override
	public int compareTo(AnglePoint ap)
	{
			if (ap.angle > this.angle)
			{
				return -1;
			}
			
			if (ap.angle < this.angle)
			{
				return 1;
			}
			
			return 0;
	}
}
