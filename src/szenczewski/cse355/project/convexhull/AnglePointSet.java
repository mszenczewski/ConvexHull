package szenczewski.cse355.project.convexhull;

import java.util.Arrays;

public class AnglePointSet extends PointSet
{
	private AnglePoint[] anglePointArray;
	private Point startingPoint;
	
	AnglePointSet(PointSet pointSet)
	{
		this.numPoints = pointSet.GetNumPoints();
		this.startingPoint = pointSet.GetStartingPoint();
		this.anglePointArray = new AnglePoint[numPoints];
		
		for (int i = 0; i < numPoints; i++)
		{
			Point p = pointSet.GetPointAtIndex(i);
			
			AnglePoint a = new AnglePoint(p, startingPoint);
			
			anglePointArray[i] = a;
			
		}
	}
	
	@Override
	public AnglePoint GetPointAtIndex(int index)
	{
		return anglePointArray[index];
	}	
	
	@Override
	public Point GetStartingPoint()
	{
		return startingPoint;
	}
	
	public void Sort()
	{
		Arrays.sort(anglePointArray);
	}
}
