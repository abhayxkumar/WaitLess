package com.example.hetavdesai.pl2project;

public class AssistanceClass {
    String assistanceID, tableNo;

    public AssistanceClass(String assistanceID, String tableNo) {
        this.assistanceID = assistanceID;
        this.tableNo = tableNo;
    }

    public AssistanceClass() {
    }

    public String getAssistanceID() {
        return assistanceID;
    }

    public void setAssistanceID(String assistanceID) {
        this.assistanceID = assistanceID;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

}
