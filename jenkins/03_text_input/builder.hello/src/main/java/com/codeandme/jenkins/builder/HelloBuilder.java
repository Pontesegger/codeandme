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
import jenkins.tasks.SimpleBuildStep;

public class HelloBuilder extends Builder implements SimpleBuildStep {

	private boolean fFailBuild;

	private String fBuildMessage;

	@DataBoundConstructor
	public HelloBuilder(boolean failBuild, String buildMessage) {
		fFailBuild = failBuild;
		fBuildMessage = buildMessage;
	}

	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener)
			throws InterruptedException, IOException {
		listener.getLogger().println("This is the Hello plugin!");
		listener.getLogger().println(getBuildMessage());

		if (isFailBuild())
			throw new AbortException("Build error forced by plugin settings");
	}

	public boolean isFailBuild() {
		return fFailBuild;
	}

	public String getBuildMessage() {
		return fBuildMessage;
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
	}
}
