package com.thuctap.bookmanage.repository;

import com.thuctap.bookmanage.entity.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<Book, Long> {
    @Query(value = "SELECT c FROM Book c WHERE c.id_chapter =?1 order by c.id_book", nativeQuery = true)
    public List<Book> showBook(Long idChapter);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Book c WHERE c.id_book=?1", nativeQuery = true)
    void deleteOnePageById(Long idPage);
}
