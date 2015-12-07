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
package org.eclipse.ease.lang.beanshell;

import java.util.Random;
import java.util.regex.Pattern;

import org.eclipse.ease.service.IScriptService;
import org.eclipse.ease.service.ScriptService;
import org.eclipse.ease.service.ScriptType;

public final class BeanShellHelper {

	/** Script type identifier for BeanShell. Must match with the script type 'name' from plugin.xml. */
	public static final String SCRIPT_TYPE_BEANSHELL = "BeanShell";

	public static String getSaveName(final String identifier) {
		// check if name is already valid
		if (isSaveName(identifier))
			return identifier;

		// not valid, convert string to valid format
		final StringBuilder buffer = new StringBuilder(identifier.replaceAll("[^a-zA-Z0-9_$]", "_"));

		// check for valid first character
		if (buffer.length() > 0) {
			final char start = buffer.charAt(0);
			if (((start < 65) || ((start > 90) && (start < 97)) || (start > 122)) && (start != '_'))
				buffer.insert(0, '_');
		} else {
			// buffer is empty, create a random string of lowercase letters
			buffer.append('_');
			for (int index = 0; index < new Random().nextInt(20); index++)
				buffer.append('a' + new Random().nextInt(26));
		}

		return buffer.toString();
	}

	public static boolean isSaveName(final String identifier) {
		return Pattern.matches("[a-zA-Z_$][a-zA-Z0-9_$]*", identifier);
	}

	/**
	 * Get the {@link ScriptType} for beanShell scripts.
	 *
	 * @return script type definition
	 */
	public static ScriptType getScriptType() {
		final IScriptService scriptService = ScriptService.getInstance();
		return scriptService.getAvailableScriptTypes().get(SCRIPT_TYPE_BEANSHELL);
	}
}
