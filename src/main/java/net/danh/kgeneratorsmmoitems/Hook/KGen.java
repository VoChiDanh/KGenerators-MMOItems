package net.danh.kgeneratorsmmoitems.Hook;

import me.kryniowesegryderiusz.kgenerators.Main;
import me.kryniowesegryderiusz.kgenerators.api.interfaces.IGeneratorLocation;
import me.kryniowesegryderiusz.kgenerators.api.objects.AbstractGeneratedObject;
import me.kryniowesegryderiusz.kgenerators.generators.generator.objects.GeneratedItem;
import me.kryniowesegryderiusz.kgenerators.logger.Logger;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class KGen extends AbstractGeneratedObject {

    public KGen() {
        super("mmoitems_items");
    }

    private String item;


    @Override
    protected boolean compareSameType(AbstractGeneratedObject generatedObject) {
        GeneratedItem gi = (GeneratedItem) generatedObject;
        return true;
    }

    @Override
    protected boolean loadTypeSpecific(Map<?, ?> generatedObjectConfig) {
        if (generatedObjectConfig.containsKey("material")) {
            this.item = (String) generatedObjectConfig.get("material");
        } else {
            Logger.error("Generators file: Cant load GeneratedMMOItems. Material is not set!");
            return false;
        }
        return this.item != null;
    }

    @Override
    public void regenerate(IGeneratorLocation generatorLocation) {
        Location generateLocation = generatorLocation.getGeneratedBlockLocation().clone().add(0.5, 0, 0.5);
        if (!Main.getMultiVersion().getBlocksUtils().isAir(generatorLocation.getGeneratedBlockLocation().getBlock()))
            generateLocation.add(0, 1, 0);
        generateLocation.setPitch(-90);
        generatorLocation.getGeneratedBlockLocation().getWorld().dropItem(generateLocation, getGuiItem());
        generatorLocation.scheduleGeneratorRegeneration();
    }

    @Override
    public ItemStack getGuiItem() {
        if (getCustomItems() == null) return new ItemStack(Material.STONE);
        else return getCustomItems().newBuilder().build();
    }

    @Override
    protected String toStringSpecific() {
        String s = "None";
        if (this.item != null) s = "Item: " + this.item;

        return s;
    }

    private MMOItem getCustomItems() {
        if (item.split(":").length == 2) {
            String[] m = item.split(":");
            if (m[0].equals("mmoitems")) {
                MMOItem item = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(m[1]), m[2]);
                if (item == null) {
                    Logger.error("Generators file: Cant load block GeneratedMMOItemsItems! Material " + this.item + " doesnt exist!");
                    return null;
                } else {
                    return item;
                }
            }
        }
        return null;
    }
}
