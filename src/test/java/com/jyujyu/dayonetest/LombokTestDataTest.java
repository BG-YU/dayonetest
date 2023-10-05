package com.jyujyu.dayonetest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LombokTestDataTest {
	@Test
	public void testDataTest() {
		TestData testData = new TestData();
		testData.setName("test");

		Assertions.assertEquals("test", testData.getName());
	}
}
