package com.codeandme.debugger.textinterpreter.debugger.breakpoints;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.LineBreakpoint;

import com.codeandme.debugger.textinterpreter.debugger.Activator;
import com.codeandme.debugger.textinterpreter.debugger.model.TextDebugModelPresentation;

public class TextLineBreakpoint extends LineBreakpoint {

	/**
	 * Default constructor needed by the Eclipse debug framework. Do not remove!
	 */
	public TextLineBreakpoint() {
	}

	public TextLineBreakpoint(final IResource resource, final int lineNumber) throws CoreException {
		this(resource, lineNumber, true);
	}

	protected TextLineBreakpoint(final IResource resource, final int lineNumber, final boolean persistent) throws CoreException {
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				IMarker marker = resource.createMarker(Activator.PLUGIN_ID + ".textLineBreakpointMarker");
				setMarker(marker);
				marker.setAttribute(IBreakpoint.ENABLED, true);
				marker.setAttribute(IBreakpoint.PERSISTED, persistent);
				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
				marker.setAttribute(IBreakpoint.ID, getModelIdentifier());
				marker.setAttribute(IMarker.MESSAGE, "Line Breakpoint: " + resource.getName() + " [line: " + lineNumber + "]");
			}
		};
		run(getMarkerRule(resource), runnable);
	}

	@Override
	public String getModelIdentifier() {
		return TextDebugModelPresentation.ID;
	}
}