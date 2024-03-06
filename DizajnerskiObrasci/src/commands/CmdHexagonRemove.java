package commands;

import adapter.HexagonAdapter;
import mvc.DrawingModel;

public class CmdHexagonRemove implements Command{
	
	private HexagonAdapter hexagon;
	private DrawingModel model;
	
	public CmdHexagonRemove(HexagonAdapter hexagon,DrawingModel model)
	{
		this.hexagon=hexagon;
		this.model=model;
	}

	@Override
	public void execute() {
		model.remove(hexagon);
		
	}

	@Override
	public void unexecute() {
		model.add(hexagon);
		
	}

}
