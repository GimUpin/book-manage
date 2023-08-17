package com.thuctap.bookmanage.service.serviceImpl;

import com.thuctap.bookmanage.dao.request.SigninRequest;
import com.thuctap.bookmanage.dao.request.SignupRequest;
import com.thuctap.bookmanage.entity.Role;
import com.thuctap.bookmanage.entity.User;
import com.thuctap.bookmanage.repository.UserRepository;
import com.thuctap.bookmanage.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repo;
    @Override
    public void signup(SignupRequest request) {
        User user = User.builder()
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .gender(request.getGender())
                .role(Role.USER)
                .build();
        repo.save(user);
    }

    @Override
    public void signin(SigninRequest request) {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    }

    @Override
    public void logOut() {
        SecurityContextHolder.clearContext();
    }
}
