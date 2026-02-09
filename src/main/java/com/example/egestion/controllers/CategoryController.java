package com.example.egestion.controllers;

import com.example.egestion.configuration.ResponseBuilder;
import com.example.egestion.exceptions.*;
import com.example.egestion.models.Category;
import com.example.egestion.services.implementations.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ResponseBuilder res;
    public CategoryController(CategoryService categoryService, ResponseBuilder responseBuilder) {
        this.categoryService = categoryService;
        this.res = responseBuilder;
    }
    @GetMapping
    public ResponseEntity getAll(){
            List<Category> categories = categoryService.getAll();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(res.responseBody("OK", "FOUND", categories));
        }
    @GetMapping("/{id}")
    public ResponseEntity getOne(@PathVariable UUID id){
            Category category = categoryService.getOne(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(res.responseBody("OK","FOUND",category));
    }
    @GetMapping("/store/{storeId}")
    public ResponseEntity getAllByStore(@PathVariable UUID storeId){
            List<Category> categories = categoryService.getAllByStore(storeId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(res.responseBody("OK","FOUND",categories));
    }
    @PostMapping("/{storeId}")
    public ResponseEntity create(@RequestBody Category category,@PathVariable UUID storeId){
            Category category1 = categoryService.add(category,storeId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(res.responseBody("CREATED","CREATION SUCCESSFULLY",category1));
    }
    @PatchMapping("/{id}")
    public ResponseEntity update(@RequestBody Category category,@PathVariable UUID id){
            Category category1 = categoryService.update(category,id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(res.responseBody("OK","UPDATE SUCCESSFULLY",category1));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id){
            categoryService.delete(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(res.responseBody("OK","DELETION SUCCESSFULLY"));
    }
}
