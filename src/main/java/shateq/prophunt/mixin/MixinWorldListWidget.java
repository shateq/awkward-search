package shateq.prophunt.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldListWidget.class)
public abstract class MixinWorldListWidget extends AlwaysSelectedEntryListWidget<WorldListWidget.Entry> {
    public MixinWorldListWidget(MinecraftClient minecraftClient, int i, int j, int k, int l, int m) {
        super(minecraftClient, i, j, k, l, m);
    }
}
