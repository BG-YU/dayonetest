package com.jyujyu.dayonetest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MyCalculatorTest {

	@Test
	void addTest() {
		MyCalculator myCalculator = new MyCalculator();

		myCalculator.add(10.0);

		Assertions.assertEquals(10.0, myCalculator.getResult());
	}

	@Test
	void minusTest() {
		MyCalculator myCalculator = new MyCalculator(10.0);

		myCalculator.minus(5.0);

		Assertions.assertEquals(5.0, myCalculator.getResult());
	}

	@Test
	void multiply() {
		MyCalculator myCalculator = new MyCalculator(2.0);

		myCalculator.multiply(2.0);

		Assertions.assertEquals(4.0, myCalculator.getResult());
	}

	@Test
	void divide() {
		MyCalculator myCalculator = new MyCalculator(10.0);

		myCalculator.divide(2.0);

		Assertions.assertEquals(5.0, myCalculator.getResult());
	}

	@Test
	void complicatedCalculateTest() {
		MyCalculator myCalculator = new MyCalculator();

		Double result = myCalculator
			.add(10.0)
			.minus(4.0)
			.multiply(2.0)
			.divide(3.0)
			.getResult();

		Assertions.assertEquals(4.0, result);
	}

	@Test
	void divideZeroTesT() {
		// given
		MyCalculator myCalculator = new MyCalculator(10.0);

		// when & then
		Assertions.assertThrows(
			MyCalculator.ZeroDivisionException.class,
			() -> myCalculator.divide(0.0)
		);
	}
}
