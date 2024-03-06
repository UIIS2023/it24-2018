package commands;

import mvc.DrawingModel;
import shapes.Line;

public class CmdLineRemove implements Command{

	private Line line;
	private DrawingModel model;
	
	public CmdLineRemove(Line line, DrawingModel model)
	{
		this.line=line;
		this.model=model;
	}
	@Override
	public void execute() {
		model.remove(line);
	}

	@Override
	public void unexecute() {
		model.add(line);
	}

}
