package com.example.finalprojectappraisal.model;

import java.util.List;

/**
 * The Appraiser class represents a property appraiser in the system who is responsible for evaluating
 * properties and providing appraisals. Each appraiser can work on multiple projects and assess various properties.
 *
 * This class stores the appraiser's personal information (name, email, contact details), as well as the list
 * of projects they are involved in. The appraiser performs property evaluations and generates reports based on
 * the findings.
 */

public class Appraiser {

    public enum AccessPermission {
        ADMIN,
        USER,
        VIEWER
    }

    private String appraiserId;             // Unique identifier for the appraiser
    private String fullName;                // Full name of the appraiser
    private String email;                   // Email address for contact
    private String phoneNumber;             // Phone number (mobile and/or office)
    private List<Project> activeProjects;    // List of project IDs the appraiser is currently working on
    private List<Project> appraisalHistory;  // List of appraisals made by the appraiser, including property details
    private AccessPermission accessPermissions;     // Access level for the appraiser (e.g., restricted to their projects, admin access)

    // Constructor
    public Appraiser(String appraiserId, String fullName, String email,
                     String phoneNumber, List<Project> activeProjects,
                     List<Project> appraisalHistory, AccessPermission accessPermissions) {
        this.appraiserId = appraiserId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.activeProjects = activeProjects;
        this.appraisalHistory = appraisalHistory;
        this.accessPermissions = accessPermissions;
    }

    // Getters and setters
    public String getAppraiserId() {
        return appraiserId;
    }

    public void setAppraiserId(String appraiserId) {
        this.appraiserId = appraiserId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Project> getActiveProjects() {
        return activeProjects;
    }

    public void setActiveProjects(List<Project> activeProjects) {
        this.activeProjects = activeProjects;
    }

    public List<Project> getAppraisalHistory() {
        return appraisalHistory;
    }

    public void setAppraisalHistory(List<Project> appraisalHistory) {
        this.appraisalHistory = appraisalHistory;
    }

    public AccessPermission getAccessPermissions() {
        return accessPermissions;
    }

    public void setAccessPermissions(AccessPermission accessPermissions) {
        this.accessPermissions = accessPermissions;
    }

    // Method to display appraiser's details in a readable format
    @Override
    public String toString() {
        return "Appraiser{" +
                "appraiserId='" + appraiserId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", activeProjects=" + activeProjects +
                ", appraisalHistory=" + appraisalHistory +
                ", accessPermissions='" + accessPermissions + '\'' +
                '}';
    }
}