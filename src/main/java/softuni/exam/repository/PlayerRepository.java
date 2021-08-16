package softuni.exam.repository;


import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.dto.PlayerSeedDto;
import softuni.exam.domain.entities.Player;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {

Player findByFirstNameAndLastName(String firstName,String lastName);
@Query("select p from  Player p where  p.team.name=:name")
List<Player> findAllByTeamName(String name);
}
