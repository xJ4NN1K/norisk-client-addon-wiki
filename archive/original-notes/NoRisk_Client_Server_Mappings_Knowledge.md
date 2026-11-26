# NoRisk Client -- Server Mappings (`nrc-server-mappings`)

## 1. Overview

The
[`nrc-server-mappings`](https://github.com/NoRiskClient/nrc-server-mappings)
repository contains the official server mappings used by **NoRisk
Client**.

Its purpose is to make supported Minecraft servers look more polished
and recognizable inside the client. A mapping can provide
server-specific information and visual assets, such as:

-   A custom server name
-   A server icon
-   A background image or banner
-   Server categories
-   Game mode information
-   Social and website links
-   Client-module configuration for that server

In short, the repository connects Minecraft server addresses with the
information and visual presentation that NoRisk Client can display.

The repository describes its purpose as providing official server
mappings for NoRisk Client and making servers look better in the client.

**Official repository:**
https://github.com/NoRiskClient/nrc-server-mappings

## 2. What Server Mappings Are -- and Are Not

A server mapping is a metadata definition for a Minecraft server. It
tells NoRisk Client how to identify and present that server.

Server mappings are not:

-   A Minecraft server host.
-   A tool for creating or managing Minecraft servers.
-   A replacement for a server's own plugins or configuration.
-   A general server-search engine or server discovery platform.
-   A server-side anti-cheat system.

The repository focuses on server presentation and client-side
configuration inside NoRisk Client.

## 3. Repository Structure

The repository is organized into several important directories:

``` text
nrc-server-mappings/
├── servers/
│   └── your-server-name/
│       ├── manifest.json
│       ├── icon.png
│       └── bg.png
├── merged/
├── scripts/
├── docs/
├── package.json
├── package-lock.json
├── README.md
└── LICENSE
```

### `servers/`

Contains the individual server mappings. Each server normally has its
own folder with a `manifest.json` file and its image assets.

### `merged/`

Contains generated, combined mapping output and assets intended for
distribution to the client. The merge process combines the individual
server definitions.

### `scripts/`

Contains scripts used to validate and merge the mapping data.

### `docs/`

Contains documentation about manifest structure, image conventions and
the contribution process.

## 4. The `manifest.json` File

Each server mapping is defined by a `manifest.json` file. This file
contains the server's addresses, display name, categories and asset
paths.

Example:

``` json
{
  "server-address": [
    "your-server.com"
  ],
  "pretty-name": "Your Server Name",
  "categorys": [
    "SURVIVAL"
  ],
  "assets": {
    "icon": "./icon.png",
    "background": "./bg.png"
  }
}
```

The property name `categorys` is intentionally written this way in the
repository's documented schema. Do not silently change it to
`categories` when creating a manifest.

### Required fields

  -----------------------------------------------------------------------
  Field                               Purpose
  ----------------------------------- -----------------------------------
  `server-address`                    An array of Minecraft server
                                      addresses associated with this
                                      mapping.

  `pretty-name`                       The display name shown for the
                                      server in NoRisk Client.

  `categorys`                         An array of categories describing
                                      the server.

  `assets`                            Defines the paths to the server's
                                      visual assets, such as its icon and
                                      background.
  -----------------------------------------------------------------------

Asset paths are relative to the server's `manifest.json` file and use
the `./` prefix.

## 5. Server Categories

The documented category values include:

-   `HG`
-   `PVP`
-   `SURVIVAL`
-   `CREATIVE`
-   `MINIGAMES`
-   `SKYBLOCK`
-   `PRISON`
-   `FACTIONS`
-   `SKYWARS`
-   `BEDWARS`
-   `UHC`
-   `KITPVP`
-   `ANARCHY`
-   `RPG`
-   `ADVENTURE`
-   `BUILD`
-   `TECHNICAL`
-   `VANILLA`
-   `MODDED`
-   `CUSTOM`

A mapping can use categories to describe the type of gameplay offered by
a server. Use valid documented values so that validation can recognize
them.

## 6. Optional Manifest Fields

The manifest can also contain additional information.

### `gamemodes`

Can describe the game modes offered by a server. Game mode entries can
include information such as:

-   `name` -- the display name of the game mode.
-   `versions` -- one Minecraft version or an array of supported
    versions.
-   `description` -- a short explanation of the game mode.

### `socials`

Can provide links to the server's online presence. Documented social
fields include:

-   `website`
-   `store`
-   `youtube`
-   `instagram`
-   `tiktok`
-   `x`
-   `discord`
-   `teamspeak`

### `disabled-modules`

Can list NoRisk Client modules that should be disabled for the mapped
server.

Examples documented by the repository include:

-   `FOV_CHANGER`
-   `FULL_BRIGHT`
-   `ZOOM`
-   `FREE_LOOK`
-   `NO_FOG`
-   `ARROW_TRAIL`
-   `PACK_TWEAKS`
-   `ITEM_MODEL`
-   `AUTO_TEXT`
-   `ITEM_HIGHLIGHTER`
-   `TNT_TIMER`
-   `WEATHER_CHANGER`
-   `TIME_CHANGER`
-   `HEALTH_INDICATORS`
-   `HITBOX`
-   `HIT_COLOR`
-   `NO_HURT_CAM`
-   `LOW_FIRE`
-   `LIGHT_LEVEL`
-   `BLOCK_OUTLINES`
-   `CUSTOM_CROSSHAIR`
-   `SHIELD_STATUS`
-   `NAME_TAGS`
-   `WAYPOINTS`
-   `PINGS`
-   `SATURATION`
-   `TOGGLE_SPRINT`
-   `COORDINATES`
-   `DIRECTION_HUD`
-   `ARMOR_STATUS`
-   `POTION_STATUS`
-   `REACH_DISPLAY`
-   `COMBO_COUNTER`
-   `JUMP_RESET`

This is server-specific client configuration. It should not be confused
with server-side anti-cheat enforcement, and it does not by itself prove
that users cannot bypass a client-side restriction.

## 7. Image and Asset Conventions

The repository defines conventions for server icons and backgrounds.

### Server icon

-   File name: `icon.png`
-   Format: PNG
-   Shape: square
-   Recommended size range: approximately 64×64 to 512×512 pixels
-   Transparency is supported.

### Server background

-   Source file name: `bg.png`
-   Format: PNG
-   Recommended dimensions: 1920×1080 pixels
-   The background should be fully opaque.
-   During the merge process, the background asset is renamed to
    `background.png` in the merged output.

### General image recommendations

-   Use clear, high-quality images.
-   Avoid visible compression artifacts.
-   Make sure the server identity remains recognizable.
-   Avoid placing important text or logos where interface overlays may
    cover them.
-   Follow the repository's documented presentation conventions.

## 8. Validation and Merge Process

The repository includes scripts to check mapping data and produce merged
output.

The documented local workflow includes:

``` bash
npm install
npm run validate
npm run merge
```

-   `npm install` installs the project's dependencies.
-   `npm run validate` checks the mapping data against the expected
    rules.
-   `npm run merge` combines the individual server mappings and prepares
    the merged output.

Validation can check items such as JSON syntax, required fields, asset
existence, URL validity, category values and data types.

The merge process turns the individual server definitions into a
combined format that can be distributed and used by NoRisk Client.

## 9. How to Add a Server Mapping

The repository's contribution guide describes this general process:

1.  Fork the `nrc-server-mappings` repository on GitHub.
2.  Create a new folder for the server inside `servers/`.
3.  Add a `manifest.json` file.
4.  Add the required image assets, such as `icon.png` and `bg.png`.
5.  Make sure the manifest follows the documented schema and image
    conventions.
6.  Run the validation and merge scripts locally, where appropriate.
7.  Create a Pull Request against the official repository.
8.  Check the automated GitHub Actions results and fix any reported
    errors.
9.  Wait for the repository maintainers to review the Pull Request.

A Pull Request may be rejected or require changes if, for example:

-   The server is offline or in maintenance.
-   The images use the wrong format or dimensions.
-   The manifest is invalid.
-   Required files or fields are missing.

Review times are not guaranteed.

## 10. Server Mappings Creator

The repository README links to the **Server Mappings Creator**:

https://pluoi.com/

The tool is presented as a way to make creating server mappings easier.
After generating a mapping, follow the repository's contribution guide
for adding it to the project.

## 11. Important Links

  ------------------------------------------------------------------------------------------------------------------------
  Resource                            URL
  ----------------------------------- ------------------------------------------------------------------------------------
  Official GitHub repository          https://github.com/NoRiskClient/nrc-server-mappings

  Manifest documentation              https://github.com/NoRiskClient/nrc-server-mappings/blob/main/docs/manifest.md

  Image conventions                   https://github.com/NoRiskClient/nrc-server-mappings/blob/main/docs/conventions.md

  Step-by-step contribution guide     https://github.com/NoRiskClient/nrc-server-mappings/blob/main/docs/step-by-step.md

  Server Mappings Creator             https://pluoi.com/
  ------------------------------------------------------------------------------------------------------------------------

## 12. AI-Ready Knowledge Summary

When explaining `nrc-server-mappings`, an AI should understand the
following:

-   `nrc-server-mappings` is the official server-mapping repository for
    NoRisk Client.
-   It associates Minecraft server addresses with metadata and visual
    assets.
-   A mapping can define a display name, icon, background, categories,
    game modes, social links and disabled client modules.
-   Individual server mappings are stored in separate folders under
    `servers/`.
-   Each server mapping is described by a `manifest.json` file.
-   The documented manifest schema uses the exact field name
    `categorys`.
-   The `scripts/` directory contains validation and merge tooling.
-   The `merged/` directory is used for generated combined output.
-   Server mappings improve how servers are represented inside NoRisk
    Client; they do not host servers or create a server-discovery
    platform.
-   `disabled-modules` describes client-side module configuration and
    should not be described as server-side anti-cheat enforcement.
-   Contributions are made through GitHub forks and Pull Requests and
    are subject to validation and maintainer review.

**One-sentence definition:**\
`nrc-server-mappings` is the official NoRisk Client repository that
gives Minecraft servers their own visual identity, metadata, categories,
game mode information and server-specific client configuration inside
the client.

