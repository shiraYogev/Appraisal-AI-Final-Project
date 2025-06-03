package com.example.finalprojectappraisal.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The `Image` class represents an image associated with a property or project.
 * This class stores the URL of the image, its category (e.g., living room, kitchen, front view),
 * and optional tags or labels related to the image.
 * It also provides functionality to display image information in a readable format.
 */
public class Image {

    public enum Category {
        EXTERIOR, LIVING_ROOM, KITCHEN, BATHROOM, BEDROOM, VIEW, DINING_ROOM,
        BALCONY, STORAGE, HALLWAY, ENTRANCE, GARDEN, ELEVATOR, PARKING, OTHER
    }

    public enum Subcategory {
        CABINETS, WORKTOP, SINK, FLOOR, DOOR, WINDOW, LIGHTING, FURNITURE,
        SHOWER, BATHTUB, TOILET, CLOSET, SHELVES, APPLIANCES, WALLS, CEILING, OTHER
    }

    private String id;
    private String url;
    private String projectId;
    private Category category;
    private List<Subcategory> subcategories;
    private Map<Subcategory, String> aiClassifications; // תוצאת סיווג Gemini לכל תת־קטגוריה
    private String description;
    private String uploadDate;
    private List<String> labels;
    private boolean isVerified;
    private Map<Subcategory, String> finalClassification; // תוצאה סופית אם נערך ידנית

    // בנאי מלא
    public Image(String url, String projectId, Category category,
                 List<Subcategory> subcategories, Map<Subcategory, String> aiClassifications,
                 String description, List<String> labels, boolean isVerified,
                 Map<Subcategory, String> finalClassification) {
        this.id = UUID.randomUUID().toString();
        this.url = url;
        this.projectId = projectId;
        this.category = category;
        this.subcategories = subcategories != null ? subcategories : new ArrayList<>();
        this.aiClassifications = aiClassifications != null ? aiClassifications : new HashMap<>();
        this.description = description;
        this.uploadDate = java.time.LocalDate.now().toString();
        this.labels = labels != null ? labels : new ArrayList<>();
        this.isVerified = isVerified;
        this.finalClassification = finalClassification != null ? finalClassification : new HashMap<>();
    }

    // Empty constructor (ל-Firestore/serialization)
    public Image() {
        this.id = UUID.randomUUID().toString(); // יצירת מזהה אוטומטי
    }

    public Image(Map<String, Object> map) {
        this.id = (String) map.get("id");
        this.url = (String) map.get("url");
        this.projectId = (String) map.get("projectId");
        this.category = map.get("category") != null ? Category.valueOf((String) map.get("category")) : null;

        // subcategories
        this.subcategories = new ArrayList<>();
        List<String> subcats = (List<String>) map.get("subcategories");
        if (subcats != null) for (String subcat : subcats) this.subcategories.add(Subcategory.valueOf(subcat));

        // aiClassifications
        this.aiClassifications = new HashMap<>();
        Map<String, String> aiMap = (Map<String, String>) map.get("aiClassifications");
        if (aiMap != null) for (Map.Entry<String, String> entry : aiMap.entrySet())
            this.aiClassifications.put(Subcategory.valueOf(entry.getKey()), entry.getValue());

        this.description = (String) map.get("description");
        this.uploadDate = (String) map.get("uploadDate");
        this.labels = (List<String>) map.get("labels");
        this.isVerified = (Boolean) map.get("isVerified");

        // finalClassification
        this.finalClassification = new HashMap<>();
        Map<String, String> finalMap = (Map<String, String>) map.get("finalClassification");
        if (finalMap != null) for (Map.Entry<String, String> entry : finalMap.entrySet())
            this.finalClassification.put(Subcategory.valueOf(entry.getKey()), entry.getValue());
    }


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("url", url);
        map.put("projectId", projectId);
        map.put("category", category != null ? category.name() : null);

        // שמירת רשימת תתי־קטגוריות כמחרוזות
        List<String> subcatNames = new ArrayList<>();
        if (subcategories != null) {
            for (Subcategory sub : subcategories) subcatNames.add(sub.name());
        }
        map.put("subcategories", subcatNames);

        // Map: שם תת־קטגוריה -> ערך
        Map<String, String> aiClassNames = new HashMap<>();
        if (aiClassifications != null) {
            for (Map.Entry<Subcategory, String> entry : aiClassifications.entrySet())
                aiClassNames.put(entry.getKey().name(), entry.getValue());
        }
        map.put("aiClassifications", aiClassNames);

        map.put("description", description);
        map.put("uploadDate", uploadDate);
        map.put("labels", labels);
        map.put("isVerified", isVerified);

        Map<String, String> finalClassNames = new HashMap<>();
        if (finalClassification != null) {
            for (Map.Entry<Subcategory, String> entry : finalClassification.entrySet())
                finalClassNames.put(entry.getKey().name(), entry.getValue());
        }
        map.put("finalClassification", finalClassNames);

        return map;
    }



    // Getters and setters (להוסיף לפי הצורך)

    public String getId() { return id; }
    public String getUrl() { return url; }
    public String getProjectId() { return projectId; }
    public Category getCategory() { return category; }
    public List<Subcategory> getSubcategories() { return subcategories; }
    public Map<Subcategory, String> getAiClassifications() { return aiClassifications; }
    public String getDescription() { return description; }
    public String getUploadDate() { return uploadDate; }
    public List<String> getLabels() { return labels; }
    public boolean isVerified() { return isVerified; }
    public Map<Subcategory, String> getFinalClassification() { return finalClassification; }

    public void setId(String id) { this.id = id; }
    public void setUrl(String url) { this.url = url; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    public void setCategory(Category category) { this.category = category; }
    public void setSubcategories(List<Subcategory> subcategories) { this.subcategories = subcategories; }
    public void setAiClassifications(Map<Subcategory, String> aiClassifications) { this.aiClassifications = aiClassifications; }
    public void setDescription(String description) { this.description = description; }
    public void setUploadDate(String uploadDate) { this.uploadDate = uploadDate; }
    public void setLabels(List<String> labels) { this.labels = labels; }
    public void setVerified(boolean verified) { isVerified = verified; }
    public void setFinalClassification(Map<Subcategory, String> finalClassification) { this.finalClassification = finalClassification; }
}