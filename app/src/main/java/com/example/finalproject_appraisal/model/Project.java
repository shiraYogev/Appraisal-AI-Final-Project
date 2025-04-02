package com.example.finalproject_appraisal.model;

import java.util.HashMap;
import java.util.List;

/**
 * The Project class represents a property appraisal project in the system.
 * It contains details about the project, including client, appraiser, property details, and images.
 */
public class Project {
    // Client and appraiser details
    private Client client;
    private Appraiser appraiser;

    // Property details
    private String fullAddress;
    private String location;
    private String buildingType;
    private String buildingCondition;
    private int numberOfFloors;

    // Apartment details
    private String apartmentNumber;
    private int floorNumber;
    private int numberOfRooms;
    private double registeredArea;
    private double grossArea;

    // Physical features (moved to a separate model, e.g., PhysicalFeatures)
    private PhysicalFeatures physicalFeatures;

    // Property images stored as a map: for each category, a list of images is maintained
    private HashMap<Image.Category, List<Image>> propertyImages;

    // Additional fields
    private String projectStatus;
    private String projectDescription;

    public Project(Client client, Appraiser appraiser, String fullAddress, String location,
                   String buildingType, String buildingCondition, int numberOfFloors,
                   String apartmentNumber, int floorNumber, int numberOfRooms,
                   double registeredArea, double grossArea, PhysicalFeatures physicalFeatures,
                   HashMap<Image.Category, List<Image>> propertyImages, String projectStatus, String projectDescription) {
        this.client = client;
        this.appraiser = appraiser;
        this.fullAddress = fullAddress;
        this.location = location;
        this.buildingType = buildingType;
        this.buildingCondition = buildingCondition;
        this.numberOfFloors = numberOfFloors;
        this.apartmentNumber = apartmentNumber;
        this.floorNumber = floorNumber;
        this.numberOfRooms = numberOfRooms;
        this.registeredArea = registeredArea;
        this.grossArea = grossArea;
        this.physicalFeatures = physicalFeatures;
        this.propertyImages = propertyImages;
        this.projectStatus = projectStatus;
        this.projectDescription = projectDescription;
    }

    // Getters and setters

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Appraiser getAppraiser() {
        return appraiser;
    }

    public void setAppraiser(Appraiser appraiser) {
        this.appraiser = appraiser;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    public String getBuildingCondition() {
        return buildingCondition;
    }

    public void setBuildingCondition(String buildingCondition) {
        this.buildingCondition = buildingCondition;
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public double getRegisteredArea() {
        return registeredArea;
    }

    public void setRegisteredArea(double registeredArea) {
        this.registeredArea = registeredArea;
    }

    public double getGrossArea() {
        return grossArea;
    }

    public void setGrossArea(double grossArea) {
        this.grossArea = grossArea;
    }

    public PhysicalFeatures getPhysicalFeatures() {
        return physicalFeatures;
    }

    public void setPhysicalFeatures(PhysicalFeatures physicalFeatures) {
        this.physicalFeatures = physicalFeatures;
    }

    public HashMap<Image.Category, List<Image>> getPropertyImages() {
        return propertyImages;
    }

    public void setPropertyImages(HashMap<Image.Category, List<Image>> propertyImages) {
        this.propertyImages = propertyImages;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }
}
