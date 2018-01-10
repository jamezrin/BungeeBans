package me.jaime29010.bungeebans;

import me.jaime29010.bungeebans.commands.BanCommand;
import me.jaime29010.bungeebans.commands.BanIPCommand;
import me.jaime29010.bungeebans.commands.UnBanCommand;
import me.jaime29010.bungeebans.commands.UnBanIPCommand;
import me.jaime29010.bungeebans.configuration.ConfigurationManager;
import me.jaime29010.bungeebans.core.BanManager;
import me.jaime29010.bungeebans.database.HikariDatabase;
import me.jaime29010.bungeebans.database.SQLQueries;
import me.jaime29010.bungeebans.listeners.PreLoginListener;
import me.jaime29010.bungeebans.utils.RedisBridge;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main extends Plugin {
    private Configuration config = null;
    private BanManager manager = null;
    private RedisBridge bridge = null;
    private HikariDatabase database = null;
    @Override
    public void onEnable() {
        config = ConfigurationManager.loadConfig("config.yml", this);
        database = new HikariDatabase(this,
                config.getString("mysql.address"),
                config.getInt("mysql.port"),
                config.getString("mysql.database"),
                config.getString("mysql.username"),
                config.getString("mysql.password")
        );
        //Creating the tables (testing the connection)
        Connection connection = null;
        try {
            PreparedStatement statement;
            connection = database.getConnection();
            if (connection == null) return;

            statement = connection.prepareStatement(SQLQueries.CREATE_BANS_TABLE);
            if (statement != null) {
                statement.execute();
            }
            statement = connection.prepareStatement(SQLQueries.CREATE_IPBANS_TABLE);
            if (statement != null) {
                statement.execute();
            }

            if(statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            getLogger().severe("Could not connect to the database, stopped loading the plugin...");
            e.printStackTrace();

            //Stop loading the plugin...
            return;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        //Registering the RedisBridge
        bridge = new RedisBridge(this);

        //Creating the manager
        manager = new BanManager(this);

        //Registering the listener
        getProxy().getPluginManager().registerListener(this, new PreLoginListener(this));

        //Registering the commands
        getProxy().getPluginManager().registerCommand(this, new BanCommand(this));
        getProxy().getPluginManager().registerCommand(this, new BanIPCommand(this));
        getProxy().getPluginManager().registerCommand(this, new UnBanCommand(this));
        getProxy().getPluginManager().registerCommand(this, new UnBanIPCommand(this));
    }

    @Override
    public void onDisable() {

    }

    public Configuration getConfig() {
        return config;
    }

    public RedisBridge getBridge() {
        return bridge;
    }

    public BanManager getManager() {
        return manager;
    }

    public HikariDatabase getHikari() {
        return database;
    }
}
