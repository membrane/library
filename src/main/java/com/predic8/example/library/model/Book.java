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

import java.net.URI;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.predic8.example.library.Constants;
import com.predic8.example.library.rest.BooksResource;
import com.sun.jersey.server.linking.Binding;
import com.sun.jersey.server.linking.Ref;
import com.sun.jersey.server.linking.Ref.Style;

@XmlRootElement(name="book", namespace=Constants.P8_LIBRARY_NS)
public final class Book extends GenericItem<Book> {
	
	private int id;
	private AuthorList authors;
	private String title;
	private String isbn;
	private String publisher;
	private String edition;
	private int year;
	private Genre genre;

	@Ref(
			resource = BooksResource.class, 
			method = "getBook",
			style = Style.ABSOLUTE, 
			bindings = @Binding(name = "id", value = "${instance.id}"))
	@XmlAttribute(namespace=Constants.XLINK_NS, name="href")
	public URI self;
	
	@Override
	public Book clone() {
		Book clone = super.clone();
		clone.authors = authors == null ? null : authors.clone();
		clone.genre = genre == null ? null : genre.clone();
		return clone;
	}
	
	public int getId() {
		return id;
	}
	
	@XmlTransient
	public void setId(int id) {
		this.id = id;
	}
	
	public AuthorList getAuthors() {
		return authors;
	}
	
	public void setAuthors(AuthorList authors) {
		this.authors = authors;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public String getEdition() {
		return edition;
	}
	
	public void setEdition(String edition) {
		this.edition = edition;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}

	public Genre getGenre() {
		return genre;
	}
	
	public void setGenre(Genre genre) {
		this.genre = genre;
	}
	
	@Override
	public boolean matches(String expr) {
		return title.contains(expr) || isbn.contains(expr) || publisher.contains(expr) || edition.contains(expr)
				|| Integer.toString(year).contains(expr);
	}
}
