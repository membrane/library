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

	private Book b(String title, String isbn, String publisher, String edition, int year, Genre genre, Author... authors) {
		Book b = new Book();
		b.setAuthors(new AuthorList(authors));
		b.setGenre(genre);
		b.setEdition(edition);
		b.setYear(year);
		b.setTitle(title);
		b.setIsbn(isbn);
		b.setPublisher(publisher);
		b.setId(db.createBookId());
		db.storeBook(b, null);
		return b;
	}
	
	public void run() {
		Genre it = g("IT"), fantasy = g("Fantasy");
		
		Author bloch = a("Joshua Bloch"), gafter = a("Neal Gafter");
		b("Effective Java: A Programming Language Guide", "978-0321356680", "Addison-Wesley Longman, Amsterdam", "2nd revised", 2008, it, bloch);
		b("Java Puzzlers: Traps, Pitfalls, and Corner Cases", "978-0321336781", "Addison-Wesley Longman, Amsterdam", "", 2005, it, bloch, gafter);
		
		Author sedgewick = a("Robert Sedgewick"), schidlowsky = a("Michael Schidlowsky");
		b("Algorithms in Java, Parts 1-4", "978-0201361209", "Addison-Wesley Longman, Amsterdam", "3rd", 2002, it, sedgewick);
		b("Algorithms in Java: Part 5, Graph Algorithms", "978-0201361216", "Addison Wesley Pub Co Inc", "3rd", 2003, it, sedgewick, schidlowsky);
		
		Author cormen = a("Thomas H. Cormen"), leiserson = a("Charles E. Leiserson"), rivest = a("Ronald L. Rivest"), stein = a("Clifford Stein");
		b("Introduction to Algorithms", "978-0262533058", "MIT Press", "3rd student", 2009, it, cormen, leiserson, rivest, stein);
		
		Author skiena = a("Steven S Skiena");
		b("The Algorithm Design Manual", "978-1848000698", "Springer", "2nd", 2011, it, skiena);
		
		Author dewdney = a("A. K. Dewdney");
		b("New Turing Omnibus", "978-0805071665", "Henry Holt", "enlarged & updated", 2003, it, dewdney);
		
		Author gamma = a("Erich Gamma"), helm = a("Richard Helm"), johnson = a("Ralph E. Johnson"), vlissides = a("John Vlissides");
		b("Design Patterns. Elements of Reusable Object-Oriented Software", "978-0201633610", "Addison-Wesley Longman, Amsterdam", "1st reprint", 1994, it, gamma, helm, johnson, vlissides);
		
		Author fowler = a("Martin Fowler");
		b("UML Distilled: A Brief Guide to the Standard Object Modeling Languange", "978-0321193681", "Addison-Wesley Longman, Amsterdam", "3rd", 2003, it, fowler);
		Author beck = a("Kent Beck"), brant = a("John Brant"), opdyke = a("William Opdyke"), roberts = a("Don Roberts");
		b("Refactoring: Improving the Design of Existing Code", "978-0201485677", "Addison-Wesley Longman, Amsterdam", "", 1999, it, fowler, beck, brant, opdyke, roberts);
		
		Author knuth = a("Donald Ervin Knuth");
		b("The Art of Computer Programming 1. Fundamental Algorithms", "978-0201896831", "Addison-Wesley Longman, Amsterdam", "3rd", 1997, it, knuth);
		b("The Art of Computer Programming 2. Seminumerical Algorithms", "978-0201896848", "Addison-Wesley Longman, Amsterdam", "3rd", 1997, it, knuth);
		b("The Art of Computer Programming 3. Sorting and Searching", "978-0201896855", "Addison-Wesley Longman, Amsterdam", "2nd", 1998, it, knuth);
		b("The Art of Computer Programming - Volume 4A: Combinatorial Algorithms 1", "978-0201038040", "Pearson Education (US)", "", 2011, it, knuth);
		
		Author tolkien = a("John Ronald Reuel Tolkien");
		b("Lord of the Rings 1: The Fellowship of the Rings", "978-0261102354", "Harpercollins UK", "international", 1991, fantasy, tolkien);
		
	}
}
