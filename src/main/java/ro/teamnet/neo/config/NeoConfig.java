package ro.teamnet.neo.config;

public class NeoConfig {
    private String schema;
    private String host;
    private String port;
    private String user;
    private String password;
    private Boolean useEmbeddedDatabase;
    private String embeddedDatabasePath;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getUseEmbeddedDatabase() {
        return useEmbeddedDatabase;
    }

    public void setUseEmbeddedDatabase(Boolean useEmbeddedDatabase) {
        this.useEmbeddedDatabase = useEmbeddedDatabase;
    }

    public String getEmbeddedDatabasePath() {
        return embeddedDatabasePath;
    }

    public void setEmbeddedDatabasePath(String embeddedDatabasePath) {
        this.embeddedDatabasePath = embeddedDatabasePath;
    }
}
