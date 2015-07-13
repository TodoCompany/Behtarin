package com.todo.behtarinhotel.simpleobjects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Andriy on 13.07.2015.
 */
public class AvailableRoomsSO {


    @SerializedName("hotelId")
    private int hotelId;
    @SerializedName("hotelName")
    private String hotelName;
    @SerializedName("hotelAddress")
    private String hotelAddress;
    @SerializedName("hotelCountry")
    private String hotelCountry;
    @SerializedName("HotelRoomResponse")
    private ArrayList<RoomSO> roomSO;

    public int getHotelId() {
        return hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public String getHotelCountry() {
        return hotelCountry;
    }

    public ArrayList<RoomSO> getRoomSO() {
        return roomSO;
    }

    // This inner classes used to parse their fucking response
    public class RoomSO {
        @SerializedName("roomTypeCode")
        private int roomTypeCode;
        @SerializedName("rateOccupancyPerRoom")
        private int rateOccupancyPerRoom;
        @SerializedName("RateInfo")
        private RateInfo rateInfo;
        @SerializedName("roomTypeDescription")
        private String description;
        @SerializedName("RoomImages")
        private RoomImages roomImages;

        public int getRoomTypeCode() {
            return roomTypeCode;
        }

        public int getRateOccupancyPerRoom() {
            return rateOccupancyPerRoom;
        }

        public RateInfo getRateInfo() {
            return rateInfo;
        }

        public String getDescription() {
            return description;
        }

        public RoomImages getRoomImages() {
            return roomImages;
        }

        public class RoomImages {
            @SerializedName("RoomImage")
            private RoomImage roomImage;

            public RoomImage getRoomImage() {
                return roomImage;
            }

            public class RoomImage {
                @SerializedName("url")
                private String url;

                public String getUrl() {
                    return url;
                }
            }
        }

        public class RateInfo {
            @SerializedName("ChargeableRateInfo")
            private ChargeableRateInfo chargeableRateInfo;

            public ChargeableRateInfo getChargeableRateInfo() {
                return chargeableRateInfo;
            }

            public class ChargeableRateInfo {
                @SerializedName("@averageRate")
                private float averageRate;

                public float getAverageRate() {
                    return averageRate;
                }

            }
        }
    }


}
