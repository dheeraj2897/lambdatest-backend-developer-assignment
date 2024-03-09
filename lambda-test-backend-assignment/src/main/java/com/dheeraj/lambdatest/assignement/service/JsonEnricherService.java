package com.dheeraj.lambdatest.assignement.service;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.dheeraj.lambdatest.assignement.model.EnrichedData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class JsonEnricherService {

    @Value("${json.enrichment.input.directory}")
    private String inputDirectory;

    @Value("${json.enrichment.output.file}")
    private String outputFile;

    private static final Logger logger = LoggerFactory.getLogger(JsonEnricherService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void enrichJsonFiles() throws IOException {
        logger.info("Enriching JSON files...");

        EnrichedData enrichedData = new EnrichedData();
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(inputDirectory + "/*.json");

        for (Resource resource : resources) {
            logger.info("Processing file: {}", resource.getFilename());
            JsonNode jsonNode = objectMapper.readTree(resource.getInputStream());
            mergeJsonNodes(enrichedData, jsonNode);
        }

        objectMapper.writeValue(new File(outputFile), enrichedData);
        logger.info("Enriched data written to: {}", outputFile);
    }

    private void mergeJsonNodes(EnrichedData dest, JsonNode source) {
        source.fields().forEachRemaining(entry -> {
            String fieldName = entry.getKey();
            JsonNode fieldValue = entry.getValue();

            logger.info("Merging field: {} - Value: {}", fieldName, fieldValue);

            if (dest.getAdditionalProperties().containsKey(fieldName)) {
                JsonNode existingValue = (JsonNode) dest.getAdditionalProperties().get(fieldName);
                JsonNode mergedValue = mergeFieldValues(existingValue, fieldValue);
                dest.setAdditionalProperty(fieldName, mergedValue);
            } else {
                dest.setAdditionalProperty(fieldName, fieldValue);
            }
        });
    }

    private JsonNode mergeFieldValues(JsonNode existingValue, JsonNode newValue) {
        if (existingValue.isArray() && newValue.isArray()) {
            // If both values are arrays, concatenate them
            ArrayNode mergedArray = objectMapper.createArrayNode();
            mergedArray.addAll((ArrayNode) existingValue);
            mergedArray.addAll((ArrayNode) newValue);
            return mergedArray;
        } else if (existingValue.isObject() && newValue.isObject()) {
            // If both values are objects, recursively merge them
            EnrichedData mergedData = new EnrichedData();
            mergeJsonNodes(mergedData, existingValue);
            mergeJsonNodes(mergedData, newValue);
            return objectMapper.valueToTree(mergedData);
        } else {
            // Choose the value from newValue, you can adjust this logic based on your priorities
            return newValue;
        }
    }
}
