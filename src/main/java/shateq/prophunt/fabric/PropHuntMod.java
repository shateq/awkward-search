package shateq.prophunt.fabric;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.List;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class PropHuntMod implements ClientModInitializer {
    public static final List<Item> operatorItems = List.of(Items.AIR, Items.BARRIER, Items.LIGHT, Items.DEBUG_STICK,
        Items.COMMAND_BLOCK, Items.COMMAND_BLOCK_MINECART, Items.CHAIN_COMMAND_BLOCK, Items.REPEATING_COMMAND_BLOCK,
        Items.SPAWNER, Items.JIGSAW, Items.STRUCTURE_BLOCK, Items.STRUCTURE_VOID);
    public static int hardLimit = 60; //TODO CONFIG
    private static boolean operator = false;

    /*public static <T> List<BoundExtractedResult<T>> sortedExtraction(String query, Collection<T> choices, ToStringFunction<T> toStr, ToStringFunction<T> opt) {
        Extractor extractor = new Extractor();
        extractor.extractTop(query, choices, toStr, new WeightedRatio());
        return List.of();
    }*/

    public static boolean isOperator() {
        if (operator) return true;
        assert MinecraftClient.getInstance().player != null;
        return MinecraftClient.getInstance().player.hasPermissionLevel(2);
    }

    @Override
    public void onInitializeClient() {
        operator = false; //1.19.3 operator items

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(
                    literal("weighted")
                        .then(argument("query", string())
                            .then(argument("string", greedyString())
                                .executes(c -> {
                                    String query = getString(c, "query");
                                    String string = getString(c, "string");

                                    c.getSource().sendFeedback(Text.literal(
                                        String.valueOf(FuzzySearch.weightedRatio(query, string))
                                    ));
                                    return 0;
                                })
                            )
                        )
                        .executes(c -> {
                            c.getSource().sendError(Text.literal("Called with no arguments! Expected 2, got 0."));
                            return 1;
                        })
                )
            );
        }
    }
}
