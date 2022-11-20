package net.danh.kgeneratorsmmoitems.Hook;

import me.kryniowesegryderiusz.kgenerators.api.interfaces.IGeneratorLocation;
import me.kryniowesegryderiusz.kgenerators.api.objects.AbstractGeneratedObject;
import me.kryniowesegryderiusz.kgenerators.logger.Logger;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class KGen extends AbstractGeneratedObject {

    private String material;

    public KGen() {
        super("mmoitems_items");
    }

    @Override
    public void regenerate(IGeneratorLocation generatorLocation) {
        ItemStack customBlock = this.getCustomBlock().newBuilder().build();
        World world = Bukkit.getWorld(generatorLocation.getGeneratedBlockLocation().getWorld().getName());
        if (customBlock != null && world != null) {
            world.dropItem(generatorLocation.getGeneratedBlockLocation(), customBlock);
        } else {
            generatorLocation.scheduleGeneratorRegeneration();
        }
    }

    @Override
    public ItemStack getGuiItem() {
        return getCustomBlock().newBuilder().build();
    }

    @Override
    protected String toStringSpecific() {
        return null;
    }

    @Override
    protected boolean compareSameType(AbstractGeneratedObject abstractGeneratedObject) {
        KGen kgen = (KGen) abstractGeneratedObject;
        return kgen.material.equals(this.material);
    }

    @Override
    protected boolean loadTypeSpecific(Map<?, ?> generatedObjectConfig) {
        if (generatedObjectConfig.containsKey("material")) {
            this.material = (String) generatedObjectConfig.get("material");
        } else {
            Logger.error("Generators file: Cant load GeneratedMMOItemsItems. Material is not set!");
            return false;
        }
        return true;
    }

    private MMOItem getCustomBlock() {
        MMOItem item = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(this.getType()), material);
        if (item == null) {
            Logger.error("Generators file: Cant load block GeneratedMMOItemsItems! Material " + material + " doesnt exist!");
        }
        return item;
    }
}
