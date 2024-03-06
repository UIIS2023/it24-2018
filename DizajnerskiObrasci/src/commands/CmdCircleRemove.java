package commands;

import mvc.DrawingModel;
import shapes.Circle;

public class CmdCircleRemove implements Command {

	private Circle circle;
	private DrawingModel model;
	
	public CmdCircleRemove(Circle circle, DrawingModel model)
	{
		this.circle=circle;
		this.model=model;
	}
	@Override
	public void execute() {
		model.remove(circle);
	}

	@Override
	public void unexecute() {
		model.add(circle);
	}
}
