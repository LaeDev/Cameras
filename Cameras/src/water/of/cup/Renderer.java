package water.of.cup;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Renderer extends MapRenderer {

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		if (map.isLocked()) {
			return;
		}
		Location eyes = player.getEyeLocation();
		double pitch = -Math.toRadians(player.getEyeLocation().getPitch());
		double yaw = Math.toRadians(player.getEyeLocation().getYaw() + 90);
		
		for (int x = 0; x < 128; x++) {
			for (int y = 0; y < 128; y++) {
				
				double yrotate = -((y) * .9 / 128 - .45);
				double xrotate = ((x) * .9 / 128 - .45);
				
				RayTraceResult result = player.getWorld().rayTraceBlocks(eyes, new Vector(Math.cos(yaw + xrotate)*Math.cos(pitch + yrotate), Math.sin(pitch + yrotate), Math.sin(yaw + xrotate)*Math.cos(pitch + yrotate)), 256);
				//Bukkit.getLogger().info("Pitch: " + pitch + ", Yaw: " + yaw);
				if (result != null) {
					canvas.setPixel(x, y, Utils.colorFromType(result.getHitBlock().getType(), result.getHitBlock().getLightFromBlocks() + result.getHitBlock().getLightFromSky()));
				} else {
					canvas.setPixel(x, y, MapPalette.PALE_BLUE);
				}
			}
		}

		// TODO: Make async
		byte[][] byteArr = toBytes(canvas);
		MapStorage.store(map.getId(), byteArr);

		map.setLocked(true);
	}

	private static byte[][] toBytes(MapCanvas canvas) {
		byte[][] bytes = new byte[128][128]; // According to doc map size is 128x128
		for (int i = 0; i < bytes.length; i++) {
			for (int j = 0; j < bytes[i].length; j++) {
				bytes[i][j] = canvas.getPixel(i, j);
			}
		}
		return bytes;
	}
}