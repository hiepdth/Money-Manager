package com.example.moneymanager.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class HistoryChild {
    private String category;
    private String type;
    private String name;
    private long amount;

    public HistoryChild(){

    }
    public HistoryChild(String category, String type, String name, long amount) {
        this.category = category;
        this.type = type;
        this.name = name;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
