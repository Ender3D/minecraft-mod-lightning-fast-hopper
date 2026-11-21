package com.ndrsbox.lightningfasthopper;

import com.ndrsbox.lightningfasthopper.block.LightningHopperBlock;
import com.ndrsbox.lightningfasthopper.block.LightningHopperBlockEntity;
import com.ndrsbox.lightningfasthopper.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lightningfasthopper implements ModInitializer {
	public static final String MOD_ID = "lightningfasthopper";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ModConfig CONFIG;

	public static Block LIGHTNING_HOPPER_BLOCK;
	public static Item LIGHTNING_HOPPER_ITEM;

	@Override
	public void onInitialize() {
		CONFIG = ModConfig.load();
		LOGGER.info("LightningFastHopper config loaded. In: {} ticks, Out: {} ticks", CONFIG.inputCooldownTicks, CONFIG.outputCooldownTicks);

		// A. Setup our specific Yarn-mapped Resource Keys
		Identifier blockId = Identifier.fromNamespaceAndPath(MOD_ID, "lightning_hopper");
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, blockId);

		Identifier itemId = Identifier.fromNamespaceAndPath(MOD_ID, "lightning_hopper");
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, itemId);

		// B. Inject the unique Identification Keys directly into your block properties structure
		LIGHTNING_HOPPER_BLOCK = Registry.register(
				BuiltInRegistries.BLOCK,
				blockKey,
				new LightningHopperBlock(BlockBehaviour.Properties.of()
						.setId(blockKey) // Stating identity key avoids the NullPointerException placement crash
						.destroyTime(3.0F)
						.explosionResistance(4.8F)
						.sound(net.minecraft.world.level.block.SoundType.METAL))
		);

		// C. Build and map your production item asset link
		LIGHTNING_HOPPER_ITEM = Registry.register(
				BuiltInRegistries.ITEM,
				itemKey,
				new BlockItem(LIGHTNING_HOPPER_BLOCK, new Item.Properties().setId(itemKey))
		);

		// D. Complete your custom BlockEntity mapping profile
		LightningHopperBlockEntity.TYPE = Registry.register(
				BuiltInRegistries.BLOCK_ENTITY_TYPE,
				Identifier.fromNamespaceAndPath(MOD_ID, "lightning_hopper"),
				net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create(LightningHopperBlockEntity::new, LIGHTNING_HOPPER_BLOCK).build()
		);
	}
}
