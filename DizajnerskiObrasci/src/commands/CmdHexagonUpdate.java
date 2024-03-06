package commands;

import adapter.HexagonAdapter;
import exception.RadiusException;
import hexagon.Hexagon;

public class CmdHexagonUpdate implements Command{

	private HexagonAdapter oldState;
	private HexagonAdapter newState;
	private HexagonAdapter original;
	
	public CmdHexagonUpdate(HexagonAdapter oldState,HexagonAdapter newState)
	{
		this.oldState=oldState;
		this.newState=newState;
	}
	public void execute() {
		original = oldState.clone();

		oldState.getHexagon().setX(newState.getHexagon().getX());
		oldState.getHexagon().setY(newState.getHexagon().getY());
		oldState.getHexagon().setR(newState.getHexagon().getR());
		oldState.getHexagon().setBorderColor(newState.getHexagon().getBorderColor());
		oldState.getHexagon().setAreaColor(newState.getHexagon().getAreaColor());

	}

	@Override
	public void unexecute() {
		oldState.getHexagon().setX(original.getHexagon().getX());
		oldState.getHexagon().setY(original.getHexagon().getY());
		oldState.getHexagon().setR(original.getHexagon().getR());
		oldState.getHexagon().setBorderColor(original.getHexagon().getBorderColor());
		oldState.getHexagon().setAreaColor(original.getHexagon().getAreaColor());

	}
	public HexagonAdapter getOriginal() {
		return original;
	}
	

}
