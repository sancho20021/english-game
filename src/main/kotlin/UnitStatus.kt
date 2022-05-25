// This aliases are probably useless
typealias Name = String
typealias Hp = Int

/**
 * We use status to store information about all players in [Game].
 */
data class UnitStatus(
    val name: Name,
    val hp: Hp,
    val attack: Hp,
    val image: String,
)