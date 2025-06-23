package com.kookee.merchandiser_backend.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Item {

    private String name;
    private int qty;

    public Item() {}

    public Item(String name, int qty) {
        this.name = name;
        this.qty = qty;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getQty() { return qty; }

    public void setQty(int qty) { this.qty = qty; }
}
