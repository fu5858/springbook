package springbook.learningtest.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {

	Object target;
	
	public UppercaseHandler(Object target){
		this.target = target;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		Object ret = method.invoke(target, args);
		
		if(ret instanceof String && method.getName().startsWith("say")){
			System.out.println("startWith 'say' " + method.getName() + " ==> " + ((String)ret).toUpperCase());
			return ((String)ret).toUpperCase();
		}else {
			System.out.println(ret);
			return ret;
		}
	}

}
