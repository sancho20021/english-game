sealed interface Item {
    val name: String
    val bonus: Bonus
}

object Whip : Item {
    override val name: String = "Old whip"
    override val bonus: Bonus = AttackBonus
}

object LatexGlove : Item {
    override val name: String = "Latex glove"
    override val bonus: Bonus = HealthBonus
}