# Validation

The release targets are Fabric 1.20.1, Forge 1.20.1, Fabric 1.21.1, and NeoForge 1.21.1.

## Automated checks

- Compile against each Minecraft version and loader.
- Run fuse unit checks: explosion exactly on tick 30, gradual defusing, zero clamp, and state reset.
- Launch a dedicated development server with actual Minecraft classes and the mod mixins.
- In an isolated test world, instantiate a test player and creeper and verify: survival through tick 29, death on tick 30, escape and full defusing, Creative and Spectator immunity, and a creeper surviving 40 ticks after forced ignition.
- Rebuild release JARs with the integration harness disabled and check their contents, metadata, and refmaps.

The integration harness uses a lightweight `Player` subclass. It validates server gameplay code, not a connected client's rendering, audio output, or network behavior. A visual multiplayer playthrough has not been performed.

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
