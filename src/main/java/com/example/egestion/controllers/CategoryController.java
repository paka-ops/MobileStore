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
        try{
            List<Category> categories = new ArrayList<>();
            categories = categoryService.getAll();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(res.responseBody("OK","FOUND",categories));
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        } catch (NotAuthenticatedException e) {
            throw new RuntimeException(e);
        } catch (ElementNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NotAuthorizedException e) {
            throw new RuntimeException(e);
        } catch (UpdateFailedException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity getOne(@PathVariable UUID id){
        try{
            Category category = categoryService.getOne(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(res.responseBody("OK","FOUND",category));
        } catch (NotAuthenticatedException e) {
            return res.NotAuthenticatedResponse(e);
        } catch (ElementNotFoundException e) {
            return res.ElementNotFoundResponse(e);
        } catch (NotAuthorizedException e) {
            return res.NotAuthorizedResponse(e);
        }
    }
    @GetMapping("/store/{storeId}")
    public ResponseEntity getAllByStore(@PathVariable UUID storeId){
        try{
            List<Category> categories = categoryService.getAllByStore(storeId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(res.responseBody("OK","FOUND",categories));
        } catch (AccessDeniedException e) {
            return res.AccessDeniedResponse(e);
        } catch (NotAuthenticatedException e) {
            return res.NotAuthenticatedResponse(e);
        } catch (ElementNotFoundException e) {
            return res.ElementNotFoundResponse(e);
        } catch (NotAuthorizedException e) {
            return res.NotAuthorizedResponse(e);
        }
    }
    @PostMapping("/{storeId}")
    public ResponseEntity create(@RequestBody Category category,@PathVariable UUID storeId){
        try{
            Category category1 = categoryService.add(category,storeId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(res.responseBody("CREATED","CREATION SUCCESSFULLY",category1));
        } catch (AccessDeniedException e) {
           return res.AccessDeniedResponse(e);
        } catch (NotAuthenticatedException e) {
            return res.NotAuthenticatedResponse(e);
        } catch (CreationFailedException e) {
            return res.CreationFailedResponse(e);
        } catch (ElementNotFoundException e) {
            return res.ElementNotFoundResponse(e);
        } catch (NotAuthorizedException e) {
            return res.NotAuthorizedResponse(e);
        } catch (UpdateFailedException e) {
            return res.UpdateFailedResponse(e);
        }
    }
    @PatchMapping("/{id}")
    public ResponseEntity update(@RequestBody Category category,@PathVariable UUID id){
        try {
            Category category1 = categoryService.update(category,id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(res.responseBody("OK","UPDATE SUCCESSFULLY",category1));
        } catch (UpdateFailedException e) {
            return res.UpdateFailedResponse(e);
        } catch (NotAuthenticatedException e) {
            return res.NotAuthenticatedResponse(e);
        } catch (AccessDeniedException e) {
            return res.AccessDeniedResponse(e);
        } catch (NotAuthorizedException e) {
            return res.NotAuthorizedResponse(e);
        } catch (ElementNotFoundException e) {
            return res.ElementNotFoundResponse(e);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id){
        try{
            categoryService.delete(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(res.responseBody("OK","DELETION SUCCESSFULLY"));
        } catch (AccessDeniedException e) {
            return res.AccessDeniedResponse(e);
        } catch (NotAuthenticatedException e) {
            return res.NotAuthenticatedResponse(e);
        } catch (ElementNotFoundException e) {
            return res.ElementNotFoundResponse(e);
        } catch (NotAuthorizedException e) {
            return res.NotAuthorizedResponse(e);
        }
    }

}
