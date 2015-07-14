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
        @SerializedName("BedTypes")
        private BedTypes bedTypes;

        public int getRoomTypeCode() {
            return roomTypeCode;
        }

        public int getRateOccupancyPerRoom() {
            return rateOccupancyPerRoom;
        }

        public RateInfo getRateInfo() {
            return rateInfo;
        }

        public String getBedDescription() { return bedTypes.getBedType().getDescription(); }

        public String getDescription() {
            return description;
        }

        public String getRoomImage() {
            return roomImages.getRoomImage().getUrl();
        }

        public float getAverageRate() {
            return rateInfo.getChargeableRateInfo().getAverageRate();
        }

        public float getOldPrice() {
            return rateInfo.getChargeableRateInfo().getOldPrice();
        }

        private class RoomImages {
            @SerializedName("RoomImage")
            private RoomImage roomImage;

            private RoomImage getRoomImage() {
                return roomImage;
            }

            private class RoomImage {
                @SerializedName("url")
                private String url;

                public String getUrl() {
                    return url;
                }
            }
        }

        private class RateInfo {
            @SerializedName("ChargeableRateInfo")
            private ChargeableRateInfo chargeableRateInfo;

            private ChargeableRateInfo getChargeableRateInfo() {
                return chargeableRateInfo;
            }

            private class ChargeableRateInfo {
                @SerializedName("@averageRate")
                private float averageRate;
                @SerializedName("@averageBaseRate")
                private float oldPrice;

                private float getAverageRate() {
                    return averageRate;
                }

                private float getOldPrice() {
                    return oldPrice;
                }

            }
        }

        private class BedTypes{
            @SerializedName("BedType")
            private BedType bedType;
            private class BedType{
                @SerializedName("description")
                private String description;

                public String getDescription() {
                    return description;
                }
            }

            public BedType getBedType() {
                return bedType;
            }
        }
    }


}
