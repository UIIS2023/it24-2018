package observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import mvc.DrawingFrame;

public class BtnObserverUpdate implements PropertyChangeListener {

	private DrawingFrame frame;
	
	public BtnObserverUpdate(DrawingFrame frame)
	{
		this.frame=frame;
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("deleteBtn")) {
			frame.getTglbtnDelete().setEnabled((boolean)evt.getNewValue());
		}
		if(evt.getPropertyName().equals("modifyBtn")) {
			frame.getTglbtnModify().setEnabled((boolean)evt.getNewValue());
		}
		if(evt.getPropertyName().equals("btnFront")) {
			frame.getBtnFront().setEnabled((boolean)evt.getNewValue());
		}
		if(evt.getPropertyName().equals("btnBack")) {
			frame.getBtnBack().setEnabled((boolean)evt.getNewValue());
		}
		if(evt.getPropertyName().equals("btnToFront")) {
			frame.getBtnToFront().setEnabled((boolean)evt.getNewValue());
		}
		if(evt.getPropertyName().equals("btnToBack")) {
			frame.getBtnToBack().setEnabled((boolean)evt.getNewValue());
		}
		
	}

	
	
}
