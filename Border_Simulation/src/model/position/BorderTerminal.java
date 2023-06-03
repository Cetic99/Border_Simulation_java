package model.position;


public abstract class BorderTerminal extends Position {

	private volatile boolean working = false;

	/**
	 * @return the working
	 */
	public boolean isWorking() {
		return working;
	}

	/**
	 * @param working the working to set
	 */
	public void setWorking(boolean working) {
		this.working = working;
	}
}
