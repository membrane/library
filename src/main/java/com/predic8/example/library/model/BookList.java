/* Copyright 2013 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */
package com.predic8.example.library.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.predic8.example.library.Constants;

@XmlRootElement(name="books", namespace=Constants.P8_LIBRARY_NS)
public class BookList {
	
	private List<Book> books;
	
	public BookList() {
		books = new ArrayList<>();
	}
	
	public BookList(Book... books) {
		this.books = Arrays.asList(books);
	}
	
	public List<Book> getBooks() {
		return books;
	}
	
	@XmlElement(name="book")
	public void setBooks(List<Book> books) {
		this.books = books;
	}

}
