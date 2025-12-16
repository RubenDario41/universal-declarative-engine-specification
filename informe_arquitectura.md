# Arquitectura de Motor Declarativo Universal

## A. Definición formal del `UniversalObject`

Un **UniversalObject** es la unidad fundamental del motor universal, definida completamente por datos externos (JSON). Su estructura es genérica y agnóstica al dominio, permitiendo representar cualquier entidad visualizable o interactuable.

### Propiedades principales

- **id** (obligatoria): Identificador único del objeto.
- **type** (obligatoria): Tipo lógico/de renderizado (ej. "container", "button", "sensor", "universal"). Determina el plugin o la lógica de visualización.
- **effect** (opcional): Efecto visual o animación a aplicar, referenciado por nombre y definido en `animation_suite.json`.
- **visual** (opcional): Propiedades visuales (color, imagen, layout, tamaño, etc.).
- **behavior** (opcional): Mapeo de gestos a efectos o transiciones (ej. tap, swipe, drag).
- **interactionContext** (opcional): Parámetros contextuales de interacción (umbrales, aislamiento, etc.).
- **metadata** (opcional): Metadatos libres (fuente de datos, identificadores, etc.).
- **children** (opcional): Lista de UniversalObject hijos, permitiendo jerarquía recursiva.

### Epigenome y State

- **epigenome**: Representa el "estado vivo" mutable del objeto (por ejemplo, "idle", "flipped", "processing"). Se serializa y persiste para restaurar el estado de la UI.
- **state**: Estado interno mutable, gestionado en tiempo de ejecución (no necesariamente persistente).

**Diferencia clave:**  

- `epigenome` es el estado observable y persistente, relevante para la navegación y el foco.  
- `state` es interno y puede ser volátil, usado para lógica temporal.

---

## B. Componentes del runtime

- **UniversalScreen**: Componente raíz que orquesta la carga y renderizado de la estructura universal a partir de los datos JSON.
- **UniversalViewModel**: Gestiona el estado global de la UI, el registro de objetos y la navegación/foco, manteniendo la sincronización entre datos y vista.
- **UniversalFocusView**: Renderiza una lista de UniversalObject aplicando las transformaciones visuales y de foco dictadas por los datos, sin imponer reglas desde el código.

---

## C. Ejemplo concreto multi-dominio

A continuación, se muestra cómo un mismo UniversalObject se instancia para distintos dominios, usando ejemplos reales del proyecto:

### 1. Carta de Tarot

```json
{
  "id": "el_loco",
  "type": "card",
  "effect": "FLIP_EFFECT",
  "visual": {
    "displayName": "El Loco",
    "image": "images/tarot/el_loco.png"
  },
  "behavior": {
    "gestures": {
      "tap": { "effect": "flip" }
    }
  },
  "metadata": {
    "arcano": "mayor",
    "numero": 0
  }
}
```

### 2. Sensor

```json
{
  "id": "sensor_temperatura",
  "type": "sensor",
  "effect": "FADE_EFFECT",
  "visual": {
    "displayName": "Sensor de Temperatura",
    "icon": "icons/thermometer.png"
  },
  "behavior": {
    "gestures": {
      "tap": { "effect": "show_details" }
    }
  },
  "metadata": {
    "dataSource": "api/sensor/temperature"
  }
}
```

### 3. Procesador de Algoritmo (Laboratorio Cuántico)

```json
{
  "id": "generador_cuantico_prototipo",
  "type": "container",
  "effect": "DRAG_TO_NAVIGATE_EFFECT",
  "visual": {
    "displayName": "Mini Lab Cuántico",
    "aspectRatio": 0.65
  },
  "behavior": {
    "gestures": {
      "drag": { "effect": "navigate_next" }
    }
  },
  "children": [
    {
      "id": "procesador_algoritmico",
      "type": "processor",
      "visual": { "displayName": "Procesador Algorítmico" }
    }
  ]
}
```

---

## D. Flujo de datos: del JSON al renderizado

1. **Carga de datos:**  
   El motor universal utiliza `JsonDataLoader` para cargar archivos JSON desde los assets (por ejemplo, `cartas_universal.json`, `deck_sensores.json`, `generador_cuantico_prototipo.json`).

2. **Parsing:**  
   Los datos se decodifican a instancias de `UniversalObject` usando la librería de serialización de Kotlin (`kotlinx.serialization`).

3. **Registro y estado:**  
   Los objetos se registran en el `UniversalViewModel`, que mantiene el estado global, el epigenome y la navegación.

4. **Instanciación y composición:**  
   `UniversalScreen` y/o `UniversalFocusView` reciben la estructura de UniversalObjects y la renderizan recursivamente, aplicando los efectos, visuales y comportamientos definidos en los datos.

5. **Interacción y actualización:**  
   Los gestos del usuario (tap, swipe, drag) se mapean a efectos o transiciones según el campo `behavior`. El estado (`epigenome`) se actualiza y persiste si corresponde.

6. **Renderizado:**  
   Cada UniversalObject se auto-renderiza según su tipo y propiedades, sin intervención de lógica de dominio en el motor. Los efectos visuales se aplican según el campo `effect`, referenciando la definición en `animation_suite.json`.

---

## Resumen

El motor declarativo universal permite definir cualquier aplicación visual/interactiva únicamente mediante datos, desacoplada del dominio.  
La estructura y el comportamiento de cada objeto se describen en JSON, y el motor se encarga de instanciar, gestionar el estado y renderizar fielmente según esos datos,  
garantizando máxima flexibilidad y extensibilidad.
