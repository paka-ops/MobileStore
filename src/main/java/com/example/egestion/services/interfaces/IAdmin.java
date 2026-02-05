package com.example.egestion.services.interfaces;

import com.example.egestion.exceptions.ElementNotFoundException;
import com.example.egestion.models.Admin;

import java.util.List;
import java.util.UUID;

public interface IAdmin {
    Admin create(Admin admin);
    void delete(UUID id);
    Admin update(Admin admin,UUID id);
    List<Admin> getAll();
    Admin getOne(UUID id) throws ElementNotFoundException;
}
