package com.codeandme.tycho.plugin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExampleViewTest {

	@Test
	public void testID() {
		assertEquals("com.codeandme.tycho.views.example", ExampleView.VIEW_ID);
	}
}