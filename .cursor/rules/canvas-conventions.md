# Canvas Study System Conventions

## Overview
36 interactive HTML study canvases in `canvas/` — one per "implement from scratch" topic. All complete.

## File Locations
```
canvas/index.html              Hub with 36 tiles (5 color-coded categories)
canvas/assets/canvas.css       Shared dark-theme design system
canvas/assets/canvas.js        Tab switching, collapsibles, LRU/LFU simulators
canvas/cache/          (4)     LRU*, LFU*, TTL, Write-Through
canvas/data-structures/(10)    HashMap, LinkedList, ArrayList, Stack, Queue, Deque, MinHeap, Trie, BST, Graph
canvas/concurrency/    (7)     ThreadPool, BlockingQueue, RWLock, RateLimiter, ProducerConsumer, Latch, Semaphore
canvas/design/         (8)     BSTIterator, Observer, PubSub, ConnPool, Logger, ConsistentHashing, EventBus, CompletableFuture
canvas/algorithms/     (7)     MergeSort, QuickSort, StringBuilder, CustomComparator, atoi, StreamOps, ImmutableBuilder
```

## HTML Structure (every canvas file)
1. `<!DOCTYPE html>` with `<link rel="stylesheet" href="../assets/canvas.css">`
2. Breadcrumb: `Hub > [Category] > [Topic]`
3. Hero header: title, description, badges (difficulty, companies), complexity row (`.cx-row`)
4. 3 tabs inside `div[data-tabscope]`: **Concept**, **Implementation**, **Interview**
5. Footer with back link + source file path
6. `<script src="../assets/canvas.js"></script>` at end of body

## Tab Content Patterns
- **Concept**: `.grid.g2` cards, SVG diagrams in `.viz`, operations walkthrough with `.step.[category]` flow boxes
- **Implementation**: `<pre class="code">` with syntax spans: `.kw` `.ty` `.cm` `.st` `.num` `.ann`
- **Interview**: complexity `<table>`, `.collapsible` follow-up cards, `.callout` / `.callout.tip` / `.callout.info`

## SVG Diagram Rules (CRITICAL)
- Minimum node box width: **120px**, minimum height: **40px**
- Text inside boxes: **10px+ padding** from edges
- Minimum gap between boxes: **30px**
- Arrow labels must NOT overlap with any box or other label
- Always use `viewBox` attribute for responsive scaling
- Font sizes: titles 12px bold, body 10-11px, labels 9-10px
- Never place more than 3 items on a single text line inside a box
- Use cubic bezier `<path>` curves instead of straight `<line>` for connections
- Test visually that no text overflows box borders

## Category Color Tokens
- `--ds` (teal) for Data Structures, `.step.ds`
- `--cache` (amber) for Caches, `.step.cache`
- `--conc` (purple) for Concurrency, `.step.conc`
- `--design` (rose) for Design Patterns, `.step.design`
- `--algo` (sky) for Algorithms, `.step.algo`

## Hub Tiles
- Each tile in `canvas/index.html` links to its canvas file
- Tile format: `<a class="tile" href="[path]"><div class="num">[ID] · ready</div><div class="tt">[Title]</div><div class="dd">[Description]</div></a>`
- Category bands: `<div class="lvlband [category]">[Name] — N topics · <b>all ready</b></div>`

## Adding a New Canvas
1. Create HTML file in the appropriate `canvas/[category]/` folder
2. Follow the exact structure above (breadcrumb, hero, 3 tabs, footer)
3. Add tile to `canvas/index.html` in the correct category section
4. Use the category's color token for `.step` flow boxes
5. Ensure SVG diagrams pass the spacing rules
