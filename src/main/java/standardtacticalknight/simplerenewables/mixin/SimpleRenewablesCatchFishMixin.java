package standardtacticalknight.simplerenewables.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.EntityBobber;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Random;

@Mixin(EntityBobber.class)
public class SimpleRenewablesCatchFishMixin {
	@Unique
	private final Random random = new Random();
	@ModifyArg(method = "yoink()I", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/EntityItem;<init>(Lnet/minecraft/core/world/World;DDDLnet/minecraft/core/item/ItemStack;)V"), index = 4,remap = false)
	private ItemStack adjustYCoord(ItemStack itemstack) {
		float f = random.nextFloat();
		if(f<0.005){//.5%
			return new ItemStack(Item.armorBootsIceskates);
		}else if(f<0.01) {//.5%
			return new ItemStack(Item.armorQuiverGold);
		}else if(f<0.02) {//1%
			return new ItemStack(Item.handcannonUnloaded);
		}else if(f<0.03) {//1%
			return new ItemStack(Item.saddle);
		}else if(f<0.1){//7%
			return new ItemStack(Block.spongeWet);
		}else if(f<0.25){//15%
			return new ItemStack(Item.sugarcane);
		}else if(f<0.4){//15%
			return new ItemStack(Block.algae);
		}else {//60%
			return new ItemStack(Item.foodFishRaw);
		}
    }
}
