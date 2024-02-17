package standardtacticalknight.simplerenewables.world;

import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.biome.provider.BiomeProviderOverworld;
import net.minecraft.core.world.config.season.SeasonConfig;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.season.Seasons;
import net.minecraft.core.world.type.WorldTypeOverworld;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.core.world.wind.WindManagerGeneric;

public class SimpleRenewablesWorldTypeSkyblock extends WorldTypeOverworld {

	public SimpleRenewablesWorldTypeSkyblock(String languageKey) {
		super(languageKey, Weather.overworldClear, new WindManagerGeneric(), SeasonConfig.builder().withSeasonInCycle(Seasons.OVERWORLD_SPRING, 7).withSeasonInCycle(Seasons.OVERWORLD_SUMMER, 7).withSeasonInCycle(Seasons.OVERWORLD_FALL, 7).withSeasonInCycle(Seasons.OVERWORLD_WINTER, 7).build());
	}
	@Override
	public int getMinY() {
		return 0;
	}

	@Override
	public int getMaxY() {
		return 255;
	}

	@Override
	public int getOceanY() {
		return 0;
	}

	@Override
	public int getOceanBlock() {
		return 0;
	}

	@Override
	public int getFillerBlock() {
		return 0;
	}

	@Override
	public BiomeProvider createBiomeProvider(World world) {
		return new BiomeProviderOverworld(world.getRandomSeed(), this);
	}

	@Override
	public ChunkGenerator createChunkGenerator(World world) {
		return new SimpleRenewablesChunkGeneratorSkyblock(world);
	}

	@Override
	public boolean isValidSpawn(World world, int x, int y, int z) {
		return true;
	}

	@Override
	public void getInitialSpawnLocation(World world) {
		world.getLevelData().setSpawn(3, 13, 3);
	}

	@Override
	public void getRespawnLocation(World world) {
	}
	@Override
	public float getCloudHeight() {
		return 108.0f;
	}
	@Override
	public boolean hasGround() {
		return false;
	}
}
