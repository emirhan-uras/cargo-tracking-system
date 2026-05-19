package com.example.cargotracking.service.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OpenRouteService {

    @Value("${heigit.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public double[] getCoordinates(String addressText) {
        try {
            String url = "https://api.openrouteservice.org/geocode/search?api_key=" + apiKey
                    + "&text=" + addressText + "&boundary.country=TR&size=1";

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            JsonNode coordinatesNode = root.path("features").get(0).path("geometry").path("coordinates");

            double lon = coordinatesNode.get(0).asDouble();
            double lat = coordinatesNode.get(1).asDouble();

            return new double[]{lon, lat};
        }
        catch (Exception e) {
            return new double[]{35.48, 38.72};
        }
    }

    public double getDistanceInKm(double[] startCoords, double[] endCoords) {
        try {
            String url = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=" + apiKey
                    + "&start=" + startCoords[0] + "," + startCoords[1]
                    + "&end=" + endCoords[0] + "," + endCoords[1];

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            double distanceInMeters = root.path("features").get(0)
                    .path("properties").path("summary").path("distance").asDouble();

            return distanceInMeters / 1000.0;
        }
        catch (Exception e) {
            return 300.0;
        }
    }
}