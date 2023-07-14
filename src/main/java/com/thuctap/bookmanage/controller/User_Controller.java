package com.thuctap.bookmanage.controller;

import com.thuctap.book.service.LoginService;
import com.thuctap.book.service.TypeService;
import com.thuctap.book.service.UserNotFoundException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

@Controller
public class User_Controller {
    @Autowired
    private LoginService userService;

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private TypeService typeService;

    @RequestMapping("homepage")
    public String HomePage(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        if (session.getAttribute("id") == null){
            model.addAttribute("message","You need login to user function!!!");
            model.addAttribute("user",new User());
            return "login";
        }
        session.setAttribute("id_book",null);
        User user = userService.findUser(Long.parseLong(session.getAttribute("id").toString()));
        model.addAttribute("user",user);
        model.addAttribute("id",session.getAttribute("id"));
        return "homepage";
    }

    @RequestMapping("/")
    public String notLogin(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        model.addAttribute("list_book",bookService.showAllBook());
        List<Type> list_type = typeService.showAllCategory();
        model.addAttribute("categories",list_type);
        if(session.getAttribute("id")!=null){
            return HomePage(request,model);
        }else {
            return "index";
        }
    }
    @RequestMapping("/pagelogin")
    public String view_login(User user, HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        if(session.getAttribute("id") != null){
            return HomePage(request,model);
        }
        return "login";
    }

    @PostMapping(value = {"/login"})
    public String login(@ModelAttribute("user") User user , Model model, HttpServletRequest request){
        User authUser = userService.login(user.getUsername(), user.getPassword());
        HttpSession session = request.getSession();
        if (Objects.nonNull(authUser)) {
            model.addAttribute("id",String.valueOf(authUser.getId_user()));
            session.setAttribute("id",String.valueOf(authUser.getId_user()));
            model.addAttribute("user", authUser);
        } else {
            model.addAttribute("message", "The username or password is incorrect!");
            return "/login";
        }
        return "homepage";
    }
    @RequestMapping(value = "change_password")
    public String changePassword(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        String x = session.getAttribute("id").toString();
        System.out.println("id user: " + x);
        User user = userService.findUser(Long.parseLong(x));
        model.addAttribute("id", String.valueOf(x));
        model.addAttribute("user",user);
        return "change_password";
    }

    @PostMapping(value = "/change_your_password")
    public String processChangePassword(@ModelAttribute("id") String id, Model model, HttpServletRequest request) {
        model.addAttribute("id",id);
        Long userId = Long.parseLong(id);
        String password = request.getParameter("password");
        String token = RandomString.make(30);
        User user = userService.findUser(userId);
        System.out.println(user.toString());
        model.addAttribute("user",user);
        try {
            userService.set_new_password(user,password);
            userService.updateResetPasswordToken(token,user.getEmail());
            model.addAttribute("user",user);
            String changePasswordLink = Utility.getSiteURL(request) + "/changepasswordSuccess?token=" + token;
            sendEmail(user.getEmail(),changePasswordLink);

            model.addAttribute("message","Please check mail to confirm change password!!!");

        } catch (MessagingException | UnsupportedEncodingException | UserNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "login";
    }

    @GetMapping("/changepasswordSuccess")
    public String ChangePasswordSuccess(@Param(value = "token") String token, Model model) throws UserNotFoundException {
        User user = userService.get(token);
        if(user != null){
            userService.updatePassword(user,user.getNew_pass());
        }else{
            model.addAttribute("title","Reset your password!");
            model.addAttribute("message","Invalid token");
        }
        return "changepasswordSuccess";
    }

    @RequestMapping("/register")
    public String register(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "register";
    }
    @PostMapping(value = "/saveUser",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String Success(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("user",new User());
            return "register";
        }
        if(Objects.nonNull(userService.checkUser(user.getUsername()))) {
            model.addAttribute("error", "Username already exists!");
        }else if(Objects.nonNull((userService.checkEmail(user.getEmail())))){
            model.addAttribute("error", " Email already exists!");
        }else {
            user.setNew_pass(user.getPassword());
            user.setPhoto("");
//            userRepo.save(user);
            return "/login";
        }
        return "/register";
    }

    @GetMapping("/forgot_password")
    public String ForgotPasswordForm(Model model){
        model.addAttribute("pageTitle","ForgotPassword");
        return "Forgot_Password_Form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPasswordForm(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        String token = RandomString.make(30);
        try {
            userService.updateResetPasswordToken(token,email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email,resetPasswordLink);
            model.addAttribute("message","Please check mail to confirm change password!!!");
        } catch (UserNotFoundException | MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "Forgot_Password_Form";
    }

    public void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("contact@test.com","Support");
        helper.setTo(email);
        String subject = "Here the link: ";
        String content = "<p>Hello. Please Confirm Change Password. By clicking on the link your password will change.</p>"
                + "<a href=\""+resetPasswordLink+"\">Change your password</a>";
        helper.setSubject(subject);
        helper.setText(content,true);

        sender.send(message);
    }
}
