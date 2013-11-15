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

import java.net.URI;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.predic8.example.library.Constants;
import com.predic8.example.library.rest.AuthorsResource;
import com.sun.jersey.server.linking.Binding;
import com.sun.jersey.server.linking.Ref;
import com.sun.jersey.server.linking.Ref.Style;

@XmlRootElement(name="author", namespace=Constants.P8_LIBRARY_NS)
public class Author implements Cloneable {
	
	private int id;
	private String name;
	
	@Ref(
			resource = AuthorsResource.class, 
			method = "getAuthor", 
			style = Style.ABSOLUTE, 
			bindings = @Binding(name = "id", value = "${instance.id}"))
	@XmlAttribute(namespace=Constants.XLINK_NS, name="href")
	public URI self;
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Author))
			return false;
		return id == ((Author) obj).id;
	}
	
	@Override
	public Author clone() {
		try {
			return (Author) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int getId() {
		return id;
	}
	
	@XmlTransient
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
