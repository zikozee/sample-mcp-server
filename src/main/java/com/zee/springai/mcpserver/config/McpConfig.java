package com.zee.springai.mcpserver.config;


import com.zee.springai.mcpserver.promptprovider.GamePromptProvider;
import com.zee.springai.mcpserver.resourceprovider.GameResourceProvider;
import com.zee.springai.mcpserver.tool.GameTools;
import io.modelcontextprotocol.server.McpServerFeatures;
import org.springframework.ai.mcp.annotation.spring.AsyncMcpAnnotationProviders;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * @dev : Ezekiel Eromosei
 * @date : 11 Oct, 2026
 */

@Configuration
public class McpConfig {

    // expose tools in an MCP Server
//    @Bean
//    ToolCallbackProvider toolCallbackProvider(GameTools gameTools){
//        return MethodToolCallbackProvider.builder()
//                .toolObjects(gameTools)
//                .build();
//    }


    /**
    // without using McpPrompt
    // if not reactive use List<McpServerFeatures.SyncPromptSpecification>
    @Bean
    public List<McpServerFeatures.AsyncPromptSpecification> asyncGamePrompts(){
        return List.of(asyncPromptSpec(
                        "gamesForPlayerCount",
                        "A prompt to find games for a specified number of players",
                        "playerCount",
                        "The number of Players",
                        "Find games for %s players",
                        "A prompt to find games for %s players"
                ),
                asyncPromptSpec(
                        "gamesForPlayerTime",
                        "A prompt to find games for given amount of time",
                        "timeInMinutes",
                        "The time in minutes",
                        "Find games play in %s minutes",
                        "A prompt to find games to play in %s minutes"
                )
        );
    }


    private McpServerFeatures.AsyncPromptSpecification asyncPromptSpec(String promptName, String promptDesc,
                                                                       String promptArgumentName, String promptArgumentDesc,
                                                                       String schemaContentText, String promptResultDesc){
        // define a prompt
        McpSchema.Prompt playerTimePrompt = McpSchema.Prompt.builder(promptName)
                .description(promptDesc)
                .arguments(Collections.singletonList(
                        McpSchema.PromptArgument.builder(promptArgumentName)
                                .description(promptArgumentDesc)
                                .required(true)
                                .build()
                ))
                .build();

        // define a prompt specification
        return new McpServerFeatures.AsyncPromptSpecification(
                playerTimePrompt,
                (exchange, getPromptRequest) -> {

                    final String argumentResult = (String) getPromptRequest.arguments().get(promptArgumentName);

                    McpSchema.PromptMessage userMessage = new McpSchema.PromptMessage(
                            McpSchema.Role.USER,
                            McpSchema.TextContent.builder(String.format(schemaContentText, argumentResult)).build()
                    );

                    return Mono.fromSupplier(() -> McpSchema.GetPromptResult.builder(Collections.singletonList(userMessage))
                            .description(String.format(promptResultDesc, argumentResult))
                            .build());
                });
    }
    */



    //Using Mcp Annotations
    @Bean
    List<McpServerFeatures.AsyncPromptSpecification> asyncGamePrompts(GamePromptProvider gamePromptProvider){
        return AsyncMcpAnnotationProviders
                .promptSpecifications(Collections.singletonList(gamePromptProvider));
    }


    /**
    @Bean
    List<McpServerFeatures.AsyncResourceSpecification> gameResources(GameRepository gameRepository){
        List<McpSchema.Role> audience = Collections.singletonList(McpSchema.Role.USER);

        McpSchema.Annotations annotations =  McpSchema.Annotations.builder()
                .audience(audience)
                .priority(1.0)
                .lastModified(
                        LocalDateTime.of(2026, 10, 12, 14, 59)
                                .atZone(ZoneId.of("Africa/Lagos"))
                                .toInstant()
                                .toString()
                )
                .build();

        McpSchema.Resource gameListResource = McpSchema.Resource.builder("games://game-list", "Game List")
                .description("A list of games available in the repository")
                .mimeType("text/plain")
                .annotations(annotations)
                .build();

        // cache
        List<String> allTitles = gameRepository.findAllTitles();

        StringBuilder gameListText = new StringBuilder();
        for(String title : allTitles){
            gameListText.append("- ").append(title).append("\n");
        }

        McpServerFeatures.AsyncResourceSpecification asyncResourceSpecification = new McpServerFeatures.AsyncResourceSpecification(
                gameListResource, (exchange, request) ->
                Mono.fromSupplier(() -> McpSchema.ReadResourceResult.builder(
                        Collections.singletonList(
                                McpSchema.TextResourceContents.builder(request.uri(), gameListText.toString())
                                        .mimeType("text/plain")
                                        .build())
                        )
                        .build()
                )
        );

        return Collections.singletonList(asyncResourceSpecification);
    }

    **/

    @Bean
    List<McpServerFeatures.AsyncResourceSpecification> gameResources(GameResourceProvider gameResourceProvider){
        return AsyncMcpAnnotationProviders
                .resourceSpecifications(Collections.singletonList(gameResourceProvider));
    }
}
