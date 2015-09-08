package com.todo.behtarinhotel.simpleobjects;

/**
 * Created by dmytro on 8/3/15.
 */
public class FilterSO {
    int minPrice;
    int maxPrice;
    int minStarRate;
    int maxStarRate;
    int minTripRate;
    int maxTripRate;

    public FilterSO(int minPrice, int maxPrice, int minStarRate, int maxStarRate, int minTripRate, int maxTripRate) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minStarRate = minStarRate;
        this.maxStarRate = maxStarRate;
        this.minTripRate = minTripRate;
        this.maxTripRate = maxTripRate;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMinStarRate() {
        return minStarRate;
    }

    public void setMinStarRate(int minStarRate) {
        this.minStarRate = minStarRate;
    }

    public int getMaxStarRate() {
        return maxStarRate;
    }

    public void setMaxStarRate(int maxStarRate) {
        this.maxStarRate = maxStarRate;
    }

    public int getMinTripRate() {
        return minTripRate;
    }

    public void setMinTripRate(int minTripRate) {
        this.minTripRate = minTripRate;
    }

    public int getMaxTripRate() {
        return maxTripRate;
    }

    public void setMaxTripRate(int maxTripRate) {
        this.maxTripRate = maxTripRate;
    }
}
