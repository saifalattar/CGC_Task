package com.example.CGC.Business;

import com.example.CGC.CGCExceptions.CGCExceptions;
import com.example.CGC.Database.CartsDatabase;
import com.example.CGC.Database.ProductDatabase;
import com.example.CGC.Database.UserDatabase;
import com.example.CGC.Schemas.*;
import com.example.CGC.Shared.SharedFunctions;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationServices {
    final private UserDatabase userDatabase;
    final private CGCExceptions CGCExceptions;
    final private ProductDatabase productDatabase;
    final private CartsDatabase cartsDatabase;

    public AuthenticationServices(UserDatabase userDatabase, ProductDatabase productDatabase, CartsDatabase cartsDatabase, @Qualifier("Exceptions") CGCExceptions CGCExceptions){
        this.userDatabase = userDatabase;
        this.productDatabase = productDatabase;
        this.CGCExceptions = CGCExceptions;
        this.cartsDatabase = cartsDatabase;
    }

    public CGCExceptions getAbyadExceptions(){
        return this.CGCExceptions;
    }

    public User createUser(User user) throws CGCExceptions {
        try{
            return userDatabase.save(user);
        } catch (Exception e) {
            if(e.getMessage().contains("already exists")){
                throw CGCExceptions.setErrorKey("UserAlreadyExists");
            }
            System.out.println(e);
            throw CGCExceptions.setErrorKey("102025");

        }
    }

    public void modifyingCartItems(boolean isAdding, String token, UUID userId, UUID productId, int quantity) throws CGCExceptions {
        try{
            if (token == null || token.isEmpty()) throw CGCExceptions.setErrorKey("162025");
            if (SharedFunctions.isTokenValid(token)){
                // in case of wrong token
                User user;
                Product product;
                try {
                    user = userDatabase.findById(userId).get();
                } catch (NoSuchElementException e) {
                    throw CGCExceptions.setErrorKey("UserNotFound");
                }

                try{
                    product = productDatabase.findById(productId).get();
                } catch (Exception e) {
                    throw CGCExceptions.setErrorKey("172025");
                }

                Optional<Carts> foundItem = user.getCartProducts().stream().filter(cartItem -> cartItem.getId().equals(user.getId() + product.getId().toString())).findFirst();

                if(isAdding){
                    if(foundItem.isPresent()){
                        if(product.getQuantity() < quantity){
                            throw CGCExceptions.setErrorKey("202025");
                        }
                        productDatabase.updateQuantity(productId, product.getQuantity() - quantity);
                        cartsDatabase.save(foundItem.get().setQuatity(foundItem.get().getQuantity() + quantity));

                    }else {
                        Carts cartItem = new Carts(quantity, product, user);
                        cartsDatabase.save(cartItem);
                    }
                }else{
                    if(foundItem.isEmpty()){
                        throw CGCExceptions.setErrorKey("192025");
                    }
                    if(foundItem.get().getQuantity() < quantity){
                        throw CGCExceptions.setErrorKey("212025");
                    }
                    else if(foundItem.get().getQuantity() > 1){
                        cartsDatabase.save(foundItem.get().setQuatity(foundItem.get().getQuantity() - quantity));
                    }else if(foundItem.get().getQuantity() == 1) {
                        cartsDatabase.deleteById(user.getId() + product.getId().toString());
                    }
                    productDatabase.updateQuantity(productId, product.getQuantity() + quantity);
                }

            }else{
                throw CGCExceptions.setErrorKey("UnAuthorized");
            }
        } catch (JwtException e) {
            throw CGCExceptions.setErrorKey("WrongToken");
        }
    }

    public User logIn(UserLoginRequestDTO user) throws CGCExceptions {
        try{
            User foundUser = userDatabase.findFirstByEmailAndPassword(user.getEmail(), user.getPassword());
            if (foundUser != null){
                SharedFunctions.log("INFO", "User found in system with data : "+foundUser);

                return foundUser;
            }else{
                User foundEmail = userDatabase.findFirstByEmail(user.getEmail());
                if(foundEmail != null){
                    SharedFunctions.log( "ERROR", "The provided password was wrong for this user with email: "+user.getEmail());
                    throw CGCExceptions.setErrorKey("WrongEmailOrPassword");
                }else {
                    SharedFunctions.log("ERROR", "User with email: "+user.getEmail()+" not exists");
                    throw CGCExceptions.setErrorKey("UserNotFound");
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public User getUserDetails(String token) throws CGCExceptions {
        if (token == null || token.isEmpty()) throw CGCExceptions.setErrorKey("162025");
        try{
            DefaultClaims user = (DefaultClaims)SharedFunctions.decodeToken(token).getBody();
            return userDatabase.findById(UUID.fromString(user.get("id").toString())).get();
        } catch (JwtException e) {
            throw CGCExceptions.setErrorKey("WrongToken");
        } catch (Exception e) {
            throw CGCExceptions.setErrorKey("UnAuthorized");
        }
    }

    public String oauthGeneration(OAuth2 oauthData) throws CGCExceptions {
        if(SharedFunctions.isOAuthDataValid(oauthData)){
            return SharedFunctions.generateToken(oauthData);
        }else {
            throw CGCExceptions.setErrorKey("UnAuthorized");
        }

    }
}
