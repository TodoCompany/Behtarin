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
    @SerializedName("checkInInstructions")
    private String checkInInstruction;


    public String getCheckInInstruction() {
        return checkInInstruction;
    }

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
        @SerializedName("rateCode")
        private int rateCode;
        @SerializedName("roomTypeCode")
        private int roomTypeCode;
        @SerializedName("rateOccupancyPerRoom")
        private int rateOccupancyPerRoom;
        @SerializedName("quotedOccupancy")
        private int maxGuests;
        @SerializedName("RateInfos")
        private RateInfos rateInfos;
        @SerializedName("roomTypeDescription")
        private String description;
        @SerializedName("RoomImages")
        private RoomImages roomImages;
        @SerializedName("smokingPreferences")
        private String smokingPreference;
        @SerializedName("cancellationPolicy")
        private String cancellationPolicy;


        private String bedDescription;
        private int bedsQuantity;


        public int getRateCode() {
            return rateCode;
        }

        public void setRateCode(int rateCode) {
            this.rateCode = rateCode;
        }

        public int getRoomTypeCode() {
            return roomTypeCode;
        }

        public int getRateOccupancyPerRoom() {
            return rateOccupancyPerRoom;
        }

        public RateInfos getRateInfos() {
            return rateInfos;
        }

        public String getBedDescription() {
            return bedDescription;
        }

        public void setBedDescription(String descr) {
            bedDescription = descr;
        }

        public int getBedsQuantity() {
            return bedsQuantity;
        }

        public void setBedsQuantity(int bedsQuantity) {
            this.bedsQuantity = bedsQuantity;
        }


        public String getDescription() {
            return description;
        }

        public String getRoomImage() {
            if (roomImages != null && roomImages.getRoomImage() != null && roomImages.getRoomImage().getUrl() != null) {
                return roomImages.getRoomImage().getUrl();
            }
            return null;
        }

        public int getMaxGuests() {
            return maxGuests;
        }

        public float getAverageRate() {
            return rateInfos.getRateInfo().getChargeableRateInfo().getAverageRate();
        }

        public float getOldPrice() {
            return rateInfos.getRateInfo().getChargeableRateInfo().getOldPrice();
        }

        public String getRateKey(){
            return rateInfos.getRateInfo().getRoomGroup().getRoom().getRateKey();
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

        private class RateInfos {
            @SerializedName("RateInfo")
            private RateInfo rateInfo;
            private RateInfo getRateInfo() {
                return rateInfo;
            }
            private class RateInfo {
                @SerializedName("ChargeableRateInfo")
                private ChargeableRateInfo chargeableRateInfo;
                @SerializedName("RoomGroup")
                private RoomGroup roomGroup;

                private ChargeableRateInfo getChargeableRateInfo() {
                    return chargeableRateInfo;
                }

                private RoomGroup getRoomGroup() {
                    return roomGroup;
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

                private class RoomGroup{
                    @SerializedName("Room")
                    private Room room;

                    private Room getRoom(){
                        return room;
                    }

                    private class Room{
                        @SerializedName("rateKey")
                        private String rateKey;

                        private String getRateKey(){
                            return rateKey;
                        }
                    }
                }
            }
        }

        public String getSmokingPreference() {
            return smokingPreference;
        }

        public void setSmokingPreference(String smokingPreference) {
            this.smokingPreference = smokingPreference;
        }

        public String getCancellationPolicy() {
            return cancellationPolicy;
        }

        public void setCancellationPolicy(String cancellationPolicy) {
            this.cancellationPolicy = cancellationPolicy;
        }
    }


}
