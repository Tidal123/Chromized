package chromized.launch;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChromizedTweaker implements ITweaker {
    public static ChromizedTweaker INSTANCE;

    private ArrayList<String> args = new ArrayList<>();

    public ChromizedTweaker() {
        INSTANCE = this;
    }

    @Override
    public void acceptOptions(List<String> args, File gameDir, final File assetsDir, String profile) {
        this.args.addAll(args);

        addArg("gameDir", gameDir);
        addArg("assetsDir", assetsDir);
        addArg("version", profile);
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        // ---
    }

    @Override
    public String[] getLaunchArguments() {
        return null;
    }

    private void addArg(String label, String value) {
        if(!this.args.contains(("--" + label)) && value != null) {
            this.args.add(("--" + label));
            this.args.add(value);
        }
    }

    private void addArg(String args, File file) {
        if(file == null) {
            return;
        }

        addArg(args, file.getAbsoluteFile());
    }

    private void addArg(Map<String, ?> args) {
        args.forEach((label, value) -> {
            if(value instanceof String) {
                addArg(label, (String) value);
            } else if(value instanceof File) {
                addArg(label, (File) value);
            }
        });
    }
}
