package com.jobboard.modesl.dto;

public class JobListing {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTagsAndSkills() {
        return tagsAndSkills;
    }

    public void setTagsAndSkills(String tagsAndSkills) {
        this.tagsAndSkills = tagsAndSkills;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getMinimumExperience() {
        return minimumExperience;
    }

    public void setMinimumExperience(String minimumExperience) {
        this.minimumExperience = minimumExperience;
    }

    public String getMaximumExperience() {
        return maximumExperience;
    }

    public void setMaximumExperience(String maximumExperience) {
        this.maximumExperience = maximumExperience;
    }

    private String title;
    private String companyName;
    private String tagsAndSkills;
    private String jobDescription;
    private String createdDate;
    private String minimumExperience;
    private String maximumExperience;
}
