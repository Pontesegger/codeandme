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
package com.codeandme.custombuilder.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;

import com.codeandme.custombuilder.builders.MyBuilder;

public class RemoveBuilder extends AbstractHandler implements IHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IProject project = AddBuilder.getProject(event);

		if (project != null) {
			try {
				final IProjectDescription description = project.getDescription();
				final List<ICommand> commands = new ArrayList<ICommand>();
				commands.addAll(Arrays.asList(description.getBuildSpec()));

				for (final ICommand buildSpec : description.getBuildSpec()) {
					if (MyBuilder.BUILDER_ID.equals(buildSpec.getBuilderName())) {
						// remove builder
						commands.remove(buildSpec);
					}
				}

				description.setBuildSpec(commands.toArray(new ICommand[commands.size()]));
				project.setDescription(description, null);
			} catch (final CoreException e) {
				// TODO could not read/write project description
				e.printStackTrace();
			}
		}

		return null;
	}
}
