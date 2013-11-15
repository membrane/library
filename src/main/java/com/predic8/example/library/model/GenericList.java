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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @param <T> the Item type
 * @param <S> the final list type
 */
public abstract class GenericList<T extends GenericItem<T>, S extends GenericList<T, S>> {

	protected ArrayList<T> items = new ArrayList<>();

	@Override
	public S clone() {
		try {
			@SuppressWarnings("unchecked")
			S clone = (S) super.clone();
			clone.items = new ArrayList<>(items.size());
			for (T a : items)
				clone.items.add(a.clone());
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public S filter(String searchExpr) {
		if (searchExpr == null || searchExpr.length() == 0)
			return (S)this;
		
		Iterator<T> it = items.iterator();
		while (it.hasNext()) {
			T current = it.next();
			if (!current.matches(searchExpr))
				it.remove();
		}
		return (S)this;
	}

}
