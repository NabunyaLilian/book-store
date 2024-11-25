package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import javax.swing.text.html.parser.Entity;
import java.util.List;

import static jakarta.transaction.Transactional.TxType.REQUIRED;
import static jakarta.transaction.Transactional.TxType.SUPPORTS;

@Transactional(SUPPORTS)
@ApplicationScoped
@Named
public class BookService {

    @Inject
    EntityManager em;

    @Inject
     IsbnGenerator generator;

    public Book find(Long id){
        return em.find(Book.class, id);
    }

    public List<Book> findAll(){
        TypedQuery<Book> query = em.createQuery("select b from Book b order by b.title desc  ", Book.class);
        return query.getResultList();
    }

    public Long countAll(){
        TypedQuery<Long> query = em.createQuery("select count(b) from Book b", Long.class);
        return query.getSingleResult();
    }

    @Transactional(REQUIRED)
    public Book create(Book book){
        book.setIsbn( generator.generateNumber());
        em.persist(book);
        return  book;
    }


    public void delete(Long id){
        em.remove(em.find(Book.class, id));
    }
}
