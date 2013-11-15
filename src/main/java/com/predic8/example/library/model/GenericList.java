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

import javax.xml.bind.annotation.XmlTransient;

/**
 * @param <T> the Item type
 * @param <S> the final list type
 */
public abstract class GenericList<T extends GenericItem<T>, S extends GenericList<T, S>> {

	public static final int LIST_PAGE_SIZE = 10;
	
	protected ArrayList<T> items = new ArrayList<>();
	
	private String searchExpr;
	
	public String getSearchExpr() {
		return searchExpr;
	}
	
	@XmlTransient
	public void setSearchExpr(String searchExpr) {
		this.searchExpr = searchExpr;
	}
	
	private int nextOffset;
	
	public int getNextOffset() {
		return nextOffset;
	}
	
	@XmlTransient
	public void setNextOffset(int nextOffset) {
		this.nextOffset = nextOffset;
	}
	
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
		
		this.searchExpr = searchExpr;
		
		Iterator<T> it = items.iterator();
		while (it.hasNext()) {
			T current = it.next();
			if (!current.matches(searchExpr))
				it.remove();
		}
		return (S)this;
	}
	
	@SuppressWarnings("unchecked")
	public S offset(int offset) {
		if (offset < 0 || offset >= items.size()) {
			items.clear();
		} else {
			items.subList(0, offset).clear();
			if (items.size() > LIST_PAGE_SIZE) {
				setNextOffset(offset + LIST_PAGE_SIZE);
				items.subList(LIST_PAGE_SIZE, items.size()).clear();
			}
		}
		return (S)this;
	}

}
