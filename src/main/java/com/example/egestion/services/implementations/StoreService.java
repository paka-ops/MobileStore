package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Store;
import com.example.egestion.repositories.StoreRepository;
import com.example.egestion.security.SecCheck;
import com.example.egestion.services.interfaces.IStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class StoreService implements IStore { private final StoreRepository storeRepository;
    private final SecCheck securityCheck;

    public StoreService(StoreRepository storeRepository, SecCheck securityCheck) {
        this.storeRepository = storeRepository;
        this.securityCheck = securityCheck;
    }

    @Override
    public List<Store> getAll() throws AccessDeniedException, NotAuthenticatedException {

        return storeRepository.findAll();
    }

    @Override
    public Store getOne(UUID id) throws ElementNotFoundException, NotAuthorizedException, AccessDeniedException, NotAuthenticatedException {
        boolean isExist = storeRepository.existsById(id);
        if(!isExist) throw new ElementNotFoundException("elemet not found ");
        Optional<Store> st = storeRepository.findById(id);
        boolean isMemberOfStore = this.securityCheck.isMemberOfStore(st.get());
        if(!isMemberOfStore) throw new NotAuthorizedException("not authorized");
        return st.get();
    }

    @Override
    public Store add(Store store) throws CreationFailedException, AccessDeniedException, NotAuthenticatedException, NotAuthorizedException {
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
        boolean isMemberOfStore = this.securityCheck.isMemberOfStore(st.get());
        if(!isMemberOfStore) throw new NotAuthorizedException("not authorized");
        store.setId(id);
        return storeRepository.save(store);

    }

    @Override
    public void delete(UUID id) throws ElementNotFoundException, AccessDeniedException, NotAuthenticatedException, NotAuthorizedException {
        this.securityCheck.hasRole("EMPLOYER");
        boolean isExist = storeRepository.existsById(id);
        if(!isExist) throw new ElementNotFoundException("element not found ");
        storeRepository.deleteById(id);
    }



}
