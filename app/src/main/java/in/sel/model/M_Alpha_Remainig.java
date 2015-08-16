package in.sel.model;

public class M_Alpha_Remainig {

	private String alphabet = "";
	private int remains = 0;

	public M_Alpha_Remainig(String alphabet, int remains) {
		super();
		this.alphabet = alphabet;
		this.remains = remains;
	}

	public String getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}

	public int getRemains() {
		return remains;
	}

	public void setRemains(int remains) {
		this.remains = remains;
	}

	@Override
	public String toString() {
		return "M_Alpha_Remainig [alphabet=" + alphabet + ", remains="
				+ remains + "]";
	}

}
