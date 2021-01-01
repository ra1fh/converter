package de.ackstorm.converter;

import java.util.ArrayList;

public class Category {
    private final int mCategoryId;
    private final ArrayList<Unit> mList;

    public Category(int res) {
        mCategoryId = res;
        mList = new ArrayList<>();
    }

    /**
     * @return string resource id of the category
     */
    public int getCategoryId() {
        return mCategoryId;
    }

    public void add(Unit unit) {
        mList.add(unit);
    }

    public ArrayList<Unit> getList() {
        return mList;
    }
}