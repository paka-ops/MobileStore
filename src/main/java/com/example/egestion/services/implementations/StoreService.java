package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Store;
import com.example.egestion.repositories.StoreRepository;
import com.example.egestion.security.SecurityCheck;
import com.example.egestion.services.interfaces.IStore;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StoreService implements IStore {
    private final StoreRepository storeRepository;
    private final SecurityCheck securityCheck;

    public StoreService(StoreRepository storeRepository, SecurityCheck securityCheck) {
        this.storeRepository = storeRepository;
        this.securityCheck = securityCheck;
    }

    @Override
    public List<Store> getAll() throws AccessDeniedException, NotAuthenticatedException {
        this.securityCheck.hasRole("ROLE_EMPLOYER");
        return storeRepository.findAll();
    }

    @Override
    public Store getOne(UUID id) throws ElementNotFoundException, NotAuthorizedException, AccessDeniedException, NotAuthenticatedException {
        boolean isExist = storeRepository.existsById(id);
        if(!isExist) throw new ElementNotFoundException("elemet not found ");
        Optional<Store> st = storeRepository.findById(id);
        boolean isElementOfStore = this.securityCheck.isElementOfStore(st.get());
        if(!isElementOfStore) throw new NotAuthorizedException("not authorized");
        return st.get();
    }

    @Override
    public Store add(Store store) throws CreationFailedException, AccessDeniedException, NotAuthenticatedException {
        this.securityCheck.hasRole("ROLE_EMPLOYER");
        try{
            Store st =  storeRepository.save(store);
            return st;
        }catch(Exception e){
            throw new CreationFailedException("error during creation");
        }

    }

    @Override
    public Store update(Store store, UUID id) throws UpdateFailedException, AccessDeniedException, NotAuthenticatedException, ElementNotFoundException, NotAuthorizedException {
        this.securityCheck.hasRole("EMPLOYER");
        Optional<Store> st = storeRepository.findById(id);
        if(!st.isPresent()) throw new ElementNotFoundException("element not found ");
        boolean isElementOfStore = this.securityCheck.isElementOfStore(st.get());
        if(!isElementOfStore) throw new NotAuthorizedException("not authorized");
        store.setId(id);
        return storeRepository.save(store);

    }

    @Override
    public void delete(UUID id) throws ElementNotFoundException, AccessDeniedException, NotAuthenticatedException {
        this.securityCheck.hasRole("EMPLOYER");
        boolean isExist = storeRepository.existsById(id);
        if(!isExist) throw new ElementNotFoundException("element not found ");
        storeRepository.deleteById(id);
    }


}
