package com.example.aptech.spring.library.Service;

import com.example.aptech.spring.library.Request.AddBookRequest;
import com.example.aptech.spring.library.dao.BookRepository;
import com.example.aptech.spring.library.entity.Book;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AdminService {
    private BookRepository bookRepository;

    public AdminService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void postBook(AddBookRequest addBookRequest){
       Book book = new Book();
       book.setTitle(addBookRequest.getTitle());
       book.setAuthor(addBookRequest.getAuthor());
       book.setDescription(addBookRequest.getDescription());
       book.setCopies(addBookRequest.getCopies());
       book.setCopiesAvailable(addBookRequest.getCopies());
       book.setCategory(addBookRequest.getCategory());
       book.setImg(addBookRequest.getImg());
       bookRepository.save(book);
   }
}
