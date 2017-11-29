/*******************************************************************************
 * Copyright (c) 2013 Christian Pontesegger.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Christian Pontesegger - initial API and implementation
 *******************************************************************************/
package com.codeandme.debugger.textinterpreter.ui.tabs;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;

public class ProjectContentProvider extends BaseWorkbenchContentProvider implements ITreeContentProvider {

    @Override
    public Object[] getChildren(final Object element) {
        if (element instanceof IProject)
            return new Object[0];

        return super.getChildren(element);
    }
}
