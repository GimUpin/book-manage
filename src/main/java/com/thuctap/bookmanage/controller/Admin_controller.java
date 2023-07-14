package com.thuctap.bookmanage.controller;

import com.thuctap.bookmanage.entity.User;
import com.thuctap.bookmanage.repository.UserRepository;
import com.thuctap.bookmanage.service.AdminService;
import com.thuctap.bookmanage.service.ChapterService;
import com.thuctap.bookmanage.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@AllArgsConstructor
@Controller
public class Admin_controller {
    @Autowired
    private final AdminService adminService;

    @Autowired
    private final ChapterService chapterService;

    @Autowired
    private final UserService userService;
    @Autowired
    private final UserRepository userRepository;

    @RequestMapping(value = "Admin_user")
    public String Admin_user(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        if (session.getAttribute("id") == null){
            model.addAttribute("user",new User());
            return "login";
        }
        model.addAttribute("list_user",adminService.showAllUser());
        model.addAttribute("id",session.getAttribute("id"));
        return "Admin_user";
    }


    @PostMapping(value = "delete_user")
    public String deleteUser(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        if (session.getAttribute("id") == null){
            model.addAttribute("user",new User());
            return "login";
        }
        String id = session.getAttribute("id").toString();
        User admin = adminService.findUserById(Long.parseLong(id));
        User user = adminService.findUserById(Long.parseLong(request.getParameter("id_user")));
        String currentDirectory = System.getProperty("user.dir");
        if(admin.getId_photo() > user.getId_photo()){
            adminService.deleteUserByAdmin(user.getId_user());
        }
        model.addAttribute("list_user",adminService.showAllUser());
        model.addAttribute("id",session.getAttribute("id"));
        return "Admin_user";
    }

    @PostMapping(value = "uplevel")
    public String upPower(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            model.addAttribute("user",new User());
            return "login";
        }
        String id = session.getAttribute("id").toString();

        User admin = userService.findUser(Long.parseLong(id));
        User user = userService.findUser(Long.parseLong(request.getParameter("id_user")));
        if(user.getId_photo() <= 2 && admin.getId_photo() > (user.getId_photo()+1)){
            user.setId_photo(user.getId_photo()+1);
            userRepository.save(user);
        }
        List<User> allUser = userRepository.showAllUser();
        model.addAttribute("list_user",allUser);
        model.addAttribute("id",session.getAttribute("id"));
        return "Admin_user";
    }

    @RequestMapping(value = "downlevel")
    public String downPower(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            model.addAttribute("user",new User());
            return "login";
        }
        String id = session.getAttribute("id").toString();
        User admin = userService.findUser(Long.parseLong(id));
        User user = userService.findUser(Long.parseLong(request.getParameter("id_user")));
        if(admin.getId_photo() > user.getId_photo() && admin.getId_photo() >= 0){
            user.setId_photo((user.getId_photo() - 1));
            userRepository.save(user);
        }
        List<User> allUser = userRepository.showAllUser();
        model.addAttribute("list_user",allUser);
        model.addAttribute("id",session.getAttribute("id"));
        return "Admin_user";
    }
}
