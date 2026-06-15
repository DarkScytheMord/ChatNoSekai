package com.chat.bot_service.service;

import org.springframework.stereotype.Service;

import com.chat.bot_service.repository.BotMessageRepository;

import com.chat.bot_service.dto.BotQuestionRequest;
import com.chat.bot_service.dto.BotResponse;
import com.chat.bot_service.model.BotMessage;

import com.chat.bot_service.dto.BotHistoryResponse;

import com.chat.bot_service.dto.ExternalMediaResult;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatBotService {

    private final BotMessageRepository botMessageRepository;
    private final AnimeMangaRecommendationClient recommendationClient;

    public ChatBotService(
            BotMessageRepository botMessageRepository,
            AnimeMangaRecommendationClient recommendationClient
    ) {
        this.botMessageRepository = botMessageRepository;
        this.recommendationClient = recommendationClient;
    }

    public BotResponse askQuestion(BotQuestionRequest request, Long userId) {
        String answer;

        try {
            List<ExternalMediaResult> recommendations =
                    recommendationClient.searchRecommendations(request.question());

            answer = buildRecommendationAnswer(request.question(), recommendations);
        } catch (Exception exception) {
            answer = "No pude conectarme a la API externa de anime/manga en este momento. " +
                    "Puede que el servicio esté ocupado o haya alcanzado su límite temporal de consultas. " +
                    "Intenta nuevamente en unos segundos.";
        }

        BotMessage botMessage = new BotMessage(
                userId,
                request.question(),
                answer,
                LocalDateTime.now()
        );

        BotMessage savedMessage = botMessageRepository.save(botMessage);

        return new BotResponse(
                savedMessage.getId(),
                savedMessage.getUserId(),
                savedMessage.getQuestion(),
                savedMessage.getAnswer(),
                savedMessage.getCreatedAt()
        );
    }

    public List<BotHistoryResponse> getMyHistory(Long userId) {
        return botMessageRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(message -> new BotHistoryResponse(
                        message.getId(),
                        message.getQuestion(),
                        message.getAnswer(),
                        message.getCreatedAt()
                ))
                .toList();
    }

    private String buildRecommendationAnswer(
            String originalQuestion,
            List<ExternalMediaResult> recommendations
    ) {
        if (recommendations == null || recommendations.isEmpty()) {
            return "No encontré resultados claros para tu búsqueda. " +
                    "Intenta describirlo con más detalles, por ejemplo: género, personajes, poderes, deporte, escenas importantes o si buscas anime o manga.";
        }

        StringBuilder builder = new StringBuilder();

        builder.append("Según tu consulta: \"")
                .append(originalQuestion)
                .append("\", estas podrían ser buenas opciones:\n\n");

        int maxResults = Math.min(recommendations.size(), 6);

        for (int i = 0; i < maxResults; i++) {
            ExternalMediaResult media = recommendations.get(i);

            builder.append(i + 1)
                    .append(". ")
                    .append(getBestTitle(media))
                    .append("\n");

            builder.append("   Categoría: ")
                    .append(media.mediaCategory())
                    .append("\n");

            if (media.score() != null) {
                builder.append("   Puntuación: ")
                        .append(media.score())
                        .append("\n");
            }

            if (media.episodes() != null) {
                builder.append("   Episodios: ")
                        .append(media.episodes())
                        .append("\n");
            }

            if (media.chapters() != null) {
                builder.append("   Capítulos: ")
                        .append(media.chapters())
                        .append("\n");
            }

            if (media.volumes() != null) {
                builder.append("   Volúmenes: ")
                        .append(media.volumes())
                        .append("\n");
            }

            if (media.type() != null) {
                builder.append("   Tipo: ")
                        .append(media.type())
                        .append("\n");
            }

            if (media.status() != null) {
                builder.append("   Estado: ")
                        .append(media.status())
                        .append("\n");
            }

            builder.append("   Sinopsis: ")
                    .append(media.synopsis())
                    .append("\n");

            if (media.url() != null) {
                builder.append("   Más información: ")
                        .append(media.url())
                        .append("\n");
            }

            builder.append("\n");
        }

        builder.append("Mi recomendación principal sería: ")
                .append(getBestTitle(recommendations.get(0)))
                .append(".");

        return builder.toString();
    }

    private String getBestTitle(ExternalMediaResult media) {
        if (media.titleEnglish() != null && !media.titleEnglish().isBlank()) {
            return media.titleEnglish() + " / " + media.title();
        }

        return media.title();
    }
}