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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;

public class SourceFileContentProvider extends BaseWorkbenchContentProvider implements ITreeContentProvider {

    private final String[] fExtensions;

    public SourceFileContentProvider(final String[] extensions) {
        fExtensions = extensions;
    }

    @Override
    public Object[] getChildren(final Object element) {
        // remove all file entries where extensions are rejected
        List<Object> children = Arrays.asList(super.getChildren(element));
        List<Object> filteredChildren = new ArrayList<Object>(children);

        for (Object child : children) {
            if ((child instanceof IFile) && (!isAccepted((IFile) child)))
                filteredChildren.remove(child);
        }

        return filteredChildren.toArray();
    }

    private boolean isAccepted(final IFile child) {
        for (String extension : fExtensions) {
            if (extension.equalsIgnoreCase(child.getFileExtension()))
                return true;
        }

        return false;
    }
}
