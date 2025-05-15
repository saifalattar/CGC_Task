package com.example.CGC.Controller;
import com.example.CGC.CGCExceptions.CGCExceptions;
import com.example.CGC.Business.AuthenticationServices;
import com.example.CGC.Schemas.ModifyingCartDTO;
import com.example.CGC.Schemas.OAuth2;
import com.example.CGC.Schemas.User;
import com.example.CGC.Schemas.UserLoginRequestDTO;
import com.example.CGC.Shared.SharedFunctions;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1.0.0/")
public class CGCAuthenticationController {
    final private AuthenticationServices services;

    public CGCAuthenticationController(AuthenticationServices services){
        this.services = services;
    }

    @PostMapping(value = "/oauth/generate", consumes = MediaType.ALL_VALUE)
    public Object oauthGenerate(@RequestBody MultiValueMap client_data) throws CGCExceptions {
        System.out.println("here");
        String clint_id = ((ArrayList)client_data.get("client_id")).get(0).toString();
        String client_scret = ((ArrayList)client_data.get("client_secret")).get(0).toString();
        OAuth2 oAuth2 = new OAuth2(clint_id, client_scret);

        HashMap res = new HashMap();
        res.put("access_token", services.oauthGeneration(oAuth2));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(path = "/CreateAccount")
    public ResponseEntity<HashMap<String, Object>> createNewUser(@Valid @RequestBody User user) throws CGCExceptions {
        HashMap<String, Object> response = new HashMap<String, Object>();
        String token = SharedFunctions.generateToken(services.createUser(user));
        response.put("userData", user.toUserResponseDTO());
        response.put("status", "201");
        response.put("token", token);
        return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.CREATED);
    }

    @PostMapping(path = "/LogIn")
    public ResponseEntity logIn(@Valid @RequestBody UserLoginRequestDTO user) throws CGCExceptions {
        User foundUser = services.logIn(user);
        HashMap<String, Object> response = new HashMap<String, Object>();
        String token = SharedFunctions.generateToken(foundUser);
        response.put("status", "200");
        response.put("userData", foundUser.toUserResponseDTO());
        response.put("token", token);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping(path = "/details")
    public ResponseEntity getUserDetails(@RequestHeader String token) throws CGCExceptions {
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("userData", services.getUserDetails(token).toUserResponseDTO());
        response.put("status", "302");
        return new ResponseEntity(response, HttpStatus.FOUND);
    }

    @PutMapping("/cart")
    public ResponseEntity<HashMap<String, Object>> addNewCartItem(
            @Valid @RequestBody ModifyingCartDTO modifyingCartDTO,
            @RequestHeader(required = false) String token
    ) throws CGCExceptions {
        services.modifyingCartItems(true, token, modifyingCartDTO.getUserId(), modifyingCartDTO.getProductId(), modifyingCartDTO.getQuantity());
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("status", "201");
        response.put("description", "Added new cart item successfully.");
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/cart")
    public ResponseEntity<HashMap<String, Object>> removeCartItem(
            @RequestHeader(required = false) String token,
            @Valid @RequestBody ModifyingCartDTO modifyingCartDTO
            ) throws CGCExceptions {
        System.out.println("token"+token);
        services.modifyingCartItems(false, token, modifyingCartDTO.getUserId(), modifyingCartDTO.getProductId(), modifyingCartDTO.getQuantity());
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("status", "201");
        response.put("description", "Removed cart item successfully.");
        return new ResponseEntity(response, HttpStatus.CREATED);

    }

}
