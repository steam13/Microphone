package nuance.com.db;

public interface NuanceQueryConstants {
    public static final String DATABASE_NAME = "NUANCE";
    public static final String DATABASE_TABLE_PROFILE = "PROFILE";
    public static final int DATABASE_VERSION = 1;
    public static final String DNS_NAME = "dns_names";
    public static final String IP_ADDRESS = "ip_address";
    public static final String PORT = "port";
    public static final String PROFILE = "CREATE TABLE PROFILE( profile_id INTEGER PRIMARY KEY AUTOINCREMENT, profile_name text,dns_names text,ip_address text,port numeric,profile_selection bool)";
    public static final String PROFILE_ID = "profile_id";
    public static final String PROFILE_NAME = "profile_name";
    public static final String SELECTION = "profile_selection";
}
