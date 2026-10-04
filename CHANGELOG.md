# Changelog

## 1.1.0 — 2026-10-04

- Add vanilla creeper-style white flashing to the full player skin and its outer layers.
- Add matching white flashing to first-person arms and sleeves.
- Synchronize the fuse using vanilla entity metadata, including the owning client and tracking players.
- Increase the fuse from 30 to 60 ticks (1.5 to 3 seconds at 20 TPS).
- Reduce the escape threshold from 7 to 6 blocks; breaking line of sight also defuses.
- Make creepers flee from nearby Survival/Adventure players instead of chasing them.
- Fix the Forge resource-pack warning by providing version-specific `pack.mcmeta` files.
- Add tests for the flash curve, synchronized fuse reset, and actual escape-path creation.

Update the server and all clients together. Remove the 1.0.0 JAR before installing 1.1.0.

## 1.0.0 — 2026-10-04

Initial release of **Creeper Reversed**.

- Reverse the creeper encounter: nearby Survival/Adventure players hiss and explode; creepers never detonate.
- Add a vanilla-style 30-tick fuse, a 3-block trigger radius, a 7-block escape threshold, and line-of-sight checks.
- Add smoke particles at the player's position and the original creeper priming sound.
- Double explosion power around charged creepers.
- Respect `mobGriefing` for terrain damage and normal player death/inventory rules.
- Exempt Creative and Spectator players.
- Support Fabric and Forge on Minecraft 1.20.1, plus Fabric and NeoForge on Minecraft 1.21.1.
- Include original project artwork, English documentation, MIT licensing, CI builds, and automated gameplay checks.

No Fabric API dependency. Install the matching JAR on the server and clients. The player uses smoke as a warning; swelling/flashing animation and lingering potion clouds are not included.
