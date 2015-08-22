package in.sel.model;

import java.util.Comparator;

/**
 * Created by Lokesh on 22-08-2015.
 */
public class EngNameDsc implements Comparator<M_Name> {


    @Override
    public int compare(M_Name lhs, M_Name rhs) {

        return rhs.getName_en().compareTo(rhs.getName_en());

    }

}
