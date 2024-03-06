package commands;

import mvc.DrawingModel;
import shapes.Rectangle;

public class CmdRectangleAdd implements Command {
	
	private Rectangle rectangle;
	private DrawingModel model;
	
	public CmdRectangleAdd(Rectangle rectangle,DrawingModel model)
	{
		this.rectangle=rectangle;
		this.model=model;
	}

	@Override
	public void execute() {
		model.add(rectangle);	
	}

	@Override
	public void unexecute() {
		model.remove(rectangle);
		
	}

}
