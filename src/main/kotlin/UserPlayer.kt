object UserPlayer : Player {

    fun clearScreen() {
        println("\u001B[100F")
        println(Array(100) {}.joinToString(separator = "\n") { " ".repeat(100) })
        println("\u001B[100F")
    }

    override fun attack(
        my: UnitStatus,
        other: UnitStatus,
        from: Int,
        to: Int,
    ): Pair<Int, Int> {
        println(other.image)
        println()
        println(other.name)
        println("    health ${other.hp}")
        println("    attack ${other.attack}")
        println("You")
        println("    health ${my.hp}")
        println("    attack ${my.attack}")
        println()
        println("The number is hidden between $from and $to")
        print("Your prediction: ")
        val (a, b) = readLine()!!.split(" ").map { it.toInt() }
        return a to b
    }

    // handling events example
    override fun event(event: Event) {
        when (event) {
            is DamageDealt -> {
                println("You've dealt ${event.amount} damage")
                readLine()
                clearScreen()
            }
            is DamageReceived -> {
                println("You've received ${event.amount} damage")
                readLine()
                clearScreen()
            }
            Death -> {
                println("You died")
                readLine()
            }
            Victory -> {
                println("You won")
                readLine()
                clearScreen()
            }
            is GetBonus -> {
                println("You have received +1 to ${
                    when(event.bonus) {
                        AttackBonus -> "attack"
                        HealthBonus -> "hp"
                    }
                }.")
                readLine()
                clearScreen()
            }
        }
    }

    override fun selectItem(items: List<Item>): Item {
        clearScreen()
        println("Please, select one of the items\n${
            items.indices.zip(items).joinToString(separator = "\n") { it.first.toString() + ". " + it.second.name }
        }")
        while (true) {
            val id = readLine()!!.toInt()
            if (id >= 0 && id < items.size) {
                return items[id]
            }
        }
    }

}