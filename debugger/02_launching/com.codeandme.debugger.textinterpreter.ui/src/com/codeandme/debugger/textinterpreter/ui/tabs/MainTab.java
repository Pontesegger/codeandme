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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.codeandme.debugger.textinterpreter.ui.TextLaunchConstants;

public class MainTab extends AbstractLaunchConfigurationTab implements ILaunchConfigurationTab {
	private boolean fDisableUpdate = false;
	private final String[] fExtensions;

	private Text fTxtFile;
	private Group fGroup;
	private Text fTxtProject;
	private Button fBtnBrowseProject;

	public MainTab(final String[] extensions) {
		fExtensions = extensions;
	}

	@Override
	public void setDefaults(final ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(TextLaunchConstants.PROJECT, "");
		configuration.setAttribute(TextLaunchConstants.FILE_LOCATION, "");
	}

	@Override
	public void initializeFrom(final ILaunchConfiguration configuration) {
		fDisableUpdate = true;

		fTxtProject.setText("");
		fTxtFile.setText("");

		try {
			fTxtProject.setText(configuration.getAttribute(TextLaunchConstants.PROJECT, ""));
			fTxtFile.setText(configuration.getAttribute(TextLaunchConstants.FILE_LOCATION, ""));
		} catch (CoreException e) {
		}

		fDisableUpdate = false;
	}

	@Override
	public void performApply(final ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(TextLaunchConstants.PROJECT, fTxtProject.getText());
		configuration.setAttribute(TextLaunchConstants.FILE_LOCATION, fTxtFile.getText());
	}

	@Override
	public boolean isValid(final ILaunchConfiguration launchConfig) {
		// allow launch when a file is selected and file exists
		try {
			String projectName = launchConfig.getAttribute(TextLaunchConstants.PROJECT, "");
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			if (project.exists()) {
				String fileName = launchConfig.getAttribute(TextLaunchConstants.FILE_LOCATION, "");
				IFile file = project.getFile(fileName);
				return file.exists();
			}

		} catch (Exception e) {
			// on any configuration error
			setErrorMessage("Invalid text file selected.");
		}

		return false;
	}

	@Override
	public boolean canSave() {
		// allow save when a file location is entered - no matter if the file
		// exists or not
		return (!fTxtProject.getText().isEmpty()) && (!fTxtFile.getText().isEmpty());
	}

	@Override
	public String getMessage() {
		return "Please select a text file.";
	}

	@Override
	public String getName() {
		return "Global";
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void createControl(final Composite parent) {
		Composite topControl = new Composite(parent, SWT.NONE);
		topControl.setLayout(new GridLayout(1, false));

		fGroup = new Group(topControl, SWT.NONE);
		fGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		fGroup.setText("Project");
		fGroup.setLayout(new GridLayout(2, false));

		fTxtProject = new Text(fGroup, SWT.BORDER);
		fTxtProject.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		fBtnBrowseProject = new Button(fGroup, SWT.NONE);
		fBtnBrowseProject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(), new WorkbenchLabelProvider(),
						new ProjectContentProvider());
				dialog.setTitle("Select project");
				dialog.setMessage("Select the project hosting your text file:");
				dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
				if (dialog.open() == Window.OK)
					fTxtProject.setText(((IResource) dialog.getFirstResult()).getName());
			}
		});
		fBtnBrowseProject.setText("Browse...");

		Group grpLaunch = new Group(topControl, SWT.NONE);
		grpLaunch.setLayout(new GridLayout(2, false));
		grpLaunch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpLaunch.setText("Main Script");

		fTxtFile = new Text(grpLaunch, SWT.BORDER);
		fTxtFile.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (!fDisableUpdate)
					updateLaunchConfigurationDialog();
			}
		});
		fTxtFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnBrowseScript = new Button(grpLaunch, SWT.NONE);
		btnBrowseScript.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(), new WorkbenchLabelProvider(),
						new SourceFileContentProvider(fExtensions));
				dialog.setTitle("Select text file");
				dialog.setMessage("Select the text file to execute:");
				dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
				if (dialog.open() == Window.OK)
					fTxtFile.setText(((IFile) dialog.getFirstResult()).getProjectRelativePath().toPortableString());
			}
		});
		btnBrowseScript.setText("Browse...");

		setControl(topControl);
	}
}
