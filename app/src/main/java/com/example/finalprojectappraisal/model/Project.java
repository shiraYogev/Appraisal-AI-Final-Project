package com.example.finalprojectappraisal.model;

import java.util.ArrayList;
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

    // Project identification
    private String projectId;


    // Client and appraiser details
    private Client client; // The client requesting the appraisal
    private Appraiser appraiser; // The appraiser performing the appraisal


    // Creation and update timestamps
    private long creationDate;
    private long lastUpdateDate;


    // Property details
    private String fullAddress; // Full address of the property
    private String location; // Location of the property
    private String buildingType; // Type of the building (e.g., apartment, house)
    private String buildingCondition; // Condition of the building (e.g., new, old, renovated)
    private int numberOfFloors; // Number of floors in the building


    // Apartment details
    private String apartmentNumber; // Apartment number
    private int floorNumber; // Floor number of the apartment
    private double numberOfRooms; // Number of rooms in the apartment (can be 2.5, 3.5, etc.)
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
    //////////// check if i need this object or not !!!!!!!! ///////
    private PhysicalFeatures physicalFeatures;
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



    //////////     Constructors:     ////////////

    /**
     * Default constructor for Firestore
     */
    public Project() {
        this.creationDate = System.currentTimeMillis();
        this.lastUpdateDate = System.currentTimeMillis();
        this.projectStatus = "In Progress";
        this.propertyImages = new ArrayList<Image>();
    }


    /**
     * Constructor with required fields for initial project creation
     */
    public Project(String projectId) {
        this();
        this.projectId = projectId;
    }


    /**
     * Constructor with all fields for complete project initialization
     */
    public Project(String projectId, Client client, Appraiser appraiser, String fullAddress, String location,
                   String buildingType, String buildingCondition, int numberOfFloors,
                   String apartmentNumber, int floorNumber, double numberOfRooms,
                   double registeredArea, double grossArea) {
        this();
        this.projectId = projectId;
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
    }


    // Constructor to initialize the project details
    public Project(String projectId, Client client, Appraiser appraiser, String fullAddress, String location,
                   String buildingType, String buildingCondition, int numberOfFloors) {
        this.projectId = projectId;
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

    public void updateLastUpdateDate() {
        this.lastUpdateDate = System.currentTimeMillis();
    }


    // Getters and Setters
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        updateLastUpdateDate();
    }

    public Appraiser getAppraiser() {
        return appraiser;
    }

    public void setAppraiser(Appraiser appraiser) {
        this.appraiser = appraiser;
        updateLastUpdateDate();
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(long lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
        updateLastUpdateDate();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        updateLastUpdateDate();
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
        updateLastUpdateDate();
    }

    public String getBuildingCondition() {
        return buildingCondition;
    }

    public void setBuildingCondition(String buildingCondition) {
        this.buildingCondition = buildingCondition;
        updateLastUpdateDate();
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
        updateLastUpdateDate();
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
        updateLastUpdateDate();
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
        updateLastUpdateDate();
    }

    public double getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(double numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
        updateLastUpdateDate();
    }

    public double getRegisteredArea() {
        return registeredArea;
    }

    public void setRegisteredArea(double registeredArea) {
        this.registeredArea = registeredArea;
        updateLastUpdateDate();
    }

    public double getGrossArea() {
        return grossArea;
    }

    public void setGrossArea(double grossArea) {
        this.grossArea = grossArea;
        updateLastUpdateDate();
    }

    public String getFlooringType() {
        return flooringType;
    }

    public void setFlooringType(String flooringType) {
        this.flooringType = flooringType;
        updateLastUpdateDate();
    }

    public String getKitchenCondition() {
        return kitchenCondition;
    }

    public void setKitchenCondition(String kitchenCondition) {
        this.kitchenCondition = kitchenCondition;
        updateLastUpdateDate();
    }

    public String getEntranceDoorCondition() {
        return entranceDoorCondition;
    }

    public void setEntranceDoorCondition(String entranceDoorCondition) {
        this.entranceDoorCondition = entranceDoorCondition;
        updateLastUpdateDate();
    }

    public String getInteriorDoorCondition() {
        return interiorDoorCondition;
    }

    public void setInteriorDoorCondition(String interiorDoorCondition) {
        this.interiorDoorCondition = interiorDoorCondition;
        updateLastUpdateDate();
    }

    public String getWindowType() {
        return windowType;
    }

    public void setWindowType(String windowType) {
        this.windowType = windowType;
        updateLastUpdateDate();
    }

    public boolean isHasBars() {
        return hasBars;
    }

    public void setHasBars(boolean hasBars) {
        this.hasBars = hasBars;
        updateLastUpdateDate();
    }

    public List<String> getAirDirection() {
        return airDirection;
    }

    public void setAirDirection(List<String> airDirection) {
        this.airDirection = airDirection;
        updateLastUpdateDate();
    }

    public boolean isHasElevator() {
        return hasElevator;
    }

    public void setHasElevator(boolean hasElevator) {
        this.hasElevator = hasElevator;
        updateLastUpdateDate();
    }

    public boolean isHasStorageRoom() {
        return hasStorageRoom;
    }

    public void setHasStorageRoom(boolean hasStorageRoom) {
        this.hasStorageRoom = hasStorageRoom;
        updateLastUpdateDate();
    }

    public boolean isHasAirConditioning() {
        return hasAirConditioning;
    }

    public void setHasAirConditioning(boolean hasAirConditioning) {
        this.hasAirConditioning = hasAirConditioning;
        updateLastUpdateDate();
    }

    public boolean isHasParking() {
        return hasParking;
    }

    public void setHasParking(boolean hasParking) {
        this.hasParking = hasParking;
        updateLastUpdateDate();
    }

    public boolean isHasCentralHeating() {
        return hasCentralHeating;
    }

    public void setHasCentralHeating(boolean hasCentralHeating) {
        this.hasCentralHeating = hasCentralHeating;
        updateLastUpdateDate();
    }

    public List<Image> getPropertyImages() {
        return propertyImages;
    }

    public void setPropertyImages(List<Image> propertyImages) {
        this.propertyImages = propertyImages;
        updateLastUpdateDate();
    }

    public void addPropertyImage(Image image) {
        if (this.propertyImages == null) {
            this.propertyImages = new ArrayList<>();
        }
        this.propertyImages.add(image);
        updateLastUpdateDate();
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
        updateLastUpdateDate();
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
        updateLastUpdateDate();
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId='" + projectId + '\'' +
                ", client=" + (client != null ? client.getFullName() : "null") +
                ", appraiser=" + (appraiser != null ? appraiser.getFullName() : "null") +
                ", fullAddress='" + fullAddress + '\'' +
                ", projectStatus='" + projectStatus + '\'' +
                '}';
    }
}
