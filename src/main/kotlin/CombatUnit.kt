interface CombatUnit {
    fun attack(my: UnitStatus, other: UnitStatus): Pair<Int, Int>
}