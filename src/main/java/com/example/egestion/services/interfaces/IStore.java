package com.example.egestion.services.interfaces;

import com.example.egestion.exceptions.CreationFailedException;
import com.example.egestion.exceptions.ElementNotFoundException;
import com.example.egestion.exceptions.UpdateFailedException;
import com.example.egestion.models.Store;

import java.util.List;
import java.util.UUID;

public interface IStore {
    List<Store> getAll();
    Store getOne(UUID id) throws ElementNotFoundException;
    Store add(Store store) throws CreationFailedException;
    Store update(Store store,UUID id) throws UpdateFailedException;
    void delete(UUID id) throws ElementNotFoundException;

}
