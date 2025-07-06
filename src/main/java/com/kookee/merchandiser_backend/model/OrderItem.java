package com.kookee.merchandiser_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int qty;

    public OrderItem() {}

    public OrderItem(String name, int qty) {
        this.name = name;
        this.qty = qty;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getQty() { return qty; }

    public void setQty(int qty) { this.qty = qty; }
}
