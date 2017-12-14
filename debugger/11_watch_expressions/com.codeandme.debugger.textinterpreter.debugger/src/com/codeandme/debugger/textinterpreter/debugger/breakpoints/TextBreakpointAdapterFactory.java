package com.codeandme.debugger.textinterpreter.debugger.breakpoints;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.ui.texteditor.ITextEditor;

public class TextBreakpointAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (adaptableObject instanceof ITextEditor) {
			if (IToggleBreakpointsTarget.class.equals(adapterType)) {
				ITextEditor editorPart = (ITextEditor) adaptableObject;
				IResource resource = (IResource) editorPart.getEditorInput().getAdapter(IResource.class);
				if (resource != null) {
					String extension = resource.getFileExtension();
					if (extension != null && extension.equals("txt")) {
						return (T) new TextLineBreakpointTarget();
					}
				}
			}
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IToggleBreakpointsTarget.class };
	}
}
