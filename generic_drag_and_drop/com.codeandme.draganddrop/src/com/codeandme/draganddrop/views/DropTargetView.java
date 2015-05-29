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
package com.codeandme.draganddrop.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.PluginDropAdapter;
import org.eclipse.ui.part.PluginTransfer;
import org.eclipse.ui.part.ViewPart;

public class DropTargetView extends ViewPart {

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		final Text txtInput = new Text(parent, SWT.BORDER | SWT.MULTI);
		txtInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		txtInput.setText("Drop text from Drag Source View to this box");

		int operations = DND.DROP_MOVE | DND.DROP_COPY;
		DropTarget target = new DropTarget(txtInput, operations);

		Transfer[] types = new Transfer[] { PluginTransfer.getInstance() };
		target.setTransfer(types);

		target.addDropListener(new PluginDropAdapter(null) {
			@Override
			protected int determineLocation(DropTargetEvent event) {
				return LOCATION_ON;
			}

			@Override
			protected Object getCurrentTarget() {
				return txtInput;
			}
		});
	}

	@Override
	public void setFocus() {
	}
}
