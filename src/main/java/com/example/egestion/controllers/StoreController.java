package com.example.egestion.controllers;

import com.example.egestion.configuration.ResponseBuilder;
import com.example.egestion.exceptions.*;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Store;
import com.example.egestion.services.implementations.StoreService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/api/v1/stores")
public class StoreController {
    private final StoreService storeService;
    private final ResponseBuilder res;
    public StoreController(StoreService storeService, ResponseBuilder res) {
        this.storeService = storeService;
        this.res = res;
    }
    @GetMapping(produces = "application/json")
    public ResponseEntity getAll(){
        List<Store> stores = storeService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK","FOUND",stores));
    }
    @GetMapping("/{id}")
    public ResponseEntity getOne(@PathVariable UUID id){
        Store store = storeService.getOne(id);
        return ResponseEntity.status(HttpServletResponse.SC_OK)
                .body(res.responseBody("OK","FOUND",store));
    }
    @PostMapping
    public ResponseEntity create(@RequestBody Store store){
        Store store1 =  storeService.add(store);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(res.responseBody("CREATED","CREATION SUCCESSFULLY",store));
    }
    @PatchMapping("/{id}")
    public  ResponseEntity update(@RequestBody Store store,@PathVariable UUID id){
        Store store1 = storeService.update(store, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK","UPDATE SUCCESSFULLY",store1));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id){
        storeService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK","DELETED SUCCESSFULLY"));
    }
    @PostMapping("/{storeId}/{employeeId}")
    public ResponseEntity addEmployee(@PathVariable UUID storeId,@PathVariable UUID employeeId){
        Store store = storeService.addEmployee(storeId,employeeId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK","EMPLOYEE ADDING SUCCESSFULLY",store));
    }


}
