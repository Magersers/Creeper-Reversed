# Changelog

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
