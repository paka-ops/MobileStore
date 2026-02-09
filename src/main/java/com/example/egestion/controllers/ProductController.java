package com.example.egestion.controllers;

import com.example.egestion.configuration.ResponseBuilder;
import com.example.egestion.models.Product;
import com.example.egestion.services.implementations.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final ResponseBuilder res;
    public ProductController(ProductService productService, ResponseBuilder res) {
        this.productService = productService;
        this.res = res;
    }
    @GetMapping(value = "/",params = {"storeId"})
    public ResponseEntity getAllByStore(@RequestParam("storeId") UUID storeId){
        Map<String,Object> response =  res.responseBody(
                "OK",
                "FOUND",
                productService.getAllByCategory(storeId));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(value = "/",params = {"categoryId"})
    public ResponseEntity getAllByCategory(@RequestParam("categoryId") UUID categoryId){
        List<Product> products = productService.getAllByCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK","FOUND",products));
    }
    @GetMapping(value = "/{productId}",params = {"categoryId"})
    public ResponseEntity getOneByCategory(@RequestParam("categoryId") UUID categoryId,@PathVariable UUID productId){
        Product product = productService.getOneByCategory(productId,categoryId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK","FOUND",product));
    }
    @GetMapping(value = "/{productId}",params = {"storeId"})
    public ResponseEntity getOneByStore(@RequestParam UUID storeId,@PathVariable UUID productId){
        Product product = productService.getOneByStore(productId,storeId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK","FOUND",product));
    }
    @PostMapping(params = {"categoryId"})
    public ResponseEntity add(Product product,UUID categoryId){
        Product pro =  productService.add(product,categoryId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(res.responseBody("CREATED","CREATION SUCCESSFULLY",pro));
    }
    @PatchMapping("/{productId}")
    public ResponseEntity update(@RequestBody Product product,@PathVariable UUID productId){
        Product pro  = productService.update(product,productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK","UPDATE SUCCESSFULLY",product));
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity delete(@PathVariable UUID productId){
        productService.delete(productId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(res.responseBody("OK","DELETION SUCCESSFULY"));
    }


}
