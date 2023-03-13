package com.example.goodreads.service;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.example.goodreads.repository.BookRepository;
import com.example.goodreads.model.Book;
import com.example.goodreads.model.BookRowMapper;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Service
public class BookH2Service implements BookRepository{
    
    @Autowired
    private JdbcTemplate db;

    @Override
    public Book getBookById(int bookId){
        try {
            Book book = db.queryForObject("SELECT * FROM book WHERE id = ?", new BookRowMapper(),bookId);
            return book;
        } 
        catch (Exception e){
            throw new ResponseStatusException (HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Book addBook(Book book){
        db.update("INSERT INTO  book(name, imageUrl) values(?, ?)",book.getName(), book.getImageUrl());
        Book savedBook = db.queryForObject("SELECT * FROM book WHERE name = ? and imageURL = ?",new BookRowMapper(), book.getName(),book.getImageUrl());
        return savedBook;
    }

    @Override
    public Book updateBook(int bookId ,Book book){
        if(book.getName() != null){
            db.update("update book set name = ? where id =? ",book.getName(),bookId);
        }
        if(book.getImageUrl() != null){
            db.update("update book set imageUrl = ? where id =? ",book.getImageUrl(),bookId);
        }
        return getBookById(bookId);
    }
      
    @Override
    public void deleteBook(int bookId){
        db.update("DELETE FROM book WHERE id =?",bookId)
    }

    @Override
    public ArrayList<Book> getBooks(){
        List<Book> bookList = db.query("select * from book" , new BookRowMapper());
        ArrayList<Book> books = new ArrayList<>(bookList);
        return books;
    }

	public JdbcTemplate getDb() {
		return db;
	}

	public void setDb(JdbcTemplate db) {
		this.db = db;
	}

}