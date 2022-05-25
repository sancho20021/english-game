interface Player : CombatUnit {
    /**
     * I didn't come up with better idea, so almost everything is event now(death, victory, transfer to another location, enemy encounter).
     * You can add more Events if you like.
     * @see Event
     */
    fun event(event: Event)

    fun selectItem(items: List<Item>): Item
}