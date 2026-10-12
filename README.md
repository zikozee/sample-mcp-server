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
    - 
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