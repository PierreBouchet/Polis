package com.gylderia.polis.models;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;

public class GylderiaChunk implements Chunk {


    private final Chunk chunk;
    private Town town;


    public GylderiaChunk(Chunk chunk, Town town) {
        this.chunk = chunk;
        this.town = town;
    }

    @Override
    public int getX() {
        return chunk.getX();
    }

    @Override
    public int getZ() {
        return chunk.getZ();
    }

    @Override
    public long getChunkKey() {
        return chunk.getChunkKey();
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    @Override
    public @NotNull World getWorld() {
        return chunk.getWorld();
    }

    @Override
    public @NotNull Block getBlock(int i, int i1, int i2) {
        return chunk.getBlock(i, i1, i2);
    }

    @Override
    public @NotNull ChunkSnapshot getChunkSnapshot() {
        return chunk.getChunkSnapshot();
    }

    @Override
    public @NotNull ChunkSnapshot getChunkSnapshot(boolean b, boolean b1, boolean b2) {
        return chunk.getChunkSnapshot(b, b1, b2);
    }

    @Override
    public boolean isEntitiesLoaded() {
        return chunk.isEntitiesLoaded();
    }

    @Override
    public @NotNull Entity[] getEntities() {
        return chunk.getEntities();
    }

    @Override
    public @NotNull BlockState[] getTileEntities() {
        return chunk.getTileEntities();
    }

    @Override
    public @NotNull BlockState[] getTileEntities(boolean b) {
        return chunk.getTileEntities(b);
    }

    @Override
    public @NotNull Collection<BlockState> getTileEntities(@NotNull Predicate<? super Block> predicate, boolean b) {
        return chunk.getTileEntities(predicate, b);
    }

    @Override
    public boolean isGenerated() {
        return chunk.isGenerated();
    }

    @Override
    public boolean isLoaded() {
        return chunk.isLoaded();
    }

    @Override
    public boolean load(boolean b) {
        return chunk.load(b);
    }

    @Override
    public boolean load() {
        return chunk.load();
    }

    @Override
    public boolean unload(boolean b) {
        return chunk.unload(b);
    }

    @Override
    public boolean unload() {
        return chunk.unload();
    }

    @Override
    public boolean isSlimeChunk() {
        return chunk.isSlimeChunk();
    }

    @Override
    public boolean isForceLoaded() {
        return chunk.isForceLoaded();
    }

    @Override
    public void setForceLoaded(boolean b) {
        chunk.setForceLoaded(b);
    }

    @Override
    public boolean addPluginChunkTicket(@NotNull Plugin plugin) {
        return chunk.addPluginChunkTicket(plugin);
    }

    @Override
    public boolean removePluginChunkTicket(@NotNull Plugin plugin) {
        return chunk.removePluginChunkTicket(plugin);
    }

    @Override
    public @NotNull Collection<Plugin> getPluginChunkTickets() {
        return chunk.getPluginChunkTickets();
    }

    @Override
    public long getInhabitedTime() {
        return chunk.getInhabitedTime();
    }

    @Override
    public void setInhabitedTime(long l) {
        chunk.setInhabitedTime(l);
    }

    @Override
    public boolean contains(@NotNull BlockData blockData) {
        return chunk.contains(blockData);
    }

    @Override
    public boolean contains(@NotNull Biome biome) {
        return chunk.contains(biome);
    }

    @Override
    public @NotNull LoadLevel getLoadLevel() {
        return chunk.getLoadLevel();
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        return chunk.getPersistentDataContainer();
    }
}