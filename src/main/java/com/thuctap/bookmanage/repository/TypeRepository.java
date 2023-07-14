package com.thuctap.book.respository;

import com.thuctap.book.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<Type,Long> {
    @Query("SELECT c.type_name FROM Type c where c.id = ?1")
    String findNameByID(Long id);

    @Query("SELECT c FROM Type c")
    List<Type> showAllCategory();
}
