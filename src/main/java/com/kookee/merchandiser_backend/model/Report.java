package com.kookee.merchandiser_backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String merchandiser;
    private String outlet;
    private LocalDate date;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "report_items", joinColumns = @JoinColumn(name = "report_id"))
    private List<Item> items = new ArrayList<>();

    public Long getId() { return id; }

    public String getMerchandiser() { return merchandiser; }
    public void setMerchandiser(String merchandiser) { this.merchandiser = merchandiser; }

    public String getOutlet() { return outlet; }
    public void setOutlet(String outlet) { this.outlet = outlet; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}
