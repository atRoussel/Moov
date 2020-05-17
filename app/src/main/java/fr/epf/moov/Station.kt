package fr.epf.moov

data class Station (val name: String, val slug: String)
{
    companion object {
        val all = arrayOf<Station>().toMutableList()
    }
}