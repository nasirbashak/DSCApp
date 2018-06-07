package com.nasirbashak007.canteenui;

public class PersonDetails {

    private String amount, item;

    public PersonDetails() {

    }

    public PersonDetails(String amount, String item) {
        this.amount = amount;
        this.item = item;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
