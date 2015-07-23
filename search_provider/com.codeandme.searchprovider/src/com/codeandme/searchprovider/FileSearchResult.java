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
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;

public class FileSearchResult implements ISearchResult {

	private final ISearchQuery fQuery;
	private final ListenerList fListeners = new ListenerList();

	private final Collection<File> fResult = new HashSet<File>();

	public FileSearchResult(ISearchQuery query) {
		fQuery = query;
	}

	@Override
	public String getLabel() {
		return fResult.size() + " file(s) found";
	}

	@Override
	public String getTooltip() {
		return "Found files in the filesystem";
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public ISearchQuery getQuery() {
		return fQuery;
	}

	@Override
	public void addListener(ISearchResultListener l) {
		fListeners.add(l);
	}

	@Override
	public void removeListener(ISearchResultListener l) {
		fListeners.remove(l);
	}

	private void notifyListeners(File file) {
		SearchResultEvent event = new FileSearchResultEvent(this, file);

		for (Object listener : fListeners.getListeners())
			((ISearchResultListener) listener).searchResultChanged(event);
	}

	public void addFile(File file) {
		fResult.add(file);
		notifyListeners(file);
	}
}
