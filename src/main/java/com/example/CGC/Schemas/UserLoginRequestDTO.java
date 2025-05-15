package com.example.CGC.Schemas;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonFormat
public class UserLoginRequestDTO {
    @JsonProperty
    @NotBlank(message = "email must be sent and not empty.")
    @Email(regexp = ".+@[a-zA-z]+\\.[a-zA-z]{1,3}")
    final private String email;

    @JsonProperty
    @NotBlank(message = "password must be sent and not empty.")
    @Size(min = 8, message = "password must be at least at length 8")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}", message = "password is weak and doesn't match the pattern, must includes small, capital, special characters and numbers with at least at length 8.")
    final private String password;

    UserLoginRequestDTO(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {return this.email;}
    public String getPassword(){return this.password;}

}
