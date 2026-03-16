package com.inventory.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "products_identity")
public class ProductIdentity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    public ProductIdentity() {
    }

    public ProductIdentity(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }
}
