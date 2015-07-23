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
package com.codeandme.searchprovider.ui;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.codeandme.searchprovider.FileSearchQuery;

public class FileSearchPage extends DialogPage implements ISearchPage {

	private ISearchPageContainer fContainer;

	private Text txtRoot;
	private Text txtFilter;
	private Button chkRecursive;

	public FileSearchPage() {
	}

	@Override
	public boolean performAction() {
		FileSearchQuery searchQuery = new FileSearchQuery(txtRoot.getText(), txtFilter.getText(), chkRecursive.getSelection());
		NewSearchUI.runQueryInForeground(fContainer.getRunnableContext(), searchQuery);

		return true;
	}

	@Override
	public void setContainer(ISearchPageContainer container) {
		fContainer = container;
	}

	@Override
	public void createControl(Composite parent) {
		Composite root = new Composite(parent, SWT.NULL);
		root.setLayout(new GridLayout(3, false));

		Label lblRoot = new Label(root, SWT.NONE);
		lblRoot.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRoot.setText("Root folder");

		txtRoot = new Text(root, SWT.BORDER);
		txtRoot.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button button = new Button(root, SWT.NONE);
		button.setText("Browse ...");
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				String result = dialog.open();
				if (result != null)
					txtRoot.setText(result);
			}
		});

		Label lblFilter = new Label(root, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText("File filter");

		txtFilter = new Text(root, SWT.BORDER);
		txtFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(root, SWT.NONE);
		new Label(root, SWT.NONE);

		chkRecursive = new Button(root, SWT.CHECK);
		chkRecursive.setText("recursive");
		new Label(root, SWT.NONE);

		// need to set the root element
		setControl(root);
	}
}
