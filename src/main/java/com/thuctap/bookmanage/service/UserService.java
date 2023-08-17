package com.thuctap.bookmanage.service;

import com.thuctap.bookmanage.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService setupUserDetailsService();
    public User login(String email, String password);
    public User checkEmail(String email);
    public User findUser(long id);

    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException;
    public User get(String resetPasswordToken);

    public void updatePassword(User user, String newPassword);
    public void set_new_password(User user, String newPassword);
    public void save(User user);

    public User changePass(User user,String email ,String oldpass,String newpass, HttpServletRequest request);
    public User forgotPass(User user,String email,HttpServletRequest request) throws UserNotFoundException;

}
