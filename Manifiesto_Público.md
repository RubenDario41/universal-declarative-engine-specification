# Universal Declarative Engine — Manifiesto Público

> Un solo motor, cualquier dominio. Cero código nuevo.

## El problema: infinitos frameworks

Crear una nueva aplicación hoy significa elegir un “stack”: React para web, SwiftUI para iOS, Jetpack Compose para Android, Unity para juegos... Cada uno impone sus propias reglas, abstracciones y límites. Seguimos reinventando los mismos patrones en silos incompatibles.

## La visión: Un patrón atómico único

¿Qué pasaría si cada elemento interactivo —un botón, un sensor, una carta del tarot, un modelo 3D— fuera esencialmente el mismo tipo de objeto? ¿Y si toda tu app se definiera simplemente con un archivo de configuración para un solo motor universal?

Este proyecto propone exactamente eso: un **Universal Declarative Engine**.

## Arquitectura central: Cuatro singletons

Esta arquitectura se basa en cuatro componentes irreductibles:

1. **`UniversalObject`**: El átomo recursivo. Todo es una instancia de esto. Definido por tipo, *estado* mutable y un *epigenoma* inmutable.
2. **`UniversalScreen`**: Lienzo único que compone y renderiza el árbol de UniversalObjects.
3. **`UniversalViewModel`**: Fuente única de verdad. Mantiene el estado global reactivo e inmutable.
4. **`UniversalFocusView`**: Gestor de atención. Controla el enfoque espacial 3D y permite la virtualización.

## Principio declarativo

No debes programar el motor. Declaras tu app en un manifiesto (JSON):

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

Cambia el manifiesto y cambia toda la app. Sin recompilar, sin agregar nuevo código de UI.

## Prueba multidominio

El mismo motor, sin modificar, ejecuta:

- Un sistema de Tarot interactivo (objetos tipo `card`).
- Un dashboard de sensores en tiempo real (objetos tipo `gauge`, `plot`).
- Un juego de puzzles (objetos tipo `tile`).

La lógica de dominio vive en plugins. El motor solo conoce `UniversalObject`.

## ¿Por qué esto importa?

- **Simplicidad:** Un paradigma único, aplicable en cualquier lugar.
- **Mantenibilidad:** Núcleo pequeño y estable. La complejidad vive en los manifiestos declarativos.
- **Potencial para herramientas:** Aparecen posibilidades universales de diseño, depuración y análisis.
- **Rendimiento:** Virtualización nativa vía FocusView, escalando a miles de objetos en tiempo real.

## Sobre este repositorio

Aquí encuentras la especificación pública, las interfaces y el core de referencia del Universal Declarative Engine. Es la implementación principal de las ideas expuestas en nuestro whitepaper (enlace a arXiv cuando disponible).

## Primeros pasos

1. Revisa la Arquitectura Central para entender el modelo formal ([ARCHITECTURE.md](./ARCHITECTURE.md)).
2. Consulta las interfaces en Kotlin.
3. Explora los ejemplos de manifiestos para tarot, dashboards y juegos en la carpeta `/examples`.
4. Únete y contribuye a la discusión para dar forma a esta visión.

## Licencia

Este proyecto se publica bajo la GNU General Public License v3.0 con una cláusula ética. Puedes usar, modificar y distribuir libremente siempre que mantengas las mismas libertades y adhieras a los principios éticos de coexistencia definidos en nuestra constitución.

## Súmate a la construcción

Esto no es otro framework más: es el intento de encontrar un patrón fundamental para el software interactivo. Si esta visión te interpela —seas desarrollador, ingeniero en robótica, diseñador de juegos o filósofo del código— tu perspectiva es valiosa.

Construyamos, juntos, el motor universal. No para dominar, sino para unificar.

---

¿Quieres aportar o necesitas más detalles técnicos? Profundiza en [ARCHITECTURE.md](./ARCHITECTURE.md) y consulta los ejemplos en `/examples`.
