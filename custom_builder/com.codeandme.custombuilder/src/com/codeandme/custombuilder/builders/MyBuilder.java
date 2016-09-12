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
package com.codeandme.custombuilder.builders;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class MyBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "com.codeandme.custombuilder.myBuilder";

	@Override
	protected IProject[] build(final int kind, final Map<String, String> args, final IProgressMonitor monitor)
			throws CoreException {

		System.out.println("Custom builder triggered");

		// get the project to build
		getProject();

		switch (kind) {

		case FULL_BUILD:
			break;

		case INCREMENTAL_BUILD:
			break;

		case AUTO_BUILD:
			break;
		}

		return null;
	}
}
