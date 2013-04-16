package springbook.user.dao;

import java.io.File;

import org.junit.Test;

public class SimpleTest {

	@Test
	public void filePath(){
		File file = new File("D:\\workspace_springsource\\STS_Juno\\springbook\\src\\");
		System.out.println(file.getAbsolutePath());
		
		File[] listOfFiles = file.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
	    	if (listOfFiles[i].isFile()) {
	    		System.out.println("File ==> " + listOfFiles[i].getName());
	    	} else if (listOfFiles[i].isDirectory()) {
	    		System.out.println("Directory ==> " + listOfFiles[i].getName());
	    	}
	    }
	}

	@Test
	public void getFileNames(){
		File folder = new File("D:\\workspace_springsource\\STS_Juno\\springbook\\src\\");
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println("File ==> " + listOfFiles[i].getName());
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory ==> " + listOfFiles[i].getName());
		      }
		    }
	}

}
