package me.kapehh.DupFinder;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class FindDuperListener implements Listener {

    private class PlayerDuperInventory {
        int Beacon, NetherStar,
            EmeraldBlock, DiamondBlock,
            GoldBlock, IronBlock,
            GoldenApple, Enchants,
            MonsterEgg, Bedrock,
            Water, Lava, CheatItem;

        public PlayerDuperInventory() {
            reset();
        }

        public void reset() {
            Beacon = NetherStar =
            EmeraldBlock = DiamondBlock =
            GoldBlock = IronBlock =
            Enchants = GoldenApple =
            MonsterEgg = Bedrock =
            Water = Lava = CheatItem = 0;
        }

        public boolean isDuper() {
            return  (Beacon >= 64) || (NetherStar > 64) ||
                    (EmeraldBlock >= 64) || (DiamondBlock >= 64) ||
                    (GoldBlock >= 64) || (IronBlock >= 64) ||
                    (GoldenApple >= 32) || (Enchants > 3) ||
                    (MonsterEgg > 0) || (Bedrock > 0) ||
                    (Water > 0) || (Lava > 0) || (CheatItem > 0);
        }

        @SuppressWarnings("deprecation")
        public void run(ItemStack[] itemStacks) {
            int amount, id, data;
            reset();
            for (ItemStack o : itemStacks) {
                if (o == null)
                    continue;
                amount = o.getAmount();
                id = o.getTypeId();
                data = o.getData().getData();
                if (amount < 0) {
                    CheatItem += 1;
                }
                switch (id) {
                    case 138:
                        Beacon += amount;
                        break;
                    case 399:
                        NetherStar += amount;
                        break;
                    case 133:
                        EmeraldBlock += amount;
                        break;
                    case 57:
                        DiamondBlock += amount;
                        break;
                    case 41:
                        GoldBlock += amount;
                        break;
                    case 42:
                        IronBlock += amount;
                        break;
                    case 322:
                        if (data > 0) {
                            GoldenApple += amount;
                        }
                        break;
                    case 383:
                        MonsterEgg += amount;
                        break;
                    case 7:
                        Bedrock += amount;
                        break;
                    case 8:
                    case 9:
                        Water += amount;
                        break;
                    case 10:
                    case 11:
                        Lava += amount;
                        break;
                }
                if (o.getEnchantments() != null) {
                    for (Integer enchantLevel : o.getEnchantments().values()) {
                        if (enchantLevel >= 10) {
                            Enchants += amount;
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder().append('[');
            if (Beacon > 0) sb.append("Beacon ").append(Beacon).append(". ");
            if (NetherStar > 0) sb.append("NetherStar ").append(NetherStar).append(". ");
            if (EmeraldBlock > 0) sb.append("EmeraldBlock ").append(EmeraldBlock).append(". ");
            if (DiamondBlock > 0) sb.append("DiamondBlock ").append(DiamondBlock).append(". ");
            if (GoldBlock > 0) sb.append("GoldBlock ").append(GoldBlock).append(". ");
            if (IronBlock > 0) sb.append("IronBlock ").append(IronBlock).append(". ");
            if (GoldenApple > 0) sb.append("GoldenApple ").append(GoldenApple).append(". ");
            if (Enchants > 0) sb.append("Enchants ").append(Enchants).append(". ");
            if (MonsterEgg > 0) sb.append("MonsterEgg ").append(MonsterEgg).append(". ");
            if (Bedrock > 0) sb.append("Bedrock ").append(Bedrock).append(". ");
            if (Water > 0) sb.append("Water ").append(Water).append(". ");
            if (Lava > 0) sb.append("Lava ").append(Lava).append(". ");
            if (CheatItem > 0) sb.append("CheatItem ").append(CheatItem).append(". ");
            return sb.append(']').toString();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        PlayerDuperInventory playerDuperInventory = new PlayerDuperInventory();

        playerDuperInventory.run(event.getInventory().getContents());
        if (playerDuperInventory.isDuper())
            logPrint(playerDuperInventory, event, event.getInventory());

        playerDuperInventory.run(event.getPlayer().getInventory().getContents());
        if (playerDuperInventory.isDuper())
            logPrint(playerDuperInventory, event, event.getPlayer().getInventory());
    }

    private void logPrint(PlayerDuperInventory playerDuperInventory, InventoryCloseEvent event, Inventory inventory) {
        DupFinder.getPluginLogger().getLog().info(
            event.getPlayer().getName() + " | " + locationToString(event.getPlayer().getLocation(), true) + " | " +
            inventory.getTitle() + " (" +
            (inventory.getHolder() == null ? "null" : "exists") +
            ") [" + inventory.getType().toString() + "] | " +
            playerDuperInventory.toString()
        );
    }

    public static String locationToString(Location in, boolean useInt) {
        if (in == null) {
            return "null";
        }
        if (useInt) {
            return in.getWorld().getName() + ":" + (long)in.getX() + "," + (long)in.getY() + "," + (long)in.getZ();
        } else {
            return in.getWorld().getName() + ":" + in.getX() + "," + in.getY() + "," + in.getZ();
        }
    }
}
