package shateq.prophunt.fabric;

import com.mojang.brigadier.arguments.StringArgumentType;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class PropHuntMod implements ClientModInitializer {
    public static final Logger LOG = LoggerFactory.getLogger(PropHuntMod.class);

    @Override
    public void onInitializeClient() {
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
