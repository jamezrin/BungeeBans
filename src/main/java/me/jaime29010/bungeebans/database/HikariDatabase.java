package me.jaime29010.bungeebans.database;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sun.rowset.CachedRowSetImpl;
import com.zaxxer.hikari.HikariDataSource;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.GroupedThreadFactory;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicReference;

public final class HikariDatabase {
    private final HikariDataSource hikari;
    private final Plugin plugin;
    public HikariDatabase(Plugin plugin, String address, int port, String database, String username, String password) {
        this.plugin = plugin;
        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(10);
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", address);
        hikari.addDataSourceProperty("port", port);
        hikari.addDataSourceProperty("databaseName", database);
        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);

        //Fixing AccessControlException when using with BungeeCord
        plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
            @Override
            public void run() {
                hikari.setThreadFactory(new ThreadFactoryBuilder().build());
            }
        });
    }

    public boolean testConnection() {
        Connection connection = null;
        try {
            connection = hikari.getConnection();
        } catch (SQLException e) {
            return false;
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    //Do nothing
                }
            }
        }
        return true;
    }

    public void disconnect() {
        hikari.close();
    }

    public CachedRowSet query(final PreparedStatement preparedStatement) {
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            CachedRowSet cachedRowSet = new CachedRowSetImpl();
            cachedRowSet.populate(resultSet);
            resultSet.close();
            preparedStatement.getConnection().close();
            if (cachedRowSet.next()) return cachedRowSet;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                preparedStatement.getConnection().close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public void execute(final PreparedStatement statement) {
        plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
            public void run() {
                try {
                    statement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        statement.close();
                        statement.getConnection().close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public PreparedStatement prepareStatement(String query, String... vars) {
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            int x = 0;
            if (query.contains("?") && vars.length != 0) {
                for (String var : vars) {
                    x++;
                    statement.setString(x, var);
                }
            }
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() {
        try {
            return hikari.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class BungeeCordThreadFactory implements ThreadFactory {
        private final Plugin plugin;
        public BungeeCordThreadFactory(Plugin plugin) {
            this.plugin = plugin;
        }
        @Override
        public Thread newThread(final Runnable runnable) {
            final AtomicReference<Thread> thread = new AtomicReference<>();
            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            });
            return thread.get();
        }
    }
}
