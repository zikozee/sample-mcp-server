package com.zee.springai.mcpserver.tool;


import com.zee.springai.mcpserver.entity.Game;
import com.zee.springai.mcpserver.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @dev : Ezekiel Eromosei
 * @date : 11 Oct, 2026
 */

@Service
@RequiredArgsConstructor
public class GameTools {

    private final GameRepository gameRepository;

    @Tool(name = "gameCount",
            description = "Returns the count of the games in the repository")
    public long gameCount(){
        return gameRepository.count();
    }

    @Tool(name = "findGamesForPlayerCount", description = "Find games suitable for the specified number of players.")
    public List<Game> findGamesForPlayerCount(@ToolParam(description = "The number of players to find games for.")int numPlayers){
        return gameRepository.findGamesForPlayerCount(numPlayers);
    }

    @Tool(name = "findGamesForPlayingTime", description = "Find games suitable for the specified playing time.")
    public  List<Game> findGamesForPlayingTime(@ToolParam(description = "The time for playing the game,")int time){
        return gameRepository.findGamesForPlayingTime(time);
    }

}
