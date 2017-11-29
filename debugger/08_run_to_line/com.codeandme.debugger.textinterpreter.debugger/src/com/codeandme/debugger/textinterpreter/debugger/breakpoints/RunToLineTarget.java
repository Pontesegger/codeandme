package com.codeandme.debugger.textinterpreter.debugger.breakpoints;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ISuspendResume;
import org.eclipse.debug.ui.actions.IRunToLineTarget;
import org.eclipse.debug.ui.actions.RunToLineHandler;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

import com.codeandme.debugger.textinterpreter.debugger.model.TextDebugTarget;
import com.codeandme.debugger.textinterpreter.debugger.model.TextStackFrame;

public class RunToLineTarget implements IRunToLineTarget {

	@Override
	public void runToLine(IWorkbenchPart part, ISelection selection, ISuspendResume target) throws CoreException {
		if (target instanceof IAdaptable) {
			IDebugTarget debugTarget = (IDebugTarget) ((IAdaptable) target).getAdapter(IDebugTarget.class);
			if (debugTarget instanceof TextDebugTarget) {
				IBreakpoint breakpoint = new TextRunToLineBreakpoint(((TextDebugTarget) debugTarget).getFile(), getLineNumber(selection));
				RunToLineHandler handler = new RunToLineHandler(debugTarget, target, breakpoint);
				handler.run(new NullProgressMonitor());
			}
		}
	}

	@Override
	public boolean canRunToLine(IWorkbenchPart part, ISelection selection, ISuspendResume target) {
		return (target instanceof TextStackFrame);
	}

	private static int getLineNumber(ISelection selection) {
		if (selection instanceof ITextSelection)
			// text selections are 0 based
			return ((ITextSelection) selection).getStartLine() + 1;

		return 0;
	}
}
