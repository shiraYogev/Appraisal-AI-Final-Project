package com.example.finalproject_appraisal.model;

import java.util.List;
/**
 * The Project class represents a property appraisal project in the system. Each project involves
 * evaluating multiple properties, and the appraiser evaluates and generates reports for the properties under
 * this project.
 *
 * This class stores details about the project, including its name, description, status, and the properties
 * associated with the project. A project can have multiple properties and may be assigned to specific appraisers
 * for evaluation.
 */

public class Project {
    // Client and appraiser details
    private Client client; // The client requesting the appraisal
    private Appraiser appraiser; // The appraiser performing the appraisal

    // Property details
    private String fullAddress; // Full address of the property
    private String location; // Location of the property
    private String buildingType; // Type of the building (e.g., apartment, house)
    private String buildingCondition; // Condition of the building (e.g., new, old, renovated)
    private int numberOfFloors; // Number of floors in the building

    // Apartment details
    private String apartmentNumber; // Apartment number
    private int floorNumber; // Floor number of the apartment
    private int numberOfRooms; // Number of rooms in the apartment
    private double registeredArea; // Registered area of the apartment
    private double grossArea; // Gross area of the apartment

    // Physical features of the apartment
    private String flooringType; // Type of flooring (e.g., tile, wood)
    private String kitchenCondition; // Condition of the kitchen (e.g., new, needs renovation)
    private String entranceDoorCondition; // Condition of the entrance door
    private String interiorDoorCondition; // Condition of interior doors
    private String windowType; // Type of windows (e.g., double-glazed)
    private boolean hasBars; // Whether the apartment has security bars on windows
    private List<String> airDirection; // Directions of air flow (e.g., north, south)

    // Additional facilities and services
    private boolean hasElevator; // Whether the building has an elevator
    private boolean hasStorageRoom; // Whether there is a storage room
    private boolean hasAirConditioning; // Whether the apartment has air conditioning
    private boolean hasParking; // Whether there is parking space
    private boolean hasCentralHeating; // Whether the building has central heating

    // Property images
    private List<Image> propertyImages; // List of property images

    // Additional fields
    private String projectStatus; // Status of the project (e.g., in progress, completed)
    private String projectDescription; // General description of the project

    // Constructor to initialize the project details
    public Project(Client client, Appraiser appraiser, String fullAddress, String location,
                   String buildingType, String buildingCondition, int numberOfFloors) {
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
        this.flooringType = flooringType;
        this.kitchenCondition = kitchenCondition;
        this.entranceDoorCondition = entranceDoorCondition;
        this.interiorDoorCondition = interiorDoorCondition;
        this.windowType = windowType;
        this.hasBars = hasBars;
        this.airDirection = airDirection;
        this.hasElevator = hasElevator;
        this.hasStorageRoom = hasStorageRoom;
        this.hasAirConditioning = hasAirConditioning;
        this.hasParking = hasParking;
        this.hasCentralHeating = hasCentralHeating;
        this.propertyImages = propertyImages;
        this.projectStatus = projectStatus;
        this.projectDescription = projectDescription;
    }

    // Getters and setters for each field
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

    public String getFlooringType() {
        return flooringType;
    }

    public void setFlooringType(String flooringType) {
        this.flooringType = flooringType;
    }

    public String getKitchenCondition() {
        return kitchenCondition;
    }

    public void setKitchenCondition(String kitchenCondition) {
        this.kitchenCondition = kitchenCondition;
    }

    public String getEntranceDoorCondition() {
        return entranceDoorCondition;
    }

    public void setEntranceDoorCondition(String entranceDoorCondition) {
        this.entranceDoorCondition = entranceDoorCondition;
    }

    public String getInteriorDoorCondition() {
        return interiorDoorCondition;
    }

    public void setInteriorDoorCondition(String interiorDoorCondition) {
        this.interiorDoorCondition = interiorDoorCondition;
    }

    public String getWindowType() {
        return windowType;
    }

    public void setWindowType(String windowType) {
        this.windowType = windowType;
    }

    public boolean isHasBars() {
        return hasBars;
    }

    public void setHasBars(boolean hasBars) {
        this.hasBars = hasBars;
    }

    public List<String> getAirDirection() {
        return airDirection;
    }

    public void setAirDirection(List<String> airDirection) {
        this.airDirection = airDirection;
    }

    public boolean isHasElevator() {
        return hasElevator;
    }

    public void setHasElevator(boolean hasElevator) {
        this.hasElevator = hasElevator;
    }

    public boolean isHasStorageRoom() {
        return hasStorageRoom;
    }

    public void setHasStorageRoom(boolean hasStorageRoom) {
        this.hasStorageRoom = hasStorageRoom;
    }

    public boolean isHasAirConditioning() {
        return hasAirConditioning;
    }

    public void setHasAirConditioning(boolean hasAirConditioning) {
        this.hasAirConditioning = hasAirConditioning;
    }

    public boolean isHasParking() {
        return hasParking;
    }

    public void setHasParking(boolean hasParking) {
        this.hasParking = hasParking;
    }

    public boolean isHasCentralHeating() {
        return hasCentralHeating;
    }

    public void setHasCentralHeating(boolean hasCentralHeating) {
        this.hasCentralHeating = hasCentralHeating;
    }

    public List<Image> getPropertyImages() {
        return propertyImages;
    }

    public void setPropertyImages(List<Image> propertyImages) {
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
