package com.zee.springai.mcpserver.tool;


import com.zee.springai.mcpserver.entity.Game;
import com.zee.springai.mcpserver.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @dev : Ezekiel Eromosei
 * @date : 11 Oct, 2026
 */

@Service
@RequiredArgsConstructor
public class GameTools {

    private final GameRepository gameRepository;

    @McpTool(name = "gameCount",
            description = "Returns the count of the games in the repository")
    public Mono<Long> gameCount(){
        return Mono.fromCallable(gameRepository::count);
    }

    @McpTool(name = "findGamesForPlayerCount", description = "Find games suitable for the specified number of players.")
    public Flux<Game> findGamesForPlayerCount(@McpToolParam(description = "The number of players to find games for.")int numPlayers){
        return Flux.fromIterable(gameRepository.findGamesForPlayerCount(numPlayers));
    }

    @McpTool(name = "findGamesForPlayingTime", description = "Find games suitable for the specified playing time.")
    public  Flux<Game> findGamesForPlayingTime(@McpToolParam(description = "The time for playing the game,")int time){
        return Flux.fromIterable(gameRepository.findGamesForPlayingTime(time));
    }

}
