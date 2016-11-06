package org.devathon.contest2016.quarry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.devathon.contest2016.Constants;
import org.devathon.contest2016.file.QuarryFile;

import java.util.*;

public class Quarry extends BukkitRunnable implements ConfigurationSerializable {

    private final World world;
    private final Vector pointA;
    private final Vector pointB;
    private final List<Block> eastWestBlocks;
    private final List<Block> northSouthBlocks;
    private final List<Block> upDownBlocks;
    private final QuarryFile quarryFile;
    private final UUID uuid;

    private BlockFace direction;
    private Block block;
    private Block controller;
    private Chest chest;
    private boolean east;
    private int currentFuel = -1;

    public Quarry(String name) {
        this.uuid = UUID.fromString(name.replace(".yml", ""));
        this.quarryFile = new QuarryFile(this);

        Map<String, Object> data = ((MemorySection)quarryFile.get("data")).getValues(true);

        this.world = Bukkit.getWorld((String) data.get("world"));
        this.pointA = Vector.deserialize(((MemorySection) data.get("point1")).getValues(true));
        this.pointB = Vector.deserialize(((MemorySection) data.get("point2")).getValues(true));

        this.direction = Constants.getEnumValueFromString(BlockFace.class, (String) data.get("direction"));
        this.east = (boolean) data.get("east");
        this.block = Location.deserialize(((MemorySection) data.get("block")).getValues(true)).getBlock();
        this.eastWestBlocks = new ArrayList<>();
        this.northSouthBlocks = new ArrayList<>();
        this.upDownBlocks = new ArrayList<>();

        make(false);
    }

    public Quarry(World world, Vector pointA, Vector pointB) {
        this.world = world;
        this.pointA = pointA;
        this.pointB = pointB;
        this.uuid = UUID.randomUUID();

        this.direction = BlockFace.NORTH;
        this.block = new Location(world, pointB.getBlockX(), pointB.getBlockY(), pointB.getBlockZ()).getBlock().getRelative(BlockFace.NORTH_WEST);
        this.eastWestBlocks = new ArrayList<>();
        this.northSouthBlocks = new ArrayList<>();
        this.upDownBlocks = new ArrayList<>();
        this.quarryFile = new QuarryFile(this);

        make(true);
    }

    public void make(boolean offset) {
        drawStructure();

        if(offset) {
            for(int i = 0; i < pointB.getBlockY()-pointA.getBlockY(); i++) {
                block = block.getRelative(BlockFace.DOWN);
            }
        }

        Block b = pointA.toLocation(world).getBlock().getRelative(BlockFace.NORTH_WEST);
        b.setType(Material.CHEST);
        chest = (Chest) b.getState();

        //createInitialTopStructure();
    }

    @Override
    public void run() {
        if(chest.getLocation().getBlock().getType() != Material.CHEST) {
            return;
        }

        boolean free = false;
        for (ItemStack itemStack : chest.getInventory().getContents()) {
            if(itemStack == null) {
                free = true;
            }
        }

        if(!free) {
            return;
        }

        block.getDrops().forEach(itemStack -> chest.getInventory().addItem(itemStack));
        block.setType(Material.AIR);

        Block nextBlock = block.getRelative(direction);

        switch (direction) {
            case NORTH:
                if(!east && (nextBlock.getRelative(BlockFace.WEST).getLocation().getBlockX() <= pointA.getBlockX()) && nextBlock.getLocation().getBlockZ()-1 <= pointA.getBlockZ()) {
                    direction = BlockFace.DOWN;
                } else if(east && (nextBlock.getRelative(BlockFace.EAST).getLocation().getBlockX() >= pointB.getBlockX()) && nextBlock.getLocation().getBlockZ()-1 <= pointA.getBlockZ()) {
                    direction = BlockFace.DOWN;
                } else if(nextBlock.getLocation().getBlockZ()-1 <= pointA.getBlockZ()) {
                    direction = (east ? BlockFace.EAST : BlockFace.WEST);
                }

                break;
            case WEST:
                if(nextBlock.getLocation().getBlockZ()-1 <= pointA.getBlockZ()) {
                    direction = BlockFace.SOUTH;
                } else {
                    direction = BlockFace.NORTH;
                }
                break;
            case SOUTH:
                if(!east && (nextBlock.getRelative(BlockFace.WEST).getLocation().getBlockX() <= pointA.getBlockX()) && nextBlock.getLocation().getBlockZ()+1 >= pointB.getBlockZ()) {
                    direction = BlockFace.DOWN;
                } else if(east && (nextBlock.getRelative(BlockFace.EAST).getLocation().getBlockX() >= pointB.getBlockX()) && nextBlock.getLocation().getBlockZ()+1 >= pointB.getBlockZ()) {
                    direction = BlockFace.DOWN;
                } else if(nextBlock.getLocation().getBlockZ()+1 >= pointB.getBlockZ()) {
                    direction = (east ? BlockFace.EAST : BlockFace.WEST);
                }

                break;
            case EAST:
                if(nextBlock.getLocation().getBlockZ()-1 <= pointA.getBlockZ()) {
                    direction = BlockFace.SOUTH;
                } else {
                    direction = BlockFace.NORTH;
                }
                break;
            case DOWN:
                if(nextBlock.getLocation().getBlockZ()-1 <= pointA.getBlockZ()) {
                    direction = BlockFace.SOUTH;
                } else {
                    direction = BlockFace.NORTH;
                }

                east = !east;
                break;
            default: break;
        }

        block = nextBlock;
        moveStructure(block.getRelative(BlockFace.UP));
    }

    private void moveStructure(Block block) {
        eastWestBlocks.forEach(block1 -> block1.setType(Material.AIR));
        northSouthBlocks.forEach(block1 -> block1.setType(Material.AIR));
        northSouthBlocks.clear();
        eastWestBlocks.clear();

        for(int x = pointA.getBlockX()+1; x < pointB.getBlockX(); x++) {
            Block block2 = new Location(world, x, pointB.getBlockY(), block.getLocation().getBlockZ()).getBlock();
            eastWestBlocks.add(block2);
            block2.setType(Material.COBBLE_WALL);
        }

        for(int z = pointA.getBlockZ()+1; z < pointB.getBlockZ(); z++) {
            Block block2 = new Location(world, block.getLocation().getBlockX(), pointB.getBlockY(), z).getBlock();
            northSouthBlocks.add(block2);
            block2.setType(Material.COBBLE_WALL);
        }

        shiftUpDown();
    }

    @SuppressWarnings("deprecation")
    private void shiftUpDown() {
        upDownBlocks.forEach(block1 -> block1.setType(Material.AIR));
        upDownBlocks.clear();

        for(int y = block.getLocation().getBlockY()+1; y < pointB.getBlockY(); y++) {
            Block block2 = new Location(world, block.getLocation().getBlockX() , y, block.getLocation().getBlockZ()).getBlock();
            upDownBlocks.add(block2);

            if(y == block.getLocation().getBlockY()+1) {
                block2.setType(Material.END_ROD);
                block2.setData((byte) 0);
            } else {
                block2.setType(Material.COBBLE_WALL);
            }
        }
    }

    private void createInitialTopStructure() {
        upDownBlocks.add(block);

        for(int x = pointA.getBlockX()+1; x < pointB.getBlockX(); x++) {
            Block block2 = new Location(world, x, block.getLocation().getBlockY(), block.getLocation().getBlockZ()).getBlock();
            eastWestBlocks.add(block2);
            block2.setType(Material.COBBLE_WALL);
        }

        for(int z = pointA.getBlockZ()+1; z < pointB.getBlockZ(); z++) {
            Block block2 = new Location(world, block.getLocation().getBlockX() , block.getLocation().getBlockY(), z).getBlock();
            northSouthBlocks.add(block2);
            block2.setType(Material.COBBLE_WALL);
        }
    }

    private void drawStructure() {
        drawLine(pointA.getBlockX(), pointA.getBlockY(), pointA.getBlockZ(), pointB.getBlockX(), pointA.getBlockY(), pointA.getBlockZ());
        drawLine(pointA.getBlockX(), pointA.getBlockY(), pointA.getBlockZ(), pointA.getBlockX(), pointB.getBlockY(), pointA.getBlockZ());
        drawLine(pointA.getBlockX(), pointA.getBlockY(), pointA.getBlockZ(), pointA.getBlockX(), pointA.getBlockY(), pointB.getBlockZ());

        drawLine(pointA.getBlockX(), pointB.getBlockY(), pointA.getBlockZ(), pointB.getBlockX(), pointB.getBlockY(), pointA.getBlockZ());
        drawLine(pointA.getBlockX(), pointB.getBlockY(), pointA.getBlockZ(), pointA.getBlockX(), pointB.getBlockY(), pointB.getBlockZ());

        drawLine2(pointB.getBlockX(), pointB.getBlockY(), pointB.getBlockZ(), pointA.getBlockX(), pointB.getBlockY(), pointB.getBlockZ());
        drawLine2(pointB.getBlockX(), pointB.getBlockY(), pointB.getBlockZ(), pointB.getBlockX(), pointA.getBlockY(), pointB.getBlockZ());
        drawLine2(pointB.getBlockX(), pointB.getBlockY(), pointB.getBlockZ(), pointB.getBlockX(), pointB.getBlockY(), pointA.getBlockZ());

        drawLine2(pointB.getBlockX(), pointA.getBlockY(), pointB.getBlockZ(), pointA.getBlockX(), pointA.getBlockY(), pointB.getBlockZ());
        drawLine2(pointB.getBlockX(), pointA.getBlockY(), pointB.getBlockZ(), pointB.getBlockX(), pointA.getBlockY(), pointA.getBlockZ());

        drawLine(pointA.getBlockX(), pointA.getBlockY(), pointB.getBlockZ(), pointA.getBlockX(), pointB.getBlockY(), pointB.getBlockZ());
        drawLine(pointB.getBlockX(), pointA.getBlockY(), pointA.getBlockZ(), pointB.getBlockX(), pointB.getBlockY(), pointA.getBlockZ());
    }

    private void drawLine(int startx, int starty, int startz, int endx, int endy, int endz) {
        for(int x = startx; x <= endx; x++) {
            for(int y = starty; y <= endy; y++) {
                for(int z = startz; z <= endz; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(Material.BRICK);
                }
            }
        }
    }

    private void drawLine2(int startx, int starty, int startz, int endx, int endy, int endz) {
        for(int x = startx; x >= endx; x--) {
            for(int y = starty; y >= endy; y--) {
                for(int z = startz; z >= endz; z--) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(Material.BRICK);
                }
            }
        }
    }

    public void save() {
        quarryFile.set("data", serialize());
        quarryFile.save();
    }

    public void destroyDrill() {
        cancel();
        northSouthBlocks.forEach(block1 -> block1.setType(Material.AIR));
        eastWestBlocks.forEach(block1 -> block1.setType(Material.AIR));
        upDownBlocks.forEach(block1 -> block1.setType(Material.AIR));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("world", world.getName());
        result.put("point1", pointA.serialize());
        result.put("point2", pointB.serialize());
        result.put("direction", direction.name());
        result.put("block", block.getLocation().serialize());
        result.put("east", east);
        return result;
    }

    public Vector getPointA() {
        return pointA;
    }

    public Vector getPointB() {
        return pointB;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Block getBlock() {
        return block;
    }
}
