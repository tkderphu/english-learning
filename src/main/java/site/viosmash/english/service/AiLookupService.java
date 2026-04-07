package site.viosmash.english.service;

import site.viosmash.english.dto.response.TextLookupResponse;

public interface AiLookupService {
    TextLookupResponse lookupText(String text);
}
