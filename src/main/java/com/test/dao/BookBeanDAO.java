package com.test.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import com.test.data.BookBean;

/**
 * BookBeanDAO handle persistence of BookBean
 * @author hjmendez
 */
public class BookBeanDAO {

    private static final Logger LOGGER = Logger.getLogger(BookBeanDAO.class.getName());
    private static final String AUTHOR_BOOK = "author";
    
	public static final String INDEX_NAME = "searchBook";
	public static final String NAME_BOOK = "name";
	
    //TODO: extract to util class
	private Index index;
    
    public BookBeanDAO(){
		this.index = SearchServiceFactory.getSearchService()
			    .getIndex(IndexSpec.newBuilder().setName(INDEX_NAME));
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
        this.createDocument(bean);
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
        this.deleteDocument(bean.getId());
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
        List<Long> foundIdList = this.searchInIndex(txt);
        return ObjectifyService.ofy().load().type(BookBean.class).ids(foundIdList).values();
    }
    
    //*****************************************************
    //management index,documents,etc 
    //TODO: extract to util class or service?
    //*****************************************************    
    
    /**
     * Add a document to index
     * @param bookBean
     */
	private void createDocument(BookBean bookBean) {
		//create document and add to index
		Document doc = Document.newBuilder()
				.setId(bookBean.getId().toString())
				//.addField(Field.newBuilder().setName(NAME_BOOK).setText(bookBean.getName()))
				//.addField(Field.newBuilder().setName(AUTHOR_BOOK).setText(bookBean.getAuthor()))
				//.addField(Field.newBuilder().setName(AUTHOR_BOOK).setText("ejemp eje")
				.addField(Field.newBuilder().setName(NAME_BOOK).setText(this.prepareFullSearch(bookBean.getName())))
				.addField(Field.newBuilder().setName(AUTHOR_BOOK).setText(this.prepareFullSearch(bookBean.getAuthor())))
				.build();
		index.put(doc);		
	}


	/**
	 * Delete document from index
	 * @param BookBean
	 */	
	private void deleteDocument(Long idBook) {
		//delete document from index
		LOGGER.info(String.format("Delete document with id: %s", idBook));  
		index.delete(idBook.toString());		
	}

	/**
	 * Search into index
	 * @param txt to search
	 * @return list of ids from documents//books founded
	 */
	private List<Long> searchInIndex(String txt) {
		LOGGER.info(String.format("Search documents with txt: %s", txt));
		List<Long> idList = new ArrayList<>();
		Results<ScoredDocument> result = index.search(txt);
		for (ScoredDocument scoredDocument : result) {
			idList.add(Long.parseLong(scoredDocument.getId()));
		}
		return idList;
	}

	/**
	 * Split words for add to documents
	 * @param data String to prepare fullSearch
	 * @return
	 */
	private String prepareFullSearch(String data) {
		LOGGER.info(String.format("Prepare data for fullSearch: %s", data));
		StringBuilder stringBuilder = new StringBuilder();		
		String[] words = data.split(" ");
		for (String word : words) {
			for(int i=0; i<word.length();i++) {
				stringBuilder.append(word.substring(0, i+1)).append(" ");				
			}
		}
		return stringBuilder.toString();	
	}

}
