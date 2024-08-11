package com.gylderia.polis.models;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Town {
    private String name;
    private final Date creationDate;

    private Rank defaultRank;
    private Rank leaderRank;
    private Map<byte[], Rank> rankList;
    private final byte[] uuid;
    private  Map<UUID, joinTownInvitation> joinTownInvitations = new HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;
    private  Map<UUID, GylderiaPlayer> players = new HashMap<>();
    private Map<Long, GylderiaChunk> chunks = new HashMap<>();


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
                //debug message
                System.out.println("Rank found: " + rankList.get(key).getDisplayName());
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

    public void addJoinTownInvitation(GylderiaPlayer player) {
        //si c'est la première invitation, on lance le scheduler
        if (joinTownInvitations.isEmpty()) {
            startScheduler();
        }
        joinTownInvitations.put(player.getUuid(), new joinTownInvitation(player, this));
    }

    public void removeJoinTownInvitation(GylderiaPlayer player) {
        if (joinTownInvitations.size() == 1) {
            stopScheduler();
        }
        joinTownInvitations.remove(player.getUuid());

    }

    public joinTownInvitation getJoinTownInvitation(GylderiaPlayer player) {
        return joinTownInvitations.get(player.getUuid());
    }

    public boolean hasJoinTownInvitation(GylderiaPlayer player) {
        return joinTownInvitations.containsKey(player.getUuid());
    }

    public void removeExpiredJoinTownInvitations() {
        Date now = new Date();
        for (joinTownInvitation invitation : joinTownInvitations.values()) {
            //si l'invitation a plus de 10 minutes, on la retire
            if (now.getTime() - invitation.getCreationTime().getTime() > 600000) {
                removeJoinTownInvitation(invitation.getPlayer());
            }
        }
    }

    private void startScheduler() {
        if (scheduledFuture == null || scheduledFuture.isCancelled()) {
            scheduledFuture = scheduler.scheduleAtFixedRate(this::removeExpiredJoinTownInvitations, 0, 1, TimeUnit.MINUTES);
        }
    }

    private void stopScheduler() {
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(false);
        }
    }

    public void addPlayer(GylderiaPlayer player) {
        players.put(player.getUuid(), player);
    }

    public void removePlayer(GylderiaPlayer player) {
        players.remove(player.getUuid());
    }

    public Map<UUID, GylderiaPlayer> getPlayers() {
        return players;
    }

    public void addChunk(GylderiaChunk chunk) {
        chunks.put(chunk.getChunkKey(), chunk);
    }

    public void removeChunk(GylderiaChunk chunk) {
        chunks.remove(chunk.getChunkKey());
    }


}
