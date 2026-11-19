package com.example.autosell;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class AutoSellConfig {
    private static final Path FILE = Path.of("config", "autosell.properties");
    public static boolean enabled = false;
    public static String command = "/sell";
    public static int delaySeconds = 10;
    public static final Set<String> selected = new HashSet<>();
    private AutoSellConfig() {}
    public static void load() {
        Properties p = new Properties();
        try { if (Files.exists(FILE)) try (Reader r=Files.newBufferedReader(FILE)) { p.load(r); }
            enabled = Boolean.parseBoolean(p.getProperty("enabled", "false"));
            command = p.getProperty("command", "/sell");
            delaySeconds = Math.max(2, Math.min(300, Integer.parseInt(p.getProperty("delaySeconds", "10"))));
            selected.clear(); String s=p.getProperty("items", ""); for (String id:s.split(",")) if(!id.isBlank()) selected.add(id.trim());
        } catch (Exception ignored) { }
    }
    public static void save() {
        Properties p=new Properties(); p.setProperty("enabled", Boolean.toString(enabled)); p.setProperty("command", command); p.setProperty("delaySeconds", Integer.toString(delaySeconds)); p.setProperty("items", String.join(",", selected));
        try { Files.createDirectories(FILE.getParent()); try(Writer w=Files.newBufferedWriter(FILE)){p.store(w,"AutoSell settings");} } catch(IOException ignored){}
    }
    public static List<Item> items() {
        List<Item> out=new ArrayList<>(); for(Item item:Registries.ITEM) out.add(item); return out;
    }
    public static String id(Item item) { return Registries.ITEM.getId(item).toString(); }
    public static Item item(String id) { return Registries.ITEM.get(Identifier.tryParse(id)); }
}
