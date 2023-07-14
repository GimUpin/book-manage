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
    @Query("SELECT c FROM Chapter c WHERE c.id_chapter=?1 ORDER BY c.id_chapter")

    public List<Chapter> showAllChapterById(Long idBook);

    @Transactional
    @Modifying
    @Query("DELETE FROM Chapter c WHERE c.id_chapter=?1")
    public void deleteChapterById(Long idChapter);
}
