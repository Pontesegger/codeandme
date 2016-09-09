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
package com.codeandme.customservice;

public class CustomServiceImpl implements ICustomService {

	private static CustomServiceImpl fInstance = null;

	public static CustomServiceImpl getInstance() {
		if (fInstance == null)
			fInstance = new CustomServiceImpl();

		return fInstance;
	}

	private CustomServiceImpl() {
	}
}
