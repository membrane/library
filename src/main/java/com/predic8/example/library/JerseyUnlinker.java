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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.uri.internal.UriTemplateParser;

import com.sun.jersey.server.linking.LinkFilter;
import com.sun.jersey.server.linking.impl.EntityDescriptor;
import com.sun.jersey.server.linking.impl.RefFieldDescriptor;

/**
 * Sort of the reverse of jersey-server-linking's {@link LinkFilter}.
 */
public class JerseyUnlinker {
	
	private static class ClassInfo {
		final Method getId;
		final EntityDescriptor entityDescriptor;
		
		public ClassInfo(Class<?> clazz) {
			try {
				getId = clazz.getMethod("getId");
			} catch (NoSuchMethodException | SecurityException e) {
				throw new IllegalArgumentException(e);
			}
			entityDescriptor = EntityDescriptor.getInstance(clazz);
		}
		
		public int getId(Object instance) {
			try {
				return (int)getId.invoke(instance);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new IllegalArgumentException(e);
			}
		}
		
		public EntityDescriptor getEntityDescriptor() {
			return entityDescriptor;
		}
	}
	
	private static Map<Class<?>, ClassInfo> classInfos = new HashMap<>();
	
	public static synchronized ClassInfo getClassInfo(Class<?> clazz) {
		ClassInfo ci = classInfos.get(clazz);
		if (ci == null) {
			ci = new ClassInfo(clazz);
			classInfos.put(clazz, ci);
		}
		return ci;
	}
	
	/**
	 * Extracts the "id" property from the given JAX-B object.
	 * 
	 * The "id" property is either the return value of the getId() call (if the return value is non-zero) or
	 * the id contained in the URL value of the object's "self" field.
	 * 
	 * The URL is analyzed using the rules of Jersey Server Linking's annotations.
	 */
	public static int getIdFromJAXBObject(UriInfo uriInfo, Object instance) {
		try {
			Class<?> clazz = instance.getClass();
			ClassInfo classInfo = getClassInfo(clazz);
			
			// call instance.getId()
			int id = classInfo.getId(instance);
			// id set?
			if (id != 0)
				return id;

			// analyze the URI instance.self
			for (RefFieldDescriptor linkField : classInfo.getEntityDescriptor().getLinkFields()) {
				if (!"self".equals(linkField.getFieldName()))
					continue;
				URI uri = (URI) linkField.getFieldValue(instance);
				
				// remove the base URI
				String base = uriInfo.getBaseUri().toString();
				String uriString = uri.toString();
				if (uriString.startsWith(base))
					uriString = uriString.substring(base.length());

				// retrieve the path pattern (e.g. 'authors/{id}') and match the URI (e.g. 'authors/5') against it
				UriTemplateParser parser = new UriTemplateParser(linkField.getLinkTemplate());
				Matcher m = parser.getPattern().matcher(uriString);
				if (!m.matches())
					throw new IllegalStateException();
				int idIndex = parser.getNames().indexOf("id");
				if (idIndex < 0 || idIndex + 1 > m.groupCount())
					throw new IllegalStateException("idIndex out of bounds");
				return Integer.parseInt(m.group(idIndex+1));
			}
			throw new IllegalStateException();
		} catch (SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
