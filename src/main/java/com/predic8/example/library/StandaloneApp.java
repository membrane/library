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
package com.predic8.example.library;

import org.glassfish.grizzly.http.server.HttpServer;

import com.predic8.example.library.rest.AuthorsResource;
import com.predic8.example.library.rest.BooksResource;
import com.predic8.example.library.rest.GenresResource;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

public class StandaloneApp {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		ResourceConfig resourceConfig = new DefaultResourceConfig(BooksResource.class, AuthorsResource.class, GenresResource.class);

		resourceConfig.getContainerResponseFilters().add(new com.sun.jersey.server.linking.LinkFilter());

		HttpServer s = GrizzlyServerFactory.createHttpServer("http://0.0.0.0:5555", resourceConfig);
		try {
			System.out.println("Press any key to stop the service...");
			System.in.read();
		} finally {
			s.stop();
		}
	}

}
