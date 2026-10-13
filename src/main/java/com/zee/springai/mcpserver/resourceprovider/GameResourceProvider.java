package com.zee.springai.mcpserver.resourceprovider;


import com.zee.springai.mcpserver.repository.GameRepository;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.mcp.annotation.McpResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * @dev : Ezekiel Eromosei
 * @date : 12 Oct, 2026
 */

@Component
@RequiredArgsConstructor
public class GameResourceProvider {

    private final GameRepository gameRepository;

    @McpResource(uri = "games://game-list", name = "Game List",
            description = "A list of games available in the repository", mimeType = MediaType.TEXT_PLAIN_VALUE,
            annotations = @McpResource.McpAnnotations(audience = {McpSchema.Role.USER},
                    lastModified = "2026-10-11T23:00:00Z",
                    priority = 1.0
            )
    )
    public Mono<McpSchema.ReadResourceResult> gameListResource(McpSchema.ReadResourceRequest request) {

        return Mono.fromSupplier(() -> {

            List<String> allTitles = gameRepository.findAllTitles();

            StringBuilder gameListText = new StringBuilder();
            for(String title : allTitles){
                gameListText.append("- ").append(title).append("\n");
            }

            return McpSchema.ReadResourceResult.builder(
                    Collections.singletonList(
                            McpSchema.TextResourceContents.builder(request.uri(), gameListText.toString())
                                    .mimeType("text/plain")
                                    .build()
                    ))
                    .build();
        });
    }

}
