package standardtacticalknight.simplerenewables.world;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;

public class SimpleRenewablesChunkDecoratorSkyblock implements ChunkDecorator {
	private final World world;

	public SimpleRenewablesChunkDecoratorSkyblock(World world) {
		this.world = world;
	}
	public void decorate(Chunk chunk) {
		if (chunk.xPosition == 0 && chunk.zPosition == 0) {
			//dirt platform
			for (int i=0; i<5; i++) {
				for (int j=10; j<13; j++) {
					for (int k=0; k<5; k++) {
						chunk.setBlockID(i, j, k, Block.dirt.id);
					}
				}
			}
			//lone tree
			WorldFeatureTree tree = new WorldFeatureTree(Block.leavesOak.id, Block.logOak.id, 4);
			tree.generate(world,world.rand,2,13,0);
			//courtesy sapling
			chunk.setBlockID(2, 13, 4, Block.saplingOak.id);
			//starter chest
			chunk.setBlockID(3, 13, 3, Block.chestPlanksOak.id);
			TileEntityChest tileentitychest = new TileEntityChest();
			chunk.setTileEntity(3, 13, 3, tileentitychest);
			tileentitychest.setInventorySlotContents(12, new ItemStack(Block.ice));
			tileentitychest.setInventorySlotContents(13, new ItemStack(Item.bucketLava));
			tileentitychest.setInventorySlotContents(14, new ItemStack(Block.ice));
			//foundation
			chunk.setBlockID(2, 10, 2, Block.bedrock.id);
		}
	}
}
