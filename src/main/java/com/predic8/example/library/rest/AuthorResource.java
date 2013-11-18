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

import java.util.concurrent.Callable;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.predic8.example.library.model.Author;
import com.predic8.example.library.model.BookList;

public class AuthorResource extends GenericItemResource {

	@Override
	protected Long getItemVersion() {
		return db.getAuthorVersion(getId());
	}

	@GET
	public Author get() throws Exception {
    	return db.transact(new Callable<Author>() {
    		public Author call() {
    			checkPrecondition(true);

    			Author a = db.getAuthorById(getId());
    			if (a == null)
    				throw new WebApplicationException(Status.NOT_FOUND);
    			return a;
    		}
    	});
	}
	
	@PUT
	public void put(final Author a) {
    	db.transact(new Runnable() {
    		public void run() {
    			checkPrecondition(false);
    	    	
    			a.setId(getId());
    			db.storeAuthor(a);
    		}
    	});
	}
	
	@DELETE
	public void delete() throws Exception {
    	db.transact(new Callable<Void>() {
    		public Void call() throws Exception {
    			checkPrecondition(false);
    	    	
    			if (!db.removeAuthor(get()))
    				throw new WebApplicationException(Response
    						.status(Status.FORBIDDEN)
    						.entity(new GenericEntity<>("Cannot delete object because there are foreign key constraints.",
    								String.class)).build());
    			return null;
    		}
    	});
	}
	
	@Path("books")
	@GET
	public BookList getBooks() throws Exception {
		return db.getBooks(get(), null);
	}

}
