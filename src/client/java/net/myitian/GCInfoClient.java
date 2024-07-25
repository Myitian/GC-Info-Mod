package net.myitian;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

@Environment(EnvType.CLIENT)
public class GCInfoClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("cgc")
                .then(literal("info")
                        .executes(context -> {
                            List<GarbageCollectorMXBean> gcmxbeans = ManagementFactory.getGarbageCollectorMXBeans();
                            int len = gcmxbeans.size();
                            for (int i = 0; i < len; i++) {
                                GarbageCollectorMXBean gcmxbean = gcmxbeans.get(i);
                                MutableText line0 = Text.literal("§a==§f Client GC Info §7#§3" + i + " §a==");
                                MutableText line1 = Text.literal("§b" + gcmxbean.getName());
                                MutableText line2 = Text.literal("§fCollection Count§7: §e" + gcmxbean.getCollectionCount());
                                MutableText line3 = Text.literal("§fCollection Time§7: §e" + gcmxbean.getCollectionTime());
                                context.getSource().sendFeedback(line0);
                                context.getSource().sendFeedback(line1);
                                context.getSource().sendFeedback(line2);
                                context.getSource().sendFeedback(line3);
                                context.getSource().sendFeedback(GCInfo.Line4);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(literal("collect")
                        .executes(context -> {
                            context.getSource().sendFeedback(Text.literal("§bSystem.gc();"));
                            System.gc();
                            return Command.SINGLE_SUCCESS;
                        }))));
    }
}