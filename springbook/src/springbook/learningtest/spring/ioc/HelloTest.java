package springbook.learningtest.spring.ioc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class HelloTest {

	@Test
	public void regsiterBeanWithHello(){
		StaticApplicationContext applicationContext = new StaticApplicationContext();
		applicationContext.registerSingleton("hello1", Hello.class);
		
		Hello hello1 = applicationContext.getBean("hello1", Hello.class);
		
		assertThat(hello1, is(notNullValue()));
	}
	
	
	@Test
	public void registerBeanWithDependency(){
		StaticApplicationContext applicationContext = new StaticApplicationContext();
		
		// register a printer bean 
		applicationContext.registerBeanDefinition(
			"printer", 									// bean name
			new RootBeanDefinition(StringPrinter.class)	// bean class => object type
		);
		
		// hello object setting
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");
		helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));
		
		// register a hello bean
		applicationContext.registerBeanDefinition("hello", helloDef);
		
		
		Hello hello = applicationContext.getBean("hello", Hello.class);
		hello.print();	// output is 'Hello Spring'
		
		
		assertThat(applicationContext.getBean("printer").toString(), is("Hello Spring"));
	}
	
	@Test
	public void genericApplicationContext(){
		GenericApplicationContext genericApplContext = new GenericApplicationContext();
		
		XmlBeanDefinitionReader beanReader = new XmlBeanDefinitionReader(genericApplContext);
		beanReader.loadBeanDefinitions("springbook/learningtest/spring/ioc/genericApplicationContext.xml");
		
		genericApplContext.refresh();
		
		Hello hello = genericApplContext.getBean("hello", Hello.class);
		hello.print();

		assertThat(genericApplContext.getBean("printer").toString(), is("Hello Spring"));
	}
	
	/**
	 * GenericApplicationContext + XmlBeanDefinitionReader
	 */
	@Test
	public void genericXmlApplicationContext(){
		GenericXmlApplicationContext genericXmlApplContext = new GenericXmlApplicationContext(
				"springbook/learningtest/spring/ioc/genericApplicationContext.xml"
				);
		
		Hello hello = genericXmlApplContext.getBean("hello", Hello.class);
		hello.print();
		
		assertThat(genericXmlApplContext.getBean("printer").toString(), is("Hello Spring"));
	}
	
	@Test
	public void parnetAndChildRelationship(){
		String basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";
		
		ApplicationContext parentContext = new GenericXmlApplicationContext(basePath + "parentContext.xml");
		
		GenericApplicationContext childContext = new GenericApplicationContext(parentContext);
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(childContext);
		
		xmlReader.loadBeanDefinitions(basePath + "childContext.xml");
		
		childContext.refresh();
		
		Printer printer = childContext.getBean("printer", Printer.class);
		
		assertThat(printer, is(notNullValue()));
		
		Hello hello = childContext.getBean("hello", Hello.class);
		
		assertThat(hello, is(notNullValue()));
		
		hello.print();
		
		assertThat(printer.toString(), is("Hello Child"));
		
	}
	
	@Test
	public void simpleBeanScanning(){
		ApplicationContext context = new AnnotationConfigApplicationContext("springbook.learningtest.spring.ioc");
		
		AnnotatedHello hello = context.getBean("annotatedHello", AnnotatedHello.class);
		
		assertThat(hello, is(notNullValue()));
	}
	
	@Test
	public void simpleBeanConfig(){
		ApplicationContext context = new AnnotationConfigApplicationContext(AnnotatedHelloConfig.class);
		
		AnnotatedHello hello = context.getBean("annotatedHello", AnnotatedHello.class);
		
		assertThat(hello, is(notNullValue()));
		
		AnnotatedHelloConfig helloConfig = context.getBean("annotatedHelloConfig", AnnotatedHelloConfig.class);
		
		assertThat(helloConfig, is(notNullValue()));
	}
}
