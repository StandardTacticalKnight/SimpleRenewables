package standardtacticalknight.simplerenewables.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.logic.PistonDirections;
import net.minecraft.core.block.piston.BlockPistonBase;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import standardtacticalknight.simplerenewables.SimpleRenewables;

@Mixin(BlockPistonBase.class)
public class SimpleRenewablesCarbonToDiamondMixin {

	@Unique
	private static final int AIR = 0;

	@Inject(method = "canPushLine", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;playSoundEffect(Lnet/minecraft/core/entity/player/EntityPlayer;IIIII)V"), remap = false, cancellable = true)
	private static void injected(World world, int x, int y, int z, int direction, CallbackInfoReturnable<Boolean> cir) {
		int xo = x + PistonDirections.xOffset[direction % 6];
		int yo = y + PistonDirections.yOffset[direction % 6];
		int zo = z + PistonDirections.zOffset[direction % 6];
		int x1 =xo;
		int y1 =yo;
		int z1 =zo;
		xo += PistonDirections.xOffset[direction % 6];
		yo += PistonDirections.yOffset[direction % 6];
		zo += PistonDirections.zOffset[direction % 6];

		int idBedrock = world.getBlockId(xo, yo, zo); //backstop block
		int idBlock = world.getBlockId(x1, y1, z1); //block to be broken
		float randNum = world.rand.nextFloat();
		Item dropItem; //item to drop
		float pitch = 4.0f; //explosion pitch

		if (idBedrock == Block.bedrock.id && idBlock == SimpleRenewables.blockCompressedCarbon.id) {
			world.dropItem(x1,y1,z1,new ItemStack(Item.coal,8));
			dropItem = Item.diamond;
			pitch = 1.0f;
		}
		else if(randNum < 0.1f && idBedrock == Block.bedrock.id && idBlock == Block.cobbleStone.id){
			dropItem = Block.gravel.asItem();
		}
		else if(randNum < 0.2f &&idBlock == Block.gravel.id){
			dropItem = Block.sand.asItem();
		}
		else if(randNum < 0.05f &&idBlock == Block.sand.id){
			dropItem = Block.blockClay.asItem();
		}else{
			return;
		}
		world.playSoundEffect((Entity)null, SoundCategory.WORLD_SOUNDS, x, y, z, "random.explode", 2.0f/pitch, (pitch + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8f) * 0.8f);
		world.dropItem(x1,y1,z1,new ItemStack(dropItem));
		world.setBlockWithNotify(x1,y1,z1, AIR);
		cir.setReturnValue(false);
	}
}
