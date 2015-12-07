/*******************************************************************************
 * Copyright (c) 2015 Christian Pontesegger and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Christian Pontesegger - initial API and implementation
 *******************************************************************************/

package org.eclipse.ease.lang.beanshell;

import org.eclipse.ease.AbstractCodeParser;

public class BeanShellCodeParser extends AbstractCodeParser {

	@Override
	protected boolean hasBlockComment() {
		return true;
	}

	@Override
	protected String getBlockCommentEndToken() {
		return "*/";
	}

	@Override
	protected String getBlockCommentStartToken() {
		return "/*";
	}

	@Override
	protected String getLineCommentToken() {
		return "//";
	}
}
