package com.und.eventapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

/**
 * Created by shiv on 21/07/17.
 */
//@Document(collection = "#{tenantId}_event")
public class Event {

    @Id
    private String id;

    private Long userid = null;

    private Boolean isanonymus = null;

    private String username = null;

    private String firstName = null;

    private String lastName = null;

    private String email = null;

    private String phone = null;

    private String time = null;


    private String name;

    private HashMap<String, String> map = null;

    public Event() {
    }

    public Event(String name) {
        this.name = name;
    }

    @ApiModelProperty(required = true, value = "unique event name")
    @NotNull
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    /**
     * userid to track
     *
     * @return userid
     **/
    @ApiModelProperty(required = true, value = "userid to track")
    @NotNull
    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Event isanonymus(Boolean isanonymus) {
        this.isanonymus = isanonymus;
        return this;
    }

    /**
     * if this property is true user details are tracked only for this session. all other user details are ignored.
     *
     * @return isanonymus
     **/
    @ApiModelProperty(value = "if this property is true user details are tracked only for this session. all other user details are ignored. ")
    public Boolean getIsanonymus() {
        return isanonymus;
    }

    public void setIsanonymus(Boolean isanonymus) {
        this.isanonymus = isanonymus;
    }

    public Event username(String username) {
        this.username = username;
        return this;
    }

    /**
     * Get username
     *
     * @return username
     **/
    @ApiModelProperty(value = "")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Event firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * Get firstName
     *
     * @return firstName
     **/
    @ApiModelProperty(value = "")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Event lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * Get lastName
     *
     * @return lastName
     **/
    @ApiModelProperty(value = "")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Event email(String email) {
        this.email = email;
        return this;
    }

    /**
     * Get email
     *
     * @return email
     **/
    @ApiModelProperty(value = "")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Event phone(String phone) {
        this.phone = phone;
        return this;
    }

    /**
     * Get phone
     *
     * @return phone
     **/
    @ApiModelProperty(value = "")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Event time(String time) {
        this.time = time;
        return this;
    }

    /**
     * Get time
     *
     * @return time
     **/
    @ApiModelProperty(value = "")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @ApiModelProperty(value = "")
    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }


}
