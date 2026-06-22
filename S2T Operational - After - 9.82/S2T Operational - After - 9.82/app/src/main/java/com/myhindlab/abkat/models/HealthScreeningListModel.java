package com.myhindlab.abkat.models;

public class HealthScreeningListModel {

    private String type;
    private String menuName;
    private int menuIcon;
    private Class<?> className;

    public HealthScreeningListModel(String type, String menuName, int menuIcon, Class<?> className) {
        this.type = type;
        this.menuName = menuName;
        this.menuIcon = menuIcon;
        this.className = className;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
