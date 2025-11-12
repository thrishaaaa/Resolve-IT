package com.legal_system.mediation.Service;
import com.legal_system.mediation.model.UserDetails;
import com.legal_system.mediation.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDetailsRepository userRepository;

    public void saveUser(UserDetails userDetails) {
        userRepository.save(userDetails);
    }

    public Optional<UserDetails> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}