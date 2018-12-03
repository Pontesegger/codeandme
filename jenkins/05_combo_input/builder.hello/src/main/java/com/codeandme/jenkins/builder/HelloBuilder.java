/*******************************************************************************
 * Copyright (c) 2018 Christian Pontesegger.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * Contributors:
 *     Christian Pontesegger - initial API and implementation
 *******************************************************************************/
package com.codeandme.jenkins.builder;

import java.io.IOException;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.AbortException;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.ListBoxModel.Option;
import jenkins.tasks.SimpleBuildStep;

public class HelloBuilder extends Builder implements SimpleBuildStep {

	private boolean fFailBuild;

	private String fBuildMessage;

	private String fBuildDelay;

	@DataBoundConstructor
	public HelloBuilder(boolean failBuild, String buildMessage, String buildDelay) {
		fFailBuild = failBuild;
		fBuildMessage = buildMessage;
		fBuildDelay = buildDelay;
	}

	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener)
			throws InterruptedException, IOException {
		listener.getLogger().println("This is the Hello plugin!");
		listener.getLogger().println(getBuildMessage());

		switch (getBuildDelay()) {
		case "long":
			Thread.sleep(10 * 1000);
			break;

		case "short":
			Thread.sleep(3 * 1000);
			break;

		case "none":
			// fall through
		default:
			// nothing to do
		}

		if (isFailBuild())
			throw new AbortException("Build error forced by plugin settings");
	}

	public boolean isFailBuild() {
		return fFailBuild;
	}

	public String getBuildMessage() {
		return fBuildMessage;
	}

	public String getBuildDelay() {
		return fBuildDelay;
	}

	@Symbol("greet")
	@Extension
	public static final class Descriptor extends BuildStepDescriptor<Builder> {

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return "Code & Me - Hello World";
		}

		public FormValidation doCheckBuildMessage(@QueryParameter String buildMessage) {
			if (buildMessage.isEmpty())
				return FormValidation.error("Please provide a build message.");
			else if (buildMessage.trim().isEmpty())
				return FormValidation.error("White space is not sufficient for a build message.");
			else
				return FormValidation.ok();
		}

		public String getDefaultBuildMessage() {
			return "This is a great build";
		}

		public ListBoxModel doFillBuildDelayItems(@QueryParameter String buildDelay) {
			ListBoxModel model = new ListBoxModel();

			model.add(new Option("None", "none", "none".equals(buildDelay)));
			model.add(new Option("Short", "short", "short".equals(buildDelay)));
			model.add(new Option("Long", "long" , "long".equals(buildDelay)));

			return model;
		}
	}
}
