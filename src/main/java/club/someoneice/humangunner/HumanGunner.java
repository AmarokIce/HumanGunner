package club.someoneice.humangunner;

import com.craftix.hostile_humans.entity.entities.Human;
import com.craftix.hostile_humans.entity.entities.HumanInventoryGenerator;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.builder.GunItemBuilder;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import tallestegg.guardvillagers.entities.Guard;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Mod(HumanGunner.ID)
public class HumanGunner {
    public static final String ID = "humangunner";

    private static double globe = 0.4D;
    private static Map<ResourceLocation, Integer> guns = Maps.newHashMap();
    private static final Multimap<Integer, ResourceLocation> gunList = HashMultimap.create();
    private static final Gson gson = (new GsonBuilder()).disableHtmlEscaping().setPrettyPrinting().create();

    private static boolean isLoadingConfig = false;
    private static final Random random = new Random();

    public HumanGunner() {
    }

    public static void setConfig() {
        try {
            File configFile = new File(FMLLoader.getGamePath().resolve("config").toFile(), "humangunner.json");
            if (!configFile.exists() || !configFile.isFile()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
                initConfig(configFile);
                placeGuns();
                return;
            }
            JsonObject config = JsonParser.parseReader(new FileReader(configFile)).getAsJsonObject();
            globe = config.get("globe").getAsDouble();
            JsonObject data = config.getAsJsonObject("guns");
            data.entrySet().forEach(it -> guns.put(ResourceLocation.tryParse(it.getKey()), it.getValue().getAsInt()));
            placeGuns();
        }
        catch (IOException ignored) {}
        finally { guns = null; }
    }

    public static void hook(Human human, boolean forceRanged) {
        if (!isLoadingConfig) {
            setConfig();
            isLoadingConfig = true;
        }

        if (forceRanged) {
            if (Math.random() >= 0.33D)
                return;
        } else if (Math.random() > globe) {
            return;
        }
        int index = randomInt(new IntOpenHashSet(gunList.keySet()));
        List<ResourceLocation> gunItems = gunList.get(index).stream().toList();
        index = human.getLevel().getRandom().nextInt(gunItems.size());
        human.setItemSlot(EquipmentSlot.MAINHAND, GunItemBuilder.create().setId(gunItems.get(index)).build());
        human.putItemAway(HumanInventoryGenerator.tier2Weapons[random.nextInt(0, HumanInventoryGenerator.tier2Weapons.length)].getDefaultInstance());
    }

    public static void hook(Guard human) {
        if (!isLoadingConfig) {
            setConfig();
            isLoadingConfig = true;
        }

        if (Math.random() > globe) {
            return;
        }

        int index = randomInt(new IntOpenHashSet(gunList.keySet()));
        List<ResourceLocation> gunItems = gunList.get(index).stream().toList();
        index = human.getLevel().getRandom().nextInt(gunItems.size());
        human.setItemSlot(EquipmentSlot.MAINHAND, GunItemBuilder.create().setId(gunItems.get(index)).build());
    }


    private static int randomInt(IntSet ints) {
        int sum = ints.intStream().sum();
        for (int it : ints.intStream().sorted().toArray()) {
            double randomInt = Math.random();
            double weight = (double) it / sum;
            if (randomInt <= weight)
                return it;
            sum -= it;
        }
        return -1;
    }

    private static void initConfig(File file) throws IOException {
        JsonObject data = new JsonObject();
        TimelessAPI.getAllCommonGunIndex().forEach(it -> {
            data.addProperty(it.getKey().toString(), 5);
            guns.put(it.getKey(), 5);
        });
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("globe", globe);
        jsonObject.add("guns", data);
        Files.write(gson.toJson(jsonObject).getBytes(), file);
    }

    private static void placeGuns() {
        guns.forEach((key, value) -> gunList.put(value, key));
    }
}
