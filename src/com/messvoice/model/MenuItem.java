package com.messvoice.model;

public class MenuItem {
    private int id;
    private String dishName;
    private String mealType;
    private String category;
    private boolean isAvailable;
    
    public MenuItem() {}
    
    public MenuItem(int id, String dishName, String mealType, String category) {
        this.id = id;
        this.dishName = dishName;
        this.mealType = mealType;
        this.category = category;
        this.isAvailable = true;
    } 
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }
    
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}