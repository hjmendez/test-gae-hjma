package com.test.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.test.data.BookBean;

public class BookSearchUtilImpl implements BookSearchUtil{
	
    private static final Logger LOGGER = Logger.getLogger(BookSearchUtilImpl.class.getName());
	
	public static final String INDEX_NAME = "searchBook";
	public static final String NAME_BOOK = "name";
	public static final String AUTHOR_BOOK = "author";
	

	private Index index;
	
	BookSearchUtilImpl(){
		LOGGER.info("create index");
		this.index = SearchServiceFactory.getSearchService()
			    .getIndex(IndexSpec.newBuilder().setName(INDEX_NAME));
	}	

	@Override
	public void createDocument(BookBean bookBean) {
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


	@Override
	public void deleteDocument(Long idBook) {
		//delete document from index
		index.delete(idBook.toString());		
	}

	@Override
	public List<Long> search(String txt) {
		List<Long> idList = new ArrayList<>();
		Results<ScoredDocument> result = index.search(txt);
		for (ScoredDocument scoredDocument : result) {
			idList.add(Long.parseLong(scoredDocument.getId()));
		}
		return idList;
	}

	@Override
	public String prepareFullSearch(String data) {
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
