interface CombatUnit {
    fun attack(
        my: UnitStatus,
        other: UnitStatus,
        from: Int,
        to: Int
    ): Pair<Int, Int>
}