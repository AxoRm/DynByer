package me.axorom.dynbyer.commands;

import com.google.common.collect.Lists;
import me.axorom.dynbyer.DynByer;
import me.axorom.dynbyer.gui.Gui;
import me.axorom.dynbyer.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SellerCommand extends AbstractCommand{
    public SellerCommand() {
        super("seller");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Неполная команда");
            return;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            DynByer.instance.reloadConfig();
            Config.getConfigClass().reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "Конфигурация перезагружена");
            return;
        }
        if (args[0].equalsIgnoreCase("menu")) {
            Gui gui = new Gui(Config.getRows(),Config.getTitle(), DynByer.items, (Player) sender);
            Bukkit.getLogger().info("Code here displaying gui");
            gui.displayInventory((Player) sender);
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if(args.length == 1) return Lists.newArrayList("menu", "reload");
        return Lists.newArrayList();
    }
}
