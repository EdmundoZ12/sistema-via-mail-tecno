package com.tecnoweb.grupo7sa.ConfigDB;

public class ConfigDB {
    private final String user;
    private final String password;
    private final String host;
    private final String port;
    private final String dbName;

    public ConfigDB() {
//        this.user = "grupo07sa";
        this.user = "postgres";

//        this.password = "grup007grup007*";
        this.password = "ejzr1203";

//        this.host = "mail.tecnoweb.org.bo";
        this.host = "127.0.0.1";

//        this.port = "5432";
        this.port = "5432";

//        this.dbName = "db_grupo07sa";
        this.dbName = "tecnodb";

    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDbName() {
        return dbName;
    }
}
