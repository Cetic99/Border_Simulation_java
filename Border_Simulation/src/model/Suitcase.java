package model;

import java.io.Serializable;
import java.util.Objects;

import model.passenger.Passenger;
import java.util.Random;

public class Suitcase implements Serializable{
	private static final long serialVersionUID = 1L;
	public static Random rand = new Random();
	private boolean allowed = true;
	private Passenger owner = null;

	public Suitcase(Passenger p) {
		this.owner = p;
		this.calculateAllowed();
	}
	/**
	 * @return the allowed
	 */
	public boolean isAllowed() {
		return allowed;
	}

	/**
	 * @param allowed the allowed to set
	 */
	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	/**
	 * @return the owner
	 */
	public Passenger getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Passenger owner) {
		this.owner = owner;
	}


	
	/*------- Helper Functions -----------*/
	private void calculateAllowed() {
		if(rand.nextDouble() <= 0.1)
			this.allowed = false;
		else
			this.allowed = true;
	}
}
