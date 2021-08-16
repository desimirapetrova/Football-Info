package softuni.exam.util;


import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface FileUtil {

    <T> T readFile(String filePath,Class<T>tClass) throws IOException, JAXBException;
}
