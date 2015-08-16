package in.sel.model;

/**
 * Created by Lokesh on 08-08-2015.
 */
public class M_AlphaCount {

    int count;
    String alphabet;

    public M_AlphaCount(int count, String alphabet) {
        this.count = count;
        this.alphabet = alphabet;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }
}
