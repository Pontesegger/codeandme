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
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.PluginTransfer;
import org.eclipse.ui.part.PluginTransferData;
import org.eclipse.ui.part.ViewPart;

import com.codeandme.draganddrop.TextDropActionDelegate;

public class DragSourceView extends ViewPart {

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		final Text txtInput = new Text(parent, SWT.BORDER | SWT.MULTI);
		txtInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		txtInput.setText("Enter text and drag it to the navigator view");

		int operations = DND.DROP_MOVE | DND.DROP_COPY;
		DragSource source = new DragSource(txtInput, operations);

		// DropTarget target = new DropTarget(txtInput, DND.DROP_MOVE | DND.DROP_COPY);
		// target.setTransfer(new Transfer[] { PluginTransfer.getInstance() });

		Transfer[] types = new Transfer[] { TextTransfer.getInstance(), PluginTransfer.getInstance() };
		source.setTransfer(types);

		source.addDragListener(new DragSourceListener() {
			@Override
			public void dragStart(DragSourceEvent event) {
				if (txtInput.getText().length() == 0)
					event.doit = false;
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				// for text drag to editors
				if (TextTransfer.getInstance().isSupportedType(event.dataType))
					event.data = txtInput.getSelectionText();

				// for plugin transfer drags to navigator views
				if (PluginTransfer.getInstance().isSupportedType(event.dataType))
					event.data = new PluginTransferData(TextDropActionDelegate.ID, txtInput.getSelectionText().getBytes());
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
			}
		});
	}

	@Override
	public void setFocus() {
	}
}
