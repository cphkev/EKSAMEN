package dat.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import dat.dto.PackingItemDTO;
import dat.dto.PackingItemResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class PackingService {
    private static final Logger log = LoggerFactory.getLogger(PackingService.class);

    public static List<PackingItemDTO> getPackingItems(String category) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Accept", "application/json")
                    .uri(new URI("https://packingapi.cphbusinessapps.dk/packinglist/" + category))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("API Response Status: {}", response.statusCode());
            log.info("API Response Body: {}", response.body());

            if (response.statusCode() == 200) {
                PackingItemResponse packingItemResponse = objectMapper.readValue(response.body(), PackingItemResponse.class);
                return packingItemResponse.getItems();
            } else {
                log.error("GET request failed. Status code: {}", response.statusCode());
            }
        } catch (Exception e) {
            log.error("Exception occurred while fetching packing items", e);
        }
        return Collections.emptyList();
    }
}