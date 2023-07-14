package com.thuctap.bookmanage.controller;

import com.thuctap.bookmanage.entity.Type;
import com.thuctap.bookmanage.entity.User;
import com.thuctap.bookmanage.photos.userID.FileUploadUtil;
import com.thuctap.bookmanage.repository.TypeRepository;
import com.thuctap.bookmanage.repository.UserRepository;
import com.thuctap.bookmanage.service.UserService;
import com.thuctap.bookmanage.service.UserNotFoundException;
import com.thuctap.bookmanage.entity.UserRegistration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Controller
public class User_Controller {

    private final UserService userService;
    private final UserRepository userRepository;
    private final TypeRepository typeRepository;


    @RequestMapping("homepage")
    public String HomePage(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        if(session.getAttribute("id") == null){
            model.addAttribute("message","You need login to user function!!!");
            model.addAttribute("user",new User());
            return "login";
        }
        User user = userService.findUser(Long.parseLong(session.getAttribute("id").toString()));
        System.out.println(user);
        System.out.println(session.getAttribute("id"));
        model.addAttribute("user",user);
        model.addAttribute("id",session.getAttribute("id"));
        return "homepage";
    }
    @RequestMapping("/")
    public String notLogin(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        List<Type> list_type = typeRepository.showAllCategory();
        model.addAttribute("categories",list_type);
        if(session.getAttribute("id")!=null){
            return HomePage(request,model);
        }else {
            model.addAttribute("user",new User());
            return "index";
        }
    }
    @RequestMapping("/login")
    public String view_login(User user, HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        if(session.getAttribute("id") != null){
            return HomePage(request,model);
        }
        return "login";
    }

    @PostMapping(value = {"/login"})
    public String login(@ModelAttribute("user") User user , Model model, HttpServletRequest request){
        User oauthUser = userService.login(user.getEmail(), user.getPassword());
        HttpSession session = request.getSession();
        if (Objects.nonNull(oauthUser)) {
            if(oauthUser.isEnable() == 1) {
                model.addAttribute("id", String.valueOf(oauthUser.getId_user()));
                session.setAttribute("id", String.valueOf(oauthUser.getId_user()));
                model.addAttribute("user", oauthUser);
                return "homepage";
            } else {
                model.addAttribute("message", "The username or password is incorrect!");
                return "login";
            }
        }
        else {
            model.addAttribute("message", "The username is disable!");
        }
        return "login";

    }
    @RequestMapping(value = "/change_password")
    public String changePassword(HttpServletRequest request, Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "change_password";
    }
    @PostMapping(value = "/change_your_password")
    public String processChangePassword(@Valid @ModelAttribute("user") User user, BindingResult result, Model model,HttpServletRequest request){
        String newPass = request.getParameter("password");
        String email = request.getParameter("email");
        String oldPass = request.getParameter("old_password");
        userService.changePass(user,email,oldPass,newPass,request);
        model.addAttribute("message", "Check your email to confirm account!");
        return "change_password";
    }
    @GetMapping("change_passwordSuccess")
    public String ChangePasswordSuccess(@Param(value = "token") String token, Model model) throws UserNotFoundException {
        User user = userService.get(token);
        if(user != null){
            userService.updatePassword(user,user.getNew_pass());
            user.setNew_pass("");
            userRepository.save(user);
        }else{
            model.addAttribute("title","Reset your password!");
            model.addAttribute("message","Invalid token");
        }
        return "change_passwordSuccess";
    }
    @RequestMapping("/register")
    public String register(Model model){
        UserRegistration user = new UserRegistration();
        model.addAttribute("user",user);
        return "register";
    }
    @PostMapping(value = "/registration",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String success(@Valid @ModelAttribute("user") UserRegistration user, BindingResult result, Model model,HttpServletRequest request) throws IllegalAccessException {
        if(result.hasErrors()){
            model.addAttribute("user",new UserRegistration());
            return "register";
        }
        if(Objects.nonNull(userService.checkEmail(user.getEmail()))) {
            return "redirect:/register?error_email";
        }else{
            userService.save(user,request);
            model.addAttribute("message", "Check your email to confirm account!");
        }
        return "register";
    }
    @GetMapping("/confirm_account")
    public String confirmRegistration(@Param(value = "token") String token, Model model){
        User user = userService.get(token);
        if(Objects.nonNull(user)){
            user.setEnable(1);
            user.setToken(null);
            userRepository.save(user);

        }
        return "confirm_account";
    }
    @GetMapping("/forgot_password")
    public String ForgotPasswordForm(Model model){
        model.addAttribute("pageTitle","ForgotPassword");
        return "Forgot_Password_Form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPasswordForm(@Valid @ModelAttribute("user") User user,HttpServletRequest request, Model model) throws UserNotFoundException {
        String email = request.getParameter("email");
        userService.forgotPass(user,email,request);
        model.addAttribute("message","Please check mail to confirm change password!!!");

        return "Forgot_Password_Form";
    }
    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model){
        User user = userService.get(token);
        if(!Objects.nonNull(user)){
            model.addAttribute("title","Reset your password!");
            model.addAttribute("message","Invalid token");
            return "message";
        }
        model.addAttribute("token",token);
        model.addAttribute("pageTitle","Reset your password!");
        return "reset_password_form";
    }
    @PostMapping("reset_password")
    public String processResetPassword(HttpServletRequest request, Model model){
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        User user = userService.get(token);
        if(!Objects.nonNull(user)){
            model.addAttribute("title","Reset your password!");
            model.addAttribute("message","Invalid token");
        }else {
            userService.updatePassword(user,password);
            return "change_passwordSuccess";
        }
        return "message";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request,Model model)
    {
        HttpSession session = request.getSession();
        session.removeAttribute("id");
        model.addAttribute("id",null);
        model.addAttribute("user",new User());
        return "/index";
    }

    @RequestMapping(value="uploadphotos")
    public String pageUpload(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        String id = session.getAttribute("id").toString();
        User user = userService.findUser(Long.parseLong(id));
        model.addAttribute("user", user);
        return "uploadphotos";
    }

    @PostMapping("/savepicture")
    public String saveFiles (HttpServletRequest request, @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        HttpSession session = request.getSession();

        if(session.getAttribute("id") == null){
            return HomePage(request,model);
        }
        Long id = Long.parseLong(session.getAttribute("id").toString());
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        User user = userService.findUser(id);
        model.addAttribute("user",user);
        if(!fileName.isEmpty()){
            user.setPhoto(fileName);
            userRepository.save(user);
            String uploadDir = "user-photos/" + user.getId_user();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            model.addAttribute("message", "Change avatar success!!!");
            return "homepage";
        } else {
            model.addAttribute("message", "Avatar is empty!!! Couldn't change:( !");
            return "uploadphotos";
        }
    }

}