package io.github.magersers.creeperreversed.visual.mixin;

import io.github.magersers.creeperreversed.FuseData;
import io.github.magersers.creeperreversed.FuseFlash;
import net.minecraft.client.Minecraft;
import net.minecraft.client.CameraType;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.nio.file.Path;

@Mixin(Minecraft.class)
public abstract class VisualClientMixin {
    @Unique private boolean creeperReversed$connected;
    @Unique private int creeperReversed$stage;
    @Inject(method = "tick", at = @At("TAIL"))
    private void creeperReversed$connect(CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;
        if (!creeperReversed$connected && mc.screen instanceof TitleScreen) {
            creeperReversed$connected = true;
            ConnectScreen.startConnecting(mc.screen, mc, ServerAddress.parseString("127.0.0.1:25581"),
                    new ServerData("Creeper Reversed visual test", "127.0.0.1:25581", false), false);
        }
    }
    @Inject(method = "runTick", at = @At("TAIL"))
    private void creeperReversed$capture(boolean render, CallbackInfo ci) throws Exception {
        Minecraft mc = (Minecraft) (Object) this;
        if (mc.player == null || mc.level == null || mc.screen != null) return;
        int ticks = mc.player.getEntityData().get(FuseData.TICKS);
        boolean white = FuseFlash.whiteOverlay(ticks) > 0;
        String image = null;
        if (creeperReversed$stage == 0 && white) {
            image = "first-person-flash.png";
            mc.options.setCameraType(CameraType.THIRD_PERSON_FRONT);
            creeperReversed$stage++;
        } else if (creeperReversed$stage == 1 && white) {
            image = "third-person-flash.png";
            creeperReversed$stage++;
        } else if (creeperReversed$stage == 2 && !white && ticks > 12) {
            image = "third-person-normal.png";
            mc.options.setCameraType(CameraType.FIRST_PERSON);
            creeperReversed$stage++;
        } else if (creeperReversed$stage == 3 && !white && ticks > 12) {
            image = "first-person-normal.png";
            creeperReversed$stage++;
        }
        if (image != null) {
            try (var screenshot = Screenshot.takeScreenshot(mc.getMainRenderTarget())) {
                screenshot.writeToFile(Path.of(mc.gameDirectory.getAbsolutePath(), image));
            }
            System.out.println("CREEPER_REVERSED_CAPTURE " + image + " fuse=" + ticks);
        }
        if (creeperReversed$stage == 4 && ticks > 30) mc.stop();
    }
}
