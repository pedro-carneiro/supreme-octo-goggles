package io.github.recipe

object Resources {
    fun getResource(location: String): String = this::class.java.getResource(location)!!.readText()
}
