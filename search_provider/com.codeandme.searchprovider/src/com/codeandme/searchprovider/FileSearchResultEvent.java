/*******************************************************************************
 * Copyright (c) 2015 Christian Pontesegger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Christian Pontesegger - initial implementation
 *******************************************************************************/
package com.codeandme.searchprovider;

import java.io.File;

import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.SearchResultEvent;

public class FileSearchResultEvent extends SearchResultEvent {

	private final File fAddedFile;

	protected FileSearchResultEvent(ISearchResult searchResult, File addedFile) {
		super(searchResult);
		fAddedFile = addedFile;
	}

	public File getAddedFile() {
		return fAddedFile;
	}
}
