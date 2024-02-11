package standardtacticalknight.simplyskyblock;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLayerBase;
import net.minecraft.core.block.BlockMesh;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlockLayer;
import net.minecraft.core.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.ItemHelper;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class SimplySkyblock implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "simplyskyblock";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Block layerChainmail;
	public static Block blockChainmail;
	public static Item itemCrucible;

    @Override
    public void onInitialize() {
        LOGGER.info("SimplySkyblock initialized.");
    }

	@Override
	public void beforeGameStart() {
		int startingBlockId = 2000;
		int itemID = 16750;

		blockChainmail = new BlockBuilder(MOD_ID)
			.setTextures(7,19)
			.setHardness(1f)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE,BlockTags.CHAINLINK_FENCES_CONNECT)
			.build(new BlockMesh("chainmailheap", startingBlockId++){
				@Override
				public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
					switch (dropCause) {
						case PICK_BLOCK: {
							return new ItemStack[]{new ItemStack(layerChainmail)};
						}
						case SILK_TOUCH:
						case EXPLOSION:
						case PROPER_TOOL: {
							return new ItemStack[]{new ItemStack(layerChainmail, 8)};
						}
					}
					return null;
				}
			});

		layerChainmail = new BlockBuilder(MOD_ID)
			.setTextures(7,19)
			.setHardness(1f)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.setItemBlock(ItemBlockLayer::new)
			.build(new BlockLayerBase("layer.chainmail", startingBlockId++,Material.metal) {
				@Override
				public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
					switch (dropCause) {
						case PICK_BLOCK: {
							return new ItemStack[]{new ItemStack(this)};
						}
						case SILK_TOUCH:
						case EXPLOSION:
						case PROPER_TOOL: {
							return new ItemStack[]{new ItemStack(this, meta + 1)};
						}
					}
					return null;
				}
				//makes mail layer act as Mesh blocks do (items pass through)
				@Override
				public boolean collidesWithEntity(Entity entity) {
					return !(entity instanceof EntityItem);
				}
			});
		((BlockLayerBase)layerChainmail).setFullBlockID(blockChainmail.id);

		itemCrucible = ItemHelper.createItem(MOD_ID,new Item("crucible", itemID++),"crucible","crucible.png");

		// Make sure to assign names and descriptions to your blocks in your mods .lang file
	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("LLL", "LLL", "LLL")
			.addInput('L', Item.chainlink)
			.create("chainLinkToLayer", layerChainmail.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("LSL", "LBL", "LLL")
			.addInput('L', Block.blockCharcoal)
			.addInput('B', Item.bucket)
			.addInput('S', Block.stone)
			.create("toCrucible", itemCrucible.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("CCC", "CMC", "LLL")
			.addInput('C', "minecraft:cobblestones")
			.addInput('M', layerChainmail)
			.addInput('L', "minecraft:logs")
			.create("toTrommel", Block.trommelIdle.getDefaultStack());

		RecipeBuilder.Shapeless(MOD_ID)
			.addInput(layerChainmail)
			.create("chainLayerToLink", new ItemStack(Item.chainlink, 9));
//		RecipeBuilder.Shapeless(MOD_ID)
//			.addInput(Block.blockCharcoal)
//			.addInput(Item.bucket)
//			.addInput(Block.cobbleStone)
//			.create("toLavaBucket", Item.bucketLava.getDefaultStack());
		RecipeBuilder.Shapeless(MOD_ID)
			.addInput(Block.cobbleStone)
			.addInput(Block.ice)
			.addInput(Block.ice)
			.addInput(Block.cobbleStone)
			.create("toCobblePerma", new ItemStack(Block.cobblePermafrost, 4));
		RecipeBuilder.Shapeless(MOD_ID)
			.addInput(Block.mud)
			.addInput(Block.gravel)
			.addInput(Block.gravel)
			.addInput(Block.mud)
			.create("toDirt", new ItemStack(Block.dirt, 4));
		RecipeBuilder.Shapeless(MOD_ID)
			.addInput(Block.sand)
			.addInput(Block.mud)
			.addInput(Item.bone)
			.create("toSoulSand", new ItemStack(Block.soulsand, 2));

		RecipeBuilder.Furnace(MOD_ID)
			.setInput(layerChainmail)
			.create("furnChainToIron", Item.ingotIron.getDefaultStack());
		RecipeBuilder.Furnace(MOD_ID)
			.setInput(Block.dirt)
			.create("furnDirtToScorched", Block.dirtScorched.getDefaultStack());

		RecipeBuilder.BlastFurnace(MOD_ID)
			.setInput(itemCrucible)
			.create("blastCrucibleToLava", Item.bucketLava.getDefaultStack());

		RecipeBuilder.Trommel(MOD_ID)
			.setInput(Block.cobbleStone)
			.addEntry(new WeightedRandomLootObject(Item.ammoPebble.getDefaultStack(), 1, 5),10)
			.addEntry(new WeightedRandomLootObject(Item.flint.getDefaultStack(), 1), 5)
			.create("trommelCobble");
		RecipeBuilder.Trommel(MOD_ID)
			.setInput(Block.dirtScorched)
			.addEntry(new WeightedRandomLootObject(Item.ammoPebble.getDefaultStack(), 1, 5),10)
			.addEntry(new WeightedRandomLootObject(Item.flint.getDefaultStack(), 1), 5)
			.addEntry(new WeightedRandomLootObject(Block.sand.getDefaultStack(), 1,3), 50)
			.addEntry(new WeightedRandomLootObject(Item.dustRedstone.getDefaultStack(), 1), 3)
			.create("trommelScorch");

		RecipeBuilder.ModifyTrommel("minecraft", "soul_sand")
			.addEntry(new WeightedRandomLootObject(Item.dustGlowstone.getDefaultStack(), 3), 1)
			.addEntry(new WeightedRandomLootObject(Item.nethercoal.getDefaultStack(), 3), 1);

	}
}
