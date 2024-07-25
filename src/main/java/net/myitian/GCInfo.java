package net.myitian;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.ModInitializer;

import static net.minecraft.server.command.CommandManager.literal;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

public class GCInfo implements ModInitializer {
    public static final MutableText Line4 = Text.literal("§a=====================");

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("gc")
                .then(literal("info")
                        .executes(context -> {
                            List<GarbageCollectorMXBean> gcmxbeans = ManagementFactory.getGarbageCollectorMXBeans();
                            int len = gcmxbeans.size();
                            for (int i = 0; i < len; i++) {
                                GarbageCollectorMXBean gcmxbean = gcmxbeans.get(i);
                                MutableText line0 = Text.literal("§a==§f Server GC Info §7#§3" + i + " §a==");
                                MutableText line1 = Text.literal("§b" + gcmxbean.getName());
                                MutableText line2 = Text.literal("§fCollection Count§7: §e" + gcmxbean.getCollectionCount());
                                MutableText line3 = Text.literal("§fCollection Time§7: §e" + gcmxbean.getCollectionTime());
                                context.getSource().sendFeedback(() -> line0, false);
                                context.getSource().sendFeedback(() -> line1, false);
                                context.getSource().sendFeedback(() -> line2, false);
                                context.getSource().sendFeedback(() -> line3, false);
                                context.getSource().sendFeedback(() -> Line4, false);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(literal("collect")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> {
                            context.getSource().sendFeedback(() -> Text.literal("§bSystem.gc();"), false);
                            System.gc();
                            return Command.SINGLE_SUCCESS;
                        }))));
    }
}