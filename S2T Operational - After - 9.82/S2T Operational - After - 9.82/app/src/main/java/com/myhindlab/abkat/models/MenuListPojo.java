package com.myhindlab.abkat.models;

public class MenuListPojo {

    private String menuName;
    private int menuIcon;
    private Class<?> className;

    public MenuListPojo() {
    }

    public MenuListPojo(String menuName, int menuIcon, Class<?> className) {
        this.menuName = menuName;
        this.menuIcon = menuIcon;
        this.className = className;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(int menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Class<?> getClassName() {
        return className;
    }

    public void setClassName(Class<?> className) {
        this.className = className;
    }
}
