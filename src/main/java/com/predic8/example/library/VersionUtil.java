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

import java.util.HashSet;
import java.util.Set;

public class VersionUtil {
	/**
	 * Parses an If-Match or If-None-Match HTTP header value.
	 * 
	 * See e.g.g http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.24 .
	 * 
	 * All ETags are supposed to be positive <code>long</code> values quoted by '"', separated by ','.
	 * 
	 * If "*" is part of the string, it is translated into -1.
	 * 
	 * @return the list of ETags (or null, if the string is not valid)
	 */
	private static Set<Long> parseETags(String etags) {
		try {
			HashSet<Long> result = new HashSet<>();
			for (String value : etags.split(",")) {
				String trimmed = value.trim();
				if ("*".equals(trimmed))
					result.add(-1l);
				else {
					if (trimmed.length() < 2 || !trimmed.startsWith("\"") || !trimmed.endsWith("\""))
						return null;
					result.add(Long.parseLong(value.substring(1, trimmed.length()-1).trim()));
				}
			}
			return result;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Implements the "If-Match" HTTP header check according RFC 2616 section 14 paragraph 24.
	 * 
	 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.24
	 * 
	 * @param entityVersion
	 *            the version of the entity in the database (or null, if the entity does not exist yet)
	 * @param ifMatch
	 *            the value of the "If-Match" HTTP request header (or null, if the header is not present)
	 * @return True, if the versions match (and normal request processing should continue). False, if HTTP status code
	 *         "412 Precondition failed." should be returned.
	 * @throws HeaderValueParsingException if the header value could not be parsed
	 */
	public static boolean ifMatch(Long entityVersion, String ifMatch) throws HeaderValueParsingException {
		if (ifMatch == null)
			return true;
		Set<Long> etags = parseETags(ifMatch);
		if (etags == null)
			throw new HeaderValueParsingException();
		return entityVersion != null && (etags.contains(-1l) || etags.contains(entityVersion));
	}

	/**
	 * Implements the "If-None-Match" HTTP header check according RFC 2616 section 14 paragraph 26.
	 * 
	 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.26
	 * 
	 * @param entityVersion
	 *            the version of the entity in the database (or null, if the entity does not exist yet)
	 * @param ifNoneMatch
	 *            the value of the "If-None-Match" HTTP request header (or null, if the header is not present)
	 * @return True, if the versions do not match (and normal request processing should continue). False, if HTTP status
	 *         code "304 Not modified" (for GET requests) or "412 Precondition failed" (for request methods other than
	 *         GET) should be returned.
	 * @throws HeaderValueParsingException if the header value could not be parsed
	 */
	public static boolean ifNoneMatch(Long entityVersion, String ifNoneMatch) throws HeaderValueParsingException {
		if (ifNoneMatch == null)
			return true;
		Set<Long> etags = parseETags(ifNoneMatch);
		if (etags == null)
			throw new HeaderValueParsingException();
		return !(entityVersion != null && (etags.contains(-1l) || etags.contains(entityVersion)));
	}
	
	public static class HeaderValueParsingException extends Exception {

		private static final long serialVersionUID = 1L;
		
	}
}
