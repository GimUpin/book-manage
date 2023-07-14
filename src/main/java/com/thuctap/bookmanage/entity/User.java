package com.thuctap.bookmanage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue()
    private Long id_user;
    @NotBlank
    @Size(min=4,max=20,message = "Name should have at least 4 characters and no more than 20 characters!!!")
    private String name;
    @NotBlank
    @Size(min=6,max=20,message = "UserName should have at least 6 characters and no more than 20 characters!!!")
    private String password;
    private String new_pass;
    @Column(name = "token")
    private String token;
    private String gender;
    @NotBlank
    private String email;
    @NotBlank
    private int id_photo;
    @Column(name= "photo",nullable = true,  length = 128)
    private String photo = null;
    public int isEnable;

    public void setEnable(int enable) {
        isEnable = enable;
    }

    public String getNew_pass() {
        return new_pass;
    }

    public void setNew_pass(String new_pass) {
        this.new_pass = new_pass;
    }

    public int isEnable() {
        return isEnable;
    }


    public User(String name, String email, String password, String gender, String token, int id_photo, int isEnable){
        super();
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.email = email;
        this.token = token;
        this.id_photo = id_photo;
        this.isEnable = isEnable;
    }


    public Long getId_user() {
        return id_user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public int getId_photo() {
        return id_photo;
    }

    public void setId_photo(int id_photo) {
        this.id_photo = id_photo;
    }

    public String getPhoto() {
        if(photo == null)
            return "user-photos/default/user_default.png";
        return "/user-photos/" + getId_photo() + "/" + photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
