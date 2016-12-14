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

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.jface.viewers.TableViewer;

/**
 * Exports a JFace {@link TableViewer} to an HTML table. The exporter considers
 * filtering and sorting to export data the same way as displayed in the viewer.
 */
public class HTMLTableExporter extends TableExporter {

	public HTMLTableExporter(TableViewer viewer) {
		super(viewer);
		
		TableExporter exporter = new TableExporter(tableViewer);
		exporter.print(outputStream);
	}

	@Override
	protected void printHeader(OutputStream output) throws IOException {
		output.write("\n\t<tr>".getBytes());

		for (String header : getHeaderData()) {
			output.write("\n\t\t<th>".getBytes());
			printText(header, output);
			output.write("</th>".getBytes());
		}

		output.write("\n\t</tr>".getBytes());
	}

	@Override
	protected void printDataRow(OutputStream output, Object element) throws IOException {
		output.write("\n\t<tr>".getBytes());
		for (String text : getTextData(element)) {
			output.write("\n\t\t<td>".getBytes());
			printText(text, output);
			output.write("</td>".getBytes());
		}
		output.write("\n\t</tr>".getBytes());
	}

	@Override
	public void print(OutputStream output) throws IOException {
		output.write("<table>".getBytes());
		super.print(output);
		output.write("\n</table>\n".getBytes());
	}

	@Override
	protected void printText(String text, OutputStream output) throws IOException {
		// escape HTML output
		text = text.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

		super.printText(text, output);
	}
}
