package shateq.prophunt.mixin;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.ToStringFunction;
import me.xdrop.fuzzywuzzy.model.BoundExtractedResult;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shateq.prophunt.fabric.PropHuntMod;

import java.util.ArrayList;
import java.util.Locale;

@Mixin(CreativeInventoryScreen.class)
public abstract class MixinCreativeInventoryScreen extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {
    @Shadow
    private TextFieldWidget searchBox;

    public MixinCreativeInventoryScreen(PlayerEntity player) {
        super(new CreativeInventoryScreen.CreativeScreenHandler(player), player.getInventory(), ScreenTexts.EMPTY);
    }

    @Inject(
        method = "search",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen$CreativeScreenHandler;scrollItems(F)V")
    )
    protected void search(CallbackInfo ci) {
        String search = this.searchBox.getText().toLowerCase(Locale.ROOT);
        var handler = (CreativeInventoryScreen.CreativeScreenHandler) this.handler;
        if (search.isEmpty() || search.startsWith("#")) return;
        handler.itemList.clear(); //TODO subject

        var items = Registry.ITEM.stream().toList();
        ToStringFunction<Item> toStr = search.startsWith(".") ?
            item -> item.toString().replaceAll("_", " ") :
            item -> item.getName().getString();

        search = search.substring(1);
        var allSorted = FuzzySearch.extractSorted(search, items, toStr);

        ArrayList<ItemStack> stacks = new ArrayList<>();
        for (BoundExtractedResult<Item> i : allSorted) {
            if (!PropHuntMod.isOperator() && PropHuntMod.operatorItems.contains(i.getReferent())) continue;
            if (i.getScore() < PropHuntMod.hardLimit) continue;

            var item = i.getReferent();
            stacks.add(item.getDefaultStack());
        }

        this.handler.itemList.addAll(stacks);
    }
    // TODO TOOLTIP WHEN .
    //@Inject(method = "renderTooltip", at = @At("TAIL"))
    //protected void tooltip(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci) {}
}
