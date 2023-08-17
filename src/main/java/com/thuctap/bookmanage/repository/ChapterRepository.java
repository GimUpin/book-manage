package com.thuctap.bookmanage.repository;

import com.thuctap.bookmanage.entity.Chapter;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface ChapterRepository extends JpaRepository<Chapter,Long> {
    @Query(value = "SELECT c FROM Chapter c WHERE c.id_chapter=?1 ORDER BY c.id_chapter", nativeQuery = true)

    public List<Chapter> showAllChapterById(Long idBook);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Chapter c WHERE c.id_chapter=?1", nativeQuery = true)
    public void deleteChapterById(Long idChapter);
}
