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
package com.codeandme.custombuilder.propertytester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;

import com.codeandme.custombuilder.commands.AddBuilder;

public class TestBuilderEnabled extends PropertyTester {

	private static final String IS_ENABLED = "isEnabled";

	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {

		if (IS_ENABLED.equals(property)) {
			final IProject project = (IProject) Platform.getAdapterManager().getAdapter(receiver, IProject.class);

			if (project != null)
				return AddBuilder.hasBuilder(project);
		}

		return false;
	}
}
