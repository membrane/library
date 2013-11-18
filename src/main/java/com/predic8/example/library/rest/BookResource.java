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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.predic8.example.library.db.Database;
import com.predic8.example.library.model.Book;

public class BookResource extends GenericItemResource {

	protected Long getItemVersion() {
		return db.getBookVersion(getId());
	}
	
    @GET
    public Book get() throws Exception {
    	return db.transact(new Callable<Book>() {
    		public Book call() {
    			checkPrecondition(true);

    			Book b = Database.getInstance().getBookById(getId());
    			if (b == null)
    				throw new WebApplicationException(Status.NOT_FOUND);
    			return b;
    		}
    	});
    }
    
    @PUT
	public void put(final Book b, @Context final UriInfo uriInfo) {
    	db.transact(new Runnable() {
    		public void run() {
    			checkPrecondition(false);
    	    	
    			b.setId(getId());
    			db.storeBook(b, uriInfo);
    		}
    	});
	}
    
	@DELETE
	public void delete() throws Exception {
    	db.transact(new Callable<Void>() {
    		public Void call() throws Exception {
    			checkPrecondition(false);
    	    	
    			db.removeBook(get());
    			return null;
    		}
    	});
	}


}
