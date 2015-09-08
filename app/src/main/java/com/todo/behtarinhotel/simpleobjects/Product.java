package com.todo.behtarinhotel.simpleobjects;

/**
     * Simple object with product data
     */
    public class Product {
        public static final String USD = "USD";
        private String productName, price;
        private String currency;

        /**
         *
         * @param productName - what user pays for
         * @param price - products price
         * @param currency - currency in which we pay the price
         */
        public Product(String productName, String price, String currency){
            this.productName = productName;
            this.price = price;
            this.currency = currency;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
                
        public String getCurrency() {
            return currency;
        }
        
        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }