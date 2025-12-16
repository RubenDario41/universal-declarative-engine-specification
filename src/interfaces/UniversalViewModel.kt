package interfaces

/**
 * UniversalViewModel: Contrato para la lógica de estado y datos.
 */
abstract class UniversalViewModel {
    abstract fun getState(): Map<String, Any?>
    abstract fun updateState(key: String, value: Any?)
}
