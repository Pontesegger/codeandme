package org.beanshell.playground;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import bsh.EvalError;
import bsh.Interpreter;

public class RunEmbedded {

	private static final String[] CODE_FRAGMENTS = new String[] { "2+3", "foo*10", "new java.io.File(\"/\").exists()",
			"sum(a,b){\n return a+b;\n }\nsum(40,2)" };

	public static void main(String[] args) throws EvalError, FileNotFoundException, IOException {
		Interpreter interpreter = new Interpreter(); // Construct an interpreter

		// set variables
		interpreter.set("foo", 5);
		interpreter.set("date", new Date());

		// get variables
		Object date = interpreter.get("date");

		// evaluate statements
		for (String code : CODE_FRAGMENTS) {
			System.out.println(interpreter.eval(code));
		}
	}
}
