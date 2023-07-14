package com.thuctap.bookmanage.service;

import com.thuctap.bookmanage.entity.Book;
import com.thuctap.bookmanage.entity.Chapter;
import com.thuctap.bookmanage.entity.ListBook;
import com.thuctap.bookmanage.repository.BookRepository;
import com.thuctap.bookmanage.repository.ChapterRepository;
import com.thuctap.bookmanage.repository.PictureRepository;
import com.thuctap.bookmanage.repository.Type_BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BookService {
    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final ChapterRepository chapterRepository;

    @Autowired
    private final Type_BookRepository type_bookRepository;

    @Autowired
    private final PictureRepository pictureRepository;

    public List<ListBook> showAllBook() {
        return bookRepository.showAllBook();
    }

    public ListBook findByName(String name) {
        return bookRepository.findByName(name);
    }

    public ListBook findById(Long id) {
        return bookRepository.findByID(id);
    }

    public List<ListBook> findBook(String name) {
        return bookRepository.findBook(name);
    }

    public List<ListBook> findByIdUser(Long id) {
        return bookRepository.findByIDuser(id);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteBookById(id);
    }

    public void saveBook(ListBook book) {
        bookRepository.save(book);
    }

    public void deleteBook(Long id_book) {
        bookRepository.deleteBookById(id_book);
        List<Chapter> chapters = chapterRepository.showAllChapterById(id_book);
        type_bookRepository.deleteByIdBook(id_book);
        for (Chapter chapter : chapters) {
            deleteChapter(chapter.getId_chapter());

        }
    }

    public void deleteChapter(Long id_chapter) {
        chapterRepository.deleteChapterById(id_chapter);
        List<Book> list = pictureRepository.showBook(id_chapter);
        for (Book book : list) {
            deleteOnePage(book.getId_book());
        }
    }

    public void deleteOnePage(Long id_page) {
        pictureRepository.deleteOnePageById(id_page);
    }

}
