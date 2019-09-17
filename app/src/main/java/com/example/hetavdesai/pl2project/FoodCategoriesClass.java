package com.example.hetavdesai.pl2project;

public class FoodCategoriesClass {

    private String catName, catImage, catID;

    public FoodCategoriesClass() {
    }

    public FoodCategoriesClass(String catName, String catImage, String catID) {
        this.catName = catName;
        this.catImage = catImage;
        this.catID = catID;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }
}