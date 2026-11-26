# NoRisk Client -- `nrc-configs` Repository Documentation

## 1. Repository Overview

The
[`NoRiskClient/nrc-configs`](https://github.com/NoRiskClient/nrc-configs)
repository is an official configuration-data repository for NoRisk
Client.

It contains structured configuration files used by the client and its
launcher. The repository is not the NoRisk Client's main source-code
repository. Instead, it stores data that describes things such as
modpacks, mod sources, Minecraft-version compatibility, translations,
staff-related data, promotional cosmetics, AFK-point experiments and
other client configuration resources.

The repository is organized around JSON configuration data and JSON
schemas. Its `CLIENT_CONTRIBUTING.md` file also documents how developers
can create and contribute modpacks.

**Official repository:** https://github.com/NoRiskClient/nrc-configs

## 2. Repository Structure

The repository root contains the following folders and files:

``` text
nrc-configs/
├── .github/
├── afkpoints/
│   └── exp/
├── packs/
├── promotional-cosmetics/
├── schemas/
├── staff/
├── translations/
│   └── client/
├── versions/
├── .gitattributes
├── .gitignore
├── CLIENT_CONTRIBUTING.md
├── crashlog.json
└── repositories.json
```

The following sections explain the purpose of each visible top-level
area. Where the repository's public documentation does not explain the
exact internal data format or runtime behavior, that limitation is
stated rather than guessed.

### `.github/`

Contains GitHub-related repository configuration. The root listing
confirms the directory exists, but the repository overview does not
document its individual files or workflows.

### `afkpoints/exp/`

Contains an `exp` subdirectory under `afkpoints`. The folder name
indicates an experimental AFK-points-related configuration area. The
root listing alone does not define the exact schema, point system, or
how the client consumes these files.

### `packs/`

Contains NoRisk modpack definitions. These JSON files describe named
packs and the mods, sources, compatibility entries and pack settings
associated with them.

This is one of the central parts of the repository. Examples of pack
definitions include `nrc-mini.json` and the `norisk-bughunter.json`
template referenced by the contribution guide.

### `promotional-cosmetics/`

Contains configuration data related to promotional cosmetics. The
repository root confirms this data area exists, but the root-level
documentation does not specify all supported cosmetic types, fields,
activation rules or display behavior.

### `schemas/`

Contains JSON schemas used to describe and validate configuration
formats. The repository includes a pack schema at:

https://github.com/NoRiskClient/nrc-configs/blob/main/schemas/pack.schema.json

The pack schema uses JSON Schema Draft 2020-12 and defines the expected
structure of modpack configuration files.

### `staff/`

Contains staff-related configuration data. The root listing confirms the
directory exists, but does not document the exact fields, access rules
or how the client displays or uses its contents.

### `translations/client/`

Contains client translation data. It is the translation-data area for
NoRisk Client. The root listing does not specify every language file or
the complete translation key format.

### `versions/`

Contains version-related configuration data. This is a separate data
area from the `packs/` directory. The root listing confirms the folder
exists, but does not by itself define every version field or the full
version-selection behavior.

### Root-level files

#### `CLIENT_CONTRIBUTING.md`

A contributor guide explaining how to create a development branch and a
modpack for contributing to NoRisk Client.

#### `repositories.json`

Maps repository reference names to Maven or other artifact repository
URLs. Pack mod entries can refer to these names through `repositoryRef`.

#### `crashlog.json`

A JSON file present at the repository root. The root listing does not
document its exact schema or runtime purpose.

#### `.gitattributes` and `.gitignore`

Standard Git configuration files controlling Git attributes and ignored
files.

------------------------------------------------------------------------

## 3. Modpack Configuration System (`packs/`)

A pack definition describes a collection of mods and the conditions
under which those mods can be resolved for different Minecraft versions
and mod loaders.

The launcher can use pack definitions to present selectable
configurations and resolve the mods needed for a profile.

A pack can define:

-   Its display name and description.
-   Parent packs from which it inherits mods.
-   Mods to exclude from inherited packs.
-   Its own mod entries.
-   Asset-pack references.
-   Whether the pack is experimental.
-   Whether mods should be automatically updated.
-   How the pack is listed in the launcher.
-   Loader-version policies.
-   Additional startup-helper paths.

The exact pack format is defined by `schemas/pack.schema.json`.

## 4. Pack Schema -- Required Fields and Main Properties

The schema requires two top-level fields:

-   `displayName`
-   `description`

### Top-level fields

  -----------------------------------------------------------------------
  Field                   Type                    Purpose
  ----------------------- ----------------------- -----------------------
  `displayName`           String                  Name displayed for the
                                                  modpack. Must not be
                                                  empty.

  `description`           String                  Description of the
                                                  pack.

  `inheritsFrom`          Array of strings or     Parent pack IDs whose
                          `null`                  mods are inherited,
                                                  processed in order.

  `excludeMods`           Array of strings or     Mod IDs excluded after
                          `null`                  inheritance.

  `mods`                  Array                   Mod definitions
                                                  included in this pack.
                                                  Defaults to an empty
                                                  array in the schema.

  `assets`                Array of strings        Asset-pack references.
                                                  Defaults to an empty
                                                  array.

  `isExperimental`        Boolean                 Marks the pack as
                                                  experimental. Defaults
                                                  to `false`.

  `listing`               Object                  Controls how the pack
                                                  appears in the
                                                  launcher's pack picker.

  `autoUpdate`            Boolean                 Controls automatic
                                                  updates for mods.
                                                  Defaults to `false`.

  `loaderPolicy`          Object or `null`        Defines loader-version
                                                  policy configuration.

  `startupHelper`         Object                  Startup-helper
                                                  configuration.
  -----------------------------------------------------------------------

### Pack inheritance

`inheritsFrom` accepts an array of pack IDs or `null`. The schema
describes inherited packs as being processed in order.

This allows one pack to build on another instead of repeating every mod
entry.

`excludeMods` can be used to remove mod IDs after inherited content has
been processed.

The contribution guide gives `["norisk-prod"]` as an example of
inheriting from another pack.

### Pack listing

The `listing` object controls how a pack is presented in the launcher's
pack picker.

  -----------------------------------------------------------------------
  Field                   Type                    Meaning
  ----------------------- ----------------------- -----------------------
  `category`              String                  Group heading in the
                                                  picker. An empty string
                                                  means the main,
                                                  unlabeled group.

  `weight`                Integer                 Sorting weight within
                                                  the group. Higher
                                                  values sort first.

  `hidden`                Boolean                 Hides the pack from the
                                                  default list; it
                                                  remains reachable
                                                  through "show more".
  -----------------------------------------------------------------------

The schema disallows additional properties inside the `listing` object.

### Experimental packs

`isExperimental` is a boolean. When enabled, the pack is marked as
experimental. The contribution guide says this marking causes a warning
to be shown.

### Automatic updates

`autoUpdate` is a boolean that controls whether mods should auto-update.
The schema default is `false`.

The contribution guide notes that after the initial mod-version setup,
auto-update can handle new versions for Maven mods.

------------------------------------------------------------------------

## 5. Mod Entries

The `mods` array contains objects describing individual mods.

According to the pack schema, each mod entry requires:

-   `id`
-   `source`
-   `compatibility`

### Mod fields

  -----------------------------------------------------------------------
  Field                   Type                    Purpose
  ----------------------- ----------------------- -----------------------
  `id`                    String                  Unique mod identifier.

  `displayName`           String or `null`        Optional display name.

  `source`                Object                  Where the mod should be
                                                  downloaded from.

  `compatibility`         Object                  Maps Minecraft versions
                                                  and loaders to
                                                  compatible mod builds.
  -----------------------------------------------------------------------

### Mod source types

The schema supports three source types:

1.  `modrinth`
2.  `maven`
3.  `url`

#### Modrinth source

A Modrinth source requires:

-   `type: "modrinth"`
-   `projectId`
-   `projectSlug`

These identify the Modrinth project.

#### Maven source

A Maven source requires:

-   `type: "maven"`
-   `repositoryRef`

It can also specify:

-   `groupId`
-   `artifactId`

`repositoryRef` points to a named repository entry in
`repositories.json`.

#### URL source

The schema recognizes `url` as a source type. The visible schema does
not define additional required URL-specific properties, so
implementations should be checked before assuming more about its exact
structure.

------------------------------------------------------------------------

## 6. Repository References (`repositories.json`)

The root-level `repositories.json` file maps short reference names to
repository URLs.

The file currently lists these references:

  -------------------------------------------------------------------------------------------------
  Reference                           Repository URL
  ----------------------------------- -------------------------------------------------------------
  `modrinth`                          `https://api.modrinth.com/maven/`

  `kosmx`                             `https://maven.kosmx.dev/`

  `norisksnapshots`                   `https://maven.norisk.gg/repository/norisk-snapshots/`

  `jitpack`                           `https://jitpack.io/`

  `fabric`                            `https://maven.fabricmc.net/`

  `central`                           `https://repo1.maven.org/maven2/`

  `geckolib`                          `https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/`

  `norisk`                            `https://dl.norisk.gg/`

  `terraformersmc`                    `https://maven.terraformersmc.com/`

  `viaversion`                        `https://repo.viaversion.com/`

  `wispforest`                        `https://maven.wispforest.io/`

  `noriskproduction`                  `https://maven.norisk.gg/repository/norisk-production/`
  -------------------------------------------------------------------------------------------------

The exact reference key is `norisksnapshots`, as shown in the source
JSON.

A Maven mod entry uses `repositoryRef` to select one of these configured
repositories. This separates repository URLs from individual mod
definitions.

Official file:\
https://github.com/NoRiskClient/nrc-configs/blob/main/repositories.json

Raw JSON:\
https://raw.githubusercontent.com/NoRiskClient/nrc-configs/main/repositories.json

------------------------------------------------------------------------

## 7. Minecraft-Version and Loader Compatibility

Each mod entry can define a `compatibility` object.

The schema describes the structure as:

``` text
Minecraft version
└── Loader name
    └── Compatibility target
```

For example, a mod may have a compatibility entry for Minecraft
`1.21.11` under the `fabric` loader.

Each compatibility target requires an `identifier` and can also contain:

-   `filename`
-   `source`

### `identifier`

A string identifying the compatible mod build. The schema says it can be
a semantic version, a Modrinth version ID or a URL.

### `filename`

An optional filename override. It can be a string or `null`.

### `source`

An optional source override for that specific Minecraft-version/loader
combination. It can contain a mod source object or `null`.

This makes it possible for a particular compatibility entry to use a
different source from the mod's general source definition.

### Important compatibility rule

Do not assume that a mod supports every Minecraft version or every
loader. The compatibility object lists the combinations that are defined
for that mod.

------------------------------------------------------------------------

## 8. Loader Policy

The optional `loaderPolicy` object defines loader-version policies for
the pack.

It contains two main areas:

### `default`

Defines default loader specifications. These are used as a fallback when
there is no matching `byMinecraft` entry.

### `byMinecraft`

Defines loader specifications for particular Minecraft versions. The
schema gives examples such as:

-   `1.20.1`
-   `1.21`
-   `1.21.*`

### Loader specification fields

  -----------------------------------------------------------------------
  Field                   Type                    Purpose
  ----------------------- ----------------------- -----------------------
  `version`               String or `null`        Exact loader version.
                                                  The schema says this
                                                  implies the `EXACT`
                                                  strategy.

  `min`                   String or `null`        Minimum loader version
                                                  for non-exact
                                                  strategies.

  `strategy`              String or `null`        Version-resolution
                                                  strategy.
  -----------------------------------------------------------------------

The schema lists these strategy values:

-   `exact`
-   `latest_compatible`
-   `min_compatible`
-   `null`

The schema describes `default` as the fallback when no `byMinecraft`
match is found.

------------------------------------------------------------------------

## 9. Startup Helper

The `startupHelper` object contains:

``` json
{
  "additional_paths": []
}
```

The `additional_paths` field is an array of strings and defaults to an
empty array.

The schema describes these as additional paths for startup. The exact
behavior of each path is determined by the launcher implementation; the
schema alone does not explain every supported path or copy operation.

------------------------------------------------------------------------

## 10. Contributing a Modpack

The repository includes a contributor guide:

https://github.com/NoRiskClient/nrc-configs/blob/main/CLIENT_CONTRIBUTING.md

The guide describes this workflow.

### Step 1 -- Create a development branch in `nrc-core`

Create a branch from the main development branch identified in the
guide:

https://github.com/NoRiskClient/nrc-core/tree/dev/mojang-mappings

The guide warns that the branch name becomes part of the JAR filename.

Example:

``` text
Branch:
dev/mojang-mappings

JAR:
nrc-core-1.0.151-dev-mojang-mappings+fabric.1.21.11
```

### Step 2 -- Create a pack in `nrc-configs`

1.  Open the `packs/` directory.
2.  Copy `norisk-bughunter.json` as a template.
3.  Rename it to the name of your pack, for example
    `my-feature-pack.json`.
4.  Create a Pull Request.

### Step 3 -- Configure the mod version

In the new pack JSON, find the mod being developed and update its
version identifier.

The contribution guide gives this example:

``` json
{
  "id": "nrc-core",
  "displayName": "nrc-core",
  "source": {
    "type": "maven",
    "repositoryRef": "noriskproduction",
    "groupId": "gg.norisk",
    "artifactId": "nrc-core"
  },
  "compatibility": {
    "1.21.11": {
      "fabric": {
        "identifier": "1.0.151-YOUR-BRANCH-NAME+fabric.1.21.11",
        "filename": null,
        "source": null
      }
    }
  }
}
```

The guide says this version setup only needs to be done once; after the
initial setup, auto-update can handle new versions.

### Step 4 -- Wait for approval

The Pull Request needs approval from a NoRisk maintainer for the
one-time setup.

### Step 5 -- Test in the launcher

1.  Open NoRisk Launcher.
2.  Create a new profile.
3.  Enable the **Show all versions** checkbox.
4.  Select your pack from the dropdown.
5.  Launch and test it.

### Ongoing development workflow

The guide summarizes the process as:

``` text
Code changes
    ↓
Version bump
    ↓
Push
    ↓
CI builds
    ↓
Discord approval
    ↓
Auto-deployed
```

More specifically:

1.  Make code changes in `nrc-core`.
2.  Increase the version in `gradle.properties`.
3.  Push the branch.
4.  Woodpecker CI builds all versions automatically.
5.  Approve in Discord when prompted.
6.  The pack is automatically updated with the new version.

------------------------------------------------------------------------

## 11. Version Naming Convention

The contribution guide describes a semantic-version-style identifier
with branch and platform information.

Example:

``` text
1.0.151-dev-mojang-mappings+fabric.1.21.11
```

The parts represent:

-   `1` -- major version
-   `0` -- minor version
-   `151` -- patch version
-   `dev-mojang-mappings` -- branch or prerelease identifier
-   `fabric` -- mod loader
-   `1.21.11` -- Minecraft version

The guide's example shows how the branch name can be included in the
built JAR name and version identifier.

------------------------------------------------------------------------

## 12. The `nrc-mini.json` Example

One pack file in the repository is:

https://github.com/NoRiskClient/nrc-configs/blob/main/packs/nrc-mini.json

It defines the NRC Mini pack. Its description presents it as a
social-focused version of NoRisk Client centered on Hosted Worlds,
Friends, Cosmetics, Emotes and Capes, without QoL or HUD modules.

This is an example of one pack definition within the wider `packs/`
system. It should not be mistaken for the entire repository.

------------------------------------------------------------------------

## 13. What the Repository Is -- and Is Not

### It is:

-   An official NoRisk Client configuration-data repository.
-   A source of modpack definitions.
-   A place where mod sources and repository references are configured.
-   A place where mod compatibility is declared by Minecraft version and
    loader.
-   A home for pack schemas and other client configuration data.
-   A repository with contributor instructions for creating and
    maintaining modpacks.

### It is not:

-   The main NoRisk Client application source code.
-   A Minecraft mod by itself.
-   A Minecraft server host.
-   A replacement for the launcher.
-   A complete technical explanation of every feature listed in every
    configuration folder.

The launcher and client software interpret these configuration files.
The JSON data describes configuration; the consuming software determines
runtime behavior.

------------------------------------------------------------------------

## 14. AI-Ready Knowledge Summary

When explaining `NoRiskClient/nrc-configs`, an AI should understand:

1.  `nrc-configs` is an official NoRisk Client repository for
    configuration data.
2.  Its root includes `packs`, `schemas`, `versions`,
    `translations/client`, `staff`, `promotional-cosmetics` and
    `afkpoints/exp`.
3.  `packs/` contains modpack definitions.
4.  Pack definitions specify display metadata, inheritance, exclusions,
    mods, assets, experimental status, automatic updates, listing
    behavior, loader policies and startup-helper paths.
5.  The pack schema is `schemas/pack.schema.json` and uses JSON Schema
    Draft 2020-12.
6.  The schema requires `displayName` and `description`.
7.  Each mod entry requires an `id`, `source` and `compatibility`
    object.
8.  Supported source types in the schema are `modrinth`, `maven` and
    `url`.
9.  Maven sources refer to named repositories through `repositoryRef`.
10. `repositories.json` maps repository references to URLs.
11. Compatibility is declared per Minecraft version and loader; do not
    assume every combination is supported.
12. Loader policies support `default` and `byMinecraft` specifications.
13. The schema lists loader strategies `exact`, `latest_compatible` and
    `min_compatible`.
14. `CLIENT_CONTRIBUTING.md` explains how to create a pack based on
    `norisk-bughunter.json`, update a mod version, submit a Pull Request
    and test the pack in the launcher.
15. The contribution guide describes an ongoing workflow involving code
    changes, version bumps, CI builds, Discord approval and automatic
    deployment.
16. The repository stores configuration and metadata; it is not the
    implementation of the client features themselves.
17. Do not invent schemas or behavior for folders whose internal files
    are not documented by the root listing or the inspected schema.

**One-sentence definition:**\
`nrc-configs` is the official NoRisk Client configuration repository
that organizes modpacks, mod sources, version compatibility, loader
policies, translations and other client-related data used by the NoRisk
launcher and client.

------------------------------------------------------------------------

## 15. Official Sources

-   Repository: https://github.com/NoRiskClient/nrc-configs
-   Pack definitions:
    https://github.com/NoRiskClient/nrc-configs/tree/main/packs
-   Pack schema:
    https://github.com/NoRiskClient/nrc-configs/blob/main/schemas/pack.schema.json
-   Repository URL mapping:
    https://github.com/NoRiskClient/nrc-configs/blob/main/repositories.json
-   Contribution guide:
    https://github.com/NoRiskClient/nrc-configs/blob/main/CLIENT_CONTRIBUTING.md
-   NRC Mini example:
    https://github.com/NoRiskClient/nrc-configs/blob/main/packs/nrc-mini.json

