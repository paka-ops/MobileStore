package com.example.egestion.services.interfaces;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Store;

import java.util.List;
import java.util.UUID;

public interface IStore {
    List<Store> getAll() throws AccessDeniedException, NotAuthenticatedException;
    Store getOne(UUID id) throws ElementNotFoundException, AccessDeniedException, NotAuthenticatedException, NotAuthorizedException;
    Store add(Store store) throws CreationFailedException, AccessDeniedException, NotAuthenticatedException;
    Store update(Store store,UUID id) throws UpdateFailedException, AccessDeniedException, NotAuthenticatedException, ElementNotFoundException, NotAuthorizedException;
    void delete(UUID id) throws ElementNotFoundException, AccessDeniedException, NotAuthenticatedException;


}
