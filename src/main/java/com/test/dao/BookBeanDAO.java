package com.test.dao;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;


import com.googlecode.objectify.ObjectifyService;
import com.test.data.BookBean;

/**
 * BookBeanDAO handle persistence of BookBean
 * @author hjmendez
 */
public class BookBeanDAO {

    private static final Logger LOGGER = Logger.getLogger(BookBeanDAO.class.getName());
    private static final String AUTHOR_BOOK = "author";
       
    private BookSearchUtil bookSearchUtil;
    
    //TODO extract to util class
    //private Index index;
    
    public BookBeanDAO(){
    	//inject search service
    	this.bookSearchUtil = new BookSearchUtilImpl();
    }    
        
    /**
     * @return list of books
     */
    public List<BookBean> list() {
        LOGGER.info("Retrieving list of books");
        //no siempre trae todos los datos ¿consistencia eventual?
        return ObjectifyService.ofy().load().type(BookBean.class).order(AUTHOR_BOOK).list();        
    }

    /**
     * @param id
     * @return book bean with given id
     */
    public BookBean get(Long id) {
        LOGGER.info("Retrieving bean " + id);
        return ObjectifyService.ofy().load().type(BookBean.class).id(id).now();
    }

    /**
     * Saves given bean
     * @param bean
     */
    public void save(BookBean bean) {
        if (bean == null) {
            throw new IllegalArgumentException("null test object");
        }
        LOGGER.info("Saving bean " + bean.getId());
        ObjectifyService.ofy().save().entity(bean).now();
        //create document to index
        bookSearchUtil.createDocument(bean);
    }

    /**
     * Deletes given bean
     * @param bean
     */
    public void delete(BookBean bean) {
        if (bean == null) {
            throw new IllegalArgumentException("null test object");
        }
        LOGGER.info("Deleting bean " + bean.getId());
        //remove document from index
        bookSearchUtil.deleteDocument(bean.getId());
        ObjectifyService.ofy().delete().entity(bean).now();        
    }
    
    /**
     * Search books by author or name
     * @param txt
     * @return list of books that 
     */
    public Collection<BookBean> search(String txt) {
        if (txt == null) {
            throw new IllegalArgumentException("null search term");
        }        
        LOGGER.info("Search by txt into name or autor " + txt);     
        //return new ArrayList<BookBean>();
        List<Long> foundIdList = bookSearchUtil.search(txt);
        return ObjectifyService.ofy().load().type(BookBean.class).ids(foundIdList).values();
    }
}
