package com.example.Sale_Savvy.admincontrollers;

// Admin Controller for Add Product functionality


import com.example.Sale_Savvy.Entities.Product;
import com.example.Sale_Savvy.adminservices.AdminProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {

    private final AdminProductService adminProductService;

    public AdminProductController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> productRequest) {
        try {
            String name = (String) productRequest.get("name");
            String description = (String) productRequest.get("description");
            Double price = Double.valueOf(String.valueOf(productRequest.get("price")));
            Integer stock = (Integer) productRequest.get("stock");
            Integer categoryId = (Integer) productRequest.get("categoryId");
            String imageUrl = (String) productRequest.get("imageUrl");

            Product addedProduct = adminProductService.addProductWithImage(name, description, price, stock, categoryId, imageUrl);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProduct(@RequestBody Map<String, Integer> requestBody) {
        try {
            Integer productId = requestBody.get("productId");
            adminProductService.deleteProduct(productId);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(
            @RequestBody Map<String, Object> requestBody)
    {
        Integer productId = (Integer) requestBody.get("productId");
        String name = (String) requestBody.get("name");
        String description = (String) requestBody.get("description");
        Double price = Double.valueOf(requestBody.get("price").toString());
        Integer stock = (Integer) requestBody.get("stock");
        Integer categoryId = (Integer) requestBody.get("categoryId");

        Product updatedProduct = adminProductService.updateProduct(
                productId,
                name,
                description,
                price,
                stock,
                categoryId
        );
        return ResponseEntity.ok(updatedProduct);
    }


    @GetMapping("/count")
    public ResponseEntity<Long> getTotalProducts()
    {
        return ResponseEntity.ok(
                adminProductService.getTotalProducts()
        );
    }

    @GetMapping("/users/count")
    public ResponseEntity<Long> getTotalUsers()
    {
        return ResponseEntity.ok(
                adminProductService.getTotalUsers()
        );
    }

    @GetMapping("/orders/count")
    public ResponseEntity<Long> getTotalOrders()
    {
        return ResponseEntity.ok(
                adminProductService.getTotalOrders()
        );
    }


    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue()
    {
        return ResponseEntity.ok(
                adminProductService.getTotalRevenue()
        );
    }


    @GetMapping("/revenue/daily")
    public ResponseEntity<BigDecimal> getDailyRevenue()
    {
        return ResponseEntity.ok(
                adminProductService.getDailyRevenue()
        );
    }


    @GetMapping("/revenue/monthly")
    public ResponseEntity<BigDecimal> getMonthlyRevenue()
    {
        return ResponseEntity.ok(
                adminProductService.getMonthlyRevenue()
        );
    }


    @GetMapping("/revenue/yearly")
    public ResponseEntity<BigDecimal> getYearlyRevenue()
    {
        return ResponseEntity.ok(
                adminProductService.getYearlyRevenue()
        );
    }



    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(
                adminProductService.getAllProducts()
        );
    }

}