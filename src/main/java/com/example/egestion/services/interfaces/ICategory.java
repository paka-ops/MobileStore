package com.example.egestion.services.interfaces;

import com.example.egestion.exceptions.CreationFailedException;
import com.example.egestion.exceptions.ElementNotFoundException;
import com.example.egestion.exceptions.UpdateFailedException;
import com.example.egestion.models.Category;

import java.util.List;
import java.util.UUID;

public interface ICategory {
    Category add(Category category) throws CreationFailedException;
    Category update(Category category, UUID id) throws UpdateFailedException;
    void  delete(UUID categoryId);
    List<Category> getAll();
    Category getOne(UUID uuid) throws ElementNotFoundException;
}
