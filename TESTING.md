# Validation

The release targets are Fabric 1.20.1, Forge 1.20.1, Fabric 1.21.1, and NeoForge 1.21.1.

## Automated checks

- Compile against each Minecraft version and loader.
- Run fuse unit checks: explosion exactly on tick 60, gradual defusing, zero clamp, state reset, and the vanilla white-overlay curve.
- Launch a dedicated development server with actual Minecraft classes and the mod mixins.
- In an isolated test world, instantiate a test player and creeper and verify: survival through tick 59, death on tick 60, escape and full defusing, Creative and Spectator immunity, and a creeper surviving 40 ticks after forced ignition.
- Verify synchronized fuse metadata and reset on detonation/escape.
- Verify removal of chase/prime goals and creation of a walkable flee path away from a nearby player.
- Rebuild release JARs with the integration harness disabled and check their contents, metadata, and refmaps.

The integration harness uses a lightweight `Player` subclass. All four targets pass these server checks. A separate Forge 1.20.1 client connected to a local test server was used to capture and inspect white and normal phases in first- and third-person views. This verifies real client rendering and receipt of server fuse metadata. Visual checks with multiple connected players, armor mods, and alternate rendering mods have not been performed.

The optional Forge-only `-PvisualTest` harness uses the `visual/` source set. It stages a disposable scene on a loopback server at port 25581, connects a development client, and saves four PNG captures in its run directory. Never enable this against a real world. Both `-PvisualTest` and `-PintegrationTest` must be absent from release builds.

## Run the integration harness

Use a disposable development world. The harness spawns entities, causes an explosion, changes `mobGriefing`, and stops the server when finished. Never run it against a real world.

1. Configure `platforms/<target>/run/server.properties` with `server-ip=127.0.0.1` and an unused port.
2. Read and accept Minecraft's EULA yourself where the loader requires it.
3. Run `./gradlew runServer -Ptarget=<target> -PintegrationTest`.
4. Check `platforms/<target>/run/integration-result.txt` for `PASS`. A Gradle success alone is insufficient because the harness writes failures before stopping the server.
5. Run `./gradlew clean build -Ptarget=<target>` before distributing. Never distribute a JAR built with `-PintegrationTest`.

## Manual playtest checklist

- Confirm the hiss and smoke originate from the player.
- Check first-person and third-person views with Steve and a custom skin.
- Verify normal and charged explosion damage and block destruction.
- Verify `mobGriefing=false` preserves terrain and `keepInventory=true` preserves inventory.
- Check obstruction, death/respawn, disconnect/reconnect, and dimension travel during the fuse.
- Connect two players and check independent fuses, sound delivery, and particles.
- Check compatibility with any additional mods in the intended modpack.
