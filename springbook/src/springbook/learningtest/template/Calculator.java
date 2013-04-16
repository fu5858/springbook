package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	
	/**
	 * Calculate the sum of numbers from the file
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public Integer calcSum(String filePath) throws IOException {
		LineCallback<Integer> sumCallback = new LineCallback<Integer>() {
			
			@Override
			public Integer doSomethingWithLine(String line, Integer value) {
				return value + Integer.valueOf(line);
			}
		};
		
		return lineReadTemplate(filePath, sumCallback, 0);
	}

	/**
	 * Calculate the multiply of numbers from the file.
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public Integer calcMultiply(String filePath) throws IOException {
		LineCallback<Integer> mulCallback = new LineCallback<Integer>() {
			
			@Override
			public Integer doSomethingWithLine(String line, Integer value) {
				return value * Integer.valueOf(line);
			}
		};
		
		return lineReadTemplate(filePath, mulCallback, 1);
	}
	
	/**
	 * Concatenate the characters from the file.
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public String concatenate(String filePath) throws IOException {
		LineCallback<String> concatenateCallback = new LineCallback<String>() {
			
			@Override
			public String doSomethingWithLine(String line, String value) {
				return value +  line;
			}
		};
		
		return lineReadTemplate(filePath, concatenateCallback, "");
	}
	
	/**
	 * Template for reading the line with try - catch statement
	 * @param filePath
	 * @param callback
	 * @param initVal
	 * @return
	 * @throws IOException
	 */
	public <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T initVal) throws IOException {
		BufferedReader br = null;

		try{
			br = new BufferedReader(new FileReader(filePath));
			T res = initVal;
			String line = null;
			while((line = br.readLine()) != null) {
				res = callback.doSomethingWithLine(line, res);
				System.out.println(res);
			}
			
			return res;
		}catch(IOException e){
			System.out.println(e.getMessage());
			throw e;
		}finally{
			if(br != null) {
				try { br.close(); }catch(IOException e){ System.out.println(e.getMessage());}
			}
		}

	}
}
