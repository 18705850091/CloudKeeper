package com.demo.enums;

public enum PageSize {
    SIZE15(15), SIZE20(20), SIZE30(30), SIZE40(40), SIZE50(50);
    int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
