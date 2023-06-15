package me.axorom.dynbyer.commands;

import com.google.common.collect.Lists;
import me.axorom.dynbyer.DynByer;
import me.axorom.dynbyer.gui.Gui;
import me.axorom.dynbyer.utils.Config;
import me.axorom.dynbyer.utils.Database;
import me.axorom.dynbyer.utils.DatabaseItem;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            sender.sendMessage(ChatColor.GREEN + "Конфигурация перезагружена");
            return;
        }
        if (args[0].equalsIgnoreCase("menu")) {
            Gui gui = new Gui(Config.getRows(),Config.getTitle(), DynByer.items, (Player) sender);
            gui.displayInventory((Player) sender);
            return;
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if(args.length == 1) return Lists.newArrayList("menu", "reload");
        return Lists.newArrayList();
    }
}
