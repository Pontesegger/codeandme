package com.codeandme.tableviewer.dnd.sorting;

import java.util.ArrayList;

public class DataModel extends ArrayList<String> {

	private static final long serialVersionUID = 1L;

	public DataModel() {
		load();
	}
	
	private void load() {
		add("Alpha");
		add("Bravo");
		add("Charlie");
		add("Delta");
		add("Echo");
		add("Foxtrot");
		add("Golf");
		add("Hotel");
		add("India");
		add("Juliet");
		add("Kilo");
		add("Lima");
		add("Mike");
		add("November");
		add("Oscar");
		add("Papa");
		add("Quebec");
		add("Romeo");
		add("Sierra");
		add("Tango");
		add("Uniform");
		add("Victor");
		add("Whiskey");
		add("X-ray");
		add("Yankee");
		add("Zulu");
	}
}
