package util;

public class Toggle {

	private boolean alreadyUpdated = false;
	private boolean toggleState = false;

	public void update(boolean condition) {
		if (condition && !alreadyUpdated) {
			alreadyUpdated = true;
			toggleState = !toggleState;
		}
		if (!condition) {
			alreadyUpdated = false;
		}
	}

	public void invertToggleState() {
		this.toggleState = !this.toggleState;
	}

	public boolean getToggleState() {
		return toggleState;
	}

}