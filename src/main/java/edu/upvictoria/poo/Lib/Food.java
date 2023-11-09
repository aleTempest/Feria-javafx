package edu.upvictoria.poo.Lib;

import java.util.HashMap;
public class Food {
    public static class FoodItem {
        private String name;
        private Float price;

        public FoodItem(String name, Float price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }
    }

    private HashMap<FoodItem,Integer> foodItems;

    public Food() {
        this.foodItems = new HashMap<>();
    }

    public Food(HashMap<FoodItem,Integer> foodItems) {
        this.foodItems = foodItems;
    }

    public void addItem(String name, Float price, Integer amount) {
        var item = new FoodItem(name,price);
        itemExists(amount,item);
        this.foodItems.put(new FoodItem(name,price),amount);
    }

    public void addItem(FoodItem item, Integer amount) {
        itemExists(amount,item);
        this.foodItems.put(item,amount);
    }

    private void itemExists(Integer amount, FoodItem item) {
        if (this.foodItems.containsKey(item)) {
            var currAmount = this.foodItems.get(item);
            this.foodItems.put(item, currAmount + amount);
        }
    }
}
