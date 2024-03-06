package commands;

import mvc.DrawingModel;
import shapes.Rectangle;

public class CmdRectangleRemove implements Command {

	private Rectangle rectangle;
	private DrawingModel model;
	
	public CmdRectangleRemove(Rectangle rectangle,DrawingModel model)
	{
		this.rectangle=rectangle;
		this.model=model;
	}

	@Override
	public void execute() {
	model.remove(rectangle);
		
	}

	@Override
	public void unexecute() {
		model.add(rectangle);
		
	}
}
