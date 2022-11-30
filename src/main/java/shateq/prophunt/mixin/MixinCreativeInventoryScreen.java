package shateq.prophunt.mixin;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenTexts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shateq.prophunt.fabric.PropHuntMod;

@Mixin(CreativeInventoryScreen.class)
public abstract class MixinCreativeInventoryScreen extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {
    @Shadow
    private TextFieldWidget searchBox;

    public MixinCreativeInventoryScreen(PlayerEntity player) {
        super(new CreativeInventoryScreen.CreativeScreenHandler(player), player.getInventory(), ScreenTexts.EMPTY);
    }
        /*List<BoundExtractedResult<String>> res = FuzzySearch.extractAll("goolge",
            Arrays.asList("google", "bing", "facebook", "linkedin", "twitter", "googleplus", "bingnews", "plexoogl"),
            i -> i.toLowerCase()
        );
        List<BoundExtractedResult<String>> sorted = FuzzySearch.extractSorted("goolge",
            Arrays.asList("google", "bing", "facebook", "linkedin", "twitter", "googleplus", "bingnews", "plexoogl"),
            i -> i.toLowerCase()
        );*/

    @Inject(method = "search", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen$CreativeScreenHandler;scrollItems(F)V"))
    protected void search(CallbackInfo ci) {
        final String string = this.searchBox.getText();
        var handler = (CreativeInventoryScreen.CreativeScreenHandler) this.handler;
        if (string.isEmpty() || string.startsWith("#")) {
            return;
        }

        try {
            for (int i = 0; i < handler.itemList.size(); i++) {
                if (i % 2 == 0) {
                    handler.itemList.set(i, Items.CARROT.getDefaultStack());
                }
            }
        } catch (IndexOutOfBoundsException e) {
            PropHuntMod.LOG.error("Tried:", e);
        }

        //TODO here
        /*ThreadedItemLooping thread = new ThreadedItemLooping(string, this.handler.itemList);
        thread.start();
        for (final Item i : Registry.ITEM) {
            final String query = i.toString().toLowerCase(Locale.ROOT);

            final int ratio = FuzzySearch.ratio(string.toLowerCase(Locale.ROOT), query);
            PropHuntMod.LOG.info("{} and {}", query, ratio);

            if (ratio > 50) {
                this.handler.itemList.add(
                    new ItemStack(i)
                );
            }
        }
        List<BoundExtractedResult<ItemStack>> results;
        Registry.ITEM.streamEntries().
            Stream < Item > itemStream = Registry.ITEM.stream();
        results = FuzzySearch.extractAll(string.toLowerCase(Locale.ROOT), itemStream, i -> i.toString());
        this.handler.itemList.addAll(itemList.forEach(Item::getDefaultStack));
        */
    }
}

