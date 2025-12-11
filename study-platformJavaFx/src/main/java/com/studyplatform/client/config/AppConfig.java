package com.studyplatform.client.config;

public class AppConfig {
    //base urls
    public static final String BASE_URL = "http://localhost:8080";
    public static final String API_URL = BASE_URL + "/api";

    //endpointy userov
    public static final String LOGIN_URL = API_URL + "/users/login";
    public static final String REGISTER_URL = API_URL + "/users/register";
    public static final String USERS_URL = API_URL + "/users";

    //endopointy groups
    public static final String GROUPS_URL = API_URL + "/groups";

    //endpointy tasks
    public static final String TASKS_URL = API_URL + "/tasks";

    //endpointy resources
    public static final String RESOURCES_URL = API_URL + "/resources";

    public static final String WS_URL = "ws://localhost:8080/ws";

    public static final int CONNECT_TIMEOUT_SECONDS = 10;
    public static final int READ_TIMEOUT_SECONDS = 30;

    //UI
    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 768;
    public static final int LOGIN_WINDOW_WIDTH = 400;
    public static final int LOGIN_WINDOW_HEIGHT = 350;
}