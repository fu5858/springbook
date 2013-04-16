package springbook.learningtest.junit;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class JunitTest {
	
	static JunitTest testObject;
	static Set<JunitTest> testObjects = new HashSet<>();
	
	@SuppressWarnings("deprecation")
	@Test
	public void test1(){
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
//		assertThat(this, is(not(sameInstance(testObject))));
//		testObject = this;
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void test2(){
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
//		assertThat(this, is(not(sameInstance(testObject))));
//		testObject = this;
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void test3(){
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
//		assertThat(this, is(not(sameInstance(testObject))));
//		testObject = this;
	}
}
