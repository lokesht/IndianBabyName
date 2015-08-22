package in.sel.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.sel.dbhelper.TableContract;

/**
 * Created by Lokesh on 22-08-2015.
 */
public class SortingValueHolder  {

    private List<Integer> genderCategory = new ArrayList<>(3);
    private String sortingColumn = TableContract.Name.NAME_FRE;

    public SortingValueHolder(boolean isIncludeCategory) {

        if (isIncludeCategory) {
            genderCategory.add(0, 0);
            genderCategory.add(1, 1);
            genderCategory.add(2, 2);

            setSortingColumn(sortingColumn);
        }
    }

    /* Copy Constructer*/
    public SortingValueHolder(SortingValueHolder sortingValueHolder)
    {
        this.genderCategory.addAll(sortingValueHolder.getGenderCategory());
        this.sortingColumn = sortingValueHolder.getSortingColumn();
    }

    public List<Integer> getGenderCategory() {
        return genderCategory;
    }

    public void setGenderCategory(List<Integer> genderCategory) {
        this.genderCategory = genderCategory;
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
            SortingValueHolder sortingValueHolder = (SortingValueHolder) o;
            if (this.getGenderCategory().size() != sortingValueHolder.getGenderCategory().size()) {
                return false;
            }

            List<Integer> t1 = new ArrayList<>(getGenderCategory());
            List<Integer> t2 = new ArrayList<>(sortingValueHolder.getGenderCategory());

            Collections.sort(t1);
            Collections.sort(t2);

            if (!t1.equals(t2))
                return false;

            if (!getSortingColumn().equalsIgnoreCase(sortingValueHolder.getSortingColumn()))
                return false;

            return true;

        }
        return super.equals(o);
    }
}
