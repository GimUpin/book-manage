package com.thuctap.bookmanage.service;

import com.thuctap.bookmanage.dao.request.SigninRequest;
import com.thuctap.bookmanage.dao.request.SignupRequest;

public interface AuthenticationService {
    void signup(SignupRequest request);

    void signin(SigninRequest request);

    void logOut();

}
