package com.thuctap.book.service;

import com.thuctap.book.entity.Book;
import com.thuctap.book.entity.Chapter;
import com.thuctap.book.entity.ListBook;
import com.thuctap.book.respository.BookRepository;
import com.thuctap.book.respository.ChapterRepository;
import com.thuctap.book.respository.PictureRepository;
import com.thuctap.book.respository.Type_BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private Type_BookRepository type_bookRepository;

    @Autowired
    private PictureRepository pictureRepository;
    public List<ListBook> showAllBook(){
        return bookRepository.showAllBook();
    }

    public ListBook findByName(String name){
        return bookRepository.findByName(name);
    }

    public ListBook findById(Long id){
        return bookRepository.findByID(id);
    }
    public List<ListBook> findBook(String name){
        return  bookRepository.findBook(name);
    }
    public List<ListBook> findByIdUser(Long id){
        return  bookRepository.findByIDuser(id);
    }

    public void deleteBookById(Long id){
        bookRepository.deleteBookById(id);
    }

    public void saveBook(ListBook book){
        bookRepository.save(book);
    }

    public void deleteBook(Long id_book){
        bookRepository.deleteBookById(id_book);
        List<Chapter> chapters = chapterRepository.showAllChapterById(id_book);
        type_bookRepository.deleteByIdBook(id_book);
        for(Chapter chapter:chapters){
            deletechapter(chapter.getId_chapter());

        }
    }
    public void deletechapter(Long id_chapter){
        chapterRepository.deleteChapterById(id_chapter);
        List<Book> list = pictureRepository.showBook(id_chapter);
        for(Book book:list){
            deleteOnePage(book.getId_book());
        }
    }

    public void deleteOnePage(Long id_page){
        pictureRepository.deleteOnepageById(id_page);
    }

}
