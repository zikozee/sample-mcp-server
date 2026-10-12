# MCP SERVER
- expose tools in an MCP Server

```java
    @Bean
    ToolCallbackProvider toolCallbackProvider(GameTools gameTools){
        return MethodToolCallbackProvider.builder()
                .toolObjects(gameTools)
                .build();
    }
```    

- there are three means of communication: STDIO, HTTP+SSE, STREAMABLE HTTP

## STDIO

### turn banner off
-  Any logging or other output that isn’t part of the MCP protocol will confuse the client and cause the MCP communication to break down

```yaml
spring:
  main:
    banner-mode: off
```

### write logs to files
  - you'll preferably want to write to file to ensure you capture all logs because
  - MCP Server communicates with a client over the STDIO transport, it does so via standard input and output.
  - If anything is written to standard output, it will be considered communication from the server to the client

### running mcp server
- build executable with ./gradlew build
- you will not run the jar itself. instead the MCP Client will run this
- Because the MCP Server implements the STDIO transport, the client is responsible for running it, and the communication takes place over standard input and standard output

### Inspecting the MCP Server 
- this helps to validation/testing and see the constituents of the mcp server
- npx is from node
- use:
  - npx @modelcontectprotocol/inspector
- add server
  - serverID: a name
  - Transport: stdio(local process)
  - Command: java
  - Arguments:
     -jar 
     /Users/zikozee/codes/my_projects/sample-mcp-server/build/libs/sample-mcp-server-0.0.1-SNAPSHOT.jar

- ensure a space after "-jar"


## HTTP+SSE
- choose a dependency for web or reactive
  - depending on the one you choose via start.spring.io spring will add either:
    - implementation 'org.springframework.ai:spring-ai-starter-mcp-server-webmvc'  OR
    - implementation 'org.springframework.ai:spring-ai-starter-mcp-server-webflux'
  
- here the app can act as a standard web or reactive application
- hence banner can be turned on
- we can log on the console (good k8s scraping)
- we server via web

### Inspecting the MCP Server 
- this helps to validation/testing and see the constituents of the mcp server
- npx is from node
- use:
  - npx @modelcontectprotocol/inspector
- add server
  - ServerID: a name
  - Transport: sse(Server-Sent Events)
  - url: http://localhost:3001/sse

# Exposing prompts and resources (see easier way below with mcp-annotations)
- Rather than encode the prompt creation in the application itself, the MCP Server can provide one or more predefined prompts, which the application can fill in with details from the UI before sending the prompt to the LLM

- prompts and its spec
```java
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

                    final String argument = (String) getPromptRequest.arguments().get(promptArgumentName);

                    McpSchema.PromptMessage userMessage = new McpSchema.PromptMessage(
                            McpSchema.Role.USER,
                            McpSchema.TextContent.builder(String.format(schemaContentText, argument)).build()
                    );

                    return Mono.fromSupplier(() -> McpSchema.GetPromptResult.builder(Collections.singletonList(userMessage))
                            .description(String.format(promptResultDesc, argument))
                            .build());
                });
    }
```

- resource and its spec
```java
@Bean
public List<McpServerFeatures.SyncResourceSpecification>
    gameResources(GameRepository gameRepository) {
  List<McpSchema.Role> audience = List.of(McpSchema.Role.USER);
  McpSchema.Annotations annotations =
      new McpSchema.Annotations(audience, 1.0);

  var gameListResource = new McpSchema.Resource(
      "games://game-list",
      "Game List",
      "A list of games available in the repository",
      "text/plain",
      annotations
  );

  var gameTitles = gameRepository.findAllTitles();
  var gameListText = new StringBuilder();
  for (String title : gameTitles) {
    gameListText.append("- ").append(title).append("\n");
  }

  var gameListResourceSpec = 
    new McpServerFeatures.SyncResourceSpecification(
      gameListResource, (exchange, request) -> {
    return new McpSchema.ReadResourceResult(
        List.of(new McpSchema.TextResourceContents(
            request.uri(),
            "text/plain",
            gameListText.toString())));
  });

  return List.of(gameListResourceSpec);
}
```