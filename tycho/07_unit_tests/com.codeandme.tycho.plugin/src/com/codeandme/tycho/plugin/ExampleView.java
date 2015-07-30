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
package com.codeandme.tycho.plugin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public class ExampleView extends ViewPart {
	public static final String VIEW_ID = "com.codeandme.tycho.views.example";

	private Label lblNewLabel;

	public ExampleView() {
	}

	@Override
	public void createPartControl(final Composite parent) {

		lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setText("Hello world!");
	}

	@Override
	public void setFocus() {
		lblNewLabel.setFocus();
	}
}
