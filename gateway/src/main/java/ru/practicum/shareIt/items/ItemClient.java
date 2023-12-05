package ru.practicum.shareIt.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareIt.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> create(ItemDto itemDto) {
        return post("", itemDto);
    }

    public ResponseEntity<Object> patch(ItemDto itemDto, Long itemId, Long userId) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> get(int itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> get(Long userId) {
        return get("/", userId);
    }

    public ResponseEntity<Object> get(Long userId, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> addComment(Long userId, int itemId, CommentsDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }

}
