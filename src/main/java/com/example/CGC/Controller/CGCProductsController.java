package com.example.CGC.Controller;

import com.example.CGC.CGCExceptions.CGCExceptions;
import com.example.CGC.Business.ProductsServices;
import com.example.CGC.Schemas.OrderRequestDTO;
import com.example.CGC.Schemas.Product;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1.0.0/products")
public class CGCProductsController {

    final private ProductsServices services;

    public CGCProductsController(ProductsServices services){
        this.services = services;
    }

    @GetMapping("/")
    public ResponseEntity<HashMap<String , Object>> getAllProducts(@RequestHeader String token)throws CGCExceptions {
        HashMap<String , Object> response = new HashMap<String, Object>();
        response.put("products", services.getAllProducts(token));
        return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.OK);
    }

    @GetMapping("/id/{productId}")
    public ResponseEntity<HashMap<String, Product>> getProductById(
            @PathVariable UUID productId,
            @RequestHeader String token
    ) throws CGCExceptions {
        HashMap<String , Product> response = new HashMap<String, Product>();
        response.put("product", services.getProductById(productId, token));
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @GetMapping("/quantity")
    public ResponseEntity<HashMap<String, Integer>> getProductQuantityById(
            @RequestParam UUID productId,
            @RequestHeader String token
    ) throws CGCExceptions {
        HashMap<String , Integer> response = new HashMap<String, Integer>();
        response.put("quantity", services.getProductQuantity(productId, token));
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @PostMapping("/AddNewProduct")
    public ResponseEntity<HashMap<String, Object>> addNewProduct(@Valid @RequestBody Product product, @RequestHeader String token) throws CGCExceptions {
        HashMap<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("status", "201");
        responseBody.put("description", "Product added successfully");
        responseBody.put("Product", services.addNewProduct(token, product));
        return new ResponseEntity<HashMap<String, Object>>(responseBody, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<HashMap<String , Object>> getProductsByName(@RequestParam String productName, @RequestHeader String token)throws CGCExceptions {
        HashMap<String , Object> responseBody = new HashMap<String, Object>();
        responseBody.put("foundProducts", services.getProductByName(productName, token));
        return new ResponseEntity<HashMap<String , Object>>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping("/{productID}")
    public ResponseEntity<HashMap<String, Object>> deleteProductByID(@PathVariable UUID productID, @RequestHeader String token) throws CGCExceptions {
        services.deleteProductByID(token, productID);
        HashMap<String , Object> response = new HashMap<String, Object>();
        response.put("status", "200");
        response.put("description", "Product deleted successfully.");
        return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.OK);
    }


    @PostMapping("/order")
    public ResponseEntity<HashMap<String, Object>> createNewOrder(@RequestBody OrderRequestDTO orderRequestDTO, @RequestHeader String token) throws CGCExceptions {
        HashMap<String , Object> response = new HashMap<String, Object>();

        response.put("order", services.createNewOrder(orderRequestDTO.getUserId(), token));
        return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.OK);
    }

    @GetMapping("/orders")
    public ResponseEntity<HashMap<String, Object>> getAllOrders(@RequestBody OrderRequestDTO orderRequestDTO, @RequestHeader String token) throws CGCExceptions {
        HashMap<String , Object> response = new HashMap<String, Object>();

        response.put("allOrders", services.getAllUserOrders(orderRequestDTO.getUserId(), token));
        return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.OK);
    }

}
