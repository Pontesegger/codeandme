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
import java.io.FileFilter;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;

public class FileSearchQuery implements ISearchQuery {

	private final File fRoot;
	private final String fFilter;
	private final boolean fRecursive;

	private final FileSearchResult fSearchResult;

	public FileSearchQuery(String root, String filter, boolean recursive) {
		fRoot = (root.isEmpty()) ? File.listRoots()[0] : new File(root);
		fFilter = filter;
		fRecursive = recursive;
		fSearchResult = new FileSearchResult(this);
	}

	@Override
	public IStatus run(IProgressMonitor monitor) throws OperationCanceledException {
		Collection<File> entries = new HashSet<File>();
		entries.add(fRoot);

		do {
			File entry = entries.iterator().next();
			entries.remove(entry);

			entry.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					if ((pathname.isFile()) && (pathname.getName().contains(fFilter))) {
						// accept file
						fSearchResult.addFile(pathname);

						return true;
					}

					if ((pathname.isDirectory()) && (fRecursive))
						entries.add(pathname);

					return false;
				}
			});

		} while (!entries.isEmpty());

		return Status.OK_STATUS;
	}

	@Override
	public String getLabel() {
		return "Filesystem search";
	}

	@Override
	public boolean canRerun() {
		return true;
	}

	@Override
	public boolean canRunInBackground() {
		return true;
	}

	@Override
	public ISearchResult getSearchResult() {
		return fSearchResult;
	}
}
