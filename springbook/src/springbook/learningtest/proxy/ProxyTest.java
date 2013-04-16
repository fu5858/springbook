package springbook.learningtest.proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;


public class ProxyTest {

	@Test
	public void simpleProxy(){
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("Daniel"), is("Hello, Daniel"));
		assertThat(hello.sayHi("Daniel"), is("Hi, Daniel"));
		assertThat(hello.sayThankYou("Daniel"), is("Thank You, Daniel"));
		
		Hello proxiedHello = new HelloUppercase(new HelloTarget());
		assertThat(proxiedHello.sayHello("Daniel"), is("HELLO, DANIEL"));
		assertThat(proxiedHello.sayHi("Daniel"), is("HI, DANIEL"));
		assertThat(proxiedHello.sayThankYou("Daniel"), is("THANK YOU, DANIEL"));
		
	}
	
	@Test
	public void simpleProxyWithInvocationHandler(){
		Hello proxiedHello = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader(), 
				new Class[]{Hello.class}, 
				new UppercaseHandler(new HelloTarget()));

		assertThat(proxiedHello.sayHello("Daniel"), is("HELLO, DANIEL"));
		assertThat(proxiedHello.sayHi("Daniel"), is("HI, DANIEL"));
		assertThat(proxiedHello.sayThankYou("Daniel"), is("THANK YOU, DANIEL"));
		
	}
	
	@Test
	public void proxyFactoryBean(){
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		pfBean.addAdvice(new UppercaseAdvice());
		
		Hello proxiedHello = (Hello)pfBean.getObject();

		assertThat(proxiedHello.sayHello("Daniel"), is("HELLO, DANIEL"));
		assertThat(proxiedHello.sayHi("Daniel"), is("HI, DANIEL"));
		assertThat(proxiedHello.sayThankYou("Daniel"), is("THANK YOU, DANIEL"));
	}
	
	static class UppercaseAdvice implements MethodInterceptor {

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			String ret = (String)invocation.proceed();
			return ret.toUpperCase();
		}
		
	}
	
	@Test
	public void pointcutAdvisor(){
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.setMappedName("sayH*");
		
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		
		Hello proxiedHello = (Hello)pfBean.getObject();

		assertThat(proxiedHello.sayHello("Daniel"), is("HELLO, DANIEL"));
		assertThat(proxiedHello.sayHi("Daniel"), is("HI, DANIEL"));
		assertThat(proxiedHello.sayThankYou("Daniel"), is("Thank You, Daniel"));
	}
	
	@Test
	public void classNamePointcutAdvisor(){
		NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut(){
			private static final long serialVersionUID = 1L;

			public ClassFilter getClassFilter(){
				return new ClassFilter(){
					public boolean matches(Class<?> clazz) {
						return clazz.getSimpleName().startsWith("HelloT");
					}
				};
			}
		};
		
		classMethodPointcut.setMappedName("sayH*");
		
		checkAdvice(new HelloTarget(), classMethodPointcut, true);
		
		class HelloWorld extends HelloTarget{};
		checkAdvice(new HelloWorld(), classMethodPointcut, false);
		
		class HelloToby extends HelloTarget{};
		checkAdvice(new HelloToby(), classMethodPointcut, true);
	}
	
	private void checkAdvice(Object target, Pointcut pointcut, boolean adviced) {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(target);
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		Hello proxiedHello = (Hello)pfBean.getObject();
		
		if(adviced){
			assertThat(proxiedHello.sayHello("Daniel"), is("HELLO, DANIEL"));
			assertThat(proxiedHello.sayHi("Daniel"), is("HI, DANIEL"));
			assertThat(proxiedHello.sayThankYou("Daniel"), is("Thank You, Daniel"));
		}else{
			assertThat(proxiedHello.sayHello("Daniel"), is("Hello, Daniel"));
			assertThat(proxiedHello.sayHi("Daniel"), is("Hi, Daniel"));
			assertThat(proxiedHello.sayThankYou("Daniel"), is("Thank You, Daniel"));
		}
	}


}
