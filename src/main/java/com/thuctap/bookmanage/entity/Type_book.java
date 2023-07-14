package com.thuctap.bookmanage.entity;

import lombok.*;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "type_book")

public class Type_book {
    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    private long id;
    private long  id_book;
    private  long id_type;
}
