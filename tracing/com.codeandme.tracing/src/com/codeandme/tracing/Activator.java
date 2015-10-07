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
package com.codeandme.tracing;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static final String PLUGIN_ID = "com.codeandme.tracing";

	public static final boolean DEBUG = "true".equalsIgnoreCase(Platform.getDebugOption("com.codeandme.tracing/debug"));

	private static final boolean DEBUG_ACTIVATOR = DEBUG | "true".equalsIgnoreCase(Platform.getDebugOption("com.codeandme.tracing/debug/activator"));

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		if (DEBUG_ACTIVATOR)
			System.out.println("Activator started");

		new MyClass().toString();

		Activator.context = bundleContext;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (DEBUG_ACTIVATOR)
			System.out.println("Activator stopped");

		Activator.context = null;
	}

	public static void debug(boolean flag, String message) {
		if (flag)
			System.out.println(PLUGIN_ID + ": " + message);
	}
}
