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

import org.eclipse.core.expressions.PropertyTester;

public class ExamplePropertyTester extends PropertyTester {

	private static final String NAME = "name";
	private static final String VALIDATED = "validated";

	public ExamplePropertyTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (NAME.equals(property))
			return receiver.toString().equals(expectedValue);

		if (VALIDATED.equals(property)) {
			// do some background checks to see if user is validated
			return true;
		}

		return false;
	}
}
