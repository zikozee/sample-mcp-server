package com.zee.springai.mcpserver.entity;


import org.springframework.data.annotation.Id;

/**
 * @dev : Ezekiel Eromosei
 * @date : 11 Oct, 2026
 */

public record Game(
        @Id
        Long id,
        String title,
        String description,
        Integer minPlayers,
        Integer maxPlayers,
        Integer minPlayingTime,
        Integer maxPlayingTime) {}