package com.thuctap.bookmanage.controller;

import com.thuctap.bookmanage.entity.ListBook;
import com.thuctap.bookmanage.entity.Type;
import com.thuctap.bookmanage.entity.User;
import com.thuctap.bookmanage.repository.BookRepository;
import com.thuctap.bookmanage.repository.TypeRepository;
import com.thuctap.bookmanage.repository.Type_BookRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Controller
public class BookController {
    @Autowired
    private final TypeRepository typeRepository;
    private final Type_BookRepository type_bookRepository;
    private final BookRepository list_bookRepository;

    @RequestMapping("/readmanga")
    public String readmanga( HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        List<ListBook> all_manga = list_bookRepository.showAllBook();
        session.setAttribute("id_manga",null);
        model.addAttribute("list_manga",all_manga);
        List<Type> list_type = typeRepository.showAllCategory();
        model.addAttribute("categories",list_type);
        if(session.getAttribute("id") != null){
            model.addAttribute("id",session.getAttribute("id").toString());
            return "listBook";
        }
        return "index";

    }
    @RequestMapping("/selectCategory")
    public String selectCategory(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        model.addAttribute("user",new User());
        String value = request.getParameter("id_category");
        List<Type> list_type = typeRepository.showAllCategory();
        System.out.println(list_type);
        model.addAttribute("categories",list_type);
        if(!value.equals("0")){
            List<Long> list_id = type_bookRepository.findByCategory(Long.parseLong(value));
            List<ListBook> list_book = new ArrayList<>();
            for(Long x:list_id){

                list_book.add(list_bookRepository.findByID(x));
            }
            model.addAttribute("list_book",list_book);
        } else {
            List<ListBook> list_book = list_bookRepository.showAllBook();
            model.addAttribute("list_book",list_book);
        }
        if(session.getAttribute("id") != null){
            model.addAttribute("id", session.getAttribute("id"));
            return "listBook";
        }
        return "index";
    }
}
