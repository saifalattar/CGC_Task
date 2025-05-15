package com.example.CGC.Business;

import com.example.CGC.CGCExceptions.CGCExceptions;
import com.example.CGC.Database.CGCOrderDatabase;
import com.example.CGC.Database.ProductDatabase;
import com.example.CGC.Database.UserDatabase;
import com.example.CGC.Schemas.CGCOrder;
import com.example.CGC.Schemas.Carts;
import com.example.CGC.Schemas.Product;
import com.example.CGC.Schemas.User;
import com.example.CGC.Shared.SharedFunctions;
import io.jsonwebtoken.JwtException;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class ProductsServices {
    final private ProductDatabase productDatabase;
    final private CGCExceptions CGCExceptions;
    final private UserDatabase userDatabase;
    final private CGCOrderDatabase cgcOrderDatabase;
    public ProductsServices(ProductDatabase productDatabase, @Qualifier("Exceptions") CGCExceptions CGCExceptions, UserDatabase userDatabase, CGCOrderDatabase cgcOrderDatabase){
        this.productDatabase = productDatabase;
        this.CGCExceptions = CGCExceptions;
        this.userDatabase = userDatabase;
        this.cgcOrderDatabase = cgcOrderDatabase;
    }

    public Product addNewProduct(String token, Product product) throws CGCExceptions {
        if (token.isEmpty()) throw CGCExceptions.setErrorKey("162025");
        try{
            if(SharedFunctions.isTokenValid(token)){
                return productDatabase.save(product);
            }else{
                throw CGCExceptions.setErrorKey("UnAuthorized");
            }
        } catch (JwtException | ClassCastException e) {
            throw CGCExceptions.setErrorKey("WrongToken");
        }
    }

    public List<Product> getAllProducts(String token) throws CGCExceptions {
        if (token.isEmpty()) throw CGCExceptions.setErrorKey("162025");
        try{
            if(SharedFunctions.isTokenValid(token)){
                List<Product> foundProducts = productDatabase.findAll();
                SharedFunctions.log( "INFO", "Found products :  "+foundProducts);
                return foundProducts;
            }else {
                SharedFunctions.log( "ERROR", "UnAuthorized :  you are not authorized to  access this service with this token");
                throw CGCExceptions.setErrorKey("UnAuthorized");
            }
        } catch (JwtException | ClassCastException e) {
            SharedFunctions.log( "ERROR", "UnAuthorized : "+e.getMessage());
            throw CGCExceptions.setErrorKey("WrongToken");
        }
    }


    public List<Product> getProductByName(String productName, String token) throws CGCExceptions {
        if (token.isEmpty()) throw CGCExceptions.setErrorKey("162025");
        try {
            if (SharedFunctions.isTokenValid(token)) {
                return productDatabase.findByproductNameLike('%' + productName + '%');
            } else {
                throw CGCExceptions.setErrorKey("UnAuthorized");
            }
        } catch (JwtException | ClassCastException e) {
            throw CGCExceptions.setErrorKey("WrongToken");
        }
    }

    public Product getProductById(UUID productId, String token) throws CGCExceptions {
        if (token.isEmpty()) throw CGCExceptions.setErrorKey("162025");
        try {
            if (SharedFunctions.isTokenValid(token)) {
                return productDatabase.getProductByID(productId);
            } else {
                throw CGCExceptions.setErrorKey("UnAuthorized");
            }
        } catch (JwtException | ClassCastException e) {
            throw CGCExceptions.setErrorKey("WrongToken");
        }
    }

    public int getProductQuantity(UUID productId, String token) throws CGCExceptions {
        if (token.isEmpty()) throw CGCExceptions.setErrorKey("162025");
        try {
            if (SharedFunctions.isTokenValid(token)) {
                return productDatabase.getProductQuantityByID(productId);
            } else {
                throw CGCExceptions.setErrorKey("UnAuthorized");
            }
        } catch (JwtException | ClassCastException e) {
            throw CGCExceptions.setErrorKey("WrongToken");
        }
    }

    public void deleteProductByID(String token , UUID id) throws CGCExceptions {
        if (token.isEmpty()) throw CGCExceptions.setErrorKey("162025");
        try {
            if (SharedFunctions.isTokenValid(token)) {
                productDatabase.deleteById(id);
            } else {
                throw CGCExceptions.setErrorKey("UnAuthorized");
            }
        } catch (JwtException | ClassCastException e) {
            throw CGCExceptions.setErrorKey("WrongToken");
        }
    }

    public CGCOrder createNewOrder(UUID userId, String token) throws CGCExceptions {

        if (token.isEmpty()) throw CGCExceptions.setErrorKey("162025");
        try {
            if (SharedFunctions.isTokenValid(token)) {
                User userRequester = userDatabase.getUserById(userId);
                // if user does not exist will throw exception
                if(userRequester == null) throw CGCExceptions.setErrorKey("UserNotFound");

                // if exists, then will create a new order
                HashMap<UUID, Integer> ids = new HashMap<>();
                float totalPrice = 0;
                for (Carts item : userRequester.getCartProducts()){
                    ids.put(item.getProduct().getId(), item.getQuantity());
                    totalPrice += item.getProduct().getPrice() * item.getQuantity();
                }

                CGCOrder newOrder = new CGCOrder(userRequester.getCartProducts(), totalPrice, userId, ids);
                cgcOrderDatabase.save(newOrder);
                return newOrder;
            } else {
                throw CGCExceptions.setErrorKey("UnAuthorized");
            }
        } catch (JwtException | ClassCastException e) {
            throw CGCExceptions.setErrorKey("WrongToken");
        }
    }

    public ArrayList<CGCOrder> getAllUserOrders(UUID userId, String token) throws CGCExceptions {

        if (token.isEmpty()) throw CGCExceptions.setErrorKey("162025");
        try {
            if (SharedFunctions.isTokenValid(token)) {
                User userRequester = userDatabase.getUserById(userId);
                // if user does not exist will throw exception
                if(userRequester == null) throw CGCExceptions.setErrorKey("UserNotFound");

                // if exists, then will create a new order
                ArrayList<CGCOrder> allOrders = cgcOrderDatabase.getAllUserOrders(userId);
                return allOrders;
            } else {
                throw CGCExceptions.setErrorKey("UnAuthorized");
            }
        } catch (JwtException | ClassCastException e) {
            throw CGCExceptions.setErrorKey("WrongToken");
        }
    }

}
