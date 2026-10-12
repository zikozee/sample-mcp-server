package com.zee.springai.mcpserver.config;


import com.zee.springai.mcpserver.tool.GameTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @dev : Ezekiel Eromosei
 * @date : 11 Oct, 2026
 */

@Configuration
public class McpConfig {

    // expose tools in an MCP Server
    @Bean
    ToolCallbackProvider toolCallbackProvider(GameTools gameTools){
        return MethodToolCallbackProvider.builder()
                .toolObjects(gameTools)
                .build();
    }
}
