package com.codeandme.touchpoint.action;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.p2.engine.spi.ProvisioningAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public class MyAction extends ProvisioningAction {

	@Override
	public IStatus execute(Map<String, Object> parameters) {
		System.out.println("**************************************************** Feature configuration");
		for (String key : parameters.keySet())
			System.out.println("Key: " + key + ", value: " + parameters.get(key));

		if (Display.getDefault() != null)
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Feature configuration", "Your feature is currently configured.");
				}
			});

		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(Map<String, Object> parameters) {
		return Status.OK_STATUS;
	}
}