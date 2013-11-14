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
package com.predic8.example.library.rest;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.predic8.example.library.db.Database;
import com.predic8.example.library.model.Author;

public class AuthorResource {

	private final int id;
	
	public AuthorResource(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@GET
	public Author get() {
		Author a = Database.getInstance().getAuthorById(id);
		if (a == null)
			throw new WebApplicationException(Status.NOT_FOUND);
		return a;
	}
	
	@PUT
	public void put(Author a) {
		a.setId(id);
		Database.getInstance().storeAuthor(a);
	}

}
