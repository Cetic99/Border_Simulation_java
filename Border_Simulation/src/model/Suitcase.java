package model;

import java.util.Objects;

import model.passenger.Passenger;

public class Suitcase {
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

	@Override
	public int hashCode() {
		return Objects.hash(allowed, owner);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Suitcase other = (Suitcase) obj;
		return allowed == other.allowed && Objects.equals(owner, other.owner);
	}
	
	/*------- Helper Functions -----------*/
	private void calculateAllowed() {
		if(Math.random() <= 0.1)
			this.allowed = false;
		else
			this.allowed = true;
	}
}
