package interfaces

/**
 * UniversalObject: El bloque fundamental de la arquitectura universal.
 * Define la estructura mínima para cualquier objeto declarativo.
 */
abstract class UniversalObject {
    abstract val id: String
    abstract val type: String
    abstract val properties: Map<String, Any?>
    abstract val children: List<UniversalObject>?
}
