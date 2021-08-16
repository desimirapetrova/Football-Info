package softuni.exam.service.impl;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.PictureSeedRootDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PictureServiceImpl implements PictureService {

    public static final String PICTURE_FILE_PATH="src/main/resources/files/xml/pictures.xml";

    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final FileUtil fileUtil;

    public PictureServiceImpl(PictureRepository pictureRepository, ModelMapper modelMapper, ValidatorUtil validatorUtil, FileUtil fileUtil) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.fileUtil = fileUtil;
    }

    @Override
    public String importPictures() throws JAXBException, IOException {
        StringBuilder sb=new StringBuilder();
        fileUtil.readFile(PICTURE_FILE_PATH, PictureSeedRootDto.class)
                .getPictures()
                .stream()
                .filter(pictureSeedDto -> {
                    boolean isValid=validatorUtil.isValid(pictureSeedDto);
                    sb.append(isValid?String.format("Successfully imported picture - %s",
                            pictureSeedDto.getUrl())
                            :"Invalid picture")
                            .append(System.lineSeparator());
                return isValid;
                }).map(pictureSeedDto ->modelMapper.map(pictureSeedDto, Picture.class))
                .forEach(pictureRepository::save);
        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return pictureRepository.count()>0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
     return Files.readString(Path.of(PICTURE_FILE_PATH));
    }

    @Override
    public Picture findById(Long id) {
        return pictureRepository.findById(id).orElse(null);
    }

    @Override
    public Picture findByUrl(String url) {
        return pictureRepository.findByUrl(url);
    }

    @Override
    public Picture getPictureByUrl(String url) {
        return pictureRepository.findByUrl(url);
    }


}
