[![Fabric](https://img.shields.io/badge/Mod_Loader-Fabric-blue)](https://fabricmc.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.18.2-green)](https://www.minecraft.net/)

# Remote PC Mod. Run Any OS inside Minecraft!

![Screenshot](pre-pro/screenshot.png)

**Warning:** currently in alpha.

This mod allows you to run any machine using VNC.

## How to run

Currently, in it's alpha status this is the only way to run it right now:

 - Prepare `MINECRAFT_DIR/config/pcmod/confog.properties`
```properties
host=vncserver
port=5900
username=you
password=password
```
 - Get yourself a screen (you could use `/give <username> pcmod:flatscreen`)
 - Right click to place it, and then right click on it again to open the screen and interact with it.
 - mac un-focus: L ctrl + L alt + delete (fn + delete)
 - re-itemize: sneak + Left click

## Special thanks

Huge shoutout to the devs at [MCVmComputers](https://github.com/Delta2Force/MCVmComputers) for making their mod. My mod reused a lot of their code to make this work.

## References

 * https://support.apple.com/guide/remote-desktop/virtual-network-computing-access-and-control-apde0dd523e/mac

## TODO

 * ~~keyboard, mouse doesn't work well~~
   * keyboard: return key doesn't work
   * mouse: not smooth, untraceable cursor movement
 * make un-focus keys properties
 * make color depth a property