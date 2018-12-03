package com.codeandme.jenkins.builder;

import java.io.IOException;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

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

	private String fBuildMessage;

	private String fBuildType;

	@DataBoundConstructor
	public HelloBuilder() {
	}

	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener)
			throws InterruptedException, IOException {
		listener.getLogger().println(getBuildMessage());

		switch (getBuildType()) {
		case "slow":
			Thread.sleep(10 * 1000);
			break;

		case "intermediate":
			Thread.sleep(3 * 1000);
			break;

		case "fast":
			// fall through
		default:
			// nothing to do
		}
	}

	@DataBoundSetter
	public void setBuildType(String buildType) {
		fBuildType = buildType;
	}

	public String getBuildType() {
		return fBuildType;
	}

	@DataBoundSetter
	public void setBuildMessage(String buildMessage) {
		fBuildMessage = buildMessage;
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

		public ListBoxModel doFillBuildTypeItems(@QueryParameter String buildType) {
			ListBoxModel model = new ListBoxModel();

			model.add(new Option("Slow", "slow", "slow".equals(buildType)));
			model.add(new Option("Fast", "fast", "fast".equals(buildType)));
			model.add(new Option("Intermediate", "intermediate" , "intermediate".equals(buildType)));

			return model;
		}
	}
}
