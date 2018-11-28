package com.codeandme.jenkins.builder;

import java.io.IOException;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.AbortException;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;

public class HelloBuilder extends Builder implements SimpleBuildStep {

	private boolean fFailBuild;

	@DataBoundConstructor
	public HelloBuilder(boolean failBuild) {
		fFailBuild = failBuild;
	}

	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener)
			throws InterruptedException, IOException {
		listener.getLogger().println("This is the Hello plugin!");

		if (isFailBuild())
			throw new AbortException("Build error forced by plugin settings");
	}

	public boolean isFailBuild() {
		return fFailBuild;
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
	}
}
