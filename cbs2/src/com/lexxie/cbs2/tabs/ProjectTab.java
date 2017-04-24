package com.lexxie.cbs2.tabs;

import java.util.ArrayList;
import java.util.List;

public class ProjectTab {

	private static List<String> tabs = new ArrayList<String>();
	
	public static void removeTabElement(int x){
		tabs.remove(x);
	}
	
	public static String getTab(int x){
		return tabs.get(x);
	}
	
	public static List<String> getTabs(){
		return tabs;
	}
	
	public ProjectTab(String projectName){
		tabs.add(projectName);
	}
	
}
