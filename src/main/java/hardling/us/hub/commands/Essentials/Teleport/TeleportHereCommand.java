package hardling.us.hub.commands.Essentials.Teleport;

import hardling.us.hub.util.CC;
import hardling.us.hub.util.Files.Lang;
import hardling.us.hub.util.command.BaseCommand;
import hardling.us.hub.util.command.CommandArgs;
import hardling.us.hub.util.command.MainCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeleportHereCommand extends BaseCommand {
    @MainCommand(name = "teleporthere", description = "Teleport FileUtil player to you", permission = "speakhub.command.teleporthere", inGameOnly = true, aliases = { "tphere", "s" })
    public boolean sendMessage(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /teleporthere <player>");
            return false;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate(Lang.OFFLINE_PLAYER
                    .replace("%player%", args[1])));
            return false;
        }
        target.teleport( player);
        player.sendMessage(CC.translate(Lang.TELEPORT_TELEPORTHERE_TO
                .replace("%player%", target.getName())));
        return false;
    }
}
