package buttondevteam.lib.player;

import org.bukkit.configuration.file.YamlConfiguration;

public class EnumPlayerData<T extends Enum<T>> {
	private final PlayerData<String> data;
	private final Class<T> cl;
	private final T def;

	public EnumPlayerData(String name, YamlConfiguration yaml, Class<T> cl, T def) {
		data = new PlayerData<String>(name, yaml, "");
		this.cl = cl;
		this.def = def;
	}

	public T get() {
		String str = data.get();
		if (str.isEmpty())
			return def;
		return Enum.valueOf(cl, str);
	}

	public void set(T value) {
		data.set(value.toString());
	}
}
