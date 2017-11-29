package com.codeandme.debugger.textinterpreter.debugger;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {
	
	public static final String PLUGIN_ID = "com.codeandme.debugger.textinterpreter.debugger";
	
	private static Activator fInstance;

	public static Activator getDefault() {
		return fInstance;
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);

		fInstance = this;
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		fInstance = null;

		super.stop(context);
	}
}
