package commands;

import java.util.Collections;

import mvc.DrawingModel;
import shapes.Shape;

public class CmdBringToFront implements Command {

	private DrawingModel model;
	private Shape shape;
	private int position;
	private int last;
	
	public CmdBringToFront(DrawingModel model,Shape shape)
	{
		this.model=model;
		this.shape=shape;
		position=model.getShapes().indexOf(shape);
		last=model.getShapes().size()-1;
	}
	@Override
	public void execute() {
	/*	for (int i = 0; i < model.getShapes().size()-1; i++) {
			if(model.getShapes().get(i).isSelected()) {
				if(i==model.getShapes().size()-1) {
					return;
				} else {
					Shape shape = model.getShapes().get(i+1);
					model.getShapes().set(i+1, model.getShapes().get(i));
					model.getShapes().set(i, shape);
				}
			}
		}*/
		for(int i=position;i<last;i++)
		{
			Collections.swap(model.getShapes(),i, i+1);
		}
	}

	@Override
	public void unexecute() {
		for(int i=last;i>position;i--)
		{
			Collections.swap(model.getShapes(),i, i-1);
		}
		
	}

}
