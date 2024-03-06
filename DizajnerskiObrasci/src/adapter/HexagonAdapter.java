package adapter;

import java.awt.Color;
import java.awt.Graphics;

import shapes.Point;
import shapes.SurfaceShape;
import hexagon.Hexagon;

public class HexagonAdapter extends SurfaceShape {
	private Hexagon hexagon;
	
	public HexagonAdapter() {
		
	}
	
	public HexagonAdapter(Point center, int r, Color insideColor, Color outsideColor) {
		this.hexagon = new Hexagon(center.getX(), center.getY(), r);
		this.hexagon.setAreaColor(insideColor);
		this.hexagon.setBorderColor(outsideColor);
	}
		
	public Hexagon getHexagon() {
		return hexagon;
	}

	public void setHexagon(Point center, int r, Color insideColor, Color outsideColor) {
		this.hexagon = new Hexagon(center.getX(), center.getY(), r);
		this.hexagon.setAreaColor(insideColor);
		this.hexagon.setBorderColor(outsideColor);
		hexagon.setSelected(true);
	}

	@Override
	public void moveBy(int byX, int byY) {
		hexagon.setX(hexagon.getX() + byX); 	
	  	hexagon.setY(hexagon.getY() + byY);
	}

	@Override
	public int compareTo(Object o) {
		if(o instanceof Hexagon) {
			Hexagon h = (Hexagon) o;
			return (int) (hexagon.getR() - h.getR());
		}
		else
			return 0;
	}
	
	@Override
	public boolean contains(int x, int y) {
		return hexagon.doesContain(x, y);
	}
	
	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		hexagon.setSelected(selected);
	}
	
	@Override
	public boolean isSelected() {
		return hexagon.isSelected();
	}
	
	public boolean equals(Object obj){
		if(obj instanceof HexagonAdapter){
			HexagonAdapter hexAdapter = (HexagonAdapter) obj;
			Point p1 = new Point(hexagon.getX(),hexagon.getY());
			Point p2 = new Point(hexAdapter.hexagon.getX(),hexAdapter.hexagon.getY());
			if(p1.equals(p2) && hexagon.getR() == hexAdapter.getHexagon().getR())
				return true;
			else
				return false;

		}
		else
			return false;
	}
	
	public HexagonAdapter clone() {
		HexagonAdapter ha = new HexagonAdapter(new Point(-1,-1),-1,Color.black, Color.black);
		
		ha.getHexagon().setX(this.getHexagon().getX());
		ha.getHexagon().setY(this.getHexagon().getY());
		ha.getHexagon().setR(this.getHexagon().getR());
		ha.getHexagon().setBorderColor(this.getHexagon().getBorderColor());
		ha.getHexagon().setAreaColor(this.getHexagon().getAreaColor());
		
		return ha;
	}
	
	public double area() {
		return hexagon.getR() * hexagon.getR() * Math.PI;
	}

	@Override
	public void draw(Graphics g) {
		hexagon.paint(g);
		
	}
	
	@Override
	public void fill(Graphics g) {
		// TODO Auto-generated method stub
	}
	
	public String toString() {
		return "Hexagon: (" + getCenter().getX() + ", " + getCenter().getY() + "), " + "Radius="+getRadius()+", Inside Color: ("+Integer.toString(getInsideColor().getRGB())+")" + ", Outside Color: ("+Integer.toString(getOutsideColor().getRGB())+")";
	}
	
	
	public int getRadius() {
		return hexagon.getR();
	}
	
	public void setRadius(int radius) {
		hexagon.setR(radius);
	}
	
	public Point getCenter() {
		return new Point(hexagon.getX(),hexagon.getY());
	}
	
	public void setCenter(Point center) {
		hexagon.setX(center.getX());
		hexagon.setY(center.getY());
	}
	public Color getInsideColor() {
		return hexagon.getAreaColor();
	}

	public void setInsideColor(Color insideColor) {
		hexagon.setAreaColor(insideColor);
	}
	
	public Color getOutsideColor() {
		return hexagon.getBorderColor();
	}

	public void setOutsideColor(Color outsideColor) {
		hexagon.setBorderColor(outsideColor);
	}
		
}
