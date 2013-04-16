package springbook.learningtest.template;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CalcSumTest {
	private Calculator calculator;
	private String numFilePath;
	
	@Before
	public void setUp(){
		calculator = new Calculator();
		numFilePath = getClass().getResource("number.txt").getPath();
	}

	@Test
	public void sumOfNumbers() throws IOException {
		assertThat(calculator.calcSum(numFilePath), is(10));
	}
	
	@Test
	public void multiplyOfNumbers() throws IOException {
		assertThat(calculator.calcMultiply(numFilePath), is(24));
	}
	
	@Test
	public void concateOfNumbers() throws IOException {
		assertThat(calculator.concatenate(numFilePath), is("1234"));
	}
	
}
