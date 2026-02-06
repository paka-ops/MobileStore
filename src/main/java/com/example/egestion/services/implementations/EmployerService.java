package com.example.egestion.services.implementations;
import com.example.egestion.exceptions.*;
import com.example.egestion.models.Employer;
import com.example.egestion.repositories.EmployerRepository;
import com.example.egestion.services.interfaces.IEmployer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class EmployerService implements IEmployer{
   private final EmployerRepository employerRepository;

    public EmployerService(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;

    }

    @Override
    @PreAuthorize("ADMIN")
    public Employer create(Employer employer) throws CreationFailedException {
        try{
          Employer em =   employerRepository.save(employer);
          return em;
        }catch(Exception e){
            throw new CreationFailedException("creation failed");
        }
    }

    @Override
    @PreAuthorize("ADMIN")
    public Employer update(Employer employer, UUID id) throws ElementNotFoundException {
        boolean idExists = employerRepository.existsById(id);
        if(!idExists) throw new ElementNotFoundException("element not found: Employer not found");
        employer.setId(id);
        return employerRepository.save(employer);
    }

    @Override
    @PreAuthorize("ADMIN")
    public void delete(UUID id) throws ElementNotFoundException {
        boolean idExists = employerRepository.existsById(id);
        if(!idExists) throw new ElementNotFoundException("element not found: Employer not found");
        employerRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("ADMIN")
    public List<Employer> getAll() {
        return employerRepository.findAll();
    }

    @Override
    @PreAuthorize("ADMIN")
    public Employer getOne(UUID id) throws ElementNotFoundException {
        Optional<Employer> employer  = employerRepository.findById(id) ;
        if(employer.isEmpty()) throw new ElementNotFoundException("element not found: employer not found");
        return employer.get();
    }

   
}
