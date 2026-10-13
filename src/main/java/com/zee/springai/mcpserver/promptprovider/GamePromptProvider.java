package com.zee.springai.mcpserver.promptprovider;


import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.mcp.annotation.McpArg;
import org.springframework.ai.mcp.annotation.McpPrompt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * @dev : Ezekiel Eromosei
 * @date : 12 Oct, 2026
 */

@Component
public class GamePromptProvider {


    @McpPrompt(name = "gamesForPlayerCount",
            description = "A prompt to find games for a specific number of player")
    public Mono<McpSchema.GetPromptResult> getGamesForPlayerCount(@McpArg(name = "playerCount", description = "The number of Player", required = true)
                                                                Integer playerCount) {




        return Mono.fromSupplier(() -> {
                    McpSchema.PromptMessage userMessage = new McpSchema.PromptMessage(
                            McpSchema.Role.USER,
                            McpSchema.TextContent.builder(String.format("Find games for %s players", playerCount)).build()
                    );

                return McpSchema.GetPromptResult.builder(Collections.singletonList(userMessage))
                .description(String.format("A prompt to find games for %s players", playerCount))
                .build();

        });
    }

    @McpPrompt(name = "gamesForPlayerTime",
            description = "A prompt to find games for given amount of time")
    public Mono<McpSchema.GetPromptResult> gamesForPlayingTime(@McpArg(name = "timeInMinutes", description = "The time in minutes", required = true)
                                                            Integer timeInMinutes) {


        return Mono.fromSupplier(() -> {
            McpSchema.PromptMessage userMessage = new McpSchema.PromptMessage(
                    McpSchema.Role.USER,
                    McpSchema.TextContent.builder(String.format("Find games play in %s minutes", timeInMinutes)).build()
            );

            return McpSchema.GetPromptResult.builder(Collections.singletonList(userMessage))
                    .description(String.format("A prompt to find games to play in %s minutes", timeInMinutes))
                    .build();
        });
    }


}
