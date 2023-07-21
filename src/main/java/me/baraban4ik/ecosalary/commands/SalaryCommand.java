package me.baraban4ik.ecosalary.commands;

import me.baraban4ik.ecosalary.EcoSalary;
import me.baraban4ik.ecosalary.commands.base.BaseCommand;
import me.baraban4ik.ecosalary.utils.Chat;
import me.baraban4ik.ecosalary.utils.Data;
import me.baraban4ik.ecosalary.utils.Items;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class SalaryCommand extends BaseCommand {
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!isPlayer(sender)) return true;
        Player player = (Player) sender;

        if (!hasPermission(player, "ecosalary.salary")) return true;
        this.giveSalary(player);

        return true;
    }

    private void giveSalary(Player player) {
        FileConfiguration config = EcoSalary.instance.getConfig();

        String playerName = player.getName();
        String groupName = EcoSalary.getPermissions().getPrimaryGroup(player);

        if (!config.getConfigurationSection("Groups").getKeys(false).contains(groupName.toLowerCase())) {
            Chat.sendMessage("Messages.no-salary", player);
            return;
        }
        int salary = config.getInt("Groups." + groupName.toLowerCase() + ".salary");
        ConfigurationSection items = config.getConfigurationSection("Groups." + groupName.toLowerCase() + ".items");

        if (!Data.getData().containsKey(playerName)) this.received(player, salary, items);
        else this.cooldown(player, playerName, groupName, salary, items, config);
    }
    private void received(Player player, int salary, ConfigurationSection items) {
        if (items != null) {
            for (String key : items.getKeys(false)) {
                ItemStack item = Items.createItem(items.getString(key + ".material"),
                        items.getInt(key + ".amount"),
                        items.getString(key + ".name"),
                        items.getStringList(key + ".lore"));
                player.getInventory().addItem(item);
            }
        }
        if (EcoSalary.VaultAPI) {
            EcoSalary.getEconomy().depositPlayer(player, salary);
        }
        else if (EcoSalary.PlayerPointsAPI) {
            EcoSalary.getPlayerPoints().give(player.getUniqueId(), salary);
        }

        Data.setData(player.getName(), System.currentTimeMillis());
        Chat.sendMessage("Messages.salary-received", player);
    }

    private void cooldown(Player player, String playerName, String groupName, int salary, ConfigurationSection items, FileConfiguration config) {
        long timeout = System.currentTimeMillis() - Data.getData().get(playerName);
        int groupTimeout = config.getInt("Groups." + groupName.toLowerCase() + ".cooldown") * 1000;

        if (timeout < (long)groupTimeout)
            Chat.sendMessage("Messages.salary-cooldown", player);
        else
            this.received(player, salary, items);
    }
}
