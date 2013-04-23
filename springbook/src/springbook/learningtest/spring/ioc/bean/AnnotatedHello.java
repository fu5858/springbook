package springbook.learningtest.spring.ioc.bean;

import org.springframework.stereotype.Component;

import springbook.learningtest.spring.ioc.Printer;

@Component
public class AnnotatedHello {

	String name;
	Printer printer;
	
	public void setName(String name) {
		this.name = name;
	}
	public void setPrinter(Printer printer) {
		this.printer = printer;
	}
	
	public String sayHello(){
		return "Hello " + name;
	}
	
	public void print(){
		printer.print(sayHello());
	}
}
