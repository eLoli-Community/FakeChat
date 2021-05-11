package com.eloli.fakechat.bukkit;

import com.eloli.fakechat.core.Config;
import com.eloli.fakechat.core.FakeChatCore;
import com.eloli.fakechat.core.PlatformAdapter;
import com.eloli.fakechat.core.config.MainConfiguration;
import com.google.common.collect.ImmutableList;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BukkitLoader extends JavaPlugin implements PlatformAdapter, Listener {
    private static final String randomString= "It's FakeChat's bug If you can see it. " + UUID.randomUUID().toString();

    public static BukkitLoader instance;
    public static FakeChatCore core;
    @Override
    public void onEnable() {
        instance = this;
        core = new FakeChatCore(this,getDataFolder().toString());

        getServer().getPluginManager().registerEvents(this,this);
        getCommand("fakechat").setExecutor((sender, command, label, args) -> {
            if(args.length == 0){
                return false;
            }else if(args.length == 1){
                if(args[0].equalsIgnoreCase("reload")){
                    try {
                        Config.init();
                        sender.sendMessage("Success");
                    } catch (IOException e) {
                        sender.sendMessage("Failed to reload.");
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }

            String uuid = null;
            Player player = getServer().getPlayer(args[1]);
            if(player == null){
                for (OfflinePlayer offlinePlayer : getServer().getOfflinePlayers()) {
                    if(args[1].equals(offlinePlayer.getName())){
                        uuid = offlinePlayer.getUniqueId().toString();
                        break;
                    }
                }
                if(uuid == null){
                    sender.sendMessage("No such player.");
                    return true;
                }
            }else {
                uuid = player.getUniqueId().toString();
            }

            switch(args[0]){
                case "put":
                    if(args.length != 4){
                        return false;
                    }
                    MainConfiguration.PlayerSetting setting = new MainConfiguration.PlayerSetting();
                    setting.replace=args[2];
                    setting.replaceSelf = Boolean.parseBoolean(args[3]);
                    Config.replace.put(uuid,setting);
                    try {
                        Config.save();
                        sender.sendMessage("Success");
                    } catch (IOException | IllegalAccessException e) {
                        sender.sendMessage("Save configure failed");
                        e.printStackTrace();
                    }
                    break;
                case "remove":
                    if(args.length != 2){
                        return false;
                    }
                    Config.replace.remove(uuid);
                    try {
                        Config.save();
                        sender.sendMessage("Success");
                    } catch (IOException | IllegalAccessException e) {
                        sender.sendMessage("Save configure failed");
                        e.printStackTrace();
                    }
                    break;
                default:
                    return false;
            }
            return true;
        });
    }

    @EventHandler(priority= EventPriority.LOWEST)
    public void cancelledIfRandom(AsyncPlayerChatEvent event){
        if(event.getMessage().equals(randomString)){
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void setRandom(AsyncPlayerChatEvent event){
        MainConfiguration.PlayerSetting setting = Config.replace.getOrDefault(event.getPlayer().getUniqueId().toString(),null);
        if(setting == null) {
            return;
        }

        if(setting.replaceSelf){
            event.setMessage(setting.replace);
            return;
        }

        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            if(!onlinePlayer.equals(event.getPlayer())){
                event.getRecipients().remove(onlinePlayer);
            }
        }

        event.getPlayer().chat(randomString);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void recoveryRandom(AsyncPlayerChatEvent event){
        MainConfiguration.PlayerSetting setting = Config.replace.getOrDefault(event.getPlayer().getUniqueId().toString(),null);
        if(setting == null) {
            return;
        }

        if(event.getMessage().equals(randomString)){
            event.setMessage(setting.replace);
            event.getRecipients().remove(event.getPlayer());
            event.setCancelled(false);
        }
    }

    @Override
    public void info(String message) {
        getLogger().info(message);
    }

    @Override
    public void info(String message, Exception exception) {
        getLogger().info(message);
        getLogger().info(exception.toString());
    }

    @Override
    public void warn(String message) {
        getLogger().warning(message);
    }

    @Override
    public void warn(String message, Exception exception) {
        getLogger().warning(message);
        getLogger().warning(exception.toString());
    }
}
