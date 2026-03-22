package com.messvoice.model;

public class MenuItem {
	 private String dishName;
	    private String dayMeal;
	    private String type;

	    public MenuItem() {}

	    public MenuItem(String dishName, String dayMeal, String type) {
	        this.dishName = dishName;
	        this.dayMeal = dayMeal;
	        this.type = type;
	    }

	    public String getDishName() { return dishName; }
	    public void setDishName(String dishName) { this.dishName = dishName; }
	    public String getDayMeal() { return dayMeal; }
	    public void setDayMeal(String dayMeal) { this.dayMeal = dayMeal; }
	    public String getType() { return type; }
	    public void setType(String type) { this.type = type; }

}
