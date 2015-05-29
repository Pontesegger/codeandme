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
package com.codeandme.draganddrop;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.IDropActionDelegate;

public class TextDropActionDelegate implements IDropActionDelegate {

	public static final String ID = "com.codeandme.draganddrop.dropText";

	@Override
	public boolean run(Object source, Object target) {
		if (source instanceof byte[]) {
			if (target instanceof IContainer) {
				IContainer parent = (IContainer) target;
				IFile file = parent.getFile(new Path("dropped text.txt"));
				if (!file.exists()) {
					try {
						file.create(new ByteArrayInputStream((byte[]) source), true, new NullProgressMonitor());
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}

				return true;

			} else if (target instanceof Text)
				((Text) target).setText(new String((byte[]) source));
		}

		return false;
	}
}
