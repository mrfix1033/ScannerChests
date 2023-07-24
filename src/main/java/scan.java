import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class scan implements CommandExecutor {
    private int loaded_chunks;
    private int checked_chunks;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Bukkit.broadcastMessage("Началось сканирование, сервер может зависнуть, это нормально");
        World world = Bukkit.getWorld("world");
        Chunk firstChunk = world.getChunkAt(new Location(world, -1000, 0, -1000));
        int firstChunkX = firstChunk.getX();
        int firstChunkZ = firstChunk.getZ();
        Chunk lastChunk = world.getChunkAt(new Location(world, 1000, 0, 1000));
        int lastChunkX = lastChunk.getX();
        int lastChunkZ = lastChunk.getZ();
        main.locset = new HashSet<>();
        loaded_chunks = 0;
        checked_chunks = 0;
        int chunkCount = (lastChunkX - firstChunkX + 1) * (lastChunkZ - firstChunkZ + 1);
        Bukkit.broadcastMessage(String.format("Всего чанков для обработки: %s", chunkCount));
        Set<BukkitTask> tasks = new HashSet<>();
        tasks.add(Bukkit.getScheduler().runTaskTimer(main.plugin, () ->
                Bukkit.broadcastMessage(String.format("§7Загружено §9%.2f §8(§c%s§8/§a%s§8) §7чанков" +
                                "\nПроверено §9%.2f §8(§c%s§8/§a%s§8) §7чанков",
                        (float) loaded_chunks / chunkCount * 100, loaded_chunks, chunkCount,
                        (float) checked_chunks / chunkCount * 100, checked_chunks, chunkCount)), 0, 20));
        tasks.add(Bukkit.getScheduler().runTaskTimer(main.plugin, () -> {
            if (checked_chunks >= chunkCount) {
                tasks.forEach(BukkitTask::cancel);
                main.loclist = Arrays.asList(main.locset.toArray(new Location[0]));
                Bukkit.broadcastMessage(String.format("\n\n\n%s", main.locset));
                Bukkit.broadcastMessage(String.format("Чанки просканированы, найдено сундуков: %s", main.locset.size()));
            }
        }, 5, 20));
        for (int chunkX = firstChunkX; chunkX <= lastChunkX; chunkX++) {
            for (int chunkZ = firstChunkZ; chunkZ <= lastChunkZ; chunkZ++) {
                if (!world.isChunkGenerated(chunkX, chunkZ)) {
                    loaded_chunks++;
                    checked_chunks++;
                    continue;
                }
                world.getChunkAtAsync(chunkX, chunkZ, chunk -> checkChunk(world, chunk));
            }
        }
        return true;
    }

    private void checkChunk(World world, Chunk chunk) {
        loaded_chunks++;
        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++) {
                Block temp = chunk.getBlock(x, 0, z);
                Block block = world.getHighestBlockAt(temp.getX(), temp.getZ());
                for (int i = 0; i < 2; i++) {
                    if (block.getType() == Material.CHEST) {
                        main.locset.add(block.getLocation());
                        Bukkit.broadcastMessage(
                                String.format("Найден сундук #%s %s", main.locset.size(), block.getLocation()));
                    }
                    block = block.getLocation().add(0, 1, 0).getBlock();
                }
            }
        checked_chunks++;
    }
}
