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
package com.predic8.example.library.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriInfo;

import com.predic8.example.library.JerseyUnlinker;
import com.predic8.example.library.model.Author;
import com.predic8.example.library.model.AuthorList;
import com.predic8.example.library.model.Book;
import com.predic8.example.library.model.BookList;
import com.predic8.example.library.model.Genre;
import com.predic8.example.library.model.GenreList;

public class Database {

	// singleton
	private static volatile Database instance;
	
	public static synchronized Database getInstance() {
		Database result = instance;
        if (result == null) {
            synchronized(Database.class) {
                result = instance;
                if (result == null) {
                	instance = result = new Database();
                }
            }
        }
        return result;
	}
	
	
	// sequences
	private int lastAuthorId, lastBookId, lastGenreId;
	
	// entities
	private Map<Integer, Author> authors = new HashMap<>();
	private Map<Integer, Book> books = new HashMap<>();
	private Map<Integer, Genre> genres = new HashMap<>();
	
	// relations
	private Map<Integer, Integer> bookGenres = new HashMap<>();
	private Map<Integer, List<Integer>> bookAuthors = new HashMap<>();
	
	private Database() {
		new InitialLoad(this).run();
	}
	
	public synchronized Author getAuthorById(int id) {
		Author author = authors.get(id);
		if (author == null)
			return null;
		return author.clone();
	}
	
	public synchronized AuthorList getAuthors() {
		AuthorList authorlist = new AuthorList();
		for (Map.Entry<Integer, Author> entry : authors.entrySet())
			if (entry.getValue() != null)
				authorlist.getAuthors().add(entry.getValue().clone());
		return authorlist;
	}

	public synchronized void storeAuthor(Author author) {
		authors.put(author.getId(), author);
	}

	public synchronized int createAuthorId() {
		return ++lastAuthorId;
	}
	
	/**
	 * @return false, if the Author could not be deleted (because of foreign key constraints)
	 */
	public synchronized boolean removeAuthor(Author author) {
		if (getBooks(author, null).getBooks().size() > 0)
			return false;
		authors.remove(author.getId());
		return true;
	}

	public synchronized Book getBookById(int id) {
		// restore Book entity
		Book book = books.get(id);
		if (book == null)
			return null;
		
		// restore Book relations
		book.setGenre(getGenreById(bookGenres.get(id)));
		AuthorList al = new AuthorList();
		for (int authorId : bookAuthors.get(id))
			al.getAuthors().add(getAuthorById(authorId));
		book.setAuthors(al);
		
		return book;
	}
	
	public synchronized BookList getBooks() {
		BookList booklist = new BookList();
		for (Map.Entry<Integer, Book> entry : books.entrySet())
			if (entry.getValue() != null)
				booklist.getBooks().add(getBookById(entry.getKey()));
		return booklist;
	}
	
	public synchronized BookList getBooks(Author byAuthor, Genre byGenre) {
		BookList booklist = getBooks();
		Iterator<Book> i = booklist.getBooks().iterator();
		while (i.hasNext()) {
			Book b = i.next();
			if (byAuthor != null && !b.getAuthors().getAuthors().contains(byAuthor))
				i.remove();
			if (byGenre != null && !b.getGenre().equals(byGenre))
				i.remove();
		}
		return booklist;
	}
	
	public synchronized void storeBook(Book book, UriInfo uriInfo) {
		// store Book relations
		bookGenres.put(book.getId(), JerseyUnlinker.getIdFromJAXBObject(uriInfo, book.getGenre()));
		List<Integer> authorIds = new ArrayList<>();
		for (Author author : book.getAuthors().getAuthors())
			authorIds.add(JerseyUnlinker.getIdFromJAXBObject(uriInfo, author));
		bookAuthors.put(book.getId(), authorIds);
		
		// store Book entity
		book.setGenre(null);
		book.setAuthors(null);
		books.put(book.getId(), book);
	}
	
	public synchronized int createBookId() {
		return ++lastBookId;
	}

	public synchronized void removeBook(Book book) {
		books.remove(book.getId());
		bookAuthors.remove(book.getId());
		bookGenres.remove(book.getId());
	}

	public synchronized Genre getGenreById(int id) {
		Genre genre = genres.get(id);
		if (genre == null)
			return null;
		return genre.clone();
	}

	public synchronized GenreList getGenres() {
		GenreList genrelist = new GenreList();
		for (Map.Entry<Integer, Genre> entry : genres.entrySet())
			if (entry.getValue() != null)
				genrelist.getGenres().add(entry.getValue().clone());
		return genrelist;
	}

	public synchronized void storeGenre(Genre genre) {
		genres.put(genre.getId(), genre);
	}
	
	public synchronized int createGenreId() {
		return ++lastGenreId;
	}

	/**
	 * @return false, if the Genre could not be deleted (because of foreign key constraints)
	 */
	public synchronized boolean removeGenre(Genre genre) {
		if (getBooks(null, genre).getBooks().size() > 0)
			return false;
		genres.remove(genre.getId());
		return true;
	}

}
