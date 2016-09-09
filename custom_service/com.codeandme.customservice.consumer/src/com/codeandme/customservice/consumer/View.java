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
package com.codeandme.customservice.consumer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.codeandme.customservice.ICustomService;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

public class View extends ViewPart {
	private Text text;

	public View() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		text = new Text(parent, SWT.BORDER | SWT.WRAP | SWT.MULTI);

		ICustomService customService = (ICustomService) getSite().getService(ICustomService.class);

		if (customService != null)
			text.setText(customService.toString());
		else
			text.setText("Service not available");
	}

	@Override
	public void setFocus() {
	}
}
