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

import com.predic8.example.library.model.Author;
import com.predic8.example.library.model.AuthorList;
import com.predic8.example.library.model.Book;
import com.predic8.example.library.model.Genre;

public class InitialLoad {

	final Database db;

	public InitialLoad(Database db) {
		this.db = db;
	}
	
	private Author a(String name) {
		Author a = new Author();
		a.setId(db.createAuthorId());
		a.setName(name);
		db.storeAuthor(a);
		return a;
	}

	private Genre g(String name) {
		Genre g = new Genre();
		g.setId(db.createGenreId());
		g.setName(name);
		db.storeGenre(g);
		return g;
	}

	private Book b(String title, String isbn, String publisher, int year, Genre genre, Author... authors) {
		Book b = new Book();
		b.setAuthors(new AuthorList(authors));
		b.setGenre(genre);
		b.setYear(year);
		b.setTitle(title);
		b.setIsbn(isbn);
		b.setPublisher(publisher);
		b.setId(db.createBookId());
		db.storeBook(b, null);
		return b;
	}
	
	public void run() {
		Author tobias = a("Tobias");
		b("My Book", "ISBN", "publisher", 2013, g("Science"), tobias);
		b("My Second Book", "ISBN", "publisher", 2013, g("Science"), tobias);
	}
}
