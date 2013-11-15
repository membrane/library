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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.predic8.example.library.db.Database;
import com.predic8.example.library.model.BookList;

@Path("books")
public class BooksResource {
    
    @Path("{id:\\d+}")
    public BookResource getBook(@PathParam("id") int id) {
        return new BookResource(id);
    }
	
    @POST
    @Path("create")
    public Response createBook(@Context UriInfo uriInfo) {
		return Response.created(
				uriInfo.getBaseUriBuilder().path("books/{id}").build(Database.getInstance().createBookId()))
				.build();
    }
    
    @GET
    public BookList getBooks(
    		@QueryParam("q") @DefaultValue("") String searchExpr) {
    	return Database.getInstance().getBooks().filter(searchExpr);
    }

}
