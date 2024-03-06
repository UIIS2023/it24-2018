package commands;

import mvc.DrawingModel;
import shapes.Donut;

public class CmdDonutAdd implements Command {

	private Donut donut;
	private DrawingModel model;
	
	public CmdDonutAdd(Donut donut, DrawingModel model)
	{
		this.donut=donut;
		this.model=model;
	}
	@Override
	public void execute() {
		model.add(donut);	
	}

	@Override
	public void unexecute() {
		model.remove(donut);
	}

}
