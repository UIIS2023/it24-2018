package commands;

import mvc.DrawingModel;
import shapes.Point;

public class CmdPointUpdate implements Command {

	
	private Point oldState;
	private Point newState;
	private Point original = new Point();//dodati ovo za svaki update cmd

	
	
	public CmdPointUpdate(Point oldState,Point newState)
	{
		this.oldState=oldState;
		this.newState=newState;
		
		
	}
	@Override
	public void execute() {
		
		original = oldState.clone(original);
		oldState = newState.clone(oldState);
	
	}

	@Override
	public void unexecute() {
		
	oldState  = original.clone(oldState);
	
	
	}
	public Point getOriginal() {
		return original;
	}

}
