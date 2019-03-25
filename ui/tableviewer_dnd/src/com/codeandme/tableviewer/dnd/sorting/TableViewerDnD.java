package com.codeandme.tableviewer.dnd.sorting;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;

public class TableViewerDnD {

	protected Shell fshell;

	private DataModel fModel = new DataModel();
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TableViewerDnD window = new TableViewerDnD();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		fshell.open();
		fshell.layout();
		while (!fshell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		fshell = new Shell();
		fshell.setSize(450, 300);
		fshell.setText("Code & me DnD Example");
		fshell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(fshell, SWT.NONE);
		TableColumnLayout tcl_composite = new TableColumnLayout();
		composite.setLayout(tcl_composite);
		
		TableViewer tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
		tcl_composite.setColumnData(tblclmnNewColumn, new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		tblclmnNewColumn.setText("Name");
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider());
		
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(fModel);
		
		DnDSortingSupport.addDnDSortingSupport(tableViewer, new DnDSortingSupport.ListDataManipulator<String>(fModel));
	}
}
