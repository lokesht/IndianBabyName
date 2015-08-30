package in.sel.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.sel.dbhelper.TableContract;

/**
 * Created by Lokesh on 22-08-2015.
 */
public class SortingValueHolder {

    private String sortingColumn = TableContract.Name.NAME_FRE;
    private boolean isMaleSelected = true;
    private boolean isFemaleSelected = true;

    public boolean isMaleSelected() {
        return isMaleSelected;
    }

    public void setIsMaleSelected(boolean isMaleSelected) {
        this.isMaleSelected = isMaleSelected;
    }

    public boolean isFemaleSelected() {
        return isFemaleSelected;
    }

    public void setIsFemaleSelected(boolean isFemaleSelected) {
        this.isFemaleSelected = isFemaleSelected;
    }

    public SortingValueHolder() {
    }

    /* Copy Constructer*/
    public SortingValueHolder(SortingValueHolder sortingValueHolder) {
        this.sortingColumn = sortingValueHolder.getSortingColumn();
        this.isFemaleSelected = sortingValueHolder.isFemaleSelected();
        this.isMaleSelected = sortingValueHolder.isMaleSelected();
    }

    public String getSortingColumn() {
        return sortingColumn;
    }

    public void setSortingColumn(String sortingColumn) {
        this.sortingColumn = sortingColumn;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof SortingValueHolder) {
            SortingValueHolder temp = (SortingValueHolder) o;
            if (this.isMaleSelected() != temp.isMaleSelected())
                return false;
            if (this.isFemaleSelected() != temp.isFemaleSelected())
                return false;
            if (!this.getSortingColumn().equalsIgnoreCase(temp.getSortingColumn()))
                return false;

            return true;
        }

        return super.equals(o);
    }
}
