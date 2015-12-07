package org.eclipse.ease.lang.beanshell;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.eclipse.ease.Logger;
import org.eclipse.ease.modules.AbstractCodeFactory;
import org.eclipse.ease.modules.IEnvironment;
import org.eclipse.ease.modules.IScriptFunctionModifier;
import org.eclipse.ease.modules.ModuleHelper;

public class BeanShellCodeFactory extends AbstractCodeFactory {

	public static final List<String> RESERVED_KEYWORDS = Arrays.asList("abstract", "continue", "for", "new", "switch", "assert", "default", "goto", "package",
			"synchronized", "boolean", "do", "if", "private", "this", "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public",
			"throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static",
			"void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while");

	private static boolean isValidMethodName(final String methodName) {
		return BeanShellHelper.isSaveName(methodName) && !RESERVED_KEYWORDS.contains(methodName);
	}

	@Override
	public String getSaveVariableName(final String variableName) {
		return BeanShellHelper.getSaveName(variableName);
	}

	@Override
	public String createFunctionWrapper(final IEnvironment environment, final String moduleVariable, final Method method) {

		final StringBuilder scriptCode = new StringBuilder();

		// parse parameters
		final List<Parameter> parameters = ModuleHelper.getParameters(method);

		// build parameter string
		final StringBuilder parameterList = new StringBuilder();
		for (final Parameter parameter : parameters)
			parameterList.append(", ").append(parameter.getName());

		if (parameterList.length() > 2)
			parameterList.delete(0, 2);

		final StringBuilder body = new StringBuilder();

		// insert hooked pre execution code
		body.append(getPreExecutionCode(environment, method));

		// insert deprecation warnings
		if (ModuleHelper.isDeprecated(method))
			body.append("\tprintError('" + method.getName() + "() is deprecated. Consider updating your code.');\n");

		// insert method call
		body.append("\t ");
		if (!method.getReturnType().equals(Void.TYPE))
			body.append(IScriptFunctionModifier.RESULT_NAME).append(" = ");

		body.append(moduleVariable).append('.').append(method.getName()).append('(');
		body.append(parameterList);
		body.append(");\n");

		// insert hooked post execution code
		body.append(getPostExecutionCode(environment, method));

		// insert return statement
		if (!method.getReturnType().equals(Void.TYPE))
			body.append("\treturn ").append(IScriptFunctionModifier.RESULT_NAME).append(";\n");

		// build function declarations
		for (final String name : getMethodNames(method)) {
			if (!isValidMethodName(name)) {
				Logger.error(IPluginConstants.PLUGIN_ID,
						"The method name \"" + name + "\" from the module \"" + moduleVariable + "\" can not be wrapped because it's name is reserved");

			} else if (!name.isEmpty()) {
				scriptCode.append(name).append("(").append(parameterList).append(") {\n");
				scriptCode.append(body);
				scriptCode.append("}\n");
			}
		}

		return scriptCode.toString();
	}

	@Override
	public String classInstantiation(final Class<?> clazz, final String[] parameters) {
		final StringBuilder code = new StringBuilder();
		code.append("new ").append(clazz.getName()).append("(");

		if (parameters != null) {
			for (final String parameter : parameters)
				code.append(parameter).append(", ");

			if (parameters.length > 0)
				code.delete(code.length() - 2, code.length());
		}

		code.append(")");

		return code.toString();
	}

	@Override
	public String createFinalFieldWrapper(final IEnvironment environment, final String moduleVariable, final Field field) {
		return getSaveVariableName(field.getName()) + " = " + moduleVariable + "." + field.getName() + ";\n";
	}

	@Override
	protected String getNullString() {
		return "null";
	}
}
