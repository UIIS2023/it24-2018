package mvc;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import adapter.HexagonAdapter;
import commands.CmdBringBackward;
import commands.CmdBringForward;
import commands.CmdBringToBack;
import commands.CmdBringToFront;
import commands.CmdCircleAdd;
import commands.CmdCircleRemove;
import commands.CmdCircleUpdate;
import commands.CmdDonutAdd;
import commands.CmdDonutRemove;
import commands.CmdDonutUpdate;
import commands.CmdHexagonAdd;
import commands.CmdHexagonRemove;
import commands.CmdHexagonUpdate;
import commands.CmdLineAdd;
import commands.CmdLineRemove;
import commands.CmdLineUpdate;
import commands.CmdList;
import commands.CmdPointAdd;
import commands.CmdPointRemove;
import commands.CmdPointUpdate;
import commands.CmdRectangleAdd;
import commands.CmdRectangleRemove;
import commands.CmdRectangleUpdate;
import commands.CmdSelect;
import commands.CmdShapeAdd;
import commands.CmdShapeDeselect;
import commands.CmdShapeRemove;
import commands.CmdShapeSelect;
import commands.Command;
import drawing.DlgCircle;
import drawing.DlgCircle;
import drawing.DlgDelete;
import drawing.DlgDonut;
import drawing.DlgHexagon;
import drawing.DlgDonut;
import drawing.DlgLine;
import drawing.DlgPoint;
import drawing.DlgRectangle;

import exception.RadiusException;
import observer.BtnObserver;
import observer.BtnObserverUpdate;
import shapes.Circle;
import shapes.Donut;
import hexagon.Hexagon;
import shapes.Line;
import shapes.Point;
import shapes.Rectangle;
import shapes.Shape;
import projectStrategy.SaveLog;
import projectStrategy.SaveManager;
import projectStrategy.SaveShapes;
public class DrawingController {

	public DrawingModel model;
	public DrawingFrame frame;
	
	private List<Shape> selectedShapes = new ArrayList<Shape>();
	private CmdList cmdList = new CmdList();

	Point startPoint;
	Point upperLeftPoint;
	
	private BtnObserver btnObserver = new BtnObserver();
	private BtnObserverUpdate btnObserverUpdate;
	
	public DrawingController(DrawingModel model, DrawingFrame frame)
	{
		this.model=model;
		this.frame=frame;
		btnObserverUpdate = new BtnObserverUpdate(frame);
		btnObserver.addPropertyChangeListener(btnObserverUpdate);
		
	}
	
	private DlgPoint dlgPoint = new DlgPoint();
	private DlgLine dlgLine = new DlgLine();
	private DlgRectangle dlgRectangle = new DlgRectangle();
	private DlgDonut dlgDonut = new DlgDonut();
	
	
	private Shape selected = null;
	
	
	/*promenljive vezane za ucitavanje loga*/
	private ArrayList<String> logList = new ArrayList<String>();
	private int logCounter=0;
	private String line;
	private boolean loadLogEnd=false;
	
	
	private Stack<Command>stackForLog = new Stack<Command>();
	
	private Shape isSelectedShape=null;
	
	private int pomCMD; // Sluzi mi kao pomocna za log modifikaciju 
	public void mouseClicked(MouseEvent e) {
/***************************Selektovanje****************************************/
		if(frame.getTglbtnSelect().isSelected()) 
		{
			isSelectedShape=null;
			selected=null;	
			
			ListIterator<Shape> it = model.getShapes().listIterator();
			while(it.hasNext()) {
				isSelectedShape = it.next();
				if(isSelectedShape.contains(e.getX(),e.getY())) {
					
					selected =isSelectedShape;
				}
				
			}
				if(selected != null) {
					if(selected.isSelected()) {
						CmdShapeDeselect cmdShapeDeselect = new CmdShapeDeselect(this,selected);
						cmdShapeDeselect.execute();
						frame.getTextAreaLog().append("Deselect - "+selected+"\n");
						
						
						undoRedo(cmdShapeDeselect,selected,"Selected","Deselect");
						frame.getBtnUndo().setEnabled(true);
						frame.getBtnRedo().setEnabled(false);
						frame.getBtnLoadNext().setEnabled(false);
						loadLogEnd = true;
						
					} else {
						CmdShapeSelect cmdShapeSelect = new CmdShapeSelect(this,selected);
						cmdShapeSelect.execute();
						frame.getTextAreaLog().append("Selected - "+selected+"\n");
						
						undoRedo(cmdShapeSelect,selected,"Deselect","Selected");
						frame.getBtnUndo().setEnabled(true);
						frame.getBtnRedo().setEnabled(false);
						frame.getBtnLoadNext().setEnabled(false);
						loadLogEnd = true;
					}
				} else {
					
				  ListIterator<Shape> it1 = selectedShapes.listIterator(); while(it1.hasNext())
				  { isSelectedShape = it1.next(); isSelectedShape.setSelected(false); }
				  selectedShapes.clear();
				 
				}

				frame.getView().repaint();
		
		}
		else if(frame.getState() == 1) {
			pointDraw(e);
		}else if (frame.getState() == 2) {
			lineDraw(e);
		} else if (frame.getState() == 3) {
			circleDraw(e);
		} else if (frame.getState() == 4) {
			rectangleDraw(e);
		} else if (frame.getState() == 5) {
			donutDraw(e);
		}else if (frame.getState() == 7) {
			hexagonDraw(e);
		} 
		selectEditUpdate();
		frame.getView().repaint();	
		
	}
/********************Point draw *****************************/	
	public void pointDraw(MouseEvent me) {
		dlgPoint.getTxtX().setText(Integer.toString(me.getX()));
		dlgPoint.getTxtY().setText(Integer.toString(me.getY()));
		dlgPoint.setColor(frame.getBtnOutsideColor().getBackground());
		dlgPoint.getBtnColor().setBackground(frame.getBtnOutsideColor().getBackground());
	
		
		dlgPoint.setVisible(true);
		if(dlgPoint.isConfirmation())
		{
			Point point = new Point(me.getX(),me.getY(),dlgPoint.getColor());
			CmdPointAdd cmdPointAdd = new CmdPointAdd(model,point);
			cmdPointAdd.execute();
			
			undoRedo(cmdPointAdd,point,"Removed","Added");
			
			frame.getTextAreaLog().append("Added - "+point+"\n");
			frame.getBtnUndo().setEnabled(true);
			frame.getBtnRedo().setEnabled(false);
			frame.getBtnLoadNext().setEnabled(false);
			loadLogEnd = true;
		
		}
		
	}
	
/********************Line draw *****************************/		
	public void lineDraw(MouseEvent me) {
		if(startPoint == null)
		{
			startPoint = new Point(me.getX(),me.getY());
		}
		else
		{
			Point endPoint = new Point(me.getX(),me.getY());
			dlgLine.getTxtStartX().setText(Integer.toString(startPoint.getX()));
			dlgLine.getTxtStartY().setText(Integer.toString(startPoint.getY()));
			dlgLine.getTxtEndX().setText(Integer.toString(endPoint.getX()));
			dlgLine.getTxtEndY().setText(Integer.toString(endPoint.getY()));
			dlgLine.setColor(frame.getBtnOutsideColor().getBackground());
			dlgLine.getBtnColor().setBackground(frame.getBtnOutsideColor().getBackground());
			
			dlgLine.setVisible(true);
			if(dlgLine.confirmation)
			{
				
				Line line = new Line(startPoint,new Point(me.getX(),me.getY()),dlgLine.getBtnColor().getBackground()); 
				
				CmdLineAdd cmdLineAdd = new CmdLineAdd(line,model);
				cmdLineAdd.execute();
				
				undoRedo(cmdLineAdd,line,"Removed","Added");
				
				frame.getTextAreaLog().append("Added - "+line+"\n");
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);
				frame.getBtnLoadNext().setEnabled(false);
				loadLogEnd = true;
			}
			startPoint=null;
		}
	}
	
/********************Rectangle draw *****************************/	
	public void rectangleDraw(MouseEvent me) {
		DlgRectangle dlgRectangle = new DlgRectangle();
		int height = 0;
		int width = 0;
		dlgRectangle.getTxtX().setText(Integer.toString(me.getX()));
		dlgRectangle.getTxtY().setText(Integer.toString(me.getY()));
		dlgRectangle.setInnerColor(frame.getBtnInsideColor().getBackground());
		dlgRectangle.getBtnInnerColor().setBackground(frame.getBtnInsideColor().getBackground());
		dlgRectangle.setBorderColor(frame.getBtnOutsideColor().getBackground());
		dlgRectangle.getBtnBorderColor().setBackground(frame.getBtnOutsideColor().getBackground());
		
		dlgRectangle.setVisible(true);
		if(dlgRectangle.isConfirmation()) {
			width=Integer.parseInt(dlgRectangle.getTxtWidth().getText());
			height=Integer.parseInt(dlgRectangle.getTxtHeight().getText());
			Rectangle rectangle = new Rectangle(new Point(me.getX(),me.getY()),height,width,dlgRectangle.getInnerColor(),dlgRectangle.getBorderColor());
			
			rectangle.setSelected(false);
			try {
				CmdRectangleAdd cmdRectangleAdd = new CmdRectangleAdd(rectangle,model);
				cmdRectangleAdd.execute();
				undoRedo(cmdRectangleAdd,rectangle,"Removed","Added");
				
				frame.getTextAreaLog().append("Added - "+rectangle+"\n");
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);
				frame.getBtnLoadNext().setEnabled(false);
				loadLogEnd = true;
			
			} catch (Exception e) {
				
				JOptionPane.showMessageDialog(frame, "Wrong entry!", "Error", JOptionPane.ERROR_MESSAGE);
				
			}	
		}
		dlgRectangle.getTxtWidth().setText("");
		dlgRectangle.getTxtHeight().setText("");
	}
	
	/********************Circle draw *****************************/		
	public void circleDraw(MouseEvent me) {
		 DlgCircle dlgCircle = new DlgCircle();
		dlgCircle.getTxtX().setText(Integer.toString(me.getX()));
		dlgCircle.getTxtY().setText(Integer.toString(me.getY()));
		dlgCircle.setInnerColor(frame.getBtnInsideColor().getBackground());
		dlgCircle.getBtnInnerColor().setBackground(frame.getBtnInsideColor().getBackground());
		dlgCircle.setBorderColor(frame.getBtnOutsideColor().getBackground());
		dlgCircle.getBtnBorderColor().setBackground(frame.getBtnOutsideColor().getBackground());
		
		
		dlgCircle.setVisible(true);
		if(dlgCircle.isConfirmation()) {
			int radius = Integer.parseInt(dlgCircle.getTxtRadius().getText());
			Circle circle = new Circle(new Point(me.getX(),me.getY()),radius,false,
							dlgCircle.getBorderColor(),dlgCircle.getInnerColor());
			try {
				CmdCircleAdd cmdCircleAdd = new CmdCircleAdd(circle,model);
				cmdCircleAdd.execute();
				undoRedo(cmdCircleAdd,circle,"Removed","Added");
			
				frame.getTextAreaLog().append("Added - "+circle+"\n");
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);
				frame.getBtnLoadNext().setEnabled(false);
				loadLogEnd = true;
			} catch (Exception e) {
				
				JOptionPane.showMessageDialog(frame, "Wrong entry!", "Error", JOptionPane.ERROR_MESSAGE);
			}	
		}
		dlgCircle.getTxtRadius().setText("");
	}
/********************Donut draw *****************************/	
	public void donutDraw(MouseEvent me) {
		DlgDonut dlgDonut = new DlgDonut();
		dlgDonut.getTxtX().setText(Integer.toString(me.getX()));
		dlgDonut.getTxtY().setText(Integer.toString(me.getY()));
		dlgDonut.setInnerColor(frame.getBtnInsideColor().getBackground());
		dlgDonut.getBtnInnerColor().setBackground(frame.getBtnInsideColor().getBackground());
		dlgDonut.setBorderColor(frame.getBtnOutsideColor().getBackground());
		dlgDonut.getBtnBorderColor().setBackground(frame.getBtnOutsideColor().getBackground());
		
		int innerRadius = 0;
		int radius = 0;
		
		dlgDonut.setVisible(true);
		if(dlgDonut.isConfirmation())
		{
			radius = Integer.parseInt(dlgDonut.getTxtRadius().getText());
			innerRadius = Integer.parseInt(dlgDonut.getTxtInnerRadius().getText());
			
			Donut donut = new Donut(new Point(me.getX(),me.getY()),radius,innerRadius,dlgDonut.getBorderColor(),dlgDonut.getInnerColor());
				if(dlgDonut.confirmation)
				{
					
					CmdDonutAdd cmdDonutAdd = new CmdDonutAdd(donut,model);
					cmdDonutAdd.execute();
					
					undoRedo(cmdDonutAdd,donut,"Removed","Added");
					frame.getTextAreaLog().append("Added - "+donut+"\n");
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
					frame.getBtnLoadNext().setEnabled(false);
					loadLogEnd = true;
				}
				
			}
		dlgDonut.getTxtRadius().setText("");
		dlgDonut.getTxtInnerRadius().setText("");
		}
/********************Hexagon draw *****************************/		
	public void hexagonDraw(MouseEvent me) {
		
		DlgHexagon dlgHexagon = new DlgHexagon();
		dlgHexagon.getTxtX().setText(Integer.toString(me.getX()));
		dlgHexagon.getTxtY().setText(Integer.toString(me.getY()));
		dlgHexagon.setInnerColor(frame.getBtnInsideColor().getBackground());
		dlgHexagon.getBtnInnerColor().setBackground(frame.getBtnInsideColor().getBackground());
		dlgHexagon.setBorderColor(frame.getBtnOutsideColor().getBackground());
		dlgHexagon.getBtnBorderColor().setBackground(frame.getBtnOutsideColor().getBackground());
		
		int hexagonRadius = 0;
	
		dlgHexagon.setVisible(true);
		if (dlgHexagon.isConfirmation()) {
			hexagonRadius = Integer.parseInt(dlgHexagon.getTxtRadius().getText());
			HexagonAdapter hexagon = new HexagonAdapter(new Point(me.getX(), me.getY()), hexagonRadius,
					dlgHexagon.getInnerColor(), dlgHexagon.getBorderColor());
			CmdHexagonAdd cmdHexagonAdd = new CmdHexagonAdd(hexagon,model);
			cmdHexagonAdd.execute();
			frame.getTextAreaLog().append("Added - "+hexagon+"\n");
			
			undoRedo(cmdHexagonAdd,hexagon,"Removed","Added");

			frame.getBtnUndo().setEnabled(true);
			frame.getBtnRedo().setEnabled(false);
			frame.getBtnLoadNext().setEnabled(false); 
			loadLogEnd = true;
			
			frame.getView().repaint();
		}
		dlgHexagon.getTxtRadius().setText("");
	}
		
/******************************MODIFY**********************************/
	public void modifyShape() {
		if (selectedShapes.get(0) instanceof Point) {
			
			Point pointHelp = (Point) selectedShapes.get(0);
			dlgPoint.getTxtX().setText(Integer.toString(pointHelp.getX()));
			dlgPoint.getTxtY().setText(Integer.toString(pointHelp.getY()));
			dlgPoint.getBtnColor().setBackground(pointHelp.getColor());
			
			dlgPoint.setVisible(true);
			if (dlgPoint.isConfirmation()) {
				Point point = new Point(Integer.parseInt(dlgPoint.getTxtX().getText()),
						Integer.parseInt(dlgPoint.getTxtY().getText()), dlgPoint.getColor(), false);
				CmdPointUpdate cmdPointUpdate = new CmdPointUpdate(pointHelp, point);
				cmdPointUpdate.execute();
				frame.getTextAreaLog().append("Modified - "+point+"\n");
				
				undoRedo(cmdPointUpdate,point,"Modified","Modified");
				frame.getView().repaint();
			}
		} else if (selectedShapes.get(0) instanceof Line) {
			
			Line lineHelp = (Line) selectedShapes.get(0);
			dlgLine.getTxtStartX().setText(Integer.toString(lineHelp.getStartPoint().getX()));
			dlgLine.getTxtStartY().setText(Integer.toString(lineHelp.getStartPoint().getY()));
			dlgLine.getTxtEndX().setText(Integer.toString(lineHelp.getEndPoint().getX()));
			dlgLine.getTxtEndY().setText(Integer.toString(lineHelp.getEndPoint().getY()));
			dlgLine.getBtnColor().setBackground(lineHelp.getColor());
			
			dlgLine.setVisible(true);
			if (dlgLine.isConfirmation()) {
				Line line = new Line(
						new Point(Integer.parseInt(dlgLine.getTxtStartX().getText()),
								Integer.parseInt(dlgLine.getTxtStartY().getText())),
						new Point(Integer.parseInt(dlgLine.getTxtEndX().getText()),
								Integer.parseInt(dlgLine.getTxtEndY().getText())),
						dlgLine.getColor());
				
				CmdLineUpdate cmdLineUpdate = new CmdLineUpdate(lineHelp, line);
				cmdLineUpdate.execute();
				frame.getTextAreaLog().append("Modified - "+line+"\n");
				
				undoRedo(cmdLineUpdate,line,"Modified","Modified");
				frame.getView().repaint();
			}
		} else if (selectedShapes.get(0) instanceof Rectangle) {
			
			Rectangle rectangleHelp = (Rectangle) selectedShapes.get(0);
			dlgRectangle.getTxtX().setText(Integer.toString(rectangleHelp.getUpperLeftPoint().getX()));
			dlgRectangle.getTxtY().setText(Integer.toString(rectangleHelp.getUpperLeftPoint().getY()));
			dlgRectangle.getTxtHeight().setText(Integer.toString(rectangleHelp.getHeight()));
			dlgRectangle.getTxtWidth().setText(Integer.toString(rectangleHelp.getWidth()));
			dlgRectangle.getBtnBorderColor().setBackground(rectangleHelp.getColor());
			dlgRectangle.getBtnInnerColor().setBackground(rectangleHelp.getInnerColor());
			
			
			dlgRectangle.setVisible(true);
			if (dlgRectangle.isConfirmation()) {
				Rectangle rectangle = new Rectangle(
						new Point(Integer.parseInt(dlgRectangle.getTxtX().getText()),
								Integer.parseInt(dlgRectangle.getTxtY().getText())),
						Integer.parseInt(dlgRectangle.getTxtHeight().getText()),
						Integer.parseInt(dlgRectangle.getTxtWidth().getText()), dlgRectangle.getBtnInnerColor().getBackground(),
						dlgRectangle.getBtnBorderColor().getBackground());
				
				CmdRectangleUpdate cmdRectangleUpdate = new CmdRectangleUpdate(rectangleHelp, rectangle);
				cmdRectangleUpdate.execute();
				frame.getTextAreaLog().append("Modified - "+rectangle+"\n");
			
				undoRedo(cmdRectangleUpdate,rectangle,"Modified","Modified");
				frame.getView().repaint();
			}
		} else if (selectedShapes.get(0) instanceof Donut) {
			
			Donut donutHelp = (Donut) selectedShapes.get(0);
			dlgDonut.getTxtX().setText(Integer.toString(donutHelp.getCenter().getX()));
			dlgDonut.getTxtY().setText(Integer.toString(donutHelp.getCenter().getY()));
			dlgDonut.getTxtRadius().setText(Integer.toString(donutHelp.getRadius()));
			dlgDonut.getTxtInnerRadius().setText(Integer.toString(donutHelp.getInnerRadius()));
			dlgDonut.getBtnBorderColor().setBackground(donutHelp.getColor());
			dlgDonut.getBtnInnerColor().setBackground(donutHelp.getInnerColor());
		
			
			dlgDonut.setVisible(true);
			if (dlgDonut.isConfirmation()) {
				Donut donut = new Donut(
						new Point(Integer.parseInt(dlgDonut.getTxtX().getText()),
								Integer.parseInt(dlgDonut.getTxtY().getText())),
						Integer.parseInt(dlgDonut.getTxtRadius().getText()),
						Integer.parseInt(dlgDonut.getTxtInnerRadius().getText()),dlgDonut.getBtnBorderColor().getBackground(), dlgDonut.getBtnInnerColor().getBackground());
				
				CmdDonutUpdate cmdDonutUpdate = new CmdDonutUpdate(donutHelp, donut);
				cmdDonutUpdate.execute();
			
				undoRedo(cmdDonutUpdate,donut,"Modified","Modified");
				frame.getTextAreaLog().append("Modified - "+donut+"\n");
				frame.getView().repaint(); 
			}

		} else if (selectedShapes.get(0) instanceof Circle) {
			
			DlgCircle dlgCircle = new DlgCircle();
			
			Circle circleHelp = (Circle) selectedShapes.get(0);
			dlgCircle.getTxtX().setText(Integer.toString(circleHelp.getCenter().getX()));
			dlgCircle.getTxtY().setText(Integer.toString(circleHelp.getCenter().getY()));
			dlgCircle.getTxtRadius().setText(Integer.toString(circleHelp.getRadius()));
			dlgCircle.getBtnBorderColor().setBackground(circleHelp.getColor());
			dlgCircle.getBtnInnerColor().setBackground(circleHelp.getInnerColor());
			dlgCircle.setVisible(true);
			if (dlgCircle.isConfirmation()) {
				Circle circle = new Circle(
						new Point(Integer.parseInt(dlgCircle.getTxtX().getText()),
								Integer.parseInt(dlgCircle.getTxtY().getText())),
						Integer.parseInt(dlgCircle.getTxtRadius().getText()),dlgCircle.getBtnBorderColor().getBackground(), dlgCircle.getBtnInnerColor().getBackground());
				
				CmdCircleUpdate cmdCircleUpdate = new CmdCircleUpdate(circleHelp, circle);
				cmdCircleUpdate.execute();
				frame.getTextAreaLog().append("Modified - "+circle+"\n");
				
				undoRedo(cmdCircleUpdate,circle,"Modified","Modified");
				frame.getView().repaint();
			}
		} else if (selectedShapes.get(0) instanceof HexagonAdapter) {
			
			DlgHexagon dlgHexagon = new DlgHexagon();
			
			HexagonAdapter hexagonHelp = (HexagonAdapter) selectedShapes.get(0);
			dlgHexagon.getTxtX().setText(Integer.toString(hexagonHelp.getCenter().getX()));
			dlgHexagon.getTxtY().setText(Integer.toString(hexagonHelp.getCenter().getY()));
			dlgHexagon.getTxtRadius().setText(Integer.toString(hexagonHelp.getRadius()));
			dlgHexagon.getBtnBorderColor().setBackground(hexagonHelp.getOutsideColor());
			dlgHexagon.getBtnInnerColor().setBackground(hexagonHelp.getInsideColor());
			
			dlgHexagon.setVisible(true);
			if (dlgHexagon.isConfirmation()) {
				HexagonAdapter hexagon = new HexagonAdapter(
						new Point(Integer.parseInt(dlgHexagon.getTxtX().getText()),
								Integer.parseInt(dlgHexagon.getTxtY().getText())),
						Integer.parseInt(dlgHexagon.getTxtRadius().getText()), dlgHexagon.getBtnInnerColor().getBackground(),
						dlgHexagon.getBtnBorderColor().getBackground());
				
				CmdHexagonUpdate cmdHexagonUpdate = new CmdHexagonUpdate(hexagonHelp, hexagon);
				cmdHexagonUpdate.execute();
				frame.getTextAreaLog().append("Modified - "+hexagon+"\n");
				
				undoRedo(cmdHexagonUpdate,hexagon,"Modified","Modified");
				frame.getView().repaint();
			}
		}
		frame.getBtnLoadNext().setEnabled(false);
		frame.getBtnRedo().setEnabled(false);
		frame.getBtnUndo().setEnabled(true);
		loadLogEnd = true;
	}
/********************************DELETE**************************************************************/					
	public void delete() {
		
		for(int i=selectedShapes.size()-1; i >=0;i--)
		{
			if(selectedShapes.get(i) instanceof Point) {
				CmdPointRemove cmdPointRemove = new CmdPointRemove((Point)selectedShapes.get(i),model);
				cmdPointRemove.execute();
				frame.getTextAreaLog().append("Removed - "+(Point) selectedShapes.get(i)+"\n");
				undoRedo(cmdPointRemove,(Point)selectedShapes.get(i),"Added","Removed");
				frame.getView().repaint();
				selectedShapes.remove(i);
			} else if(selectedShapes.get(i) instanceof Line) {
				CmdLineRemove cmdLineRemove = new CmdLineRemove((Line)selectedShapes.get(i),model);
				cmdLineRemove.execute();
				frame.getTextAreaLog().append("Removed - "+(Line) selectedShapes.get(i)+"\n");
				undoRedo(cmdLineRemove,(Line)selectedShapes.get(i),"Added","Removed");
				frame.getView().repaint();
				selectedShapes.remove(i);
			} else if(selectedShapes.get(i) instanceof Rectangle) {
				CmdRectangleRemove cmdRectangleRemove = new CmdRectangleRemove((Rectangle)selectedShapes.get(i),model);
				cmdRectangleRemove.execute();
				frame.getTextAreaLog().append("Removed - "+(Rectangle) selectedShapes.get(i)+"\n");
				undoRedo(cmdRectangleRemove,(Rectangle)selectedShapes.get(i),"Added","Removed");
				frame.getView().repaint();
				selectedShapes.remove(i);
			} else if(selectedShapes.get(i) instanceof Circle) {
				CmdCircleRemove cmdCircleRemove = new CmdCircleRemove((Circle)selectedShapes.get(i),model);
				cmdCircleRemove.execute();
				frame.getTextAreaLog().append("Removed - "+(Circle) selectedShapes.get(i)+"\n");
				undoRedo(cmdCircleRemove,(Circle)selectedShapes.get(i),"Added","Removed");
				frame.getView().repaint();
				selectedShapes.remove(i);
			} else if(selectedShapes.get(i) instanceof Donut) {
				CmdDonutRemove cmdDonutRemove  = new CmdDonutRemove((Donut)selectedShapes.get(i),model);
				cmdDonutRemove.execute();
				frame.getTextAreaLog().append("Removed - "+(Donut) selectedShapes.get(i)+"\n");
				undoRedo(cmdDonutRemove,(Donut)selectedShapes.get(i),"Added","Removed");
				frame.getView().repaint();
				selectedShapes.remove(i);
			} else if(selectedShapes.get(i) instanceof HexagonAdapter) {
				CmdHexagonRemove cmdHexagonRemove = new CmdHexagonRemove((HexagonAdapter)selectedShapes.get(i),model);
				cmdHexagonRemove.execute();
				frame.getTextAreaLog().append("Removed - "+(HexagonAdapter) selectedShapes.get(i)+"\n");
				undoRedo(cmdHexagonRemove,(HexagonAdapter)selectedShapes.get(i),"Added","Removed");
				frame.getView().repaint();
				selectedShapes.remove(i);
			}
		}
		
		if(selectedShapes.size() == 0)
		{
			btnObserver.setModifyBtnActivated(false);
			btnObserver.setDeleteBtnActivated(false);
		}
		frame.getBtnLoadNext().setEnabled(false);
		frame.getBtnUndo().setEnabled(true);
		frame.getBtnRedo().setEnabled(false);
		selectEditUpdate();
		loadLogEnd = true;
	}
	
/******************UNDO-REDO****************************************/
	public void undoRedo(Command comand,Shape shape,String undo,String redo)
	{
		cmdList.deleteElementsAfterPointer(cmdList.getCurrent() - 1);
		cmdList.getList().add(comand);
		cmdList.getShapes().add(shape);
		cmdList.getLogCommandsUndo().add(undo);//dodavanje stringa undo
		cmdList.getLogCommandsRedo().add(redo);//dodavanje  stringa redo 
		cmdList.setCurrent(cmdList.getCurrent()+1);//dodati su jedan shape i jedna komanda 
	
	}
	
/***************************UNDO SHAPE**********************************/
	
	public void undoShape() {
	
		frame.getTextAreaLog().append("Undo "+cmdList.getLogCommandsUndo().get(cmdList.getCurrent()-1)+" - "+cmdList.getShapes().get(cmdList.getCurrent()-1)+"\n");
		/*Koristimo da bi element koji vratimo bio selektovan i upisan u listu selektovanih  */
		if(cmdList.getLogCommandsUndo().get(cmdList.getCurrent()-1).equals("Added")) {
			CmdShapeSelect cmdShapeSelect = new CmdShapeSelect(this,cmdList.getShapes().get(cmdList.getCurrent()-1));
			cmdShapeSelect.execute();
			
		}
		
		cmdList.undo();//vrsi se undo za odredjenu comandu i spusta current
		selectEditUpdate();
		frame.getView().repaint();
	}
	

/**************************REDO SHAPE*************************************/	
	
	public void redoShape() {
		
		frame.getTextAreaLog().append("Redo "+cmdList.getLogCommandsRedo().get(cmdList.getCurrent())+" - "+cmdList.getShapes().get(cmdList.getCurrent())+"\n");
		/*Koristimo da bi elemenat koji removujemo bio unselektovan i obrisan iz liste selektovanih */
		if(cmdList.getLogCommandsRedo().get(cmdList.getCurrent()).equals("Removed"))
		{
			CmdShapeDeselect cmdShapeDeselect = new CmdShapeDeselect(this,cmdList.getShapes().get(cmdList.getCurrent()));
			cmdShapeDeselect.execute();
		}
		
		cmdList.redo();// vrsi se redo za odredjenu komandu i povecanje currenta 
		selectEditUpdate();
		frame.getView().repaint();
		
	}
/********************************DOSTUPNOST DUGMETA**************************************************/
	

	public void selectEditUpdate() {
		if(selectedShapes.size() !=0)//sve sem 0
		{
			if(selectedShapes.size()==1)//1
			{
				btnObserver.setModifyBtnActivated(true);
				btnUpdate();
				
			} else {//razlicito od 0 i 1
				btnObserver.setModifyBtnActivated(false);
				//bring
				btnObserver.setBringToBackBtnActivated(false);
				btnObserver.setBringToFrontBtnActivated(false);
				btnObserver.setToBackBtnActivated(false);
				btnObserver.setToFrontBtnActivated(false);
			}
			btnObserver.setDeleteBtnActivated(true);
		} else {//0
			btnObserver.setModifyBtnActivated(false);
			btnObserver.setDeleteBtnActivated(false);
			//bring
			btnObserver.setBringToBackBtnActivated(false);
			btnObserver.setBringToFrontBtnActivated(false);
			btnObserver.setToBackBtnActivated(false);
			btnObserver.setToFrontBtnActivated(false);
		
		}
	}
	
	public void btnUpdate() {
	
		ListIterator<Shape> it = model.getShapes().listIterator();
		while(it.hasNext())
		{
			selected = it.next();
			if(selected.isSelected()) {
			
				if(model.getShapes().size() !=1) {
					if(selected.equals(model.get(model.getShapes().size()-1))) {//gleda se u odnosu na poslednji u listi 
						btnObserver.setBringToBackBtnActivated(true);
						btnObserver.setBringToFrontBtnActivated(false);
						btnObserver.setToBackBtnActivated(true);
						btnObserver.setToFrontBtnActivated(false);
					} else if (selected.equals(model.get(0))) {//gleda se u odnosu na prvi 
						btnObserver.setBringToBackBtnActivated(false);
						btnObserver.setBringToFrontBtnActivated(true);
						btnObserver.setToBackBtnActivated(false);
						btnObserver.setToFrontBtnActivated(true);
					} else {
						//ukoliko se nalazi izmedju prvog i poslednjeg
						btnObserver.setBringToBackBtnActivated(true);
						btnObserver.setBringToFrontBtnActivated(true);
						btnObserver.setToBackBtnActivated(true);
						btnObserver.setToFrontBtnActivated(true);
					}
				}
			}
		}
	}
	
	
	
	//Vratiti skroz na dno 
	public void ToBack() {
		frame.getBtnRedo().setEnabled(false);
		CmdBringToBack cmdBringToBack = new CmdBringToBack(model,selectedShapes.get(0));
		cmdBringToBack.execute();
		frame.getTextAreaLog().append("Moved to back - "+selectedShapes.get(0)+"\n");
		undoRedo(cmdBringToBack,selectedShapes.get(0),"Moved to front","Moved to back");
		frame.getView().repaint();
		btnUpdate();

	}
	public void Back() {
		frame.getBtnRedo().setEnabled(false);
		CmdBringBackward cmd = new CmdBringBackward(model,selectedShapes.get(0));
		cmd.execute();
		frame.getTextAreaLog().append("Moved backward - "+selectedShapes.get(0)+"\n");
		undoRedo(cmd,selectedShapes.get(0),"Moved forward","Moved backward");
		frame.getView().repaint();
		btnUpdate();
	}
	public void ToFront() {
		frame.getBtnRedo().setEnabled(false);
		CmdBringToFront cmdBringToFront = new CmdBringToFront(model,selectedShapes.get(0));
		cmdBringToFront.execute();
		frame.getTextAreaLog().append("Moved to front - "+selectedShapes.get(0)+"\n");
		undoRedo(cmdBringToFront,selectedShapes.get(0),"Moved to back","Moved to front");	
		frame.getView().repaint();
		btnUpdate();
	}
	//Izneti za jedan
		public void Front() {
			frame.getBtnRedo().setEnabled(false);
			CmdBringForward cmdBringForward = new CmdBringForward(model,selectedShapes.get(0));
			cmdBringForward.execute();
			frame.getTextAreaLog().append("Moved forward - "+selectedShapes.get(0)+"\n");
			undoRedo(cmdBringForward,selectedShapes.get(0),"Moved backward","Moved forward");
			frame.getView().repaint();
			btnUpdate();
		}
	//Doneti napred
	
	
	
	

/*************************Open log**********************************************/	
	
	public void openLog() throws IOException{
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt","txt");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(filter);
		
		fileChooser.setDialogTitle("Open log");
		int userSelection = fileChooser.showOpenDialog(null);
		if(userSelection == JFileChooser.APPROVE_OPTION) {
			File paintingToLoad = fileChooser.getSelectedFile();
			
			cmdList.setCurrent(-1);
			model.getShapes().clear();
			cmdList.getShapes().clear();
			cmdList.getList().clear();
			cmdList.getLogCommandsUndo().clear();
			frame.getBtnUndo().setEnabled(false);
			cmdList.getLogCommandsRedo().clear();
			frame.getBtnRedo().setEnabled(false);
			selectedShapes.clear();
			logList.clear();
			
			logCounter=0;
			selectEditUpdate();
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader(paintingToLoad));
			
			while((line = bufferedReader.readLine()) != null) {
				logList.add(line);
				
	
			}
			bufferedReader.close();
			
			
			frame.getBtnLoadNext().setEnabled(true);
			frame.getBtnUndo().setEnabled(false);
		}
	}
/***********************************Save log *************************************/
	
	public void saveLog() {
		JFileChooser fileChooser  = new JFileChooser();//OTVARANJE PROZORA 
		fileChooser.setDialogTitle("Save log");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt","txt");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(filter);
		
		if(fileChooser.showSaveDialog(frame.getParent()) == JFileChooser.APPROVE_OPTION) {
			System.out.println("Successfully saved "+fileChooser.getSelectedFile().getName()+" file!");
			File file = fileChooser.getSelectedFile();
			String filePath = file.getAbsolutePath();
			File logToSave = new File(filePath+".txt");
			
			SaveManager manager = new SaveManager(new SaveLog());
			manager.save(frame, logToSave);
			
			frame.getView().repaint();
		}
	}
	
/*********************** Save painting ******************************************/
	
	public void savePainting() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save painting!");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".bin","bin");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(filter);
		

		int userSelection = fileChooser.showSaveDialog(null);
		if(userSelection == JFileChooser.APPROVE_OPTION) {
			File paintingToSave = fileChooser.getSelectedFile();
			File logToSave;
			String filePath = paintingToSave.getAbsolutePath();
			if(!filePath.endsWith(".bin") && !filePath.contains(".")) {
				paintingToSave = new File(filePath + ".bin");
				logToSave = new File(filePath + ".txt");
			}
			
			String filename = paintingToSave.getPath();
			System.out.println("Save painting as: "+paintingToSave.getAbsolutePath());
			System.out.println(filename.substring(filename.lastIndexOf("."), filename.length()));
			if(filename.substring(filename.lastIndexOf("."), filename.length()).contains(".bin")) {
				filename=paintingToSave.getAbsolutePath().substring(0,filename.lastIndexOf("."))+".txt";
				logToSave=new File(filename);
				SaveManager savePainting = new SaveManager(new SaveShapes());
				SaveManager saveLog = new SaveManager(new SaveLog());
				savePainting.save(model, paintingToSave);
				saveLog.save(frame, logToSave);
			} else {
				JOptionPane.showMessageDialog(null, "Wrong file extension");
			}
		
		}
	}
/********************Open painting *********************************************/
	
	public void openPainting() throws IOException,ClassNotFoundException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		fileChooser.setDialogTitle("Open painting");
		int userSelection = fileChooser.showOpenDialog(null);
		
		if(userSelection == JFileChooser.APPROVE_OPTION) {
			File paintingToLoad = fileChooser.getSelectedFile();
			loadPainting(paintingToLoad);
			
		}
	}
	
/*****************************Load painting *******************************************/
	
	@SuppressWarnings("unchecked")
	public void loadPainting(File paintingToLoad) throws FileNotFoundException,IOException,ClassNotFoundException {
		frame.getTextAreaLog().setText("");
		File file = new File(paintingToLoad.getAbsolutePath().replace("bin", "txt"));
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String logLine;
		
		//ispis u teks area
		while((logLine = bufferedReader.readLine()) != null) {
			frame.getTextAreaLog().append(logLine +"\n");
		}
		bufferedReader.close();
		
		
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(paintingToLoad));
		try {
			cmdList.setCurrent(-1);
			model.getShapes().clear();
			cmdList.getList().clear();
			cmdList.getShapes().clear();
			cmdList.getLogCommandsRedo().clear();
			cmdList.getLogCommandsUndo().clear();
			selectedShapes.clear();
			frame.getBtnRedo().setEnabled(false);
			frame.getBtnUndo().setEnabled(false);
			selectEditUpdate();
			
			//dodavanje u glavnu listu
			model.getShapes().addAll((ArrayList<Shape>)objectInputStream.readObject());
			objectInputStream.close();
		
		} catch (InvalidClassException ice) {
			
		} catch (SocketTimeoutException ste) {
			
		} catch (EOFException eofe) {
			
		} catch (IOException exc) {
			exc.printStackTrace();
		}
		for (int i = 0; i < model.getShapes().size(); i++) {
			if(model.getShapes().get(i).isSelected()) {
				selectedShapes.add(model.getShapes().get(i));
			}
		}
		frame.getView().repaint();
		
	}
/****************************Load log *****************************************************/
	
	public void loadLog(File paintingToLoad) throws IOException, RadiusException {
		loadLogEnd = false ;
		cmdList.setCurrent(-1);
		model.getShapes().clear();
		cmdList.getShapes().clear();
		cmdList.getList().clear();
		cmdList.getLogCommandsUndo().clear();
		frame.getBtnUndo().setEnabled(false);
		cmdList.getLogCommandsRedo().clear();
		frame.getBtnRedo().setEnabled(false);
		selectedShapes.clear();
		logList.clear();
		
		logCounter=0;
		frame.getTextAreaLog().setText("");
		selectEditUpdate();
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(paintingToLoad));
		
		while((line = bufferedReader.readLine()) != null) {
			logList.add(logCounter++, line);
			
		}
		bufferedReader.close();
		
		
		frame.getBtnLoadNext().setEnabled(true);
		frame.getBtnUndo().setEnabled(true);
		loadNext();
	}
	
	public void loadNext() throws RadiusException {
		Shape shape = null;
		
		if(logCounter < logList.size()) {
			line = logList.get(logCounter);
			if(line.contains("Point")) {
				int x = Integer.parseInt(line.substring(line.indexOf("(")+1, line.indexOf(",")));
				int y = Integer.parseInt(line.substring(line.indexOf(",")+2,line.indexOf(")")));
				String color = line.substring(line.lastIndexOf("(")+1, line.lastIndexOf(")"));
				Color c = frame.getBtnInsideColor().getBackground();
				c= new Color(Integer.parseInt(color));
				shape = new Point(x,y,c);
				
				
				if(logCounter == logList.size()-1) {
					frame.getBtnLoadNext().setEnabled(false);
					loadLogEnd = true;
				}
			} else if(line.contains("Line")) {
				int x = Integer.parseInt(line.substring(line.indexOf("(")+1,line.indexOf(",")));
				int y = Integer.parseInt(line.substring(line.indexOf(",")+2,line.indexOf(")")));
				int x1 = Integer.parseInt(line.substring(ordinalIndexOf(line,"(",1)+1,ordinalIndexOf(line,",",1)));
				int y1 = Integer.parseInt(line.substring(ordinalIndexOf(line,",",1)+2,ordinalIndexOf(line,")",1)));
				String color1 = line.substring(line.lastIndexOf("(")+1, line.lastIndexOf(")"));
				Color color2 = frame.getBtnInsideColor().getBackground();
				
				color2 = new Color(Integer.parseInt(color1));
				
				shape = new Line(new Point(x,y),new Point(x1,y1),color2);
				
				if(logCounter == logList.size()-1) {
					frame.getBtnLoadNext().setEnabled(false);
					loadLogEnd = true;
				}
				
				
			} else  if(line.contains("Rectangle")) {
			
				int x = Integer.parseInt(line.substring(line.indexOf("(")+1, line.indexOf(",")));
				int y = Integer.parseInt(line.substring(line.indexOf(",")+2, line.indexOf(")")));
			 	int w = Integer.parseInt(line.substring(ordinalIndexOf(line,"=",0)+1, ordinalIndexOf(line,",",2)));
			 	
				int h = Integer.parseInt(line.substring(ordinalIndexOf(line,"=",1)+1, ordinalIndexOf(line, ",", 3)));
				String insideColor = line.substring(ordinalIndexOf(line, "(", 1)+1, ordinalIndexOf(line, ")", 1));
				String outsideColor = line.substring(line.lastIndexOf("(")+1, line.lastIndexOf(")"));
				
				Color iC = frame.getBtnInsideColor().getBackground();
				Color oC = frame.getBtnOutsideColor().getBackground();

				iC=new Color(Integer.parseInt(insideColor));
				oC=new Color(Integer.parseInt(outsideColor));

				shape = new Rectangle(new Point(x,y), w, h, iC, oC);
				
				if(logCounter == logList.size()-1) {
					frame.getBtnLoadNext().setEnabled(false);
					loadLogEnd = true;
				}
				
			} else if(line.contains("Circle")) {
				int x = Integer.parseInt(line.substring(line.indexOf("(")+1, line.indexOf(",")));
				int y = Integer.parseInt(line.substring(line.indexOf(",")+2, line.indexOf(")")));
				int r = Integer.parseInt(line.substring(ordinalIndexOf(line,"=",0)+1,ordinalIndexOf(line,",",2)));
				String insideColor1 = line.substring(ordinalIndexOf(line,"(",1)+1,ordinalIndexOf(line,")",1));
				String outsideColor1 = line.substring(line.lastIndexOf("(")+1,line.lastIndexOf(")"));
				
				Color insideColor2 = frame.getBtnInsideColor().getBackground();
				Color outsideColor2  = frame.getBtnOutsideColor().getBackground();
				
				insideColor2 = new Color(Integer.parseInt(insideColor1));
				outsideColor2 = new Color(Integer.parseInt(outsideColor1));
				
				shape = new Circle(new Point(x,y),r,outsideColor2,insideColor2);
				
				if(logCounter == logList.size()-1) {
					frame.getBtnLoadNext().setEnabled(false);
					loadLogEnd = true;
				}
			} else if(line.contains("Donut")) {
				int x = Integer.parseInt(line.substring(line.indexOf("(")+1, line.indexOf(",")));
				int y = Integer.parseInt(line.substring(line.indexOf(",")+2, line.indexOf(")")));
				int r = Integer.parseInt(line.substring(ordinalIndexOf(line,"=",0)+1,ordinalIndexOf(line,",",2)));
				int innerR = Integer.parseInt(line.substring(ordinalIndexOf(line,"=",1)+1, line.lastIndexOf(".")));
				String outsideColor1 =  line.substring(ordinalIndexOf(line, "(", 2)+1, ordinalIndexOf(line, ")", 2));
				String insideColor1 = line.substring(ordinalIndexOf(line, "(", 1)+1, ordinalIndexOf(line, ")", 1));
				
				Color insideColor2 = frame.getBtnInsideColor().getBackground();
				Color outsideColor2  = frame.getBtnOutsideColor().getBackground();
				
				insideColor2 = new Color(Integer.parseInt(insideColor1));
				outsideColor2 = new Color(Integer.parseInt(outsideColor1));
				
				shape = new Donut(new Point(x,y),r,innerR,outsideColor2,insideColor2);
				
				if(logCounter == logList.size()-1) {
					frame.getBtnLoadNext().setEnabled(false);
					loadLogEnd = true;
				}
			} else if(line.contains("Hexagon")) {
				int x = Integer.parseInt(line.substring(line.indexOf("(")+1, line.indexOf(",")));
				int y = Integer.parseInt(line.substring(line.indexOf(",")+2, line.indexOf(")")));
			 	int r = Integer.parseInt(line.substring(ordinalIndexOf(line,"=",0)+1, ordinalIndexOf(line,",",2)));
			 	String insideColor1 = line.substring(ordinalIndexOf(line, "(", 1)+1, ordinalIndexOf(line, ")", 1));
				String outsideColor1 = line.substring(line.lastIndexOf("(")+1, line.lastIndexOf(")"));
				
				Color insideColor2 = frame.getBtnInsideColor().getBackground();
				Color outsideColor2  = frame.getBtnOutsideColor().getBackground();
				
				insideColor2 = new Color(Integer.parseInt(insideColor1));
				outsideColor2 = new Color(Integer.parseInt(outsideColor1));
				
				shape = new HexagonAdapter(new Point(x,y),r,insideColor2,outsideColor2);
				
				if(logCounter == logList.size()-1) {
					frame.getBtnLoadNext().setEnabled(false);
					loadLogEnd = true;
				}
			}
			//-------UNDO ADDED LOG -----------------
			if(line.contains("Undo Added")) {
				CmdShapeAdd cmdShapeAdd = new CmdShapeAdd(model,shape);
				CmdShapeSelect cmdShapeSelect = new CmdShapeSelect(this,shape);
				cmdShapeAdd.execute();
				cmdShapeSelect.execute();//jer mora da je selektovan ukoliko se vraca nazad!!!
				frame.getTextAreaLog().append("Added - "+shape+"\n");
				undoRedo(cmdShapeAdd,shape,"Removed","Added");
				//-------ADDED LOG -----------------
			} else if (line.contains("Added")) {
				CmdShapeAdd cmdShapeAdd = new CmdShapeAdd(model,shape);
				cmdShapeAdd.execute();
				frame.getTextAreaLog().append("Added - "+shape+"\n");
				undoRedo(cmdShapeAdd,shape,"Removed","Added");
				//-------DESELECT LOG -----------------
			} else if(line.contains("Deselect")) {
				
				shape = model.getShapes().get(model.getShapes().indexOf(shape));
					
				CmdShapeDeselect cmdShapeDeselect = new CmdShapeDeselect(this,shape);
				cmdShapeDeselect.execute();
				
				frame.getTextAreaLog().append("Deselect - "+shape+"\n");
				undoRedo(cmdShapeDeselect,shape,"Selected","Deselect");
			
				//-------SELECTED LOG ------------------------
			} else if (line.contains("Selected")) {
				
				shape = model.getShapes().get(model.getShapes().indexOf(shape));
				
				CmdShapeSelect cmdShapeSelect = new CmdShapeSelect(this,shape);
				cmdShapeSelect.execute();
				
				frame.getTextAreaLog().append("Selected - "+shape+"\n");
				undoRedo(cmdShapeSelect,shape,"Deselect","Selected");
				
				//-----REMOVED LOG -------------------------
			}  else if(line.contains("Removed")) {
				CmdShapeRemove cmdShapeRemove = new CmdShapeRemove(model,shape);
				cmdShapeRemove.execute();
				selectedShapes.remove(shape);
				undoRedo(cmdShapeRemove,shape,"Added","Removed");
				
				if(line.contains("Undo"))
					{
						frame.getTextAreaLog().append("Undo Removed - "+shape+"\n");
					}
				else
					{
						frame.getTextAreaLog().append("Removed - "+shape+"\n");
					}	
				
			//******MODIFIED LOG***************/	
			}  else if(line.contains("Modified")) {
				
				if(shape instanceof Point)
				{
					CmdPointUpdate cmdPointUpdate=null;
					Point point = (Point) shape;
					Point helpP = (Point) selectedShapes.get(0);
					if(line.contains("Undo Modified"))
					{
						//frame.getTextAreaLog().append("123456");
						for (int i = cmdList.getShapes().size()-1; i >= 0; i--) {
							if(cmdList.getShapes().get(i).equals(helpP) && cmdList.getLogCommandsUndo().get(i)=="Modified")
							{
								pomCMD=i;
							}
						}
						cmdPointUpdate=(CmdPointUpdate)cmdList.getList().get(pomCMD);
						cmdPointUpdate.unexecute();
						frame.getTextAreaLog().append("Undo Modified - "+cmdPointUpdate.getOriginal().toString()+"\n");
						
						undoRedo(cmdPointUpdate,cmdPointUpdate.getOriginal(),"Modified","Modified");
					} else {
						cmdPointUpdate = new CmdPointUpdate(helpP,point);
						cmdPointUpdate.execute();
						frame.getTextAreaLog().append("Modified - "+point+"\n");
						undoRedo(cmdPointUpdate,point,"Modified","Modified");
					}
					
				}  else if (shape instanceof Line) {
						CmdLineUpdate cmdLineUpdate=null;
						Line line1 = (Line) shape;
						Line helpL = (Line) selectedShapes.get(0);
						
						if(line.contains("Undo Modified"))
						{//frame.getTextAreaLog().append("123456");
							for (int i = cmdList.getShapes().size()-1; i >= 0; i--) {
								if(cmdList.getShapes().get(i).equals(helpL) && cmdList.getLogCommandsUndo().get(i)=="Modified" )
								{
									pomCMD=i;
								}
							}
							cmdLineUpdate=(CmdLineUpdate)cmdList.getList().get(pomCMD);
							cmdLineUpdate.unexecute();
							frame.getTextAreaLog().append("Undo Modified - "+cmdLineUpdate.getOriginal().toString()+"\n");
							
							
						} else {
							cmdLineUpdate = new CmdLineUpdate(helpL,line1);
							cmdLineUpdate.execute();
							frame.getTextAreaLog().append("Modified - "+line1+"\n");
						}
						
						undoRedo(cmdLineUpdate,line1,"Modified","Modified");
					
					}  else if (shape instanceof Rectangle) {
					
						CmdRectangleUpdate cmdRectangleUpdate=null;
						Rectangle rectangle = (Rectangle) shape;
						Rectangle helpR = (Rectangle) selectedShapes.get(0);
						
						if(line.contains("Undo Modified"))
						{//frame.getTextAreaLog().append("123456");
							for (int i = cmdList.getShapes().size()-1; i >= 0; i--) {
								if(cmdList.getShapes().get(i).equals(helpR) && cmdList.getLogCommandsUndo().get(i)=="Modified")
								{
									pomCMD=i;
								}
							}
							cmdRectangleUpdate=(CmdRectangleUpdate)cmdList.getList().get(pomCMD);
							cmdRectangleUpdate.unexecute();
							frame.getTextAreaLog().append("Undo Modified - "+cmdRectangleUpdate.getOriginal().toString()+"\n");
							
						} else {
							cmdRectangleUpdate = new CmdRectangleUpdate(helpR,rectangle);
							cmdRectangleUpdate.execute();
							frame.getTextAreaLog().append("Modified - "+rectangle+"\n");
						}
						
						undoRedo(cmdRectangleUpdate,rectangle,"Modified","Modified");
					
					}  else if (shape instanceof Donut) {
						
						CmdDonutUpdate cmdDonutUpdate=null;
						Donut donut = (Donut) shape;
						Donut helpD = (Donut) selectedShapes.get(0);
						
						if(line.contains("Undo Modified"))
						{//frame.getTextAreaLog().append("123456");
							for (int i = cmdList.getShapes().size()-1; i >= 0; i--) {
								if(cmdList.getShapes().get(i).equals(helpD)&& cmdList.getLogCommandsUndo().get(i)=="Modified")
								{
									pomCMD=i;
								}
							}
							cmdDonutUpdate=(CmdDonutUpdate)cmdList.getList().get(pomCMD);
							cmdDonutUpdate.unexecute();
							frame.getTextAreaLog().append("Undo Modified - "+cmdDonutUpdate.getOriginal().toString()+"\n");
							
							
						} else {
							cmdDonutUpdate = new CmdDonutUpdate(helpD,donut);
							cmdDonutUpdate.execute();
							frame.getTextAreaLog().append("Modified - "+donut+"\n");
						}
						
						
						undoRedo(cmdDonutUpdate,donut,"Modified","Modified");
					
					} else if (shape instanceof Circle) {
						CmdCircleUpdate cmdCircleUpdate=null;
						Circle circle = (Circle) shape;
						Circle helpC = (Circle) selectedShapes.get(0);
						
						if(line.contains("Undo Modified"))
						{//frame.getTextAreaLog().append("123456");
							for (int i = cmdList.getShapes().size()-1; i >= 0; i--) {
								if(cmdList.getShapes().get(i).equals(helpC)&& cmdList.getLogCommandsUndo().get(i)=="Modified")
								{
									pomCMD=i;
								}
							}
							cmdCircleUpdate=(CmdCircleUpdate)cmdList.getList().get(pomCMD);
							cmdCircleUpdate.unexecute();
							frame.getTextAreaLog().append("Undo Modified - "+cmdCircleUpdate.getOriginal().toString()+"\n");
							
							
						} else {
							cmdCircleUpdate = new CmdCircleUpdate(helpC,circle);
							cmdCircleUpdate.execute();
							frame.getTextAreaLog().append("Modified - "+circle+"\n");
						}
						
						undoRedo(cmdCircleUpdate,circle,"Modified","Modified");
					
					} else if (shape instanceof HexagonAdapter) {
						CmdHexagonUpdate cmdHexagonUpdate=null;
						HexagonAdapter hexagon = (HexagonAdapter) shape;
						HexagonAdapter helpH = (HexagonAdapter) selectedShapes.get(0);
						
						if(line.contains("Undo Modified"))
						{//frame.getTextAreaLog().append("123456");
							for (int i = cmdList.getShapes().size()-1; i >= 0; i--) {
								if(cmdList.getShapes().get(i).equals(helpH)&& cmdList.getLogCommandsUndo().get(i)=="Modified")
								{
									pomCMD=i;
								}
							}
							cmdHexagonUpdate=(CmdHexagonUpdate)cmdList.getList().get(pomCMD);
							cmdHexagonUpdate.unexecute();
							frame.getTextAreaLog().append("Undo Modified - "+cmdHexagonUpdate.getOriginal().toString()+"\n");
							
							
						} else {
							cmdHexagonUpdate = new CmdHexagonUpdate(helpH,hexagon);
							cmdHexagonUpdate.execute();
							frame.getTextAreaLog().append("Modified - "+hexagon+"\n");
						}
						
						undoRedo(cmdHexagonUpdate,hexagon,"Modified","Modified");
					}
				//*******BACK LOG*******************
			} else if(line.contains("Moved backward")) {
				
				CmdBringBackward cmdBringBackward = new CmdBringBackward(model,selectedShapes.get(0));
				cmdBringBackward.execute();
				frame.getTextAreaLog().append("Moved backward - "+selectedShapes.get(0)+"\n");
				undoRedo(cmdBringBackward,selectedShapes.get(0),"Moved forward","Moved backward");
				//******TO BACK LOG*****************
			} else if(line.contains("Moved to back")) {
				
				CmdBringToBack cmdBringToBack = new CmdBringToBack(model,selectedShapes.get(0));
				cmdBringToBack.execute();
				frame.getTextAreaLog().append("Moved to back - "+selectedShapes.get(0)+"\n");
				undoRedo(cmdBringToBack,selectedShapes.get(0),"Moved to front","Moved to back");
			//*********TO FRONT LOG *******************
			} else if(line.contains("Moved to front")) {
				
				CmdBringToFront cmdBringToFront = new CmdBringToFront(model,selectedShapes.get(0));
				cmdBringToFront.execute();
				frame.getTextAreaLog().append("Moved to front - "+selectedShapes.get(0)+"\n");
				undoRedo(cmdBringToFront,selectedShapes.get(0),"Moved to back","Moved to front");
				
			} else if(line.contains("Moved forward")) {
			//**********FORWARD LOG*********************
				CmdBringForward cmdBringForward = new CmdBringForward(model,selectedShapes.get(0));
				cmdBringForward.execute();
				frame.getTextAreaLog().append("Moved forward -"+selectedShapes.get(0)+"\n");
				undoRedo(cmdBringForward,selectedShapes.get(0),"Moved backward","Moved forward");
				
			}
			logCounter++;
			frame.getView().repaint();
		}
		
		selectEditUpdate();
		frame.getBtnUndo().setEnabled(false);
	}
	
	/*za iscitavanje iz loga */
	public int ordinalIndexOf(String string,String subString,int n)
	{
		int pos = -1;
		do {
			pos = string.indexOf(subString,pos+1);
		}while(n-- > 0 && pos !=-1);
		return pos;
	}

	public List<Shape> getSelectedShapes() {
		return selectedShapes;
	}
 	
	public boolean isloadLogEnd() {
		return loadLogEnd;
	}
	
	public CmdList getCmdList() {
		return cmdList;
	}

}
