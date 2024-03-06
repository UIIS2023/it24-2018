package commands;

import mvc.DrawingModel;
import shapes.Circle;

public class CmdCircleAdd implements Command{
	
	private Circle circle;
	private DrawingModel model;
	
	public CmdCircleAdd(Circle circle, DrawingModel model)
	{
		this.circle=circle;
		this.model=model;
	}
	@Override
	public void execute() {
		model.add(circle);	
	}

	@Override
	public void unexecute() {
		model.remove(circle);
	}

}
