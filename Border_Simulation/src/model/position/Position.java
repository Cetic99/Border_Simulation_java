package model.position;

import javafx.scene.image.ImageView;
import model.vehicle.Vehicle;

public class Position {
	
	private boolean taken = false;
	private Vehicle vehicle;
	private ImageView imView;
	
	
	public synchronized void takePosition(Vehicle v) {
		this.setVehicle(v);
		this.setTaken(true);
	}
	
	public synchronized void releasePosition() {
		this.setTaken(false);
	}
	/**
	 * @return the vehicle
	 */
	public Vehicle getVehicle() {
		return vehicle;
	}

	/**
	 * @param vehicle the vehicle to set
	 */
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	/**
	 * @return the taken
	 */
	public boolean isTaken() {
		return taken;
	}

	/**
	 * @param taken the taken to set
	 */
	public void setTaken(boolean taken) {
		this.taken = taken;
	}

	/**
	 * @return the imView
	 */
	public ImageView getImView() {
		return imView;
	}

	/**
	 * @param imView the imView to set
	 */
	public void setImView(ImageView imView) {
		this.imView = imView;
	}
	public void updateImage() {
		this.imView.setImage(this.vehicle.getImage());
	}
}
