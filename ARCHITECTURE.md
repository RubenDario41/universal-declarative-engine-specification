# Core Architecture Specification

## 1. The UniversalObject: Formal Definition

The `UniversalObject` is the foundational, recursive unit of the entire system. It is a data-centric entity, completely defined by external JSON configuration and agnostic to any specific domain.

### 1.1 Properties

- **`id`** (String, mandatory): A unique identifier for the object within its tree.
- **`type`** (String, mandatory): A logical key that maps to a plugin responsible for the object's behavior and rendering (e.g., `card`, `sensor_source`, `visual_display`, `container`).
- **`epigenome`** (Map, optional): The **persistent, observable state** of the object (e.g., `"idle"`, `"flipped"`, `"processing"`). This state is serialized and restored to maintain UI continuity across sessions. It represents the object's "lived experience."
- **`state`** (Map, optional): The **internal, mutable state** managed at runtime. It can be volatile and is used for transient logic, calculations, or temporary values not required for persistence.
- **`visual`** (Map, optional): Visual properties such as color, image references, layout constraints, and size.
- **`behavior`** (Map, optional): Declarative mapping of user gestures (tap, swipe, drag) to effects or state transitions.
- **`children`** (List of UniversalObject, optional): An ordered list of child `UniversalObject` instances, enabling the composition of complex hierarchies from this single primitive.

### 1.2 Key Axioms

- **Axiom of Universality**: Any element in any interactive domain can be modeled as a `UniversalObject`.

- **Axiom of Recursion**: A `UniversalObject` can contain other `UniversalObject`s, enabling infinite compositional depth.
- **Axiom of Declarativeness**: The structure and initial configuration of an object tree are defined purely in a declarative manifest.

## 2. Runtime Components

The engine runtime is composed of three singleton managers that operate on the `UniversalObject` tree:

| Component | Responsibility |
| :--- | :--- |
| **`UniversalScreen`** | The root orchestrator. Loads the declarative manifest (JSON), parses it into a tree of `UniversalObject` instances, and manages the overall render cycle. |
| **`UniversalViewModel`** | The single source of truth. Maintains the global application state, registers all objects, manages the `epigenome`, and handles navigation and focus logic, ensuring data-view synchronization. |
| **`UniversalFocusView`** | The performance and attention core. Renders the list of `UniversalObject`s by applying visual and focus transformations **strictly as dictated by the object's data**, without imposing hard-coded domain rules. |

## 3. The Declarative Data Flow

The application lifecycle is a direct interpretation of data:

1. **Manifest Load**: A JSON file (e.g., `tarot-deck.json`) is loaded from the asset bundle.
2. **Parsing & Instantiation**: The JSON is deserialized into a tree of `UniversalObject` data structures.
3. **Registration**: The tree is passed to the `UniversalViewModel`, which registers each object and establishes reactive state bindings.
4. **Rendering**: The `UniversalScreen` and `UniversalFocusView` traverse the registered tree. Each object's `type` resolves to a specific plugin that handles its rendering and interaction logic, using properties from `visual`, `epigenome`, and `state`.
5. **Interaction Loop**: User gestures are mapped via the `behavior` property to effects (defined in a shared animation suite) or direct `epigenome`/`state` updates, which propagate reactively through the `UniversalViewModel`.

## 4. Multi-Domain Demonstration

The power of this architecture is proven through configuration alone. Below are summaries of the three provided examples. The engine code is identical for all.

### 4.1. Tarot System (`examples/tarot-universal.json`)

Models a deck of Major Arcana cards. Each `card`-type object has an `epigenome` containing immutable symbolic metadata (arcana, element, frequency) and a `state` for mutable properties like `face_up` and `position`. A `flip` gesture in the `behavior` block triggers a visual effect.

### 4.2. Sensor Dashboard (`examples/sensor-dashboard-universal.json`)

Models a real-time physics monitor. A `sensor_source`-type object has an `epigenome` defining its sensor type (`ACCELEROMETER`) and unit. Its `state` is continuously updated with live readings. A separate `visual_display`-type object listens to this data channel and renders it.

### 4.3. Quantum Generator Lab (`examples/quantum-lab-universal.json`)

Models a complex generative system. It combines a `sensor_source` (for physical entropy), a `service_source` (fetching moon phase from an astronomy API), and an `algorithm_processor` (which harmonizes the data streams using the golden ratio). The final output is rendered by a `card`-type object, demonstrating how primitive types can be composed to create emergent, domain-specific behavior.

## 5. Conclusion

This architecture demonstrates that a unified, declarative model for interactive applications is both feasible and powerful. By reducing any application to a tree of configured `UniversalObject` instances, it achieves a level of abstraction where the engine is truly universal, and complexity is managed entirely through structured data.

## Esquema de Flujo del Motor Universal (Ilustrativo)

```mermaid
flowchart TD
    A[JSON Configuration] --> B[ConfigManager.kt]
    B --> C[UniversalObject<br/>instances]
    
    D[MainActivity.kt] --> E[UniversalObject.Render]
    E --> F{¿Tiene children?}
    
    F -->|Sí| G[RenderAsContainer]
    F -->|No| H[RenderAsLeaf]
    
    G --> I{Layout Type?}
    I -->|horizontal_list| J[LazyRow]
    I -->|vertical_list| K[LazyColumn]
    I -->|row/column| L[Simple Layout]
    
    H --> M{¿Tiene imagen?}
    M -->|Sí| N[RenderWithImage]
    M -->|No| O[RenderGeneric]
    
    N --> P[resolveUniversalImage]
    P --> Q[Coil ImageLoader]
    
    O --> R[Subscribe to EventBus]
    R --> S[Show live data]
    
    %% Plugins y efectos
    T[EffectRegistry.kt] --> U[EffectApplied]
    V[FunctionalRegistry.kt] --> W[Activate Plugin]
    
    %% Conexiones
    E --> W
    N --> U
    O --> U
    
    %% Eventos
    X[User Gestures] --> Y[detectDragGestures]
    Y --> Z[Publish to EventBus]
    Z --> R
    
    %% Estilos
    classDef input fill:#bbdefb,stroke:#0d47a1
    classDef process fill:#c8e6c9,stroke:#1b5e20
    classDef output fill:#fff9c4,stroke:#f57f17
    classDef plugin fill:#e1bee7,stroke:#4a148c
    classDef event fill:#ffccbc,stroke:#bf360c
    
    class A,B,D input
    class C,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S process
    class T,U,V,W plugin
    class X,Y,Z event
```

## 1. The UniversalObject: A Formal Definition

The `UniversalObject` (UO) is the atomic, recursive building block of the entire system. It is formally defined as a 7-tuple:

`UO = (id, type, state, epigenome, children, renderer, lifecycle)`

### 1.1 Tuple Components

- **`id`**: A unique string identifier.
- **`type`**: A string key mapping to a **Plugin** responsible for the object's behavior and rendering (e.g., `text`, `image`, `gauge`, `card`, `container`).
- **`state`**: Mutable, observable data for the object. This changes during user interaction (e.g., `text content`, `numeric value`, `position coordinates`).
- **`epigenome`**: An **immutable** property map defining inherent characteristics, constraints, and default styling. Passed to children, can be overridden, and supports theme propagation (similar to biological epigenetic inheritance).
- **`children`**: Ordered list of child `UniversalObject` instances, allowing deep, composable hierarchies.
- **`renderer`**: Reference to the plugin component translating `state` and `epigenome` into a platform-specific representation (pixels, sound, haptics, etc.).
- **`lifecycle`**: Manages the object's phases (`created`, `active`, `suspended`, `archived`). Controlled by the FocusView's virtualization for resource efficiency.

### 1.2 Tuple Axioms

- **Universality**: Every element in any application domain is an instance of `UniversalObject`.
- **Recursion**: A `UniversalObject` can contain other `UniversalObject`s, with no conceptual limit on nesting.
- **Declarativity**: Structure and initial configuration of a UO tree are defined purely declaratively (e.g., via JSON manifest).

---

## 2. Unified Runtime Components

### 2.1 UniversalScreen

- Singleton canvas.
- Hosts the root `UniversalObject` tree.
- Orchestrates the render cycle across all active objects.
- Handles platform-level events (window resizing, app lifecycle).

### 2.2 UniversalViewModel

- Single source of truth for app state.
- Holds a reactive reference to the `state` of every `UniversalObject`.
- Applies state transition functions immutably.
- Enables centralized time-travel debugging and state serialization.

### 2.3 UniversalFocusView

- Attention and performance engine; models user focus in 3D spatial context.
- **Focus Regions**: Tracks which objects are in `primary focus`, `near buffer`, or `latent field`.
- **Lifecycle Control**: Signals `UniversalObject.lifecycle` to activate, suspend, or archive objects based on focus region.
- **Virtualization**: Only fully renders objects in/near focus, enabling the engine to handle extremely large object trees.

---

## 3. The Plugin System

Plugins bridge the universal engine to domain-specific needs.

A plugin must provide:

1. **Behavior**: Reacts to state changes and user events.
2. **Renderer**: Translates state and epigenome into visuals/interactions.

Plugins are discovered at runtime by their registered `type` string.
> See [examples/](./examples) for live plugin usage.

---

## 4. Declarative Manifest

The entire application is defined through a JSON (or equivalent) manifest, specifying:

1. The initial tree of `UniversalObject` instances.
2. Mapping of `type` strings to plugin identifiers.
3. Initial `state` and `epigenome` for each object.
4. Declarative event–response rules.

**Example snippet:**  

```json
{
  "root_object": {
    "id": "app_root",
    "type": "vertical_container",
    "children": [
      {
        "id": "title",
        "type": "text",
        "epigenome": {"font_size": 24},
        "state": {"content": "Hello, World"}
      },
      {
        "id": "sensor_display",
        "type": "gauge",
        "epigenome": {"min": 0, "max": 100, "color": "green"},
        "state": {"value": 42}
      }
    ]
  }
}
```

Change the manifest, and you change the whole app. No recompilation, no new UI code.

For further examples, visit [`examples/`](./examples).

---

## 5. Base Interfaces in Kotlin

For implementation details, see the core interfaces in [`src/interfaces/`](./src/interfaces/):

```kotlin
// UniversalObject.kt
package org.universal.engine

data class UniversalObject(
    val id: String,
    val type: String,
    val epigenome: Map<String, Any> = emptyMap(),
    val children: List<UniversalObject> = emptyList()
) {
    // Lifecycle state managed by UniversalFocusView
    enum class LifecycleState { CREATED, ACTIVE, SUSPENDED, ARCHIVED }
}

// UniversalScreen.kt
interface UniversalScreen {
    fun setRootObject(root: UniversalObject)
    fun requestRender()
}

// UniversalViewModel.kt
interface UniversalViewModel {
    fun getState(objectId: String): State?
    fun updateState(objectId: String, stateUpdate: (State) -> State)
    data class State(val data: Map<String, Any>)
}

// UniversalFocusView.kt
interface UniversalFocusView {
    fun setFocus(point: FocusPoint)
    data class FocusPoint(val x: Float, val y: Float, val z: Float = 0f)
}
```

---

For further details, see the full specification and implementation in this repository, or contact the maintainers for architecture discussions.
