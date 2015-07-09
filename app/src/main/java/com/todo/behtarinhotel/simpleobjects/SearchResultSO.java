package com.todo.behtarinhotel.simpleobjects;

/**
 * Created by dmytro on 7/8/15.
 */
public class SearchResultSO {

    String photoURL;
    String hotelName;
    String city;
    String address;
    int likeCounter;
    float stars;
    float minPrice;
    String tripAdvisorRatingURL;
    String locationDescription;
    float latitude;
    float longitude;

    public SearchResultSO() {
    }

    public SearchResultSO(String photoURL,
                          String hotelName,
                          String city,
                          String address,
                          int likeCounter,
                          float stars,
                          float minPrice,
                          String tripAdvisorRatingURL,
                          String locationDescription,
                          float latitude,
                          float longitude) {
        this.photoURL = photoURL;
        this.hotelName = hotelName;
        this.city = city;
        this.address = address;
        this.likeCounter = likeCounter;
        this.stars = stars;
        this.minPrice = minPrice;
        this.tripAdvisorRatingURL = tripAdvisorRatingURL;
        this.locationDescription = locationDescription;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
