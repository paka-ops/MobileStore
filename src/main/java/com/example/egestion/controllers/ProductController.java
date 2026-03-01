package com.example.egestion.controllers;

import com.example.egestion.configuration.ResponseBuilder;
import com.example.egestion.models.Product;
import com.example.egestion.models.Stock;
import com.example.egestion.services.implementations.ProductService;
import com.example.egestion.services.implementations.StockService;
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
    private final StockService stockService;

    public ProductController(ProductService productService, ResponseBuilder res, StockService stockService) {
        this.productService = productService;
        this.res = res;
        this.stockService = stockService;
    }
    @GetMapping(params = {"storeId"})
    public ResponseEntity getAllByStore(@RequestParam("storeId") UUID storeId){
        Map<String,Object> response =  res.responseBody(
                "OK",
                "FOUND",
                productService.getAllByStore(storeId));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(params = {"categoryId"})
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
    public ResponseEntity add(@RequestBody Product product,@RequestParam UUID categoryId){
        Product pro =  productService.add(product,categoryId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(res.responseBody("CREATED","CREATION SUCCESSFULLY",pro));
    }
    @PatchMapping("/{productId}")
    public ResponseEntity update(@RequestBody Product product,@PathVariable UUID productId){
        Product pro  = productService.update(product,productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK","UPDATE SUCCESSFULLY",pro));
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity delete(@PathVariable UUID productId){
        productService.delete(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(res.responseBody("NO_CONTENT","DELETION SUCCESSFULY"));
    }
    @GetMapping("/{productId}/stats")
    public ResponseEntity getProductStats(@PathVariable UUID productId){
        Stock stock = stockService.getProductStodck(productId);
        return ResponseEntity.status(200)
                .body(res.responseBody("OK","FOUND" ,stock));
    }
    @PatchMapping(value = "/{productId}",params = "quantity")
    public ResponseEntity restockProduct(@PathVariable UUID productId,@RequestParam double quantity){
        Product product = productService.restockProduct(quantity,productId);
        return ResponseEntity.status(200)
                .body(res.responseBody("OK","FOUND" ,product));
    }


}
