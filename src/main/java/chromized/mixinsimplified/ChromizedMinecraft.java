package chromized.mixinsimplified;

import chromized.Chromized;
import chromized.event.EventBus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourcePack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

public class ChromizedMinecraft {
    private Minecraft parent;

    public ChromizedMinecraft(Minecraft parent) {
        this.parent = parent;
    }

    public void preinit(CallbackInfo ci, List<IResourcePack> defaultResourcePacks, DefaultResourcePack mcDefaultResourcePack, List<IResourcePack> resourcePacks) {
        EventBus.INSTANCE.register(Chromized.INSTANCE);
    }

    public void init() {

    }

    public void loop() {

    }
}
