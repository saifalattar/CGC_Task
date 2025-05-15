package com.example.CGC.Schemas;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;


public class ModifyingCartDTO {
    @JsonProperty
//    @NotBlank(message = "userId must be sent and not empty.")
    final private UUID userId;

    @JsonProperty
//    @NotBlank(message = "productId must be sent and not empty.")
    final private UUID productId;

    @JsonProperty
    @Min(value = 1, message = "quantity not be negative")
    final private int quantity;

    ModifyingCartDTO(){
        this.productId = UUID.randomUUID();
        this.userId = UUID.randomUUID();
        this.quantity = 1;
    }

    public int getQuantity() {
        return quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public UUID getUserId() {
        return userId;
    }
}
