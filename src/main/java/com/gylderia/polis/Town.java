package com.gylderia.polis;

import java.util.*;

public class Town {
    private final String name;
    private final Date creationDate;

    private Rank defaultRank;
    private Rank leaderRank;
    private Map<byte[], Rank> rankList;
    private final byte[] uuid;

    public Town(String name, Date creationDate, byte[] uuid, Map<byte[], Rank> rankList) {       //TODO intéger map <String , Rank> pour gérer les ranks custom
        this.name = name;
        this.creationDate = creationDate;
        this.uuid = uuid;
        this.rankList = rankList;

        for (Rank rank : rankList.values()) {
            if (rank.isDefault()) {
                defaultRank = rank;
            }
            if (rank.isLeader()) {
                leaderRank = rank;
            }
        }
    }

    public byte[] getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Map<byte[], Rank> getRanks() {
        return rankList;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public Rank getDefaultRank() {
        return defaultRank;
    }

    public void setDefaultRank(Rank defaultRank) {
        this.defaultRank = defaultRank;
    }

    public Rank getRank(byte[] uuid) {
        //return rank using equals method
        for (byte[] key : rankList.keySet()) {
            if (Arrays.equals(key, uuid)) {
                return rankList.get(key);
            }
        }
        return null;
    }

    public boolean removeRank(byte[] uuid) {
        rankList.remove(uuid);
        //TODO remove from cache and database
        //TODO set players with this rank to default rank

        return true;
    }

    public void addRank(Rank rank) {
        rankList.put(rank.getUuid(), rank);
        //TODO add to cache and database
    }

    public boolean setRankDisplayName (byte[] uuid, String displayName) {
        Rank rank = rankList.get(uuid);
        if (rank == null) {
            return false;
        }
        //change rank's key in ranks map
         return rank.setDisplayName(displayName);

         //TODO update database and cache
    }

    public Rank getLeaderRank() {
        return leaderRank;
    }

    public void setLeaderRank(Rank leaderRank) {
        this.leaderRank = leaderRank;
    }
}
