package commands;

import mvc.DrawingModel;
import shapes.Donut;

public class CmdDonutRemove implements Command {


	private Donut donut;
	private DrawingModel model;
	
	public CmdDonutRemove(Donut donut, DrawingModel model)
	{
		this.donut=donut;
		this.model=model;
	}
	@Override
	public void execute() {
		model.remove(donut);	
	}

	@Override
	public void unexecute() {
		model.add(donut);
	}
}
