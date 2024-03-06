package mvc;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import adapter.HexagonAdapter;
import hexagon.Hexagon;
import shapes.Circle;
import commands.Command;
import shapes.Donut;
import shapes.Line;
import shapes.Point;
import shapes.Rectangle;
import shapes.Shape;

public class DrawingModel {
	
	private List<Shape> shapes = new ArrayList<Shape>();
	
	public void add(Shape shape)
	{
		shapes.add(shape);
	}
	
	public void remove(Shape shape)
	{
		shapes.remove(shape);
	}
	
	public Shape get(int i)
	{
		return shapes.get(i);
	}

	public List<Shape> getShapes() {
		return shapes;
	}

	
}
