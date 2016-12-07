package buttondevteam.lib;

public abstract class TBMCPlayerBase {
	/**
	 * This method returns the filename for this player data. For example, for Minecraft-related data, use MC UUIDs, for Discord data, use Discord IDs, etc.
	 */
	public abstract String getFileName();

	/**
	 * This method returns the folder the file is in. For example, for Minecraft data, this should be "minecraft", for Discord, "discord", etc.
	 */
	public abstract String getFolder();
	
	
}