package com.kookee.merchandiser_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Item {

    @Column(name = "name")
    private String name;

    @Column(name = "qty")
    private int qty;

    @Column(name = "expiry")
    private String expiry;

    @Column(name = "notes")  // ✅ New field for notes
    private String notes;

    public Item() {}

    public Item(String name, int qty, String expiry, String notes) {
        this.name = name;
        this.qty = qty;
        this.expiry = expiry;
        this.notes = notes;
    }

    // --- Getters ---
    public String getName() {
        return name;
    }

    public int getQty() {
        return qty;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getNotes() {
        return notes;
    }

    // --- Setters ---
    public void setName(String name) {
        this.name = name;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
