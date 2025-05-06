package com.example.finalproject_appraisal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The `Image` class represents an image associated with a property or project.
 * This class stores the URL of the image, its category (e.g., living room, kitchen, front view),
 * and optional tags or labels related to the image.
 * It also provides functionality to display image information in a readable format.
 */
public class Image {

    // Enum for categories to make the categorization of images more robust and type-safe
    public enum Category {
        EXTERIOR, LIVING_ROOM, KITCHEN, BATHROOM, BEDROOM, VIEW, OTHER
    }

    private String id;        // Unique identifier for the image
    private String url;       // URL of the image (location of the image in the cloud or server)
    private Category category; // Category of the image (e.g., front view, living room, kitchen, etc.)
    private List<AiImageTag> aiTags; // Tags related to the image (e.g., AI-detected features)
    private String description; // Optional description of the image (e.g., a caption)
    private String uploadDate; // Upload date in ISO format (YYYY-MM-DD)
    private String propertyId; // The ID of the property this image belongs to

    // Constructor
    public Image(String url, Category category, String propertyId, String description, List<AiImageTag> aiTags, boolean isVerified) {
        this.id = generateUniqueId(); // Generate unique ID
        this.url = url;
        this.category = category;
        this.aiTags = aiTags != null ? aiTags : new ArrayList<>();
        this.description = description;
        this.uploadDate = java.time.LocalDate.now().toString(); // Set upload date to current date
        this.propertyId = propertyId;
    }

    // Generate unique ID
    private String generateUniqueId() {
        return UUID.randomUUID().toString(); // Using UUID to generate a unique ID
    }

    // Add an AI tag to the image
    public void addAiTag(AiImageTag tag) {
        if (!this.aiTags.contains(tag)) {
            this.aiTags.add(tag);
        }
    }

    // Remove an AI tag from the image
    public void removeAiTag(String tagType, String tagValue) {
        this.aiTags.removeIf(tag -> tag.getType().equals(tagType) && tag.getValue().equals(tagValue));
    }

    // Update image category
    public void updateCategory(Category category) {
        this.category = category;
    }

    // Check if the image has a specific feature (AI tag)
    public boolean hasFeature(String featureType, String featureValue) {
        return this.aiTags.stream().anyMatch(tag -> tag.getType().equals(featureType) && (featureValue == null || tag.getValue().equals(featureValue)));
    }

    // Convert the image object to JSON (basic data for transfer)
    public String toJSON() {
        return "{" +
                "\"id\":\"" + id + "\"," +
                "\"url\":\"" + url + "\"," +
                "\"category\":\"" + category + "\"," +
                "\"aiTags\":" + aiTags.toString() + "," +
                "\"description\":\"" + description + "\"," +
                "\"uploadDate\":\"" + uploadDate + "\"," +
                "\"propertyId\":\"" + propertyId + "\"" +
                "}";
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Category getCategory() {
        return category;
    }

    public List<AiImageTag> getAiTags() {
        return aiTags;
    }

    public String getDescription() {
        return description;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public String getPropertyId() {
        return propertyId;
    }

    // Setters
    public void setUrl(String url) {
        this.url = url;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setAiTags(List<AiImageTag> aiTags) {
        this.aiTags = aiTags;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }
}