package com.rultech.naman.syncnewcontact;

public class userModel {
    String userId;
    String versionCpde;

    public userModel() {
        this.userId = "";
        this.versionCpde = "";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVersionCpde() {
        return versionCpde;
    }

    public void setVersionCpde(String versionCpde) {
        this.versionCpde = versionCpde;
    }
}
