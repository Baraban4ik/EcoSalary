package me.baraban4ik.ecosalary.utils;

import me.baraban4ik.ecosalary.EcoSalary;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat {

    public static void sendMessage(String path, CommandSender sender) {
        List<String> message = EcoSalary.instance.getConfig().getStringList(path);
        if (message.isEmpty()) return;

        message.forEach(x -> sender.sendMessage(format(x, sender)));
    }
    public static void sendPluginMessage(String message, CommandSender sender) {
        sender.sendMessage(format(message, sender));
    }
    public static void sendPluginMessage(List<String> message, CommandSender sender) {
        message.forEach(x -> sender.sendMessage(format(x, sender)));
    }

    private static String format(String text, CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                text = PlaceholderAPI.setPlaceholders(player, text);
            }
            text = text.replace("%player%", player.getDisplayName());

            text = replaceSalary(player, text);
            text = replaceCooldown(player, text);

        }
        if (EcoSalary.instance.getVersion() >= 1.16f) {
            Pattern pattern = Pattern.compile(EcoSalary.instance.getConfig().getString("hex-pattern", "&#[a-fA-F0-9]{6}"));
            Matcher match = pattern.matcher(text);

            while (match.find()) {
                String color = text.substring(match.start(), match.end());
                text = text.replace(color, String.valueOf(ChatColor.of(color)));

                match = pattern.matcher(text);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String replaceSalary(Player player, String text) {
        String groupName = EcoSalary.getPermissions().getPrimaryGroup(player);
        int salary = EcoSalary.instance.getConfig().getInt("Groups." + groupName.toLowerCase() + ".salary", 0);

        if (salary == 0) return text;
        return text.replace("%salary%", String.valueOf(salary));
    }
    public static String replaceCooldown(Player player, String text) {
        String playerName = player.getName();
        String groupName = EcoSalary.getPermissions().getPrimaryGroup(player);

        if (Data.getData().containsKey(playerName)) {
            long timeout = System.currentTimeMillis() - Data.getData().get(playerName);
            int groupTimeout = EcoSalary.instance.getConfig().getInt("Groups." + groupName.toLowerCase() + ".cooldown", 86400) * 1000;

            String cooldown = Time.getCooldownTime((long) (groupTimeout / 1000) - timeout / 1000L);
            return text.replace("%time%", cooldown);
        }
        return text;
    }
}
