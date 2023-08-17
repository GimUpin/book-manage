package com.thuctap.bookmanage.repository;

import com.thuctap.bookmanage.entity.ListBook;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BookRepository extends JpaRepository<ListBook,Long> {
    @Query(value = "SELECT c FROM ListBook c where c.id_user = ?1", nativeQuery = true)
    List<ListBook> findByIDuser(Long id_user);

    @Query(value = "SELECT c FROM ListBook c where c.name_book = ?1", nativeQuery = true)
    ListBook findByName(String name);

    @Query(value = "SELECT c FROM ListBook c where c.id_list = ?1", nativeQuery = true)
    ListBook findByID(Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM ListBook c WHERE c.id_list=?1", nativeQuery = true)
    void deleteBookById(Long id);

    @Query(value = "SELECT c FROM ListBook c WHERE c.name_book like %?1% OR c.description like %?1%", nativeQuery = true)
    List<ListBook> findBook(String name);

    @Query(value = "SELECT c FROM ListBook c ORDER BY c.day,c.time DESC ", nativeQuery = true)
    List<ListBook> showAllBook();

    @Query(value = "SELECT c FROM ListBook c ORDER BY c.count_view DESC ", nativeQuery = true)
    List<ListBook> showHotBook();

}
