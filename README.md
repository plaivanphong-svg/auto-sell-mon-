# AutoSell Fabric client mod
Target: Minecraft 1.21.11, Fabric Loader 0.20.1, Java 21.

- Right Shift opens the Allowed Items screen.
- Click item icons to whitelist/unwhitelist them.
- Search field filters by item name or registry ID.
- Config stored at `config/autosell.properties`.

**Server command behavior:** the configured `/sell` command is sent when any selected item exists in inventory. If your server's `/sell` sells the entire inventory, whitelist detection does not guarantee only whitelisted items are sold; the server's command semantics determine what is actually sold. Use this only on servers where selling is permitted.

Build: `gradle build` (or use Gradle 9 compatible installation; Fabric Loom downloads dependencies).
