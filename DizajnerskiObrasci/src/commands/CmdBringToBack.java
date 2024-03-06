package commands;

import java.util.Collections;

import mvc.DrawingModel;
import shapes.Shape;

public class CmdBringToBack implements Command{

	private DrawingModel model;
	private Shape shape;
	private int position;
	
	public CmdBringToBack(DrawingModel model,Shape shape)
	{
		this.model=model;
		this.shape=shape;
		position=model.getShapes().indexOf(shape);
	}
	
	

	@Override
	public void execute() {
	for (int i = position; i > 0; i--) {
		Collections.swap(model.getShapes(), i, i-1);
		}
		
	}

	@Override
	public void unexecute() {
		for (int i = 0; i < position; i++) {
			Collections.swap(model.getShapes(), i, i+1);
			}
			
		}
		
}

