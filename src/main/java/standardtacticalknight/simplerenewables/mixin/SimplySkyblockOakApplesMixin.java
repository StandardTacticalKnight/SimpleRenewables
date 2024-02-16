package standardtacticalknight.simplerenewables.mixin;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLeavesBase;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLeavesBase.class)
public class  SimplySkyblockOakApplesMixin {
	@Inject(method = "getBreakResult", at = @At("TAIL"),remap = false, cancellable = true)
	private void addApples(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity, CallbackInfoReturnable<ItemStack[]> cir) {
		if(world.rand.nextFloat()<0.01){//1% chance to replace sapling drop
			int id = cir.getReturnValue()[0].itemID;
			if(id == Block.saplingOak.id || id == Block.saplingOakRetro.id){
				//SimpleRenewables.LOGGER.info("apple dropped");
				cir.setReturnValue(new ItemStack[]{new ItemStack(Item.foodApple, 1)});
			}
		}
	}
}
