package com.app.test;

import java.lang.reflect.Method;

public interface IUITestor {
	
	/**
	 * Testing UI elements 
	 * */
	public void doUITest(Method m, Object ui, Object o);
}
