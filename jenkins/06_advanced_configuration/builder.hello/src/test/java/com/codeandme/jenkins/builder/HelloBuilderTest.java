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

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.model.queue.QueueTaskFuture;

public class HelloBuilderTest {

	@Rule
	public JenkinsRule fJenkinsInstance = new JenkinsRule();
	
	@Test
	public void successfulBuild() throws Exception {
		HelloBuilder builder = new HelloBuilder( "JUnit test run");
		
		FreeStyleProject job = fJenkinsInstance.createFreeStyleProject();
		job.getBuildersList().add(builder);
		FreeStyleBuild build = fJenkinsInstance.buildAndAssertSuccess(job);
		
		fJenkinsInstance.assertLogContains("JUnit test run", build);
	}

	@Test
	public void failedBuild() throws Exception {
		HelloBuilder builder = new HelloBuilder( "JUnit test fail");
		builder.setFailBuild(true);
		
		FreeStyleProject job = fJenkinsInstance.createFreeStyleProject();
		job.getBuildersList().add(builder);
		QueueTaskFuture<FreeStyleBuild> buildResult = job.scheduleBuild2(0);
		
		fJenkinsInstance.assertBuildStatus(Result.FAILURE, buildResult);
		fJenkinsInstance.assertLogContains("JUnit test fail", buildResult.get());
	}
}
