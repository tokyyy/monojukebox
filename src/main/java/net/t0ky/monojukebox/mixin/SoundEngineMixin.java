package net.t0ky.monojukebox.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.t0ky.monojukebox.JukeboxConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {

    @Shadow
    @Final
    private Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onSoundEngineTick(boolean paused, CallbackInfo ci) {
        if (paused) return;

        Minecraft client = Minecraft.getInstance();
        if (client.gameRenderer == null || client.gameRenderer.mainCamera() == null) return;

        Camera camera = client.gameRenderer.mainCamera();
        Vec3 cameraPos = camera.position();

        Vec3 lookVec = Vec3.directionFromRotation(camera.xRot(), camera.yRot()).normalize();

        this.instanceToChannel.forEach((sound, channelHandle) -> {
            if (sound != null && sound.getSource() == SoundSource.RECORDS) {
                Vec3 realPos = new Vec3(sound.getX(), sound.getY(), sound.getZ());
                double distance = cameraPos.distanceTo(realPos);

                if (distance > 0.001) {
                    Vec3 monoPos = cameraPos.add(lookVec.scale(distance));
                    Vec3 targetPos = realPos.lerp(monoPos, JukeboxConfig.blendFactor);

                    channelHandle.execute(source -> source.setSelfPosition(targetPos));
                }
            }
        });
    }
}
