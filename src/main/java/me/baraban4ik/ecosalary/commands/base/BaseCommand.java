package me.baraban4ik.ecosalary.commands.base;

import me.baraban4ik.ecosalary.MESSAGES;
import me.baraban4ik.ecosalary.commands.BaseTabCompleter;
import me.baraban4ik.ecosalary.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class BaseCommand extends BaseTabCompleter implements CommandExecutor {

    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String[] args);

    public boolean hasPermission(@NotNull CommandSender sender, @NotNull String permission) {
        if (sender.hasPermission(permission)) return true;

        Chat.sendMessage("Messages.no-permission", sender);
        return false;
    }
    public boolean isPlayer(@NotNull CommandSender sender) {
        if (sender instanceof Player) return true;

        Chat.sendPluginMessage(MESSAGES.NO_PLAYER, sender);
        return false;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return this.execute(sender, args);
    }
}
