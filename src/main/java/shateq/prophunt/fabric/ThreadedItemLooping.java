package shateq.prophunt.fabric;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public class ThreadedItemLooping extends Thread {
    private final DefaultedList<ItemStack> itemList;
    private final String search;

    public ThreadedItemLooping(String search, DefaultedList<ItemStack> list) {
        this.search = search;
        this.itemList = list;
    }

    @Override
    public void run() {
        for (final Item i : Registry.ITEM) {
            final String query = i.toString().toLowerCase(Locale.ROOT);

            final int ratio = FuzzySearch.partialRatio(search.toLowerCase(Locale.ROOT), query);
            PropHuntMod.LOG.info("{} and {}", query, ratio);

            if (ratio >= 50) {
                this.itemList.add(
                    new ItemStack(i)
                );
                PropHuntMod.LOG.warn("{} is true!", query);
            }
        }
        PropHuntMod.LOG.info("searching: {}", this.search);
    }
}
