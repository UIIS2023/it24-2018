package commands;


import mvc.DrawingModel;

import adapter.HexagonAdapter;

public class CmdHexagonAdd implements Command{

	private HexagonAdapter hexagon;
	private DrawingModel model;
	
	public CmdHexagonAdd(HexagonAdapter hexagonAdapter, DrawingModel model)
	{
		this.hexagon=hexagonAdapter;
		this.model=model;
	}
	
	@Override
	public void execute() {
		model.add(hexagon);
	}

	@Override
	public void unexecute() {
		model.remove(hexagon);
		
	}

}
