package standardtacticalknight.simplerenewables.world;

import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;

public class SimpleRenewablesChunkGeneratorSkyblock extends ChunkGenerator {
	public SimpleRenewablesChunkGeneratorSkyblock(World world) {
		super(world, new SimpleRenewablesChunkDecoratorSkyblock(world));
	}
	@Override
	protected ChunkGeneratorResult doBlockGeneration(Chunk chunk) {
		return new ChunkGeneratorResult();
	}
}

