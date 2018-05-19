package util;

public class SinglePress {

	private boolean alreadyTriggered = false;
	private boolean state = false;

	public void update(Boolean condition) {
		if (condition == null) {
			return;
		}
		if (alreadyTriggered) {
			state = false;
		}
		if (condition && !alreadyTriggered) {
			state = true;
		}
		if (condition && state) {
			alreadyTriggered = true;
		}
		if (!condition) {
			alreadyTriggered = false;
		}
	}

	public boolean getState() {
		return state;
	}

}