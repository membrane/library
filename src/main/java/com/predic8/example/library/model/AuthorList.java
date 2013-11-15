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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.predic8.example.library.Constants;

@XmlRootElement(name="authors", namespace=Constants.P8_LIBRARY_NS)
public final class AuthorList extends GenericList<Author, AuthorList> {

	public AuthorList() {
	}

	public AuthorList(Author... authors) {
		super();
		this.items.ensureCapacity(authors.length);
		for (Author author : authors)
			this.items.add(author);
	}
	
	public List<Author> getAuthors() {
		return items;
	}
	
	@XmlElement(name="author")
	public void setAuthors(List<Author> authors) {
		items.clear();
		items.addAll(authors);
	}

}
