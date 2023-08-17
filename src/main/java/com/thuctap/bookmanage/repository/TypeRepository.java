package com.thuctap.bookmanage.repository;

import com.thuctap.bookmanage.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<Type,Long> {
    @Query(value = "SELECT c.type_name FROM Type c where c.id = ?1", nativeQuery = true)
    String findNameByID(Long id);

    @Query(value = "SELECT c FROM Type c", nativeQuery = true)
    List<Type> showAllCategory();
}
