/*
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here

package com.example.player.service;
import com.example.player.model.PlayerRowMapper;
import com.example.player.repository.PlayerRepository;
import com.example.player.model.Player;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
 

@Service
public class PlayerH2Service implements PlayerRepository{
    @Autowired
    private JdbcTemplate db;

    @Override 
    public ArrayList<Player> getAllPlayers(){

        List<Player> playersList = db.query("SELECT * FROM team", new PlayerRowMapper());
        ArrayList<Player> players = new ArrayList<>(playersList);
        return players;
        
    }

    @Override
    public Player addPlayer(Player player){
        db.update("INSERT INTO team (playerName,jerseyNumber,role) values (?,?,?)", player.getPlayerName(),player.getJerseyNumber(),player.getRole());

        Player savedPlayer = db.queryForObject("SELECT * FROM team where playerName = ? AND jerseyNumber = ? AND role = ?",new PlayerRowMapper(),player.getPlayerName(),player.getJerseyNumber(),player.getRole());
        return savedPlayer;


    }

    @Override
    public Player getPlayer(int playerId){

        try{
           
           Player player = db.queryForObject("select * from team where playerId = ?", new PlayerRowMapper(),playerId);
           return player;

        }
        catch(Exception e){
              throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Player updatePlayer(int playerId,Player player){
        
         if(player.getPlayerName() != null){
            db.update("update team set playerName = ? where playerId = ?",player.getPlayerName(),playerId);
         }

         if(player.getJerseyNumber() != 0){
            db.update("update team set jerseyNumber = ? where playerId = ?",player.getJerseyNumber(),playerId);
         }
         if(player.getRole()!= null){
            db.update("update team set role = ? where playerId = ?",player.getRole(),playerId);
         }
         return getPlayer(playerId);

        
    }
    
   @Override
   public void deletePlayer(int playerId){
      db.update("delete from  team where playerId = ?",playerId);
            
   }

}