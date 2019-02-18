package szenczewski.cse355.project.convexhull;

import java.util.Arrays;
import java.util.Scanner;

public class PointSet
{
	private Point[] pointArray;
	protected int numPoints;
	
	PointSet()
	{
		this.numPoints = 0;
	}
	
	PointSet(PointSet ps)
	{
		this.numPoints = ps.numPoints;
		this.pointArray = new Point[numPoints];

		for (int i = 0; i < numPoints; i++)
		{			

			this.pointArray[i] = ps.GetPointAtIndex(i);
		}
	}
		
	public int GetNumPoints()
	{
		return numPoints;
	}
	
	public Point GetPointAtIndex(int index)
	{
		return pointArray[index];
	}
	
	public void Read(Scanner s)
	{
		numPoints = s.nextInt();
		s.nextLine();
		
		pointArray = new Point[numPoints];
		
		for (int i = 0; i < numPoints; i++)
		{			
			String dump = s.nextLine();
			
			dump = dump.replace("[", "");
			dump = dump.replace("]", " ");
			dump = dump.replace(",", " ");
			
			String[] dumpArray = dump.split("\\s+");
			
			int x = Integer.parseInt(dumpArray[0]);
			int y = Integer.parseInt(dumpArray[1]);

			pointArray[i] = new Point(x, y);
		}	
	}
	
	public Point GetStartingPoint()
	{
		Point p = pointArray[0];
		
		for (int i = 0; i < numPoints; i++)
		{
			if (p.GetY() > pointArray[i].GetY())
			{
				p = pointArray[i];
			}
			
			if (p.GetY() == pointArray[i].GetY() &&
				p.GetX() > pointArray[i].GetX()	 
				)
			{
				p = pointArray[i];
			}
		}
		
		return p;
	}
	
	public void AddPoint(Point p)
	{
		numPoints++;
		
		Point[] newPointArray = new Point[numPoints];
		
		for (int i = 0; i < numPoints - 1; i++)
		{
			newPointArray[i] = pointArray[i];
		}
		
		newPointArray[numPoints-1] = p;
		
		pointArray = newPointArray;
	}
	
	public void RemovePoint(Point p)
	{
		Boolean found = false;
		
		for (int i = 0; i < numPoints; i++)
		{
			if (p == pointArray[i])
			{
				found = true;
			}
		}
		
		if (!found)
		{
			return;
		}
		
		numPoints--;

		Point[] newPointArray = new Point[numPoints];
		
		int j = 0;
		for (int i = 0; i < numPoints + 1; i++)
		{
			if (p != pointArray[i])
			{
				newPointArray[j] = pointArray[i];
				j++;
			}
		}
		
		pointArray = newPointArray;
	}
	
	public int GetSmallestX()
	{
		int x = pointArray[0].GetX();
		
		for (int i = 0; i < numPoints; i++)
		{
			if (pointArray[i].GetX() < x)
			{
				x = pointArray[i].GetX();
			}	
		}
		
		return x;
	}
	
	public int GetBiggestX()
	{
		int x = pointArray[0].GetX();
		
		for (int i = 0; i < numPoints; i++)
		{
			if (pointArray[i].GetX() > x)
			{
				x = pointArray[i].GetX();
			}	
		}
		
		return x;
	}
	
	public int GetSmallestY()
	{
		int y = pointArray[0].GetY();
		
		for (int i = 0; i < numPoints; i++)
		{
			if (pointArray[i].GetY() < y)
			{
				y = pointArray[i].GetY();
			}	
		}
		
		return y;
	}
	
	public int GetBiggestY()
	{
		int y = pointArray[0].GetY();
		
		for (int i = 0; i < numPoints; i++)
		{
			if (pointArray[i].GetY() > y)
			{
				y = pointArray[i].GetY();
			}	
		}
		
		return y;
	}
	
	public double GetYMedian()
	{
		int[] y_array = new int[numPoints];
		
		for (int i = 0; i < numPoints; i++)
		{
			y_array[i] = pointArray[i].GetY();
		}
		
		Arrays.sort(y_array);
				
		if (numPoints % 2 == 0)
		{
			return y_array[numPoints/2];
		}

		int median_index_low = numPoints/2;
		int median_index_high = median_index_low + 1;
		
		return (y_array[median_index_low] + y_array[median_index_high])/2;
	}
	
	public void Multiply(int n)
	{
		for (int i = 0; i < numPoints; i++)
		{
			pointArray[i].Multiply(n); 
		}
	}
}
