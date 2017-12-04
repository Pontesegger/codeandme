package com.codeandme.debugger.textinterpreter.debugger.breakpoints;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.ITextEditor;

import com.codeandme.debugger.textinterpreter.debugger.model.TextDebugModelPresentation;

public class TextLineBreakpointTarget implements IToggleBreakpointsTarget {

	/**
	 * Returns the editor being used to edit a txt file, associated with the given part, or <code>null</code> if none.
	 * 
	 * @param part
	 *            workbench part
	 * @return the editor being used to edit a txt file, associated with the given part, or <code>null</code> if none
	 */
	private static ITextEditor getEditor(IWorkbenchPart part) {
		if (part instanceof ITextEditor) {
			ITextEditor editorPart = (ITextEditor) part;
			IResource resource = (IResource) editorPart.getEditorInput().getAdapter(IResource.class);
			if (resource != null) {
				String extension = resource.getFileExtension();
				if (extension != null && extension.equals("txt"))
					return editorPart;
			}
		}

		return null;
	}

	@Override
	public boolean canToggleLineBreakpoints(IWorkbenchPart part, ISelection selection) {
		return true;
	}

	@Override
	public void toggleLineBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
		ITextEditor textEditor = getEditor(part);
		if (textEditor != null) {
			IResource resource = (IResource) textEditor.getEditorInput().getAdapter(IResource.class);
			ITextSelection textSelection = (ITextSelection) selection;
			int lineNumber = textSelection.getStartLine();
			IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(TextDebugModelPresentation.ID);
			for (int i = 0; i < breakpoints.length; i++) {
				IBreakpoint breakpoint = breakpoints[i];
				if (resource.equals(breakpoint.getMarker().getResource())) {
					if (((ILineBreakpoint) breakpoint).getLineNumber() == (lineNumber + 1)) {
						// existing breakpoint; delete
						breakpoint.delete();
						return;
					}
				}
			}

			// new breakpoint; create
			TextLineBreakpoint lineBreakpoint = new TextLineBreakpoint(resource, lineNumber + 1);
			DebugPlugin.getDefault().getBreakpointManager().addBreakpoint(lineBreakpoint);
		}
	}

	@Override
	public boolean canToggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) {
		return false;
	}

	@Override
	public void toggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
		throw new RuntimeException("toggleMethodBreakpoints() not supported");
	}

	@Override
	public boolean canToggleWatchpoints(IWorkbenchPart part, ISelection selection) {
		return false;
	}

	@Override
	public void toggleWatchpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
		throw new RuntimeException("toggleWatchpoints() not supported");
	}
}
