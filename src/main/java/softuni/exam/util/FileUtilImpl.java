package softuni.exam.util;

import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
@Component
public class FileUtilImpl implements FileUtil {

    @Override
    @SuppressWarnings("unchecked")
    public <T>T readFile(String filePath, Class<T> tClass) throws IOException, JAXBException {
        JAXBContext jaxbContext=JAXBContext.newInstance(tClass);
        Unmarshaller unmarshaller=jaxbContext.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new FileReader(filePath));
    }
}
