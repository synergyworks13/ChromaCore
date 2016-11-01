package buttondevteam.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class TBMCCoreAPI {
	/**
	 * Updates or installs the specified plugin. The plugin must use Maven.
	 * 
	 * @param name
	 *            The plugin's repository name.
	 * @return Error message or empty string
	 */
	public static String UpdatePlugin(String name) {
		List<String> plugins = GetPluginNames();
		String correctname = null;
		for (String plugin : plugins) {
			if (plugin.equalsIgnoreCase(name)) {
				correctname = plugin; // Fixes capitalization
				break;
			}
		}
		if (correctname == null) {
			Bukkit.getLogger().warning("There was an error while updating TBMC plugin: " + name);
			return "Can't find plugin: " + name;
		}
		Bukkit.getLogger().info("Updating TBMC plugin: " + correctname);
		String ret = "";
		URL url;
		try {
			url = new URL("https://jitpack.io/com/github/TBMCPlugins/" + correctname + "/master-SNAPSHOT/" + correctname
					+ "-master-SNAPSHOT.jar"); // ButtonLib exception not required, not a separate plugin
			FileUtils.copyURLToFile(url, new File("plugins/" + correctname + ".jar"));
		} catch (FileNotFoundException e) {
			ret = "Can't find JAR, the build probably failed. Build log (scroll to bottom):\nhttps://jitpack.io/com/github/TBMCPlugins/"
					+ correctname + "/master-SNAPSHOT/build.log";
		} catch (IOException e) {
			ret = "IO error!\n" + e.getMessage();
		} catch (Exception e) {
			Bukkit.getLogger().warning("Error!\n" + e);
			ret = e.toString();
		}
		return ret;
	}

	/**
	 * Retrieves all the repository names from the GitHub organization.
	 * 
	 * @return A list of names
	 */
	public static List<String> GetPluginNames() {
		List<String> ret = new ArrayList<>();
		try {
			String resp = DownloadString("https://api.github.com/orgs/TBMCPlugins/repos");
			JsonArray arr = new JsonParser().parse(resp).getAsJsonArray();
			for (JsonElement obj : arr) {
				JsonObject jobj = obj.getAsJsonObject();
				ret.add(jobj.get("name").getAsString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String DownloadString(String urlstr) throws MalformedURLException, IOException {
		URL url = new URL(urlstr);
		URLConnection con = url.openConnection();
		con.setRequestProperty("User-Agent", "TBMCPlugins");
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		in.close();
		return body;
	}

	public static void SendException(String sourcemsg, Exception e) {
		Bukkit.getPluginManager().callEvent(new TBMCExceptionEvent(sourcemsg, e));
		Bukkit.getLogger().warning(sourcemsg);
		e.printStackTrace();
	}
}
