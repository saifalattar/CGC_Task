package com.example.CGC.Schemas;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;


@JsonFormat
@Entity
public class CGCOrder {
    @JsonProperty
    @Column
    @Id
    final private UUID orderId = UUID.randomUUID();

    @JsonProperty
    @Column
    @OneToMany(mappedBy = "cgcOrder")
    final private Set<Carts> cartItems;

    @JsonProperty
    @Column
    final private float totalPrice;

    @JsonProperty
    @Column
    final private UUID userId;

    @JsonProperty
    @Column
    final private HashMap<UUID, Integer> cartItemsId;

    public CGCOrder() {
        this.cartItemsId = null;
        this.cartItems = null;
        this.totalPrice = 0;
        this.userId = null;
    }

    public CGCOrder(Set<Carts> cartItemsInOrder, float totalPrice, UUID userId, HashMap<UUID, Integer> cartItemsId) {
        this.cartItems = cartItemsInOrder;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.cartItemsId = cartItemsId;
    }
}
