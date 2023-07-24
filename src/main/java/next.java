import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class next implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player p = (Player) sender;
        int i;
        try {
            i = Integer.parseInt(args[0]);
        } catch (Exception e) {
            if (args.length != 0)
                p.sendMessage(e.toString());
            i = main.i++ % main.loclist.size();
        }
        p.sendMessage(String.format("Телепорт на сундук %s/%s", i + 1, main.locset.size()));
        p.teleport(main.loclist.get(i));
        return true;
    }
}
