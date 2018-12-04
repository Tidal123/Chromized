package chromized.mixins;

import chromized.mixinsimplified.ChromizedMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow
    private static Minecraft theMinecraft;
    @Shadow
    @Final
    private DefaultResourcePack mcDefaultResourcePack;
    @Shadow
    @Final
    private List<IResourcePack> defaultResourcePack;
    @Shadow
    @Final
    private List<IResourcePack> defaultResourcePacks;
    @Shadow
    private boolean enableGLErrorChecking;
    private ChromizedMinecraft chromizedMinecraft = new ChromizedMinecraft((Minecraft) (Object) this);

    @Inject(method = "startGame", at = @At("HEAD"))
    private void preinit(CallbackInfo ci) {
        chromizedMinecraft.preinit(ci, defaultResourcePacks, mcDefaultResourcePack, defaultResourcePack);
    }

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    private void loop(CallbackInfo info) {
        chromizedMinecraft.loop();
    }

    @Inject(method = "startGame", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        chromizedMinecraft.init();
    }
}
