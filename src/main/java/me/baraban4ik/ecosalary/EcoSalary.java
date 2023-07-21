package me.baraban4ik.ecosalary;

import me.baraban4ik.ecosalary.bstats.Metrics;
import me.baraban4ik.ecosalary.commands.BaseTabCompleter;
import me.baraban4ik.ecosalary.commands.EcoSalaryCommand;
import me.baraban4ik.ecosalary.commands.SalaryCommand;
import me.baraban4ik.ecosalary.utils.Chat;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EcoSalary extends JavaPlugin {

    public static EcoSalary instance;

    public static boolean VaultAPI = false;
    public static boolean PlayerPointsAPI = false;

    private static Economy economy;
    private static Permission perms;
    private static PlayerPointsAPI ppAPI;

    @Override
    public void onLoad() { instance = this; }

    @Override
    public void onEnable() {
        getHooks();

        if (VaultAPI || PlayerPointsAPI) {

            Chat.sendPluginMessage(MESSAGES.ENABLE_MESSAGE, Bukkit.getConsoleSender());
            new Metrics(this, 18900);

            this.saveDefaultConfig();
            this.registerCommands();
        }
    }

    private void registerCommands() {
        this.getCommand("salary").setExecutor(new SalaryCommand());
        this.getCommand("ecosalary").setExecutor(new EcoSalaryCommand());
        this.getCommand("ecosalary").setTabCompleter(new BaseTabCompleter());
    }

    private void getHooks() {
        String economy = this.getConfig().getString("economy", "Vault");

        if (!getServer().getPluginManager().isPluginEnabled("Vault")) {
            this.getLogger().warning("Disabled due to Vault dependency not found!");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!this.setupPermissions()) {
            this.getLogger().warning("Disabled due to Permission Plugin not found!");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.setupPermissions();

        if (economy.equalsIgnoreCase("Vault")) {
            if (!this.setupEconomy()) {
                this.getLogger().warning("Disabled due to Economy Plugin not found!");
                this.getServer().getPluginManager().disablePlugin(this);
                return;
            }
			PlayerPointsAPI = false;
            VaultAPI = true;
			
			this.setupEconomy();
        }
        else if (economy.equalsIgnoreCase("PlayerPoints")) {
            if (!getServer().getPluginManager().isPluginEnabled("PlayerPoints")) {
                this.getLogger().warning("Disabled due to PlayerPoints dependency not found!");
                this.getServer().getPluginManager().disablePlugin(this);
                return;
            }
			VaultAPI = false;
            PlayerPointsAPI = true;
			
            ppAPI = PlayerPoints.getInstance().getAPI();
        }
        else {
            this.getLogger().warning("Disabled due to " + economy + " Plugin not support!");
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

	public void reload() {
        this.updateConfig();
		this.reloadConfig();

        getHooks();
	}
    private void updateConfig() {
        if (!this.getConfig().get("config-version").equals(this.getDescription().getVersion())) {
            File file = new File(EcoSalary.instance.getDataFolder(), "config.yml");
            file.renameTo(new File(EcoSalary.instance.getDataFolder(), "config.yml.old"));

            this.saveDefaultConfig();
        }
    }
		
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;

        economy = rsp.getProvider();
        return true;
    }
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) return false;
        perms = rsp.getProvider();
        return true;
    }

    public static Economy getEconomy() {
        return economy;
    }
    public static Permission getPermissions() {
        return perms;
    }
    public static PlayerPointsAPI getPlayerPoints() {
        return ppAPI;
    }

    public Float getVersion() {
        String version = Bukkit.getVersion();
        String pattern = "[^0-9\\.\\:]";
        String versionMinecraft = version.replaceAll(pattern, "");

        return Float.parseFloat(versionMinecraft.substring(versionMinecraft.indexOf(":") + 1, versionMinecraft.lastIndexOf(".")));
    }
}
