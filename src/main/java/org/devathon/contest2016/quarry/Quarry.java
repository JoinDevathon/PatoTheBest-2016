package org.devathon.contest2016.quarry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Quarry implements Runnable {

    private final World world;
    private final Chest chest;
    private final Vector pointA;
    private final Vector pointB;
    private final List<Block> eastWestBlocks;
    private final List<Block> northSouthBlocks;
    private final List<Block> upDownBlocks;

    private BlockFace direction;
    private Block block;
    private boolean east;
    private int currentFuel = -1;

    public Quarry(World world, Vector pointA, Vector pointB) {
        this.world = world;
        this.pointA = pointA;
        this.pointB = pointB;

        this.direction = BlockFace.NORTH;
        this.block = new Location(world, pointB.getBlockX(), pointB.getBlockY(), pointB.getBlockZ()).getBlock().getRelative(BlockFace.NORTH_WEST);
        this.eastWestBlocks = new ArrayList<>();
        this.northSouthBlocks = new ArrayList<>();
        this.upDownBlocks = new ArrayList<>();

        drawStructure();

        pointA.toLocation(world).getBlock().setType(Material.DIAMOND_BLOCK);
        pointB.toLocation(world).getBlock().setType(Material.GOLD_BLOCK);
        Block b = pointA.toLocation(world).getBlock().getRelative(BlockFace.NORTH_WEST);
        b = b.getRelative(BlockFace.DOWN);
        b.setType(Material.CHEST);
        chest = (Chest) b.getState();
    }

    @Override
    public void run() {
        boolean free = false;
        for (ItemStack itemStack : chest.getInventory().getStorageContents()) {
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
    }

    private void moveStructure(Block block, BlockFace blockFace) {
        if(blockFace == BlockFace.EAST || blockFace == BlockFace.WEST) {
            eastWestBlocks.forEach(block1 -> block1.setType(Material.AIR));

            for(int x = pointA.getBlockX()+1; x < pointB.getBlockX(); x++) {
                Block block2 = new Location(world, x, block.getLocation().getBlockY(), block.getLocation().getBlockZ()).getBlock();
                eastWestBlocks.add(block2);
                block2.setType(Material.COBBLE_WALL);
            }
        }

        if(blockFace == BlockFace.NORTH || blockFace == BlockFace.SOUTH) {
            northSouthBlocks.forEach(block1 -> block1.setType(Material.AIR));

            for(int z = pointA.getBlockZ()+1; z < pointB.getBlockZ(); z++) {
                Block block2 = new Location(world, block.getLocation().getBlockX() , block.getLocation().getBlockY(), z).getBlock();
                northSouthBlocks.add(block2);
                block2.setType(Material.COBBLE_WALL);
            }
        }

        if(blockFace == BlockFace.DOWN) {
            upDownBlocks.clear();

            for(int y = pointA.getBlockY()+1; y < pointB.getBlockY(); y++) {
                Block block2 = new Location(world, block.getLocation().getBlockX() , y, block.getLocation().getBlockZ()).getBlock();
                upDownBlocks.add(block2);
                block2.setType(Material.COBBLE_WALL);
            }
        }
    }

    private void shiftUpDown() {
        upDownBlocks.forEach(block1 -> block1.setType(Material.AIR));

        for(int y = pointA.getBlockY()+1; y < pointB.getBlockY(); y++) {
            Block block2 = new Location(world, block.getLocation().getBlockX() , y, block.getLocation().getBlockZ()).getBlock();
            upDownBlocks.add(block2);
            block2.setType(Material.COBBLE_WALL);
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
                    block.setType(Material.IRON_BLOCK);
                }
            }
        }
    }

    private void drawLine2(int startx, int starty, int startz, int endx, int endy, int endz) {
        for(int x = startx; x >= endx; x--) {
            for(int y = starty; y >= endy; y--) {
                for(int z = startz; z >= endz; z--) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(Material.IRON_BLOCK);
                }
            }
        }
    }

    public Vector getPointA() {
        return pointA;
    }

    public Vector getPointB() {
        return pointB;
    }
}
