package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.PlayerSeedDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.service.PictureService;
import softuni.exam.service.PlayerService;
import softuni.exam.service.TeamService;
import softuni.exam.util.ValidatorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    public static final String PLAYER_FILE_PATH="src/main/resources/files/json/players.json";
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final Gson gson;
    private final PlayerRepository playerRepository;
    private final TeamService teamService;
    private final PictureService pictureService;

    public PlayerServiceImpl(ModelMapper modelMapper, ValidatorUtil validatorUtil, Gson gson, PlayerRepository playerRepository, TeamService teamService, PictureService pictureService) {
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.gson = gson;
        this.playerRepository = playerRepository;
        this.teamService = teamService;
        this.pictureService = pictureService;
    }

    @Override
    public String importPlayers() throws IOException {
        StringBuilder sb=new StringBuilder();
        Arrays.stream(gson.fromJson(readPlayersJsonFile(), PlayerSeedDto[].class))
                .filter(playerSeedDto -> {
                    boolean isValid=validatorUtil.isValid(playerSeedDto);
                    sb.append(isValid?String.format("Successfully imported player: %s %s",
                            playerSeedDto.getFistName(),playerSeedDto.getLastName())
                            :"Invalid player")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(playerSeedDto ->{
                    Player player=this.modelMapper.map(playerSeedDto,Player.class);
            Team team=teamService.getTeamByName(playerSeedDto.getTeam().getName());
            Picture picture=pictureService.getPictureByUrl(playerSeedDto.getPicture().getUrl());
                    player.setPicture(picture);
                    player.setTeam(team);
            //modelMapper.map(playerSeedDto,Player.class);
            return player;
        } )
                .forEach(playerRepository::save);
        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return
                playerRepository.count()>0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return Files.readString(Path.of(PLAYER_FILE_PATH));
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        //TODO Implement me
        return "";
    }

    @Override
    public String exportPlayersInATeam() {
        StringBuilder sb=new StringBuilder();
        playerRepository.findAllByTeamName("North Hub")
                .forEach(player -> {
                    sb.append(String.format("Player name: %s %s - %s Number: %d",
                            player.getFirstName(),player.getLastName(),
                            player.getNumber())).append(System.lineSeparator());
                });
        return sb.toString();
    }
}
