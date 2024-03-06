package commands;

import java.util.ArrayList;


import shapes.Shape;

public class CmdList {

	private ArrayList<Command> list = new ArrayList<Command>();//Lista komandi
	private ArrayList<Shape> shapes = new ArrayList<Shape>();//Lista sejpova
	private ArrayList<String> logCommandsUndo = new ArrayList<String>();//sluze za smestanje stringova za undo
	private ArrayList<String> logCommandsRedo = new ArrayList<String>();//sluzi za smestanje stringova za redo 
	
	private int current = 0;
	
	
	public void deleteElementsAfterPointer(int current)
	{
		if(list.size()<1)
		{
			return;
		}
		for(int i = list.size()-1;i>current;i--)
		{
			list.remove(i);
			shapes.remove(i);
			logCommandsUndo.remove(i);
			logCommandsRedo.remove(i);
		
		}
	}
	
	public void undo() {
		
		list.get(current-1).unexecute();
		current--;
		
	}
	
	public void redo() {
		//if(current + 1 == list.size()-1)
		//{
		//	return;
		//}		
		
		current++; 
		list.get(current-1).execute();
		
	}
	
	public int getCurrent() {
		return current;
	}
	
	public void setCurrent(int current) {
		this.current = current;
	}

	public ArrayList<Command> getList() {
		return list;
	}
	
	public ArrayList<Shape> getShapes() {
		return shapes;
	}

	public ArrayList<String> getLogCommandsUndo() {
		return logCommandsUndo;
	}
	
	public ArrayList<String> getLogCommandsRedo() {
		return logCommandsRedo;
	}

}
