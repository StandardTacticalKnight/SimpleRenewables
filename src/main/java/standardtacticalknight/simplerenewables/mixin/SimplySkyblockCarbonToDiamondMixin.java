package standardtacticalknight.simplerenewables.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.logic.PistonDirections;
import net.minecraft.core.block.piston.BlockPistonBase;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundType;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import standardtacticalknight.simplerenewables.SimpleRenewables;

@Mixin(BlockPistonBase.class)
public class SimplySkyblockCarbonToDiamondMixin {
	@Inject(method = "canPushLine", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;playSoundEffectForPlayer(Lnet/minecraft/core/entity/player/EntityPlayer;IIIII)V"), remap = false)
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
		int idBedrock = world.getBlockId(xo, yo, zo);
		int idBlock = world.getBlockId(x1, y1, z1);
		if (idBedrock == Block.bedrock.id && idBlock == SimpleRenewables.blockCompressedCarbon.id) {
			world.playSoundEffect(SoundType.WORLD_SOUNDS, x, y, z, "random.explode", 4.0f, (1.0f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2f) * 0.2f);
			world.dropItem(x1,y1,z1,new ItemStack(Item.diamond));
		}
	}
}
