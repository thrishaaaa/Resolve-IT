package com.legal_system.mediation.Service;

import com.legal_system.mediation.model.Mediators;
import com.legal_system.mediation.repository.MediatorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MediatorsService {

    @Autowired
    private MediatorsRepository mediatorsRepository;

    // Register new mediator
    public void addMediators(Mediators mediators) {
        Optional<Mediators> existing = mediatorsRepository.findByEmail(mediators.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("Email already registered!");
        }

        if (!mediators.getPassword().equals(mediators.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match!");
        }

        mediators.setStatus("Active");
        mediators.setIs_verified(false);
        mediatorsRepository.save(mediators);
    }

    public List<Mediators> getAllMediators() {
        return mediatorsRepository.findAll();
    }

    public Mediators findMediator(int id) {
        Optional<Mediators> theMediator = mediatorsRepository.findById(id);
        return theMediator.orElse(null);
    }

    // Login validation
    public Mediators authenticate(String email, String password) {
        Optional<Mediators> mediator = mediatorsRepository.findByEmail(email);
        if (mediator.isPresent() && mediator.get().getPassword().equals(password)) {
            return mediator.get();
        }
        return null;
    }

    // Find by email
    public Optional<Mediators> findByEmail(String email) {
        return mediatorsRepository.findByEmail(email);
    }
}
