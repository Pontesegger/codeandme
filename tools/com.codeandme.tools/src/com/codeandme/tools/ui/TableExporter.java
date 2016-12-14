/*******************************************************************************
 * Copyright (c) 2016 Christian Pontesegger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Christian Pontesegger - initial implementation
 *******************************************************************************/
package com.codeandme.tools.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Exports a JFace {@link TableViewer} to a string representation. The exporter
 * considers filtering and sorting to export data the same way as displayed in
 * the viewer.
 */
public class TableExporter {

	private TableViewer fViewer;
	private boolean fPrintHeader = true;
	private boolean fFilterElements = true;
	private boolean fSortElements = true;

	/**
	 * Constructor.
	 * 
	 * @param viewer
	 *            tableViewer to export
	 */
	public TableExporter(TableViewer viewer) {
		fViewer = viewer;
	}

	/**
	 * Define if the table header should be printed. Defaults to
	 * <code>true</code>.
	 * 
	 * @param printHeader
	 *            <code>true</code> to print the header
	 */
	public void setPrintHeader(boolean printHeader) {
		fPrintHeader = printHeader;
	}

	/**
	 * Define if table elements should be filtered before output. Defaults to
	 * <code>true</code>
	 * 
	 * @param filterElements
	 *            <code>true</code> to enable filtering
	 */
	public void setFilterElements(boolean filterElements) {
		fFilterElements = filterElements;
	}

	/**
	 * Define if table elements should be sorted before output. Defaults to
	 * <code>true</code>
	 * 
	 * @param filterElements
	 *            <code>true</code> to enable sorting
	 */
	public void setSortElements(boolean sortElements) {
		fSortElements = sortElements;
	}

	/**
	 * Print table representation to the given output stream.
	 * 
	 * @param output
	 *            output stream
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void print(OutputStream output) throws IOException {
		if (fPrintHeader)
			printHeader(output);

		printData(output);
	}

	/**
	 * Prints a single data cell content.
	 * 
	 * @param text
	 *            cell content to print
	 * @param output
	 *            output stream
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void printText(String text, OutputStream output) throws IOException {
		output.write(text.getBytes());
	}

	/**
	 * Retrieve all string representations for the table header.
	 * 
	 * @return data strings
	 */
	protected String[] getHeaderData() {
		String[] output = new String[fViewer.getTable().getColumnCount()];

		int[] order = fViewer.getTable().getColumnOrder();
		for (int index = 0; index < fViewer.getTable().getColumnCount(); index++) {
			TableColumn column = fViewer.getTable().getColumn(order[index]);
			output[index] = column.getText();
		}

		return output;
	}

	/**
	 * Prints the whole header.
	 * 
	 * @param output
	 *            output stream
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void printHeader(OutputStream output) throws IOException {
		String[] textData = getHeaderData();
		for (int index = 0; index < textData.length; index++) {
			if (index > 0)
				printColumnDelimiter(output);

			if (textData[index] != null)
				printText(textData[index], output);
		}

		output.write('\n');
	}

	/**
	 * Prints the whole data area.
	 * 
	 * @param output
	 *            output stream
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void printData(OutputStream output) throws IOException {
		IContentProvider contentProvider = fViewer.getContentProvider();
		if (contentProvider instanceof IStructuredContentProvider) {
			Object input = fViewer.getInput();
			Object[] elements = ((IStructuredContentProvider) contentProvider).getElements(input);

			// filter elements
			if (fFilterElements) {
				for (ViewerFilter filter : fViewer.getFilters())
					elements = filter.filter(fViewer, (Object) null, elements);
			}

			// sort elements
			if (fSortElements) {
				ViewerComparator comparator = fViewer.getComparator();
				if (comparator != null)
					comparator.sort(fViewer, elements);
			}

			// print elements
			for (Object element : elements)
				printDataRow(output, element);
		}
	}

	/**
	 * Retrieve all string representations for a table row.
	 * 
	 * @param element
	 *            table element
	 * @return data strings
	 */
	protected String[] getTextData(Object element) {
		String[] output = new String[fViewer.getTable().getColumnCount()];

		IBaseLabelProvider labelProvider = fViewer.getLabelProvider();
		ITableLabelProvider globalLabelProvider = null;
		if (labelProvider instanceof ITableLabelProvider)
			globalLabelProvider = (ITableLabelProvider) labelProvider;

		int[] order = fViewer.getTable().getColumnOrder();
		for (int columnIndex = 0; columnIndex < fViewer.getTable().getColumnCount(); columnIndex++) {
			String text = null;
			if (globalLabelProvider != null)
				text = globalLabelProvider.getColumnText(element, order[columnIndex]);

			else if (fViewer.getLabelProvider(columnIndex) instanceof ILabelProvider)
				text = ((ILabelProvider) fViewer.getLabelProvider(order[columnIndex])).getText(element);

			output[columnIndex] = text;
		}

		return output;
	}

	/**
	 * Prints a single data row.
	 * 
	 * @param output
	 *            output stream
	 * @param element
	 *            data row elements
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void printDataRow(OutputStream output, Object element) throws IOException {
		String[] textData = getTextData(element);
		for (int index = 0; index < textData.length; index++) {
			if (index > 0)
				printColumnDelimiter(output);

			if (textData[index] != null)
				printText(textData[index], output);
		}

		output.write('\n');
	}

	/**
	 * Prints a single data row.
	 * 
	 * @param output
	 *            output stream
	 * @param element
	 *            data row elements
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void printColumnDelimiter(OutputStream output) throws IOException {
		output.write(',');
	}

	@Override
	public String toString() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try {
			print(output);
		} catch (IOException e) {
			// not expected to happen as ByteArrayOutputStream does not throw
			e.printStackTrace();
		}

		return output.toString();
	}
}
