package me.jaime29010.bungeebans.core;

import me.jaime29010.bungeebans.Main;
import me.jaime29010.bungeebans.core.entries.BanEntry;
import me.jaime29010.bungeebans.core.entries.IPBanEntry;
import me.jaime29010.bungeebans.database.SQLQueries;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BanManager {
    private final Main main;
    public BanManager(Main main) {
        this.main = main;
    }
    public void ban(String punished, String punisher, String reason) {
        Connection connection = null;
        try {
            connection = main.getHikari().getConnection();
            if (connection == null) return;

            PreparedStatement statement = connection.prepareStatement(SQLQueries.INSERT_BAN);
            statement.setString(1, punished);
            statement.setString(2, punisher);
            statement.setString(3, reason);

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void banip(ProxiedPlayer punished, String punisher, String reason) {
        Connection connection = null;
        try {
            connection = main.getHikari().getConnection();
            if (connection == null) return;

            PreparedStatement statement = connection.prepareStatement(SQLQueries.INSERT_IPBAN);
            statement.setString(1, punished.getAddress().getAddress().toString());
            statement.setString(2, punished.getName());
            statement.setString(3, punisher);
            statement.setString(4, reason);

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void unban(String name) {
        Connection connection = null;
        try {
            connection = main.getHikari().getConnection();
            if (connection == null) return;

            PreparedStatement statement = connection.prepareStatement(SQLQueries.DELETE_BAN);
            statement.setString(1, name);

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void unbanip(InetAddress address) {
        Connection connection = null;
        try {
            connection = main.getHikari().getConnection();
            if (connection == null) return;

            PreparedStatement statement = connection.prepareStatement(SQLQueries.DELETE_IPBAN);
            statement.setString(1, address.toString());

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void unbanip(String name) {
        Connection connection = null;
        try {
            connection = main.getHikari().getConnection();
            if (connection == null) return;

            PreparedStatement statement = connection.prepareStatement(SQLQueries.DELETE_IPBAN2);
            statement.setString(1, name);

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public BanEntry getBanEntry(String name) {
        BanEntry entry = null;
        Connection connection = null;
        try {
            connection = main.getHikari().getConnection();
            if (connection == null) return null;

            PreparedStatement statement = connection.prepareStatement(SQLQueries.SELECT_BAN);
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                entry = new BanEntry(
                        resultSet.getString("punished"),
                        resultSet.getString("punisher"),
                        resultSet.getString("reason")
                );
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return entry;
    }

    public IPBanEntry getIPBanEntry(InetAddress address) {
        IPBanEntry entry = null;
        Connection connection = null;
        try {
            connection = main.getHikari().getConnection();
            if (connection == null) return null;

            PreparedStatement statement = connection.prepareStatement(SQLQueries.SELECT_IPBAN);
            statement.setString(1, address.toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                entry = new IPBanEntry(
                        resultSet.getString("address"),
                        resultSet.getString("punished"),
                        resultSet.getString("punisher"),
                        resultSet.getString("reason")
                );
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return entry;
    }

    public IPBanEntry getIPBanEntry(String name) {
        IPBanEntry entry = null;
        Connection connection = null;
        try {
            connection = main.getHikari().getConnection();
            if (connection == null) return null;

            PreparedStatement statement = connection.prepareStatement(SQLQueries.SELECT_IPBAN2);
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                entry = new IPBanEntry(
                        resultSet.getString("address"),
                        resultSet.getString("punished"),
                        resultSet.getString("punisher"),
                        resultSet.getString("reason")
                );
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return entry;
    }
}
