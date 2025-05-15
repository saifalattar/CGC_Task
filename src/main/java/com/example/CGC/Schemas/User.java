package com.example.CGC.Schemas;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.*;

@Entity(name = "Users")
@JsonFormat
public class User {
    @Id
    @JsonProperty
    final private UUID id = UUID.randomUUID();

    @JsonProperty
    @NotBlank(message = "username must be sent and not empty.")
    @Column
    @Size(min = 4, message = "username must be at least at length 4")
    final private String username;

    @JsonProperty
    @NotBlank(message = "email must be sent and not empty.")
    @Column(unique = true)
    @Email(regexp = ".+@[a-zA-z]+\\.[a-zA-z]{1,3}")
    final private String email;

    @JsonProperty
    @Column
    @NotBlank(message = "password must be sent and not empty.")
    @Size(min = 8, message = "password must be at least at length 8")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}", message = "password is weak and doesn't match the pattern, must includes small, capital, special characters and numbers with at least at length 8.")
    final private String password;

    @JsonProperty
    @Column
    @NotBlank(message = "phone must be sent and not empty.")
    @Pattern(regexp = "(01[0125]\\d{8})|(00201[0125]\\d{8})", message = "phone doesn't match the egyptian mobile phones pattern.")
    final private String phone;

    @JsonProperty
    @Column
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Carts> cart = new HashSet<>();
//
//    @JsonIgnore
//    @Column(name = "products")
//    @ManyToMany
//    @JoinTable(
//            joinColumns = @JoinColumn(name = "userID"),
//            inverseJoinColumns = @JoinColumn(name = "productID")) // the join table always written in the owner side of the table which will be the user here.
//    private Set<Product> products = new HashSet<>();

    public User(){
        this.email=null;
        this.phone=null;
        this.username=null;
        this.password = null;
    }

    public User(String username, String email, String phone, String password){
        this.email=email;
        this.phone=phone;
        this.username=username;
        this.password = password;
    }

    public Set<Carts> getCartProducts(){
        return cart;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public UUID getId(){
        return id;
    }

    public HashMap<String, Object> userClaims(){
        HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("email", this.email);
        claims.put("phone", this.phone);
        claims.put("username", this.username);
        claims.put("id", this.id);
        return claims;
    }

    public UserResponseDTO toUserResponseDTO(){
        return new UserResponseDTO(email, username, phone, id, cart);
    }

}
