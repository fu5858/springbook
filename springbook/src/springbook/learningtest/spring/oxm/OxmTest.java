package springbook.learningtest.spring.oxm;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="/OxmTest-context.xml")
@ContextConfiguration	// if context.xml file is in same folder, the default context file is (class name) + -context.xml.
public class OxmTest {
	@Autowired
	Unmarshaller unmarshaller;
	
	@Test
	public void unmarshallerSqlMap() throws XmlMappingException, IOException {
		Source xmlSource = new StreamSource(getClass().getResourceAsStream("sqlmap.xml"));
		
		Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(xmlSource);
		
		List<SqlType> sqlList = sqlmap.getSql();
		
		assertThat(sqlList.get(0).getKey(), is("add"));
		for(SqlType sql : sqlList){
//			System.out.println(sql.getKey());
//			System.out.println(sql.getValue());
			System.out.println(sql.getKey() + "\t\t" + sql.getValue());
		}
	}
}
