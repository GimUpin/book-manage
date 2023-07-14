package com.thuctap.book.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_book;
    private Long id_chapter;
    private String link;
    private LocalDate day;
    private LocalTime time;

    public String getLink(String id_book){
        return "/book/"+ id_book + "/" + this.id_chapter + "/" + this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
