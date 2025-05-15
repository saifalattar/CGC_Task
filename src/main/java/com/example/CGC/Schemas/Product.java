package com.example.CGC.Schemas;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity(name = "Products")
@JsonFormat
public class Product {
    @Id
    @JsonProperty
    final private UUID id = UUID.randomUUID();

    @JsonProperty
    @Column
    @NotBlank(message = "productName must be sent and not empty.")
    @Size(min = 4, message = "productName name must be at least at length of 4.")
    final private String productName;

    @JsonProperty
    @Column
    @NotNull(message = "price must be sent and not empty.")
    final private float price;

    @JsonProperty
    @Column
    @Min(value = 0, message = "quantity must not be negative")
    @NotNull(message = "quantity must be sent and not empty.")
    final private int quantity;

    @JsonProperty
    @Column
    @NotBlank(message = "color must be sent and not empty.")
    @Size(min = 3, message = "color name must be at least at length of 3.")
    final private String color;

    @JsonProperty
    @Column
    @NotNull(message = "images must be sent.")
    final private List<String> images;

    @JsonProperty
    @Column
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Set<Carts> cart;

    public Product(){
        this.color = null;
        this.images = null;
        this.productName = null;
        this.price = 0;
        this.quantity = 0;
    }

    public Product(String productName, String color, float price, List<String> images, int quantity){
        this.color = color;
        this.images = images;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public UUID getId(){
        return id;
    }

    public float getPrice() {
        return price;
    }
}
