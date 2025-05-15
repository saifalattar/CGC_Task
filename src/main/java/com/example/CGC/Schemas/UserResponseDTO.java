package com.example.CGC.Schemas;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Set;
import java.util.UUID;

@JsonFormat
public record UserResponseDTO(String email, String username, String phone, UUID id, Set<Carts> cart){}
