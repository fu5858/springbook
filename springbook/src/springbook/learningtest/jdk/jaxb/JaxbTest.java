package springbook.learningtest.jdk.jaxb;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

public class JaxbTest {

	@Test
	public void readSqlmap() throws JAXBException, IOException {
		String contextPath = Sqlmap.class.getPackage().getName();
//		System.out.println(contextPath);
		JAXBContext context = JAXBContext.newInstance(contextPath);
//		System.out.println(context.toString());
		Unmarshaller unmarshaller = context.createUnmarshaller();
//		System.out.println(getClass().getResourceAsStream("sqlmap.xml"));
		
		Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(getClass().getResourceAsStream("sqlmap.xml"));

		List<SqlType> sqlList = sqlmap.getSql();
		
//		System.out.println(sqlList.get(0).getKey());
//		System.out.println(sqlList.get(0).getValue());
		assertThat(sqlList.get(0).getKey(), is("add"));
		
		for(Iterator<SqlType> iterator = sqlList.listIterator(); iterator.hasNext(); ){
//			System.out.println(iterator.next().getKey());
			System.out.println(iterator.next().getValue());
		}
	}
	
	@Test
	public void getFileList() throws IOException {
		String filePath = new File("").getAbsolutePath();
//		System.out.println(filePath);
		String fileDir = "\\src\\springbook\\user\\sqlservice\\";
//		System.out.println(filePath + fileDir);
		String fullPath = filePath + fileDir;
//		System.out.println(new File(fileDir).getAbsolutePath());
//		String fullPath = new File(fileDir).getAbsolutePath();
		
		File folder = new File(fullPath);
//		System.out.println(fullPath);
		File[] listOfFiles = folder.listFiles();
		System.out.println(listOfFiles[0]);
		for(int i = 0; i < listOfFiles.length; i++){
			System.out.println(i + " group");
			System.out.println(listOfFiles[i].getName());
			System.out.println(listOfFiles[i].getAbsoluteFile().getName());
			System.out.println();
		}
	}
}
