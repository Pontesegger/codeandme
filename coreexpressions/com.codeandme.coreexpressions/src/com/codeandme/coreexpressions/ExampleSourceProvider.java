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
package com.codeandme.coreexpressions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

public class ExampleSourceProvider extends AbstractSourceProvider {

	public static final String CURRENT_STATUS = "com.codeandme.coreexpressions.currentStatus";
	public static final String CURRENT_USER = "com.codeandme.coreexpressions.currentUser";

	private Object fUser = null;
	private String fStatus = "startup";

	public ExampleSourceProvider() {
	}

	public void setCurrentUser(Object user) {
		fUser = user;
		fStatus = (user == null) ? "logged off" : "logged on";

		fireSourceChanged(ISources.WORKBENCH, CURRENT_USER, fUser);
		fireSourceChanged(ISources.WORKBENCH, CURRENT_STATUS, fStatus);
	}

	@Override
	public void dispose() {
	}

	@Override
	public Map getCurrentState() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(CURRENT_USER, fUser);
		map.put(CURRENT_STATUS, fStatus);

		return map;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] { CURRENT_USER, CURRENT_STATUS };
	}
}
