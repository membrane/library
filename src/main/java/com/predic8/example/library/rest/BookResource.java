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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.predic8.example.library.VersionUtil;
import com.predic8.example.library.db.Database;
import com.predic8.example.library.model.Book;

public class BookResource {

	private final Database db = Database.getInstance();

	@PathParam("id")
	private int id;
	@HeaderParam("If-Match")
	private String ifMatch;
	@HeaderParam("If-None-Match")
	private String ifNoneMatch;
	
	public int getId() {
		return id;
	}
	
	private void checkPrecondition(boolean returnNotModifiedIfCheckFailed) {
		Long version = db.getBookVersion(id);
		if (ifMatch != null && !VersionUtil.ifMatch(version, ifMatch) ||
				ifNoneMatch != null && !VersionUtil.ifNoneMatch(version, ifNoneMatch)) {
			if (returnNotModifiedIfCheckFailed) {
				throw new WebApplicationException(Response.notModified(version == null ? null : version.toString()).build());
			} else {
				throw new WebApplicationException(Status.PRECONDITION_FAILED);
			}
		}
	}
	
    @GET
    public Book get() throws Exception {
    	return db.transact(new Callable<Book>() {
    		public Book call() {
    			checkPrecondition(true);

    			Book b = Database.getInstance().getBookById(id);
    			if (b == null)
    				throw new WebApplicationException(Status.NOT_FOUND);
    			return b;
    		}
    	});
    }
    
    @PUT
	public void put(
			final Book b,
			@Context final UriInfo uriInfo) {
    	db.transact(new Runnable() {
    		public void run() {
    			checkPrecondition(false);
    	    	
    			b.setId(id);
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
