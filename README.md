# Lightning Fast Hopper (High-Volume Sorting & Item Transport)

Vanilla hoppers are entirely too slow for high-efficiency automation. They transfer items one by one and take a massive 8 game ticks per action, meaning your resource-heavy sorting setups, massive mob drops, and high-velocity water streams constantly overflow.

Lightning Fast Hopper introduces a completely independent, custom block type that completely replaces legacy transport restrictions. It isolates your automated production pipelines into high-throughput networks without affecting or modifying standard vanilla hoppers.

## Features

Isolated Custom Block: Independent item registration name (lightningfasthopper:lightning_hopper). Places and acts cleanly alongside vanilla iron hoppers without overriding their behaviors.
Asynchronous Processing Loops: Features decoupled input and output phases. Your hopper dynamically arbitrates cooldown schedules based on the type of handling action it just performed.
Hyper-Velocity Suction: Custom bounding-box loops directly target and collect passing entities moving down water streams, completely avoiding legacy internal vanilla inventory checks.
Dynamic Stack Scaling: Seamlessly reads item stack data tags. Fully supports modded packages that increase stack sizes past 64 (such as 256 or 1024 maximum capacities).
Batch Transfer Processing: Configurable output pipelines that can move single items, fractional groups, or entire 64-count item stacks in a single game frame.
Resource Pack Friendly: Fully structured client-side resource models and blockstates. Blends natively with vanilla or high-definition textures like PartiX.


## How It Works

Traditional item manipulation methods are slow and heavily restricted by hardcoded ticks. Lightning Fast Hopper overrides this limitations with localized variable tracking.

When floating item components enter the block's overhead vacuum zone, the code checks the item's maximum modded storage configuration. If allowed by your configuration rules, it instantly processes the entire stack. On output, instead of relying on legacy private methods, it actively calculates the exact direction the hopper nozzle is pointing, queries the public data paths of the facing container, and safely injects blocks in large configured batches.



## Instructions for Use

### Installation

Ensure your server environment is running the Fabric Loader.
Drop the lightningfasthopper-*.jar file into your server and client mods/ directories.
Launch the game once to auto-generate your custom server parameters profile. [1]
Crafting Recipe

Build your custom sorting network in survival using standard resource materials:

**Ingredients:**
- 4  Gold Ingots
- 1 Standard Iron Hopper
- 1 Block of Redstone.

### Pattern Matrix:
- [ Gold ] [ Blank ] [ Gold ]
- [ Gold ] [ Hopper ] [ Gold ]
- [ Blank ] [Redstone] [ Blank ]


### Configuration Profile Tuning

Navigate to your instance directory path at /config/lightningfasthopper.json to fine-tune your performance thresholds:

```json

{

"inputCooldownTicks": 2,

"outputCooldownTicks": 2,

"pullEntireStack": true,

"outputTransferBatchSize": 1

}
```


- **inputCooldownTicks** / **outputCooldownTicks**: Tick counts the block waits before repeating operations (Minecraft runs at 20 ticks per second). Set to 1 or 2 for incredible sorting throughput, or 0 for instantaneous frame transfers.
- **pullEntireStack**: Set to true to immediately pull floating stacks down out of water flows.
- **outputTransferBatchSize**: The number of items pushed into chests simultaneously. Set to 1 for default single-item step behavior, or up to 64 to drain full inventory blocks instantly on every cooldown tick.


## License

This mod is licensed under the [MIT License](LICENSE).
