package standardtacticalknight.simplerenewables;

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


public class SimpleRenewables implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "simplerenewables";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Block blockCompressedCarbon;
	public static Block layerChainmail;
	public static Block blockChainmail;
	public static Item itemCrucible;
    @Override
    public void onInitialize() {
        LOGGER.info("SimpleRenewables initialized");
    }

	@Override
	public void beforeGameStart() {
		int startingBlockId = 2700;
		int itemID = 18750;

		blockCompressedCarbon = new BlockBuilder(MOD_ID)
			.setTextures("compressedCarbon.png")
			.setHardness(0.1f)
			.build(new Block("compressedcarbon", startingBlockId++,Material.stone){
				@Override
				public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
					switch (dropCause) {
						case PICK_BLOCK:
                        case EXPLOSION:
                        case IMPROPER_TOOL: {
							return new ItemStack[]{new ItemStack(blockCompressedCarbon)};
						}
						//should only happen when pushed by piston
						case SILK_TOUCH:{
							return new ItemStack[]{new ItemStack(Item.coal, 8)};
						}
                    }
					return null;
				}
			});

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
		//Shaped recipes
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
		RecipeBuilder.Shaped(MOD_ID)
			.setShape(" C ", "CMC", " C ")
			.addInput('C', Block.blockCharcoal)
			.addInput('M', Block.blockNetherCoal)
			.create("toCarbonCompressed", blockCompressedCarbon.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("###", "#R#", "###")
			.addInput('#', Block.cobbleStone)
			.addInput('R', Item.dustRedstone)
			.create("ToNetherrack", Block.netherrack.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("###", "RRQ", "###")
			.addInput('#', Block.cobbleStoneMossy)
			.addInput('R', Item.dustRedstone)
			.addInput('Q', Item.quartz)
			.create("ToMotionsense", Block.motionsensorIdle.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("###", "# #", "###")
			.addInput('#', Item.ingotSteelCrude)
			.create("ToSpawnerDeact", Block.mobspawnerDeactivated.getDefaultStack());

		//Chain armor recipes
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("LLL", "L L", "   ")
			.addInput('L', layerChainmail)
			.create("LayerToMailHelm", Item.armorHelmetChainmail.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("L L", "LLL", "LLL")
			.addInput('L', layerChainmail)
			.create("LayerToMailChes", Item.armorChestplateChainmail.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("LLL", "L L", "L L")
			.addInput('L', layerChainmail)
			.create("LayerToMailLegg", Item.armorLeggingsChainmail.getDefaultStack());
		RecipeBuilder.Shaped(MOD_ID)
			.setShape("   ", "L L", "L L")
			.addInput('L', layerChainmail)
			.create("LayerToMailBoot", Item.armorBootsChainmail.getDefaultStack());

		//Shapeless recipes
		RecipeBuilder.Shapeless(MOD_ID)
			.addInput(layerChainmail)
			.create("chainLayerToLink", new ItemStack(Item.chainlink, 9));
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

		//Furnace recipes
		RecipeBuilder.Furnace(MOD_ID)
			.setInput(layerChainmail)
			.create("furnChainToIron", Item.ingotIron.getDefaultStack());
		RecipeBuilder.Furnace(MOD_ID)
			.setInput(Block.dirt)
			.create("furnDirtToScorched", Block.dirtScorched.getDefaultStack());

		RecipeBuilder.BlastFurnace(MOD_ID)
			.setInput(itemCrucible)
			.create("blastCrucibleToLava", Item.bucketLava.getDefaultStack());

		//Trommel recipes
		RecipeBuilder.Trommel(MOD_ID)
			.setInput(Block.cobbleStone)
			.addEntry(new WeightedRandomLootObject(Item.ammoPebble.getDefaultStack(), 1, 5),10)
			.addEntry(new WeightedRandomLootObject(Item.flint.getDefaultStack(), 1), 5)
			.create("trommelCobble");
		RecipeBuilder.Trommel(MOD_ID)
			.setInput(Block.dirtScorched)
			.addEntry(new WeightedRandomLootObject(Item.ammoPebble.getDefaultStack(), 1, 5),10)
			.addEntry(new WeightedRandomLootObject(Item.flint.getDefaultStack(), 1), 3)
			.addEntry(new WeightedRandomLootObject(Block.sand.getDefaultStack(), 1,3), 50)
			.addEntry(new WeightedRandomLootObject(Item.dustRedstone.getDefaultStack(), 1,2), 7)
			.addEntry(new WeightedRandomLootObject(Block.spinifex.getDefaultStack(), 1), 1)
			.addEntry(new WeightedRandomLootObject(Block.deadbush.getDefaultStack(), 1), 1)
			.create("trommelScorch");
		RecipeBuilder.Trommel(MOD_ID)
			.setInput(Block.grass)
			.addEntry(new WeightedRandomLootObject(Item.seedsPumpkin.getDefaultStack(), 1),1)
			.addEntry(new WeightedRandomLootObject(Block.cactus.getDefaultStack(), 1),1)
			.addEntry(new WeightedRandomLootObject(Block.mushroomBrown.getDefaultStack(), 1),1)
			.addEntry(new WeightedRandomLootObject(Block.mushroomRed.getDefaultStack(), 1),1)
			.addEntry(new WeightedRandomLootObject(Block.saplingBirch.getDefaultStack(), 1),1)
			.addEntry(new WeightedRandomLootObject(Block.saplingCacao.getDefaultStack(), 1),1)
			.addEntry(new WeightedRandomLootObject(Block.saplingCherry.getDefaultStack(), 1),1)
			.addEntry(new WeightedRandomLootObject(Block.saplingOak.getDefaultStack(), 1),1)
			.addEntry(new WeightedRandomLootObject(Block.saplingEucalyptus.getDefaultStack(), 1),1)
			.addEntry(new WeightedRandomLootObject(Block.saplingOakRetro.getDefaultStack(), 1),1)
			.addEntry(new WeightedRandomLootObject(Block.saplingPine.getDefaultStack(), 1),1)
			.addEntry(new WeightedRandomLootObject(Block.saplingShrub.getDefaultStack(), 1),1)
			.addEntry(new WeightedRandomLootObject(Block.dirt.getDefaultStack(), 1),100)
			.create("trommelGrass");
		RecipeBuilder.ModifyTrommel("minecraft", "soul_sand")
			.addEntry(new WeightedRandomLootObject(Item.dustGlowstone.getDefaultStack(), 3), 1)
			.addEntry(new WeightedRandomLootObject(Item.nethercoal.getDefaultStack(), 3), 1);
	}
}
