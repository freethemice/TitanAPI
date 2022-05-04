package com.firesoftitan.play.titanbox.api.runnables;

import com.firesoftitan.play.titanbox.api.managers.TaskManager;
import com.firesoftitan.play.titanbox.libs.runnables.TitanSaverRunnable;
import org.bukkit.plugin.java.JavaPlugin;

public class SaverRunnable  extends TitanSaverRunnable {

    public SaverRunnable(JavaPlugin plugin) {
        super(plugin);
    }
    @Override
    public void run() {
        TaskManager.instance.saveALL();
    }

}
