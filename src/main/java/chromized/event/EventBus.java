package chromized.event;

import com.google.common.reflect.TypeToken;
import net.minecraft.client.Minecraft;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("UnstableApiUsage")
public class EventBus {
    public static final EventBus INSTANCE = new EventBus();
    public static boolean ALLOW_PROFILE = false;
    private HashMap<Class<?>, CopyOnWriteArrayList<EventSubscriber>> subscriptions = new HashMap<>();

    public void register(Object obj) {
        TypeToken<?> token = TypeToken.of(obj.getClass());

        Set superClasses = token.getTypes().rawTypes();
        for (Object temp : superClasses) {
            Class<?> clazz = (Class<?>) temp;

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getAnnotation(InvokeEvent.class) == null) {
                    continue;
                }

                if (method.getParameters()[0] == null) {
                    throw new IllegalArgumentException("Couldn't find parameter inside of " + method.getName() + "!");
                }

                Class<?> event = method.getParameters()[0].getType();

                Priority priority = method.getAnnotation(InvokeEvent.class).priority();
                method.setAccessible(true);

                if (this.subscriptions.containsKey(event)) {
                    this.subscriptions.get(event).add(new EventSubscriber(obj, method, priority));
                    this.subscriptions.get(event).sort(Comparator.comparingInt(a -> a.getPriority().value));
                } else {
                    this.subscriptions.put(event, new CopyOnWriteArrayList<>());
                    this.subscriptions.get(event).add(new EventSubscriber(obj, method, priority));
                    this.subscriptions.get(event).sort(Comparator.comparingInt(a -> a.getPriority().value));
                }
            }
        }
    }

    public void unregister(Object obj) {
        this.subscriptions.values().forEach(map -> map.removeIf(it -> it.getInstance() == obj));
    }

    public void unregister(Class<?> clazz) {
        this.subscriptions.values().forEach(map -> map.removeIf(it -> it.getInstance().getClass() == clazz));
    }

    public void post(Object event) {
        if (event == null) {
            return;
        }
        if (event instanceof RenderTickEvent)
            ALLOW_PROFILE = false;
        boolean profile = Minecraft.getMinecraft().isCallingFromMinecraftThread() && Minecraft.getMinecraft().theWorld != null && ALLOW_PROFILE;
        if (profile) {
            Minecraft.getMinecraft().mcProfiler.startSection(event.getClass().getSimpleName());
        }
        this.subscriptions.getOrDefault(event.getClass(), new CopyOnWriteArrayList<>()).forEach((sub) -> {
            if (profile) {
                String name = sub.getObjName();
                Minecraft.getMinecraft().mcProfiler.startSection(name);
                Minecraft.getMinecraft().mcProfiler.startSection(sub.getMethodName());
            }
            try {
                sub.getMethod().invoke(sub.getInstance(), event);
            } catch (Exception e) {
                if (e instanceof InvocationTargetException) {
                    ((InvocationTargetException) e).getTargetException().printStackTrace();
                }
                e.printStackTrace();
            }
            if (profile) {
                Minecraft.getMinecraft().mcProfiler.endSection();
                Minecraft.getMinecraft().mcProfiler.endSection();
            }
        });
        if (profile)
            Minecraft.getMinecraft().mcProfiler.endSection();
    }
}