package shateq.prophunt.fabric;

import com.mojang.brigadier.arguments.StringArgumentType;
import me.xdrop.fuzzywuzzy.Extractor;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.ToStringFunction;
import me.xdrop.fuzzywuzzy.algorithms.WeightedRatio;
import me.xdrop.fuzzywuzzy.model.BoundExtractedResult;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class PropHuntMod implements ClientModInitializer {
    public static final Logger LOG = LoggerFactory.getLogger(PropHuntMod.class);
    public static int hardLimit = 60; //TODO CONFIG
    public static final List<Item> operatorItems = List.of(Items.COMMAND_BLOCK, Items.AIR);
    private static boolean operator = false;

    public static <T> List<BoundExtractedResult<T>> sortedExtraction(String query, Collection<T> choices, ToStringFunction<T> toStr, ToStringFunction<T> opt) {
        Extractor extractor = new Extractor();
        extractor.extractTop(query, choices, toStr, new WeightedRatio());
        return List.of();
    }

    public static boolean isOperator() {
        if (operator) return true;
        assert MinecraftClient.getInstance().player != null;
        return MinecraftClient.getInstance().player.hasPermissionLevel(2);
    }

    @Override
    public void onInitializeClient() {
        operator = false; //1.19.3 operator items
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            LOG.debug("Registering helper commands");

            ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                literal("ratio")
                    .then(literal("partial")
                        .then(argument("search", StringArgumentType.string())
                            //.then(argument("query", StringArgumentType.string()))
                        )
                        .executes(context -> {
                            String s = context.getArgument("search", String.class);
                            //String q = context.getArgument("query", String.class);

                            //String ratio = String.valueOf(FuzzySearch.partialRatio(s, q));
                            context.getSource().sendFeedback(Text.of("search: " + s));
                            return 0;
                        })
                    )
                    .then(literal("just")
                        .then(argument("search", StringArgumentType.string())
                            .then(argument("query", StringArgumentType.string()))
                        )
                        .executes(context -> {
                            String s = context.getArgument("search", String.class);
                            String q = context.getArgument("query", String.class);

                            String ratio = String.valueOf(FuzzySearch.ratio(s, q));
                            context.getSource().sendFeedback(Text.of(ratio));
                            return 0;
                        })
                    )
            ));
        }
    }
}
