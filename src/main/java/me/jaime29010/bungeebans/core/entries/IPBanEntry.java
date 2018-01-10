package me.jaime29010.bungeebans.core.entries;

public class IPBanEntry extends BanEntry {
    private final String address;
    public IPBanEntry(String address, String punished, String punisher, String reason) {
        super(punished, punisher, reason);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
