package com.example.aptech.spring.library.Service;

import com.example.aptech.spring.library.Request.AddBookRequest;
import com.example.aptech.spring.library.dao.BookRepository;
import com.example.aptech.spring.library.dao.CheckoutRepository;
import com.example.aptech.spring.library.dao.ReviewRepository;
import com.example.aptech.spring.library.entity.Book;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class AdminService {
    private BookRepository bookRepository;
    private ReviewRepository reviewRepository;
    private CheckoutRepository checkoutRepository;
    @Autowired
    public AdminService(BookRepository bookRepository, ReviewRepository reviewRepository, CheckoutRepository checkoutRepository) {
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.checkoutRepository = checkoutRepository;
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

   public void increaseBookQuantity(Long bookId)throws Exception{
       Optional<Book> book = bookRepository.findById(bookId);
       if(!book.isPresent()){
           throw new Exception("Book not found");
       }

       book.get().setCopiesAvailable(book.get().getCopiesAvailable()+1);
       book.get().setCopies(book.get().getCopies()+1);

       bookRepository.save(book.get());
   }

    public void decreaseBookQuantity(Long bookId)throws Exception{
        Optional<Book> book = bookRepository.findById(bookId);
        if(!book.isPresent() || book.get().getCopiesAvailable() <=0
        || book.get().getCopies() <=0 ){
            throw new Exception("Book not found or quantity locked");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable()-1);
        book.get().setCopies(book.get().getCopies()-1);

        bookRepository.save(book.get());
    }

    public void deleteBook(Long bookId) throws Exception{
        Optional<Book> book = bookRepository.findById(bookId);
        if(!book.isPresent() ){
            throw new Exception("Book not found or quantity locked");
        }

        checkoutRepository.deleteAllByBookId(book.get().getId());
        reviewRepository.deleteAllByBookId(book.get().getId());
        bookRepository.delete(book.get());
    }


}
