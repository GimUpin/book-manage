package com.thuctap.bookmanage.service;

import com.thuctap.bookmanage.controller.Utility;
import com.thuctap.bookmanage.entity.User;
import com.thuctap.bookmanage.entity.UserRegistration;
import com.thuctap.bookmanage.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final EmailService emailSender;

    public User login(String email, String password){
        return userRepository.findByUsernameAndPassword(email,password);
    }
    public User checkEmail(String email){
        return userRepository.findByEmail(email);
    }
    public User findUser(long id) {
        return userRepository.findByID(id);
    }

    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user != null){
            user.setToken(token);
            userRepository.save(user);
        }else throw new UserNotFoundException("Could not find any user " + email);
    }
    public User get(String resetPasswordToken){
        return userRepository.findByToken(resetPasswordToken);
    }

    public void updatePassword(User user, String newPassword){
        user.setPassword(newPassword);
        user.setToken(null);
        userRepository.save(user);
    }
    public void set_new_password(User user, String newPassword){
        user.setNew_pass(newPassword);
        userRepository.save(user);
    }
    public User save(UserRegistration user_registration, HttpServletRequest request) throws IllegalAccessException {
        String token = UUID.randomUUID().toString();
        User user = new User(user_registration.getName(),
                                user_registration.getEmail(),
                                user_registration.getPassword(),
                                user_registration.getGender(),
                                token,
                                0,
                                user_registration.getPhoto(),
                                0
                                );
        String link = Utility.getSiteURL(request) + "/confirm_account?token=" + user.getToken();
        sendEmail(user, sendVerificationEmail(link));
        userRepository.save(user);
        return user;
    }

    public User changePass(User user,String email ,String oldpass,String newpass, HttpServletRequest request){
        String token = UUID.randomUUID().toString();
        user = userRepository.findByEmail(email);
        if(user.getPassword().equals(oldpass)){
            user.setNew_pass(newpass);
            user.setToken(token);
            String link = Utility.getSiteURL(request) + "/change_passwordSuccess?token=" + user.getToken();
            sendEmail(user, sendChangePassEmail(link));
            userRepository.save(user);
        }
        return user;
    }
    public User forgotPass(User user,String email,HttpServletRequest request) throws UserNotFoundException {
        String token = UUID.randomUUID().toString();
        user = userRepository.findByEmail(email);
        updateResetPasswordToken(token,email);
        String link = Utility.getSiteURL(request) + "/reset_password?token=" + user.getToken();
        sendEmail(user, sendResetPassEmail(link));
        userRepository.save(user);
        return user;
    }

    private void sendEmail(User user, String link) {
        try {
            emailSender.send(user.getEmail(),
                    link);
            userRepository.save(user);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }





    public String sendVerificationEmail(String link){
        return  "<p>Hello. Please Confirm Account. By clicking on the link your account will enable.</p>"
                + "<a href=\""+link+"\">Active Account</a>";

    }

    public String sendChangePassEmail(String link){
        return  "<p>Hello. Please Confirm Change Password. By clicking on the link your password will change.</p>"
                + "<a href=\""+link+"\">Change your password</a>";

    }
    public String sendResetPassEmail(String link){
        return  "<p>Hello. Please Confirm Reset Password. By clicking on the link your password will reset.</p>"
                + "<a href=\""+link+"\">Reset your password</a>";

    }


}
