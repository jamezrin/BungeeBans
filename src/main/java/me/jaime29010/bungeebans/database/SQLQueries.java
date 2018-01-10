package me.jaime29010.bungeebans.database;

public final class SQLQueries {
    public static final String CREATE_BANS_TABLE = "CREATE TABLE IF NOT EXISTS bans (punished varchar(16) NOT NULL, punisher varchar(16) NOT NULL, reason varchar(16) NOT NULL, PRIMARY KEY (punished)) DEFAULT CHARSET=utf8;";
    public static final String CREATE_IPBANS_TABLE = "CREATE TABLE IF NOT EXISTS ipbans (address varchar(16) NOT NULL, punished varchar(16) NOT NULL, punisher varchar(16) NOT NULL, reason varchar(16) NOT NULL, PRIMARY KEY (address)) DEFAULT CHARSET=utf8;";

    public static final String SELECT_BAN = "SELECT * FROM bans WHERE punished=?;";
    public static final String SELECT_IPBAN = "SELECT * FROM ipbans WHERE address=?;";
    public static final String SELECT_IPBAN2 = "SELECT * FROM ipbans WHERE punished=?;";

    public static final String INSERT_BAN = "INSERT INTO bans VALUES(?, ?, ?);";
    public static final String INSERT_IPBAN = "INSERT INTO ipbans VALUES(?, ?, ?, ?);";

    public static final String DELETE_BAN = "DELETE FROM bans WHERE punished=?;";
    public static final String DELETE_IPBAN = "DELETE FROM ipbans WHERE address=?;";
    public static final String DELETE_IPBAN2 = "DELETE FROM ipbans WHERE punished=?;";
}