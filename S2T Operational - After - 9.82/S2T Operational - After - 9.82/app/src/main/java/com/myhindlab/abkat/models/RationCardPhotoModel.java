package com.myhindlab.abkat.models;

public class RationCardPhotoModel {

    private String imagePath;
    private String imageName;

    public RationCardPhotoModel(String imagePath, String imageName) {
        this.imagePath = imagePath;
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getImageName() {
        return imageName;
    }
}