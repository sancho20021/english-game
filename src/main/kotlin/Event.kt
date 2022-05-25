sealed interface Event
object Death : Event
object Victory : Event
data class DamageReceived(val amount: Int) : Event
data class DamageDealt(val amount: Int) : Event
data class GetBonus(val bonus: Bonus) : Event