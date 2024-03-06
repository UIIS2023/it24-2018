package shapes;

public class Proba {

	public static void main(String[] args) {
/*	Point p=new Point(5,6);
	System.out.println(p.toString());
Point p2=p;
System.out.println(p2.toString());*/
		
/*Object[] objects = new Object[3];
objects[0] = new Point(5, 7);
objects[1] = new Line(new Point(1, 0), new Point(5, 0));
objects[2] = new Circle(new Point(5, 7), 6);
for (int i = 0; i < objects.length; i++)
 System.out.println(objects[i].toString());*/
		
		Circle c=new Circle(new Point(6,7),8);
		Circle c1=new Circle(new Point(6,7),8);
		boolean t=c.equals(c1);
		System.out.println(t);
		
	}

}
