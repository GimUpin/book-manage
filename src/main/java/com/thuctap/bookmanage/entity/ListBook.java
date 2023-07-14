package com.thuctap.bookmanage.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
@NoArgsConstructor
@Data
@Entity
@Table(name = "listbook")
public class ListBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_list;
    private Long id_user;
    private String name_book;
    private String description;
    private int count_chapter;
    private int count_view;
    private String image;
    private LocalTime time;
    private LocalDate day;

    public ListBook(Long id_user, String name, int count_chapter, String image, String content, LocalDate day, LocalTime time) {
        this.id_user = id_user;
        this.name_book = name;
        this.count_chapter=count_chapter;
        this.image = image;
        this.description = content;
        this.day = day;
        this.time = time;
    }
}
