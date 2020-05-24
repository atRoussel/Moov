package fr.epf.moov.model

data class Traffic (val line: String, val slug: String, val title: String, val message: String)
{
    companion object {
        val all = arrayOf<Traffic>().toMutableList()
    }
}