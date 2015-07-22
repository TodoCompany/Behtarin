package com.todo.behtarinhotel.simpleobjects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by dmytro on 7/8/15.
 */
public class SearchResultSO implements Serializable{
    @SerializedName("thumbNailUrl")
    String photoURL;
    @SerializedName("hotelId")
    int hotelId;
    @SerializedName("name")
    String hotelName;
    @SerializedName("city")
    String city;
    @SerializedName("address1")
    String address;
    @SerializedName("tripAdvisorReviewCount")
    int likeCounter;
    @SerializedName("hotelRating")
    float stars;
    @SerializedName("lowRate")
    float minPrice;
    @SerializedName("tripAdvisorRatingUrl")
    String tripAdvisorRatingURL;
    @SerializedName("shortDescription")
    String locationDescription;
    @SerializedName("latitude")
    float latitude;
    @SerializedName("longitude")
    float longitude;

    public SearchResultSO() {
    }



    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLikeCounter() {
        return likeCounter;
    }

    public void setLikeCounter(int likeCounter) {
        this.likeCounter = likeCounter;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public String getTripAdvisorRatingURL() {
        return tripAdvisorRatingURL;
    }

    public void setTripAdvisorRatingURL(String tripAdvisorRatingURL) {
        this.tripAdvisorRatingURL = tripAdvisorRatingURL;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
}