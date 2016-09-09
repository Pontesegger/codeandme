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
package com.codeandme.coreexpressions.samples;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.ISourceProvider;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

import com.codeandme.coreexpressions.ExampleSourceProvider;

public class Login extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench().getService(
				ISourceProviderService.class);
		ISourceProvider provider = service.getSourceProvider(ExampleSourceProvider.CURRENT_USER);
		if (provider instanceof ExampleSourceProvider)
			((ExampleSourceProvider) provider).setCurrentUser("John Doe");

		return null;
	}
}
