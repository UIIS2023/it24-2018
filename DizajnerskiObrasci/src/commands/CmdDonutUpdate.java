package commands;

import java.awt.Color;

import exception.RadiusException;
import shapes.Donut;

public class CmdDonutUpdate implements Command {
	
	private Donut oldState;
	private Donut newState;
	private Donut original = new Donut();

	public CmdDonutUpdate(Donut oldState, Donut newState)
	{
		this.oldState=oldState;
		this.newState=newState;
	}
	
	public void execute() {
		original = oldState.clone();

		oldState.getCenter().setX(newState.getCenter().getX());
		oldState.getCenter().setY(newState.getCenter().getY());
		try {
			oldState.setRadius(newState.getRadius());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oldState.setInnerRadius(newState.getInnerRadius());
		oldState.setColor(newState.getColor());
		oldState.setInnerColor(newState.getInnerColor());
		System.out.println(original.toString());
		System.out.println(oldState.toString());
	}

	@Override
	public void unexecute() {
		
		oldState.getCenter().setX(original.getCenter().getX());
		oldState.getCenter().setY(original.getCenter().getY());
		try {
			oldState.setRadius(original.getRadius());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oldState.setInnerRadius(original.getInnerRadius());
		oldState.setColor(original.getColor());
		oldState.setInnerColor(original.getInnerColor());
		System.out.println("Tacka x");
		System.out.println(original.getCenter().getX());
		System.out.println(oldState.getCenter().getX());
		System.out.println("Tacka y");
		System.out.println(original.getCenter().getY());
		System.out.println(oldState.getCenter().getY());
	
		
	}

	public Donut getOriginal() {
		return original;
	}


}
