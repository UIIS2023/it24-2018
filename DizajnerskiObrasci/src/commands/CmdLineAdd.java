package commands;

import mvc.DrawingModel;
import shapes.Line;

public class CmdLineAdd implements Command{

	private Line line;
	private DrawingModel model;
	
	public CmdLineAdd(Line line,DrawingModel model)
	{
		this.line=line;
		this.model=model;
	}
	
	
	@Override
	public void execute() {
		model.add(line);
		
	}

	@Override
	public void unexecute() {
		model.remove(line);
		
	}
	

}
