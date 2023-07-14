package com.thuctap.book.respository;

import com.thuctap.book.entity.Type_book;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface Type_BookRepository extends JpaRepository<Type_book,Long> {
    @Query("SELECT c.id_book FROM Type_book c WHERE c.id_type =?1")
    List<Long> findByCategory(Long id);
    @Transactional
    @Modifying
    @Query("DELETE FROM Type_book c WHERE c.id_book=?1")
    void deleteByIdBook(Long id_book);
}
