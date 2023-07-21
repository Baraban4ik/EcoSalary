package me.baraban4ik.ecosalary.commands;

import com.google.common.collect.Lists;
import me.baraban4ik.ecosalary.EcoSalary;
import me.baraban4ik.ecosalary.commands.base.BaseCommand;
import me.baraban4ik.ecosalary.utils.Chat;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EcoSalaryCommand extends BaseCommand {
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload"))
                this.reload(sender);
            else
                this.sendHelp(sender);
        }
        else this.sendHelp(sender);

        return true;
    }

    private void reload(CommandSender sender) {
        if (!hasPermission(sender, "ecosalary.reload")) return;

        Chat.sendPluginMessage("  &f&lᴇ&a&lsᴀʟᴀʀʏ  &8|  &fPlugin successfully reloaded!", sender);
        EcoSalary.instance.reload();

    }
    private void sendHelp(CommandSender sender) {
        if (!hasPermission(sender, "ecosalary.help")) return;
        Chat.sendMessage("Messages.help", sender);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("reload", "help");
        return new ArrayList<>();
    }
}
