package com.chat.bot_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.chat.bot_service.dto.ExternalMediaResult;
import com.chat.bot_service.dto.JikanMediaItem;
import com.chat.bot_service.dto.JikanSearchResponse;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnimeMangaRecommendationClient {

    private final RestClient restClient;

    public AnimeMangaRecommendationClient(
            RestClient.Builder restClientBuilder,
            @Value("${jikan.api.base-url}") String baseUrl
    ) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public List<ExternalMediaResult> searchRecommendations(String userQuestion) {
        String searchQuery = buildSearchQuery(userQuestion);
        String normalizedQuestion = normalize(userQuestion);

        boolean asksForManga = containsAny(
                normalizedQuestion,
                "manga",
                "manhwa",
                "manhua",
                "capitulo",
                "capitulos",
                "leer"
        );

        boolean asksForAnime = containsAny(
                normalizedQuestion,
                "anime",
                "episodio",
                "episodios",
                "temporada",
                "ver"
        );

        List<ExternalMediaResult> results = new ArrayList<>();

        if (asksForManga && !asksForAnime) {
            results.addAll(searchManga(searchQuery, 5));
            return results;
        }

        if (asksForAnime && !asksForManga) {
            results.addAll(searchAnime(searchQuery, 5));
            return results;
        }

        // Si la pregunta no es clara, buscamos en anime y manga.
        results.addAll(searchAnime(searchQuery, 3));
        results.addAll(searchManga(searchQuery, 3));

        return results;
    }

    private List<ExternalMediaResult> searchAnime(String query, int limit) {
        JikanSearchResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/anime")
                        .queryParam("q", query)
                        .queryParam("sfw", true)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .body(JikanSearchResponse.class);

        if (response == null || response.data() == null) {
            return List.of();
        }

        return response.data()
                .stream()
                .map(item -> mapToExternalMediaResult(item, "ANIME"))
                .toList();
    }

    private List<ExternalMediaResult> searchManga(String query, int limit) {
        JikanSearchResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/manga")
                        .queryParam("q", query)
                        .queryParam("sfw", true)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .body(JikanSearchResponse.class);

        if (response == null || response.data() == null) {
            return List.of();
        }

        return response.data()
                .stream()
                .map(item -> mapToExternalMediaResult(item, "MANGA"))
                .toList();
    }

    private ExternalMediaResult mapToExternalMediaResult(JikanMediaItem item, String mediaCategory) {
        String imageUrl = null;

        if (item.images() != null && item.images().jpg() != null) {
            imageUrl = item.images().jpg().imageUrl();
        }

        return new ExternalMediaResult(
                item.malId(),
                mediaCategory,
                item.title(),
                item.titleEnglish(),
                cleanSynopsis(item.synopsis()),
                item.score(),
                item.episodes(),
                item.chapters(),
                item.volumes(),
                item.type(),
                item.status(),
                item.url(),
                imageUrl
        );
    }

    private String buildSearchQuery(String question) {
        String normalized = normalize(question);

        // Casos conocidos para mejorar búsquedas por descripción.
        if (containsAny(normalized, "titan", "titanes", "gigantes", "muralla", "murallas")) {
            return "attack on titan";
        }

        if (containsAny(normalized, "pirata", "piratas", "tesoro", "sombrero de paja")) {
            return "one piece";
        }

        if (containsAny(normalized, "ninja", "ninjas", "zorro", "kyubi", "hokage")) {
            return "naruto";
        }

        if (containsAny(normalized, "demonio", "demonios", "respiracion", "cazador de demonios")) {
            return "demon slayer";
        }

        if (containsAny(normalized, "alquimia", "alquimista", "armadura", "piedra filosofal")) {
            return "fullmetal alchemist";
        }

        if (containsAny(normalized, "cuaderno", "matar escribiendo", "shinigami", "death note")) {
            return "death note";
        }

        if (containsAny(normalized, "hechiceros", "maldiciones", "dedos", "sukuna", "gojo")) {
            return "jujutsu kaisen";
        }

        if (containsAny(normalized, "heroes", "superheroes", "academia", "quirk", "quirks")) {
            return "my hero academia";
        }

        if (containsAny(normalized, "baloncesto", "basket", "basquetbol")) {
            return "kuroko no basket";
        }

        if (containsAny(normalized, "voleibol", "voley")) {
            return "haikyuu";
        }

        if (containsAny(normalized, "futbol", "mundial", "delantero", "ego")) {
            return "blue lock";
        }

        if (containsAny(normalized, "boxeo", "boxeador")) {
            return "hajime no ippo";
        }

        if (containsAny(normalized, "magia", "grimorio", "sin magia", "rey mago")) {
            return "black clover";
        }

        if (containsAny(normalized, "espadas", "demonios", "hermana demonio")) {
            return "demon slayer";
        }

        if (containsAny(normalized, "videojuego", "atrapado en un juego", "realidad virtual")) {
            return "sword art online";
        }

        if (containsAny(normalized, "reencarna", "reencarnacion", "isekai", "otro mundo")) {
            return "mushoku tensei";
        }

        return extractUsefulWords(normalized);
    }

    private boolean containsAny(String text, String... words) {
        for (String word : words) {
            if (text.contains(word)) {
                return true;
            }
        }

        return false;
    }

    private String extractUsefulWords(String text) {
        String cleaned = text
                .replace("anime", "")
                .replace("manga", "")
                .replace("manhwa", "")
                .replace("manhua", "")
                .replace("donde", "")
                .replace("cuando", "")
                .replace("cual", "")
                .replace("cuál", "")
                .replace("podria", "")
                .replace("podría", "")
                .replace("recomienda", "")
                .replace("recomiendame", "")
                .replace("recomiéndame", "")
                .replace("parecido", "")
                .replace("similar", "")
                .replace("no me se el nombre", "")
                .replace("no me sé el nombre", "")
                .replace("hay un", "")
                .replace("hay una", "")
                .replace("quiero ver", "")
                .replace("quiero leer", "")
                .replace("me acuerdo que", "")
                .replace("creo que", "")
                .trim();

        if (cleaned.isBlank()) {
            return "anime";
        }

        return cleaned;
    }

    private String normalize(String text) {
        String lowerText = text.toLowerCase().trim();

        return Normalizer.normalize(lowerText, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

    private String cleanSynopsis(String synopsis) {
        if (synopsis == null || synopsis.isBlank()) {
            return "Sin sinopsis disponible.";
        }

        String cleaned = synopsis
                .replace("[Written by MAL Rewrite]", "")
                .replace("(Source: MyAnimeList)", "")
                .trim();

        if (cleaned.length() > 350) {
            return cleaned.substring(0, 350) + "...";
        }

        return cleaned;
    }
}