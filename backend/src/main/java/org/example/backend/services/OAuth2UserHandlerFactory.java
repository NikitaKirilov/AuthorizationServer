package org.example.backend.services;

import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class OAuth2UserHandlerFactory {

    private final Map<OAuth2ProviderType, OAuth2UserHandler> handlersMap = new EnumMap<>(OAuth2ProviderType.class);

    public OAuth2UserHandlerFactory(List<OAuth2UserHandler> handlers) {
        handlers.forEach(handler -> handlersMap.put(handler.getHandlerType(), handler));
    }

    public OAuth2UserHandler getHandler(OAuth2ProviderType type) {
        return handlersMap.get(type);
    }
}
