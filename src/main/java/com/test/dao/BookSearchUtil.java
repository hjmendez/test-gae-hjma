package com.test.dao;


import java.util.List;

import com.test.data.BookBean;

public interface BookSearchUtil {

	public void createDocument(BookBean bookBean);
	
	public void deleteDocument(Long idBookBean);
	
	public String prepareFullSearch(String data);
	
	public List<Long> search(String txt);
}
