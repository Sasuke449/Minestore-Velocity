package me.abhiram.minestorevelocity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MinestoreCommand {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("command")
    @Expose
    private String command;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCommand() {
        return command;
    }
}
