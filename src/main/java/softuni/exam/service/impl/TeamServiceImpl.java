package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.TeamSeedRootDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.TeamRepository;
import softuni.exam.service.PictureService;
import softuni.exam.service.TeamService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TeamServiceImpl implements TeamService {
    public static final String TEAM_FILE_PATH = "src/main/resources/files/xml/teams.xml";

    private final PictureService pictureService;
    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final FileUtil fileUtil;

    public TeamServiceImpl(PictureService pictureService, TeamRepository teamRepository, ModelMapper modelMapper, ValidatorUtil validatorUtil, FileUtil fileUtil) {
        this.pictureService = pictureService;
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.fileUtil = fileUtil;
    }

    @Override
    public String importTeams() throws JAXBException, IOException {
        StringBuilder sb = new StringBuilder();
        fileUtil.readFile(TEAM_FILE_PATH, TeamSeedRootDto.class)
                .getTeams()
                .stream()
                .filter(teamSeedDto -> {
                    boolean isValid = validatorUtil.isValid(teamSeedDto);
                    sb.append(isValid ? String.format("Successfully imported - %s",
                            teamSeedDto.getName())
                            : "Invalid team")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(teamSeedDto -> {
                    Team team = modelMapper.map(teamSeedDto, Team.class);
                    team.setPicture(pictureService.findByUrl(teamSeedDto.getPicture().getUrl()));
                    return team;
                })
                .forEach(teamRepository::save);
        return sb.toString();
    }

    @Override
    public boolean areImported() {

        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {

        return Files.readString(Path.of(TEAM_FILE_PATH));
    }

    @Override
    public Team getTeamByName(String name) {

        return null;
    }
}
