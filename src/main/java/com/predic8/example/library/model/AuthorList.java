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

@XmlRootElement(name="authors", namespace=Constants.P8_LIBRARY_NS)
public class AuthorList {
	
	private List<Author> authors;
	
	public AuthorList() {
		authors = new ArrayList<>();
	}
	
	public AuthorList(Author... authors) {
		this.authors = Arrays.asList(authors);
	}
	
	@Override
	public AuthorList clone() {
		try {
			AuthorList clone = (AuthorList) super.clone();
			clone.authors = new ArrayList<>(authors.size());
			for (Author a : authors)
				clone.authors.add(a.clone());
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	
	public List<Author> getAuthors() {
		return authors;
	}
	
	@XmlElement(name="author")
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

}
