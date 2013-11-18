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

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.predic8.example.library.VersionUtil;
import com.predic8.example.library.VersionUtil.HeaderValueParsingException;
import com.predic8.example.library.db.Database;

/**
 * Generic superclass of resource classes representing entities (as opposed to collections, controllers, etc.).
 */
public abstract class GenericItemResource {

	protected final Database db = Database.getInstance();
	
	@PathParam("id")
	private int id;

	public int getId() {
		return id;
	}

	/**
	 * @return The current version (HTTP header "ETag") of the entity represented by this resource.
	 */
	protected abstract Long getItemVersion();

	@HeaderParam("If-Match")
	private String ifMatch;
	@HeaderParam("If-None-Match")
	private String ifNoneMatch;

	/**
	 * Checks "If-Match" and "If-None-Match" headers agains the current version of the entity resource.
	 * 
	 * Throws "412 Precondition failed", if check fails (or the header value's format is not recognized).
	 * 
	 * {@link #checkPrecondition(boolean)} should only be called from within a transaction (
	 * {@link Database#transact(Runnable)} and others).
	 * 
	 * @param returnNotModifiedIfCheckFailed
	 *            Whether to throw "304 Not modified" instead of "412 Precondition failed" if the check fails.
	 */
	protected void checkPrecondition(boolean returnNotModifiedIfCheckFailed) {
		try {
			Long version = getItemVersion();
			if (ifMatch != null && !VersionUtil.ifMatch(version, ifMatch) ||
					ifNoneMatch != null && !VersionUtil.ifNoneMatch(version, ifNoneMatch)) {
				if (returnNotModifiedIfCheckFailed) {
					throw new WebApplicationException(Response.notModified(version == null ? null : version.toString()).build());
				} else {
					throw new WebApplicationException(Status.PRECONDITION_FAILED);
				}
			}
		} catch (HeaderValueParsingException e) {
			throw new WebApplicationException(Status.PRECONDITION_FAILED);
		}
	}

}
