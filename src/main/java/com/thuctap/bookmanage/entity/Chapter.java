package com.thuctap.bookmanage.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chapter")

public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_chapter;
    private Long id_book;
    private int number;
    private String content;
    private int count_chapter;
    private Long preChap;
    private Long nextChap;
    private LocalDate day;
    private LocalTime time;

}
