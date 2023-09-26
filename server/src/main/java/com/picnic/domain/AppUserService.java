package com.picnic.domain;

import com.picnic.data.UserRepository;
import com.picnic.model.AppRole;
import com.picnic.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService implements UserDetailsService {

    PasswordEncoder encoder;

    UserRepository repository;
    @Autowired
    public AppUserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.encoder = passwordEncoder;
        this.repository = repository;
    }

    public Result<AppUser> addUser(String username, String password) {
        Result<AppUser> result = validate(username, password);
        if (!result.isSuccess()) {
            return result;
        }

        password = encoder.encode(password);

        AppUser appUser = new AppUser(username, password, true, List.of("USER"));

        try {
            result.setPayload(repository.save(appUser));
            result.setPayload(appUser);
        } catch (DuplicateKeyException e) {
            result.addMessage("The provided username already exists", ResultType.INVALID);
        }

        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository.findByUsername(username);

        if (appUser == null || !appUser.isEnabled()) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return appUser;
    }

    private Result<AppUser> validate(String username, String password) {
        Result<AppUser> result = new Result<>();
        if (username == null || username.isBlank()) {
            result.addMessage("username is required", ResultType.INVALID);
            return result;
        }

        if (password == null) {
            result.addMessage("password is required", ResultType.INVALID);
            return result;
        }

        if (username.length() > 50) {
            result.addMessage("username must be less than 50 characters", ResultType.INVALID);
        }

        if (!isValidPassword(password)) {
            result.addMessage("password must be at least 8 character and contain a digit," +
                            " a letter, and a non-digit/non-letter",ResultType.INVALID);
        }

        return result;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        return digits > 0 && letters > 0 && others > 0;
    }


}
