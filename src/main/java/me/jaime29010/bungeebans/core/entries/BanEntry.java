package me.jaime29010.bungeebans.core.entries;

public class BanEntry {
    private final String punished;
    private final String punisher;
    private final String reason;
    public BanEntry(String punished, String punisher, String reason) {
        this.punished = punished;
        this.punisher = punisher;
        this.reason = reason;
    }

    public String getPunished() {
        return punished;
    }

    public String getPunisher() {
        return punisher;
    }

    public String getReason() {
        return reason;
    }
}
