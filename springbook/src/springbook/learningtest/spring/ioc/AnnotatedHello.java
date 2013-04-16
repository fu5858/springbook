package springbook.learningtest.spring.ioc;

import org.springframework.stereotype.Component;

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
