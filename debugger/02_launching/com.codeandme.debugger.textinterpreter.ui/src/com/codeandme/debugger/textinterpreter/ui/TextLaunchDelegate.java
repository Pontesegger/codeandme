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
package com.codeandme.debugger.textinterpreter.ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.debug.ui.ILaunchShortcut2;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import com.codeandme.textinterpreter.TextInterpreter;

public class TextLaunchDelegate implements ILaunchShortcut, ILaunchShortcut2, ILaunchConfigurationDelegate {

	/**
	 * Retrieve the source file from an {@link ILaunchConfiguration}.
	 * 
	 * @param configuration
	 *            configuration to use
	 * @return source file or <code>null</code>
	 * @throws CoreException
	 */
	public static IFile getSourceFile(final ILaunchConfiguration configuration) throws CoreException {
		String projectName = configuration.getAttribute(TextLaunchConstants.PROJECT, "");
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		if (project.exists()) {
			String fileName = configuration.getAttribute(TextLaunchConstants.FILE_LOCATION, "");
			IFile file = project.getFile(fileName);

			if (file.exists())
				return file;
		}

		return null;
	}

	/**
	 * Read an {@link IFile}.
	 * 
	 * @param file
	 *            {@link IFile} to read
	 * @return String content of file
	 * @throws IOException
	 * @throws CoreException
	 */
	private static String toString(final IFile file) throws IOException, CoreException {
		InputStreamReader reader = new InputStreamReader(file.getContents());
		final StringBuffer out = new StringBuffer();

		final char[] buffer = new char[1024];
		int bytes = 0;
		do {
			bytes = reader.read(buffer);
			if (bytes > 0)
				out.append(buffer, 0, bytes);
		} while (bytes != -1);

		return out.toString();
	}

	// **********************************************************************
	// ILaunchShortcut
	// **********************************************************************

	@Override
	public final void launch(final IEditorPart editor, final String mode) {
		launch(getLaunchableResource(editor), mode);
	}

	@Override
	public final void launch(final ISelection selection, final String mode) {
		launch(getLaunchableResource(selection), mode);
	}

	// **********************************************************************
	// ILaunchShortcut2
	// **********************************************************************

	@Override
	public final IResource getLaunchableResource(final IEditorPart editorpart) {
		final IEditorInput input = editorpart.getEditorInput();
		if (input instanceof FileEditorInput)
			return ((FileEditorInput) input).getFile();

		return null;
	}

	@Override
	public final IResource getLaunchableResource(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			for (final Object element : ((IStructuredSelection) selection).toArray()) {
				if (element instanceof IFile)
					return (IResource) element;
			}
		}

		return null;
	}

	@Override
	public final ILaunchConfiguration[] getLaunchConfigurations(final IEditorPart editorpart) {
		return getLaunchConfgurations(getLaunchableResource(editorpart));
	}

	@Override
	public final ILaunchConfiguration[] getLaunchConfigurations(final ISelection selection) {
		return getLaunchConfgurations(getLaunchableResource(selection));
	}

	// **********************************************************************
	// ILaunchConfigurationDelegate
	// **********************************************************************

	@Override
	public void launch(final ILaunchConfiguration configuration, final String mode, final ILaunch launch, final IProgressMonitor monitor) throws CoreException {
		IFile file = getSourceFile(configuration);
		if (file != null) {
			// we have a valid script, lets feed it to the script engine
			launch(file, configuration, mode, launch, monitor);
		}
	}

	// **********************************************************************
	// internal stuff
	// **********************************************************************

	/**
	 * Get all launch configurations that target a dedicated resource file.
	 * 
	 * @param resource
	 *            root file to execute
	 * @return {@link ILaunchConfiguration}s using resource
	 */
	private ILaunchConfiguration[] getLaunchConfgurations(IResource resource) {
		List<ILaunchConfiguration> configurations = new ArrayList<ILaunchConfiguration>();

		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(TextLaunchConstants.LAUNCH_CONFIGURATION_TYPE_ID);

		// try to find existing configurations using the same file
		try {
			for (ILaunchConfiguration configuration : manager.getLaunchConfigurations(type)) {
				try {
					IFile file = getSourceFile(configuration);
					if (resource.equals(file))
						configurations.add(configuration);

				} catch (CoreException e) {
					// could not read configuration, ignore
				}
			}
		} catch (CoreException e) {
			// could not load configurations, ignore
		}

		return configurations.toArray(new ILaunchConfiguration[configurations.size()]);
	}

	/**
	 * Launch a resource. Try to launch using a launch configuration. Used for
	 * contextual launches
	 * 
	 * @param file
	 *            source file
	 * @param mode
	 *            launch mode
	 */
	private void launch(final IResource file, final String mode) {

		if (file instanceof IFile) {
			// try to save dirty editors
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().saveAllEditors(true);

			try {
				ILaunchConfiguration[] configurations = getLaunchConfgurations(file);
				if (configurations.length == 0) {
					// no configuration found, create new one
					ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
					ILaunchConfigurationType type = manager.getLaunchConfigurationType(TextLaunchConstants.LAUNCH_CONFIGURATION_TYPE_ID);

					ILaunchConfigurationWorkingCopy configuration = type.newInstance(null, file.getName());
					configuration.setAttribute(TextLaunchConstants.PROJECT, file.getProject().getName());
					configuration.setAttribute(TextLaunchConstants.FILE_LOCATION, file.getProjectRelativePath().toPortableString());

					// save and return new configuration
					configuration.doSave();

					configurations = new ILaunchConfiguration[] { configuration };
				}

				// launch
				configurations[0].launch(mode, new NullProgressMonitor());

			} catch (CoreException e) {
				// could not create launch configuration, run file directly
				launch((IFile) file, null, mode, null, new NullProgressMonitor());
			}
		}
	}

	/**
	 * Execute script code from an {@link IFile}.
	 * 
	 * @param file
	 *            file to execute
	 * @param configuration
	 *            launch configuration
	 * @param mode
	 *            launch mode
	 * @param launch
	 * @param monitor
	 */
	private void launch(final IFile file, final ILaunchConfiguration configuration, final String mode, final ILaunch launch, final IProgressMonitor monitor) {
		// create new interpreter
		TextInterpreter interpreter = new TextInterpreter();

		try {
			interpreter.setCode(toString(file));
			interpreter.schedule();

		} catch (Exception e) {
			// TODO handle this exception (but for now, at least know it
			// happened)
			throw new RuntimeException(e);
		}
	}
}