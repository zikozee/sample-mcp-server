package com.zee.springai.mcpserver.tool;

import com.zee.springai.mcpserver.entity.Game;
import com.zee.springai.mcpserver.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.context.annotation.Import;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJdbcTest(properties = "spring.sql.init.data-locations=classpath:data.sql")
@Import(GameTools.class)
class GameToolsIntegrationTests {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    @Autowired
    private GameTools gameTools;

    @Autowired
    private GameRepository gameRepository;

    @Test
    void gameCountReflectsPersistedGamesAndEmptyRepository() {
        assertEquals(3L, gameTools.gameCount().block(TIMEOUT));

        gameRepository.deleteAll();

        assertEquals(0L, gameTools.gameCount().block(TIMEOUT));
    }

    @Test
    void findGamesForPlayerCountIncludesBoundariesAndExcludesUnsuitableGames() {
        assertEquals(List.of("Family game", "Quick duel"), titlesForPlayerCount(2));
        assertEquals(List.of("Family game"), titlesForPlayerCount(3));
        assertEquals(List.of("Family game"), titlesForPlayerCount(4));
        assertEquals(List.of(), titlesForPlayerCount(9));
    }

    @Test
    void findGamesForPlayingTimeIncludesBoundariesAndExcludesUnsuitableGames() {
        assertEquals(List.of("Family game", "Quick duel"), titlesForPlayingTime(20));
        assertEquals(List.of("Family game"), titlesForPlayingTime(30));
        assertEquals(List.of("Family game"), titlesForPlayingTime(40));
        assertEquals(List.of(), titlesForPlayingTime(41));
    }

    private List<String> titlesForPlayerCount(int players) {
        return gameTools.findGamesForPlayerCount(players)
                .map(Game::title).sort().collectList().block(TIMEOUT);
    }

    private List<String> titlesForPlayingTime(int minutes) {
        return gameTools.findGamesForPlayingTime(minutes)
                .map(Game::title).sort().collectList().block(TIMEOUT);
    }
}
