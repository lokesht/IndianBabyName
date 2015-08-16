package in.sel.exception;

public class ValueNotInsertedException extends Exception {
	private static final long serialVersionUID = 1L;
	String s = "";

	public ValueNotInsertedException() {
		// s = "ValueNotInsertedException";
	}

	public ValueNotInsertedException(String exc) {
		s = exc;// + "ValueNotInsertedException";
	}

	@Override
	public String toString() {
		return (s);
	}
}
