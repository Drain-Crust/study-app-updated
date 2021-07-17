package com.example.study_application;

import org.jetbrains.annotations.NotNull;

public class TasksList {

    // initial variables
    private final String TITLE;
    private final String IDS;
    private final String STATUS;
    private final String SPECIFICATIONS;
    private boolean selected;
    private boolean expanded;

    // allowing for the usage of the argument variables.
    public TasksList(String IDs, String title, String status, String Specifications) {
        this.IDS = IDs;
        this.TITLE = title;
        this.STATUS = status;
        this.SPECIFICATIONS = Specifications;
        this.expanded = false;
        this.selected = false;
    }

    //used private variables so to use them i have to call the variables through methods set and get
    public String getIDS() {
        return IDS;
    }

    public String getTITLE() {
        return TITLE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public String getSPECIFICATIONS() {
        return SPECIFICATIONS;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    // the layout to put data inside listArray
    @Override
    public @NotNull String toString() {
        return "TasksList{" +
                "IDs='" + IDS + '\'' +
                ", title='" + TITLE + '\'' +
                ", status='" + STATUS + '\'' +
                ", Specifications='" + SPECIFICATIONS + '\'' +
                ", expanded=" + expanded + '\'' +
                ", selected='" + selected + '}';
    }
}
