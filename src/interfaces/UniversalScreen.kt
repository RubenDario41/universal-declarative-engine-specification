package interfaces

/**
 * UniversalScreen: Representa una pantalla o contexto visual.
 */
abstract class UniversalScreen {
    abstract val id: String
    abstract val objects: List<UniversalObject>
}
